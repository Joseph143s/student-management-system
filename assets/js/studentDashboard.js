// assets/js/studentDashboard.js
import { apiClient } from './apiClient.js';

// 🔒 Route Guard Protection Engine
const currentRole = localStorage.getItem('user_role');
if (currentRole !== 'ROLE_STUDENT' && currentRole !== 'STUDENT') {
    localStorage.clear();
    window.location.href = '/index.html';
}

// Global state tracking arrays cache
let globalCoursesCache = [];
let globalDeptsCache = [];
let studentEnrollmentsCache = [];
// Maps a courseId -> { departmentId, departmentName } so we can still show
// a department badge in the catalog even though CourseResponseDTO no
// longer includes departmentId/departmentName directly (see buildCourseDepartmentMap()).
let courseDeptLookup = new Map();
const loggedInStudentId = parseInt(localStorage.getItem('user_id'));

// Current filter state for the catalog view
let activeSearchTerm = '';
let activeDeptFilter = 'ALL';
let activeYearFilter = 'ALL';

// The backend has no "get address by student id" endpoint — an Address is only
// reachable by its own addressId (GET/PUT/DELETE /addresses/{id}). Once the
// student creates an address we remember its id locally so we can manage it.
const addressIdStorageKey = `student_address_id_${loggedInStudentId}`;
let currentAddressId = localStorage.getItem(addressIdStorageKey)
    ? parseInt(localStorage.getItem(addressIdStorageKey))
    : null;

/**
 * 🧭 TAB VIEW SWITCHER SUITE LAYOUT INTERCEPTOR
 */
const sidebarItems = document.querySelectorAll('.sidebar-item');
sidebarItems.forEach(item => {
    item.addEventListener('click', () => {
        sidebarItems.forEach(i => i.classList.remove('active'));
        document.querySelectorAll('.tab-panel').forEach(p => p.classList.remove('active'));

        item.classList.add('active');
        const targetId = item.dataset.target;
        document.getElementById(targetId).classList.add('active');
        document.getElementById('currentNavTitle').innerText = item.innerText;

        // Refresh the enrollments tab whenever the student opens it,
        // so it always reflects the latest enroll/drop actions.
        if (targetId === 'view-enrollments') {
            populateMyEnrollments();
        }
    });
});

/**
 * 🔔 Lightweight toast notification (replaces blocking alert() calls)
 */
