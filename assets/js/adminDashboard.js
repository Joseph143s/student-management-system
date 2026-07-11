// assets/js/adminDashboard.js
import { apiClient } from './apiClient.js';

let editingFacultyId = null;
let editingStudentId = null;
// 🔒 Route Guard Protection Engine
const currentRole = localStorage.getItem('user_role');
if (currentRole !== 'ROLE_ADMIN' && currentRole !== 'ADMIN') {
    localStorage.clear();
    window.location.href = '/index.html';
}

/**
 * Master Workspace Renderer Engine
 * Resolves concurrent API calls to unwrap, fallback map, and render DTO sets
 */
async function renderAdminWorkspace() {
    try {
        // Triggers concurrent backend collection queries.
        const results = await Promise.allSettled([
            apiClient.get('/departments'),  // GET /departments
            apiClient.get('/courses'),      // GET /courses
            apiClient.get('/students'),     // GET /students
            apiClient.get('/faculties'),      // GET /faculty
            apiClient.get('/enrollments')   // GET /enrollments
        ]);

        const labels = ['/departments', '/courses', '/students', '/faculties', '/enrollments'];
        const [depts, courses, students, faculty, enrollments] = results.map((r, i) => {
            if (r.status === 'fulfilled') return r.value;
            console.error(`Endpoint ${labels[i]} failed:`, r.reason?.message || r.reason);
            return []; // fall back to an empty list so other tables still render
        });

        // 🏢 1. Structural Mapping: Department Records Grid UI
        const deptTableBody = document.getElementById('deptTable');
        if (deptTableBody) {
            if (depts && depts.length > 0) {
                deptTableBody.innerHTML = depts.map(d => {
                    const id = d.departmentId !== undefined ? d.departmentId : d.id;
                    const name = d.departmentName !== undefined ? d.departmentName : d.name;
                    const code = d.departmentCode !== undefined ? d.departmentCode : (d.code || 'N/A');

                    return `
                        <tr>
                            <td><strong>#DEP-${id}</strong></td>
                            <td><span class="badge badge-student">${code}</span></td>
                            <td>${name}</td>
                        </tr>
                    `;
                }).join('');
            } else {
                deptTableBody.innerHTML = `<tr><td colspan="3" style="text-align:center; color:#a0aec0;">No active institutional departments deployed.</td></tr>`;
            }
        }

        // 🏢 2. Dropdown Hydration: Form Selection Controls
        const optionsHTML = '<option value="" disabled selected>-- Select Mapped Department --</option>' +
            depts.map(d => {
                const id = d.departmentId || d.id || (d.data ? d.data.id : null);
                const name = d.departmentName || d.name || (d.data ? d.data.name : 'Unknown Department');
                if (!id) return '';
                return `<option value="${id}">${name}</option>`;
            }).join('');

        const courseDeptDropdown = document.getElementById('courseDept');
        if (courseDeptDropdown) courseDeptDropdown.innerHTML = optionsHTML;

        const facRegDeptDropdown = document.getElementById('facRegDept');
        if (facRegDeptDropdown) facRegDeptDropdown.innerHTML = optionsHTML;

        const stdRegDeptDropdown = document.getElementById('stdRegDept');
        if (stdRegDeptDropdown) stdRegDeptDropdown.innerHTML = optionsHTML;

        // 📚 3. Structural Mapping: Course Registry Grid UI
        const courseTableBody = document.getElementById('courseTable');
        if (courseTableBody) {
            if (courses && courses.length > 0) {
                courseTableBody.innerHTML = courses.map(c => {
                    const id = c.courseId !== undefined ? c.courseId : c.id;
                    const name = c.coursename !== undefined ? c.coursename : c.name;
                    const duration = c.duration || 'N/A';
                    const fee = c.fee || 0;

                    return `
                        <tr>
                            <td><span class="badge badge-student">${duration}</span></td>
                            <td>
                                <a href="#" class="view-roster-link" data-id="${id}" data-name="${name}">${name}</a>
                                <br/><small>Fee: ₹${fee}</small>
                            </td>
                            <td><button class="action-btn btn-delete revoke-course-btn" data-id="${id}">Revoke Course</button></td>
                        </tr>
                    `;
                }).join('');
            } else {
                courseTableBody.innerHTML = `<tr><td colspan="3" style="text-align:center; color:#a0aec0;">No academic courses currently registered.</td></tr>`;
            }
        }

        // 🧑‍🏫 4. Structural Mapping: Faculty Profiles Registry Grid UI
        const facultyTableBody = document.getElementById("facultyProfilesTable");
        let actualFacultyList = [];

        if (Array.isArray(faculty)) {
            actualFacultyList = faculty;
        } else if (faculty && Array.isArray(faculty.data)) {
            actualFacultyList = faculty.data;
        } else if (faculty && Array.isArray(faculty.content)) {
            actualFacultyList = faculty.content;
        } else if (faculty && Array.isArray(faculty.list)) {
            actualFacultyList = faculty.list;
        }

        console.log("Faculty Response Array:", actualFacultyList);

        if (facultyTableBody) {
            if (actualFacultyList.length > 0) {
                facultyTableBody.innerHTML = actualFacultyList.map(f => {
                    const id = f.facultyId || f.id || f.userId || 'N/A';
                    const name = f.facultyName || f.name || 'No Name Provided';
                    const email = f.email || 'N/A';
                    const context = f.specialization || 
                                    (f.department ? (f.department.departmentName || f.department.name) : null) || 
                                    f.departmentName || 
                                    'General Context';

                    return `
                        <tr>
                            <td>#FAC-${id}</td>
                            <td><strong>${name}</strong></td>
                            <td>${email}</td>
                            <td><span class="badge badge-student">${context}</span></td>
                            <td>

                                 <button
                                      class="action-btn btn-edit edit-faculty-btn"
                                      data-id="${id}">
                                      Update
                                 </button>
                                <button
                                    class="action-btn btn-delete delete-faculty-btn"
                                    data-id="${id}">
                                    Remove Faculty
                                </button>
                            </td>
                        </tr>
                    `;
                }).join("");
            } else {
                facultyTableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:#a0aec0; padding:20px;">No Faculty Found</td></tr>`;
            }
        }

        // 👥 5. Structural Mapping: Student Profiles Grid UI
        const profilesTableBody = document.getElementById('profilesTable');
        if (profilesTableBody) {
            if (students && students.length > 0) {
                profilesTableBody.innerHTML = students.map(s => {
                    const id = s.studentId || s.id || 'N/A';
                    const nameIdentity = s.name || s.studentName || 'Profile Mapped';
                    const emailAddress = s.email || 'N/A';
                    const departmentLabel = s.departmentName || s.departmentCode || (s.department ? (s.department.departmentName || s.department.name) : null) || 'Unassigned Workspace';

                    return `
                        <tr>
                            <td>#STD-${id}</td>
                            <td><strong>${nameIdentity}</strong></td>
                            <td>${emailAddress}</td>
                            <td><span class="badge badge-student">${departmentLabel}</span></td>
                            <td>
                                     <button
                                        class="action-btn btn-edit edit-student-btn"
                                        data-id="${id}">
                                        Update
                                     </button>

                                     <button
                                           class="action-btn btn-delete delete-user-btn"
                                           data-id="${id}">
                                           Remove Student
                                  </button>
                           </td>
                        </tr>
            `;
                }).join('');
            } else {
                profilesTableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:#a0aec0; padding:20px;">No registered student records found inside the database table.</td></tr>`;
            }
        }

        // 🎓 6. Structural Mapping: Full Enrollment Roster Grid UI
        const rosterTableBody = document.getElementById('rosterTableBody');
        const assignedFacultyBox = document.getElementById('assignedFacultyBox');
        if (rosterTableBody) {
            if (enrollments && enrollments.length > 0) {
                rosterTableBody.innerHTML = enrollments.map(r => {
                    const courseMatch = courses.find(c => {
                        const cid = c.courseId !== undefined ? c.courseId : c.id;
                        return parseInt(cid) === parseInt(r.courseId);
                    });
                    const courseName = courseMatch ? (courseMatch.coursename || courseMatch.name) : `Course #${r.courseId}`;
                    return `
                        <tr>
                            <td><strong>#EN-${r.id}</strong></td>
                            <td><strong>${r.studentName || 'Profile Mapped'}</strong></td>
                            <td>#STD-${r.studentId}</td>
                            <td><span class="badge badge-student">${courseName}</span> ${r.academicYear || ''}</td>
                            <td><span class="badge ${r.grade ? 'badge-admin' : 'badge-faculty'}">${r.grade || 'Ungraded'}</span></td>
                        </tr>
                    `;
                }).join('');
                if (assignedFacultyBox) {
                    assignedFacultyBox.innerHTML = `🎓 <strong>Total Enrollments:</strong> ${enrollments.length} across ${courses.length} course(s)`;
                }
            } else {
                rosterTableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; color:#a0aec0; font-style:italic;">No students are currently enrolled in any course.</td></tr>`;
                if (assignedFacultyBox) assignedFacultyBox.innerText = "🎓 No enrollment records found in the registry.";
            }
        }

    } catch (err) {
        console.error("Critical rendering failure encountered:", err.message);
    }
}