function showToast(message, type = 'success') {
    let container = document.getElementById('toastContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toastContainer';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = `toast-item toast-${type}`;
    toast.innerText = message;
    container.appendChild(toast);

    setTimeout(() => toast.classList.add('toast-visible'), 10);
    setTimeout(() => {
        toast.classList.remove('toast-visible');
        setTimeout(() => toast.remove(), 300);
    }, 3200);
}

function setLoadingState(isLoading) {
    const tableBody = document.getElementById('studentCatalogTable');
    if (isLoading && tableBody) {
        tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:#a0aec0; padding:20px;">Loading course catalog…</td></tr>`;
    }
}

/**
 * Master Student Console Initialization Engine
 * Concurrent fetch pipeline maps relative cross-referenced variables instantly
 *
 * ⚠️ IMPORTANT PERMISSION CHANGES IN THE LATEST BACKEND PACKAGE:
 * - GET /students (list-all) is now ADMIN/FACULTY only — a STUDENT gets 403.
 *   We fetch the student's own record via GET /students/{id} instead.
 * - GET /enrollments (list-all) is now ADMIN only — a STUDENT gets 403.
 *   We fetch via GET /enrollments/student/{studentId} instead.
 */
async function renderStudentConsole() {
    setLoadingState(true);
    try {
        // Triggers concurrent backend collection queries across all endpoints a STUDENT can actually reach
        const [courses, depts, enrollments, myProfile] = await Promise.all([
            apiClient.get('/courses'),                                    // GET /courses (every course added by Admin/Faculty)
            apiClient.get('/departments'),                                // GET /departments (to resolve branch name displays)
            apiClient.get(`/enrollments/student/${loggedInStudentId}`),   // GET /enrollments/student/{id} (own registrations only)
            apiClient.get(`/students/${loggedInStudentId}`)               // GET /students/{id} (own profile only — /students list is Admin/Faculty only now)
        ]);

        globalCoursesCache = courses || [];
        globalDeptsCache = depts || [];
        studentEnrollmentsCache = enrollments || [];

        // Render Top Welcome banner metrics using StudentResponseDTO (id, name, email, courseNames, city, departmentName)
        if (myProfile) {
            document.getElementById('stdWelcome').innerText = `Welcome, ${myProfile.name || 'Student'}`;
            renderCurrentAddress(myProfile);
            populateProfileTab(myProfile);
        } else {
            document.getElementById('stdWelcome').innerText = `Active Student ID: #${loggedInStudentId}`;
        }

        // Hydrate global academic registry courses list table out-of-the-box
        populateDepartmentFilterOptions();
        await buildCourseDepartmentMap();
        renderCatalogStats();
        populateCourseRegistrationCatalog();

    } catch (err) {
        console.error("Student portal rendering failure:", err.message);
        const tableBody = document.getElementById('studentCatalogTable');
        if (tableBody) {
            tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:#e53e3e; padding:20px;">Could not load the course catalog. Please refresh the page.</td></tr>`;
        }
    }
}

/**
 * 🗂️ CourseResponseDTO no longer carries departmentId/departmentName (only
 * courseId, coursename, duration, fee, programYear), so we can't badge a
 * course's department straight from GET /courses anymore. We rebuild that
 * mapping client-side using the still-available GET /courses/department/{id}
 * endpoint (one call per department, run in parallel).
 */
async function buildCourseDepartmentMap() {
    courseDeptLookup = new Map();
    if (globalDeptsCache.length === 0) return;

    const lookups = await Promise.all(globalDeptsCache.map(async d => {
        const deptId = d.departmentId !== undefined ? d.departmentId : d.id;
        const deptName = d.departmentName || d.name || `Branch #${deptId}`;
        try {
            const deptCourses = await apiClient.get(`/courses/department/${deptId}`);
            return { deptId, deptName, courses: deptCourses || [] };
        } catch (err) {
            return { deptId, deptName, courses: [] };
        }
    }));

    lookups.forEach(({ deptId, deptName, courses }) => {
        courses.forEach(c => {
            const courseId = c.courseId !== undefined ? c.courseId : c.id;
            courseDeptLookup.set(parseInt(courseId), { departmentId: deptId, departmentName: deptName });
        });
    });
}

/**
 * 📊 Quick stats strip: total courses on offer vs. how many the student has already claimed
 */
function renderCatalogStats() {
    const statsBar = document.getElementById('catalogStatsBar');
    if (!statsBar) return;

    const totalCourses = globalCoursesCache.length;
    const activeEnrollments = studentEnrollmentsCache.filter(e => e.status === 'ACTIVE').length;
    const completedEnrollments = studentEnrollmentsCache.filter(e => e.status === 'COMPLETED').length;
    const remaining = Math.max(totalCourses - activeEnrollments - completedEnrollments, 0);

    statsBar.innerHTML = `
        <div class="stat-pill"><strong>${totalCourses}</strong><span>Courses Offered</span></div>
        <div class="stat-pill stat-pill-success"><strong>${activeEnrollments}</strong><span>Currently Enrolled</span></div>
        <div class="stat-pill"><strong>${completedEnrollments}</strong><span>Completed</span></div>
        <div class="stat-pill"><strong>${remaining}</strong><span>Still Available</span></div>
    `;
}

/**
 * 🏷️ Fills the department filter dropdown from live department data
 */
function populateDepartmentFilterOptions() {
    const select = document.getElementById('departmentFilterSelect');
    if (!select) return;

    const existingValue = select.value || 'ALL';
    const options = ['<option value="ALL">All Departments</option>'];

    globalDeptsCache.forEach(d => {
        const id = d.departmentId !== undefined ? d.departmentId : d.id;
        const name = d.departmentName || d.name || `Branch #${id}`;
        options.push(`<option value="${id}">${name}</option>`);
    });

    select.innerHTML = options.join('');
    select.value = existingValue;
}

/**
 * Hydrates and lists all course items deployed by Admin with reactive action markers.
 * Respects the current search term, department filter, and program-year filter.
 */
function populateCourseRegistrationCatalog() {
    const tableBody = document.getElementById('studentCatalogTable');
    if (!tableBody) return;

    if (globalCoursesCache.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:#a0aec0; padding:20px;">No academic courses currently registered in system catalog.</td></tr>`;
        return;
    }

    const searchLower = activeSearchTerm.trim().toLowerCase();

    const visibleCourses = globalCoursesCache.filter(c => {
        const name = (c.coursename !== undefined ? c.coursename : c.name) || '';
        const courseId = c.courseId !== undefined ? c.courseId : c.id;
        const deptInfo = courseDeptLookup.get(parseInt(courseId));

        const matchesSearch = searchLower === '' || name.toLowerCase().includes(searchLower);
        const matchesDept = activeDeptFilter === 'ALL' ||
            (deptInfo && parseInt(deptInfo.departmentId) === parseInt(activeDeptFilter));
        const matchesYear = activeYearFilter === 'ALL' || parseInt(c.programYear) === parseInt(activeYearFilter);

        return matchesSearch && matchesDept && matchesYear;
    });

    if (visibleCourses.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:#a0aec0; padding:20px;">No courses match your search / filter. Try clearing them.</td></tr>`;
        return;
    }

    tableBody.innerHTML = visibleCourses.map(c => {
        // Resilience key parameters matching your Spring Boot variations
        const courseId = c.courseId !== undefined ? c.courseId : c.id;
        const name = c.coursename !== undefined ? c.coursename : c.name;
        const duration = c.duration || 'N/A';
        const fee = c.fee || 0;
        const programYear = c.programYear || null;

        // 🔍 Relational Cross-reference lookup built from buildCourseDepartmentMap()
        const deptInfo = courseDeptLookup.get(parseInt(courseId));
        const deptNameDisplay = deptInfo ? deptInfo.departmentName : 'Unassigned';

        // 🛡️ State Evaluator Check: what's the student's current relationship with this course?
        const matchingEnrollments = studentEnrollmentsCache.filter(e => parseInt(e.courseId) === parseInt(courseId));
        const activeEnrollment = matchingEnrollments.find(e => e.status === 'ACTIVE');
        const completedEnrollment = matchingEnrollments.find(e => e.status === 'COMPLETED');

       let actionUIElement;

if (activeEnrollment) {
    actionUIElement = `
        <button class="action-btn" disabled
            style="background:#38a169;color:white;cursor:not-allowed;">
            Already Registered
        </button>`;
}
else {
    actionUIElement = `
        <button class="action-btn btn-success enroll-course-trigger"
            data-id="${courseId}"
            data-name="${name}">
            Register
        </button>`;
}

       return `
<tr>
    <td>${duration}</td>

    <td>${name}</td>

    <td>${deptNameDisplay}</td>

    <td>${actionUIElement}</td>
</tr>
`;
    }).join('');
}

/**
 * 🎓 "My Enrollments" tab: shows exactly what the student has already signed up for,
 * and now lets them drop an ACTIVE course (PATCH /enrollments/{id}/drop is STUDENT-only).
 */
function populateMyEnrollments() {
    const wrapper = document.getElementById('myEnrollmentsTable');
    if (!wrapper) return;

    if (studentEnrollmentsCache.length === 0) {
        wrapper.innerHTML = `<tr><td colspan="5" style="text-align:center; color:#a0aec0; padding:20px;">You haven't enrolled in any courses yet. Head to "Course Registration" to get started.</td></tr>`;
        return;
    }

    wrapper.innerHTML = studentEnrollmentsCache.map(e => {
        // EnrollmentResponseDTO already carries courseName/studentName directly — no lookup needed
        const name = e.courseName || `Course #${e.courseId}`;
        const course = globalCoursesCache.find(c => parseInt(c.courseId !== undefined ? c.courseId : c.id) === parseInt(e.courseId));
        const duration = course ? (course.duration || 'N/A') : 'N/A';

        const deptInfo = courseDeptLookup.get(parseInt(e.courseId));
        const deptNameDisplay = deptInfo ? deptInfo.departmentName : 'N/A';

        const statusBadgeClass = e.status === 'ACTIVE' ? 'badge-admin' : (e.status === 'COMPLETED' ? 'badge-faculty' : 'badge-student');

        let rowAction = '';
        if (e.status === 'ACTIVE') {
            rowAction = `<button class="action-btn btn-danger drop-course-trigger" data-enrollment-id="${e.enrollmentId}" data-name="${name}">Drop</button>`;
        } else {
            rowAction = `<span class="badge ${statusBadgeClass}">${e.status}${e.grade ? ' • ' + e.grade : ''}</span>`;
        }

        return `
            <tr>
                <td><strong>${name}</strong></td>
                <td><span class="badge badge-student">${duration}</span></td>
                <td><span class="badge badge-faculty">${deptNameDisplay}</span></td>
                <td>
                    <span class="badge ${statusBadgeClass}">${e.status}</span>
                    <br/><small style="color:#718096;">Enrolled: ${e.enrolmentDate || 'N/A'}</small>
                </td>
                <td>${rowAction}</td>
            </tr>
        `;
    }).join('');
}

/**
 * 🔎 Search box: filters catalog live as the student types a course name
 */
const catalogSearchField = document.getElementById('catalogSearchField');
if (catalogSearchField) {
    catalogSearchField.addEventListener('input', (e) => {
        activeSearchTerm = e.target.value;
        populateCourseRegistrationCatalog();
    });
}

/**
 * 🏷️ Department filter dropdown
 */
const departmentFilterSelect = document.getElementById('departmentFilterSelect');
if (departmentFilterSelect) {
    departmentFilterSelect.addEventListener('change', (e) => {
        activeDeptFilter = e.target.value;
        populateCourseRegistrationCatalog();
    });
}

/**
 * 🏷️ Program Year filter dropdown (new field on CourseResponseDTO — 1 through 4)
 */
const yearFilterSelect = document.getElementById('yearFilterSelect');
if (yearFilterSelect) {
    yearFilterSelect.addEventListener('change', (e) => {
        activeYearFilter = e.target.value;
        populateCourseRegistrationCatalog();
    });
}

/**
 * Centralized Event Delegation Interceptor
 * Efficiently handles instant auto-enroll and drop-course click streams cleanly
 */
document.body.addEventListener('click', async (e) => {
    if (e.target.classList.contains('enroll-course-trigger')) {
        e.preventDefault();

        const courseTargetId = parseInt(e.target.dataset.id);
        const courseName = e.target.dataset.name || 'this course';

        const confirmed = window.confirm(`Enroll in "${courseName}"?`);
        if (!confirmed) return;

        // 🔑 EnrollmentRequestDTO only needs studentId + courseId — the server always
        // sets status to ACTIVE and stamps the enrollment date itself. Do NOT send a
        // "status" string here: EnrollmentStatus only accepts ACTIVE / COMPLETED / DROPPED,
        // and anything else (e.g. "ENROLLED") gets rejected by Jackson before validation runs.
        const payload = {
            studentId: loggedInStudentId,
            courseId: courseTargetId
        };

        e.target.disabled = true;
        e.target.innerText = 'Enrolling…';

        try {
            // Fires network transaction pipeline back to port 9092
            console.log("Enrollment Payload:", payload);
            await apiClient.post('/enrollments', payload); // POST /enrollments
            showToast(`Enrolled in "${courseName}" successfully! 🎉`, 'success');

            // Refresh enrollment cache + all dependent views without a full reload
            studentEnrollmentsCache = await apiClient.get(`/enrollments/student/${loggedInStudentId}`) || [];
            renderCatalogStats();
            populateCourseRegistrationCatalog();
        } catch (err) {
            showToast("Enrollment rejected: " + err.message, 'error');
            e.target.disabled = false;
            e.target.innerText = 'Enroll';
        }
    }

    if (e.target.classList.contains('drop-course-trigger')) {
        e.preventDefault();

        const enrollmentId = parseInt(e.target.dataset.enrollmentId);
        const courseName = e.target.dataset.name || 'this course';

        const confirmed = window.confirm(`Drop "${courseName}"? You can re-enroll later if seats remain available.`);
        if (!confirmed) return;

        e.target.disabled = true;
        e.target.innerText = 'Dropping…';

        try {
            // PATCH /enrollments/{enrollmentId}/drop — STUDENT-only status transition.
            // NOTE: apiClient.js needs a `patch(url)` method alongside its existing
            // get/post/put helpers for this call to work.
            await apiClient.patch(`/enrollments/${enrollmentId}/drop`);
            showToast(`Dropped "${courseName}".`, 'success');

            studentEnrollmentsCache = await apiClient.get(`/enrollments/student/${loggedInStudentId}`) || [];
            renderCatalogStats();
            populateMyEnrollments();
            populateCourseRegistrationCatalog();
        } catch (err) {
            showToast("Drop request rejected: " + err.message, 'error');
            e.target.disabled = false;
            e.target.innerText = 'Drop';
        }
    }
});

/**
 * 👤 MY PROFILE TAB
 *
 * ⚠️ The backend now restricts PUT /students/{id} to ADMIN only, so a
 * STUDENT can no longer edit their own name/email/department from here —
 * that request would come back 403 Forbidden. This tab is now read-only,
 * built straight from StudentResponseDTO (id, name, email, courseNames, city, departmentName).
 */
function populateProfileTab(profile) {
    const summaryEl = document.getElementById('profileSummaryView');
    if (!summaryEl) return;

    const courseList = (profile.courseNames && profile.courseNames.length > 0)
        ? profile.courseNames.join(', ')
        : 'Not enrolled in any course yet';

    summaryEl.innerHTML = `
        <div class="profile-row"><strong>Student ID:</strong> #${profile.id}</div>
        <div class="profile-row"><strong>Name:</strong> ${profile.name || 'N/A'}</div>
        <div class="profile-row"><strong>Email:</strong> ${profile.email || 'N/A'}</div>
        <div class="profile-row"><strong>Department:</strong> ${profile.departmentName || 'N/A'}</div>
        <div class="profile-row"><strong>City on File:</strong> ${profile.city || 'Not set — see Residential Address tab'}</div>
        <div class="profile-row"><strong>Enrolled Courses:</strong> ${courseList}</div>
    `;
}

/**
 * Render Address entries cleanly.
 * StudentResponseDTO only exposes `city`, so the summary line uses that as a
 * quick indicator; the full record (state/pincode) is loaded separately in
 * loadAddressTab() using the locally-remembered addressId.
 */
function renderCurrentAddress(studentProfile) {
    const addressView = document.getElementById('currentAddressView');
    if (!addressView) return;

    if (studentProfile && studentProfile.city) {
        addressView.innerHTML = `📍 <strong>City on file:</strong> ${studentProfile.city}`;
    } else {
        addressView.innerHTML = `❌ <em>No primary address configured yet inside your institutional profile registry.</em>`;
    }
}

/**
 * 🏠 ADDRESS TAB — full CRUD, matching what a STUDENT is actually allowed to do:
 * POST /addresses (create), GET/PUT/DELETE /addresses/{id} (self-managed via
 * the addressId we remember locally, since there's no "get by student" route).
 */
async function loadAddressTab() {
    const formTitle = document.getElementById('addressFormTitle');
    const submitBtn = document.getElementById('addressSubmitBtn');
    const deleteBtn = document.getElementById('addressDeleteBtn');
    const currentAddressDetail = document.getElementById('currentAddressDetailView');

    if (!currentAddressId) {
        if (formTitle) formTitle.innerText = 'Add Residential Address';
        if (submitBtn) submitBtn.innerText = 'Save Address';
        if (deleteBtn) deleteBtn.style.display = 'none';
        if (currentAddressDetail) currentAddressDetail.innerHTML = '';
        return;
    }

    try {
        const address = await apiClient.get(`/addresses/${currentAddressId}`);
        if (formTitle) formTitle.innerText = 'Update Residential Address';
        if (submitBtn) submitBtn.innerText = 'Update Address';
        if (deleteBtn) deleteBtn.style.display = 'inline-block';

        document.getElementById('addrCity').value = address.city || '';
        document.getElementById('addrState').value = address.state || '';
        document.getElementById('addrPincode').value = address.pincode || '';

        if (currentAddressDetail) {
            currentAddressDetail.innerHTML = `
                📍 <strong>City:</strong> ${address.city} |
                <strong>State:</strong> ${address.state} |
                <strong>Pincode:</strong> ${address.pincode}
            `;
        }
    } catch (err) {
        // Stored addressId no longer resolves (e.g. deleted from another session) — reset to "create" mode
        currentAddressId = null;
        localStorage.removeItem(addressIdStorageKey);
        if (formTitle) formTitle.innerText = 'Add Residential Address';
        if (submitBtn) submitBtn.innerText = 'Save Address';
        if (deleteBtn) deleteBtn.style.display = 'none';
        if (currentAddressDetail) currentAddressDetail.innerHTML = `<em>No address on file.</em>`;
    }
}

document.getElementById('addressForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const payload = {
        city: document.getElementById('addrCity').value.trim(),
        state: document.getElementById('addrState').value.trim(),
        pincode: document.getElementById('addrPincode').value.trim(),
        studentId: loggedInStudentId
    };

    const submitBtn = document.getElementById('addressSubmitBtn');
    submitBtn.disabled = true;

    try {
        if (currentAddressId) {
            // PUT /addresses/{id} — updates city/state/pincode only
            await apiClient.put(`/addresses/${currentAddressId}`, payload);
            showToast("Residential address updated successfully! 🏠", 'success');
        } else {
            // POST /addresses — creates a new record and links it to this student
            const created = await apiClient.post('/addresses', payload);
            currentAddressId = created.addressId;
            localStorage.setItem(addressIdStorageKey, currentAddressId);
            showToast("Residential address saved successfully! 🏠", 'success');
        }
        await loadAddressTab();
        // Re-sync the profile's `city` field, which is derived server-side from this address
        const refreshedProfile = await apiClient.get(`/students/${loggedInStudentId}`);
        populateProfileTab(refreshedProfile);
        renderCurrentAddress(refreshedProfile);
    } catch (err) {
        showToast("Address update rejected: " + err.message, 'error');
    } finally {
        submitBtn.disabled = false;
    }
});