/**
 * Centralized Event Delegation Interceptor
 * Catches all operational clicks (Revoke, Delete, and View Roster) safely
 */
document.body.addEventListener("click", async (e) => {

    // ===========================
    // A. VIEW COURSE ROSTER
    // ===========================
    if (e.target.classList.contains("view-roster-link")) {
        e.preventDefault();

        const courseTargetId = parseInt(e.target.dataset.id);
        const courseTargetName = e.target.dataset.name;

        const modal = document.getElementById("rosterModal");
        const title = document.getElementById("rosterModalTitle");
        const facultyBox = document.getElementById("modalAssignedFacultyBox");
        const tableBody = document.getElementById("modalRosterTableBody");

        title.innerText = `Roster Analysis for: ${courseTargetName}`;
        facultyBox.innerText = "Loading...";
        tableBody.innerHTML = `<tr><td colspan="5">Loading...</td></tr>`;

        modal.classList.add("open");

        try {

            const [enrollments, courses] = await Promise.all([
                apiClient.get("/enrollments"),
                apiClient.get("/courses")
            ]);

            const course = courses.find(c => c.courseId == courseTargetId);

            facultyBox.innerHTML = `
                🧑‍🏫 <strong>Instructor:</strong>
                ${course?.facultyName || "Not Assigned"}
            `;

            const roster = enrollments.filter(e => e.courseId == courseTargetId);

            if (roster.length === 0) {
                tableBody.innerHTML =
                    `<tr><td colspan="5">No students enrolled.</td></tr>`;
                return;
            }

            tableBody.innerHTML = roster.map(r => `
                <tr>
                    <td>${r.enrollmentId}</td>
                    <td>${r.studentName}</td>
                    <td>${r.studentId}</td>
                    <td>${r.academicYear}</td>
                    <td>${r.grade || "Ungraded"}</td>
                </tr>
            `).join("");

        } catch (err) {
            alert(err.message);
        }
    }

    // ===========================
    // B. DELETE COURSE
    // ===========================
    if (e.target.classList.contains("revoke-course-btn")) {

        const id = e.target.dataset.id;

        if (!confirm("Delete this course?")) return;

        try {

            await apiClient.delete(`/courses/${id}`);

            alert("Course Deleted");

            renderAdminWorkspace();

        } catch (err) {

            alert(err.message);

        }

    }

    // ===========================
    // C. DELETE STUDENT
    // ===========================
    if (e.target.classList.contains("delete-user-btn")) {

        const id = e.target.dataset.id;

        if (!confirm("Delete this student?")) return;

        try {

            await apiClient.delete(`/students/${id}`);

            alert("Student Deleted");

            renderAdminWorkspace();

        } catch (err) {

            alert(err.message);

        }

    }

    // ===========================
    // D. DELETE FACULTY
    // ===========================
    if (e.target.classList.contains("delete-faculty-btn")) {

        const id = e.target.dataset.id;

        if (!confirm("Delete this faculty?")) return;

        try {

            await apiClient.delete(`/faculties/${id}`);

            alert("Faculty Deleted");

            renderAdminWorkspace();

        } catch (err) {

            alert(err.message);

        }

    }

    // ===========================
    // E. UPDATE STUDENT
    // ===========================
    if (e.target.classList.contains("edit-user-btn")) {

        const id = e.target.dataset.id;

        try {

            const student = await apiClient.get(`/students/${id}`);

            const name = prompt("Student Name", student.name);
            if (name == null) return;

            const email = prompt("Student Email", student.email);
            if (email == null) return;

            const departmentId = prompt("Department Id", student.departmentId);
            if (departmentId == null) return;

            await apiClient.put(`/students/${id}`, {
                name,
                email,
                departmentId: Number(departmentId)
            });

            alert("Student Updated");

            renderAdminWorkspace();

        } catch (err) {

            alert(err.message);

        }

    }

    // ===========================
    // F. UPDATE FACULTY
    // ===========================
    if (e.target.classList.contains("edit-faculty-btn")) {

        const id = e.target.dataset.id;

        try {

            const faculty = await apiClient.get(`/faculties/${id}`);

            const facultyName = prompt("Faculty Name", faculty.facultyName);
            if (facultyName == null) return;

            const email = prompt("Faculty Email", faculty.email);
            if (email == null) return;

            const specialization = prompt("Specialization", faculty.specialization);
            if (specialization == null) return;

            const departmentId = prompt("Department Id", faculty.departmentId);
            if (departmentId == null) return;

            await apiClient.put(`/faculties/${id}`, {
                facultyName,
                email,
                specialization,
                departmentId: Number(departmentId)
            });

            alert("Faculty Updated");

            renderAdminWorkspace();

        } catch (err) {

            alert(err.message);

        }

    }

});

/**
 * ➕ Request DTO Form Submit: Department Infrastructure Deployment
 */
const deptForm = document.getElementById('deptForm');
if (deptForm) {
    deptForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const nameInput = document.getElementById('deptName');
        const codeInput = document.getElementById('deptCode');
        const payload = {
            departmentName: nameInput.value.trim(),
            departmentCode: codeInput.value.trim()
        };
        try {
            await apiClient.post('/departments', payload); // POST /departments
            nameInput.value = '';
            codeInput.value = '';
            await renderAdminWorkspace();
        } catch (err) {
            alert("Department Deployment Rejected: " + err.message);
        }
    });
}

/**
 * ➕ Request DTO Form Submit: Academic Course Deployment
 */
const courseForm = document.getElementById('courseForm');
if (courseForm) {
    courseForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const payload = {
            coursename: document.getElementById('courseName').value.trim(),
            duration: document.getElementById('courseDuration').value.trim(),
            fee: parseFloat(document.getElementById('courseFee').value),
            programYear: parseInt(document.getElementById('courseProgramYear').value),
            departmentId: parseInt(document.getElementById('courseDept').value)
        };

        try {
            await apiClient.post('/courses', payload); // POST /courses
            document.getElementById('courseName').value = '';
            document.getElementById('courseDuration').value = '';
            document.getElementById('courseFee').value = '';
            document.getElementById('courseProgramYear').selectedIndex = 0;
            document.getElementById('courseDept').selectedIndex = 0;
            await renderAdminWorkspace();
        } catch (err) {
            alert("Course Deployment Rejected: " + err.message);
        }
    });
}