const addressDeleteBtn = document.getElementById('addressDeleteBtn');
if (addressDeleteBtn) {
    addressDeleteBtn.addEventListener('click', async () => {
        if (!currentAddressId) return;
        const confirmed = window.confirm('Remove your residential address on file?');
        if (!confirmed) return;

        addressDeleteBtn.disabled = true;
        try {
            // DELETE /addresses/{id} is STUDENT-permitted. NOTE: apiClient.js needs a
            // `delete(url)` method alongside its existing get/post/put helpers.
            await apiClient.delete(`/addresses/${currentAddressId}`);
            localStorage.removeItem(addressIdStorageKey);
            currentAddressId = null;
            document.getElementById('addressForm').reset();
            showToast("Residential address removed.", 'success');
            await loadAddressTab();
            const refreshedProfile = await apiClient.get(`/students/${loggedInStudentId}`);
            populateProfileTab(refreshedProfile);
            renderCurrentAddress(refreshedProfile);
        } catch (err) {
            showToast("Could not remove address: " + err.message, 'error');
        } finally {
            addressDeleteBtn.disabled = false;
        }
    });
}

// Load the address tab's current record whenever the student opens it
sidebarItems.forEach(item => {
    if (item.dataset.target === 'configure-address') {
        item.addEventListener('click', loadAddressTab);
    }
});

// 🚪 Global Session Termination
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = '/index.html';
});

// Initial boot execution call sequence
renderStudentConsole();
loadAddressTab();