/**
 * ➕ Faculty Profile Deployment Submit Handler (Authorized Admin Only)
 */
const adminFacultyRegForm = document.getElementById('adminFacultyRegForm');
if (adminFacultyRegForm) {
    adminFacultyRegForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('jwt_token'); // Fetch Admin Bearer validation token
        
        const payload = {
                       name: document.getElementById('facRegName').value.trim(),            // Fulfills User authentication credentials
            facultyName: document.getElementById('facRegName').value.trim(),     // ✅ Fulfills FacultyRequestDTO
            username: document.getElementById('facRegUsername').value.trim(),    // Fulfills User authentication credentials
            email: document.getElementById('facRegEmail').value.trim(),          // Fulfills both DTOs
            specialization: document.getElementById('facRegSpecialization').value.trim(), // ✅ Fulfills FacultyRequestDTO
            password: document.getElementById('facRegPassword').value,           // Fulfills User authentication credentials
            departmentId: parseInt(document.getElementById('facRegDept').value), // ✅ Fulfills FacultyRequestDTO
            role: "FACULTY"
        };

        try {
            const response = await fetch('http://localhost:9092/auth/register/faculty', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(payload)
            });

            const result = await response.json();
            if (!response.ok) {
                throw new Error(result.message || `HTTP Error ${response.status}`);
            }

            alert("Faculty account and profile successfully processed by Admin! 🎉");
            adminFacultyRegForm.reset();
            await renderAdminWorkspace(); // Dynamically updates layout data lists grids
        } catch(err) {
            alert("Faculty Onboarding Rejected: " + err.message);
        }
    });
}

/**
 * ➕ Student Profile Deployment Submit Handler (Authorized Admin Only)
 */
const adminStudentRegForm = document.getElementById('adminStudentRegForm');
if (adminStudentRegForm) {
    adminStudentRegForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('jwt_token'); // Fetch active Admin authorization clearance
        
        const payload = {
            name: document.getElementById('stdRegName').value.trim(),
            username: document.getElementById('stdRegUsername').value.trim(),
            email: document.getElementById('stdRegEmail').value.trim(),
            password: document.getElementById('stdRegPassword').value,
            departmentId: parseInt(document.getElementById('stdRegDept').value),
            courseIds: [], // Defaults to empty list for initial onboarding
            role: "STUDENT"
        };

        try {
            const response = await fetch('http://localhost:9092/auth/register/student', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(payload)
            });

            const result = await response.json();
            if (!response.ok) {
                throw new Error(result.message || `HTTP Error ${response.status}`);
            }

            alert("Student account successfully provisioned by Admin! 🎉");
            adminStudentRegForm.reset();
            await renderAdminWorkspace(); // Refresh the profiles management grid layout view
        } catch (err) {
            alert("Student Deployment Rejected: " + err.message);
        }
    });
}

// 🚪 Close Roster Modal Listener (toggles the .open class the CSS expects)
const closeRosterModal = document.getElementById('closeRosterModal');
if (closeRosterModal) {
    closeRosterModal.addEventListener('click', () => {
        const rosterModal = document.getElementById('rosterModal');
        if (rosterModal) rosterModal.classList.remove('open');
    });
}

// 🚪 Global Session Termination
const logoutBtn = document.getElementById('logoutBtn');
if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
        localStorage.clear();
        window.location.href = '/index.html';
    });
}

// Initial boot execution call sequence
renderAdminWorkspace();
