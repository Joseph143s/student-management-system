// assets/js/facultyDashboard.js
import { apiClient } from './apiClient.js';

// 🔒 Route Guard Protection Engine
const currentRole = localStorage.getItem('user_role');
if (currentRole !== 'ROLE_FACULTY' && currentRole !== 'FACULTY') {
    localStorage.clear();
    window.location.href = '/index.html';
}

const loggedInFacultyUserId = parseInt(localStorage.getItem('user_id'));

/**
 * Render and load dynamic student grading fields matching enrollment records
 */
async function renderFacultyWorkspace() {
    try {
        // Query global database datasets concurrently to display user strings
        //
        // 🔧 FIX: this previously called apiClient.get('/faculties') (plural), while
        // adminDashboard.js calls apiClient.get('/faculty') (singular) for the same
        // resource. That mismatch would 404 unless your backend genuinely exposes both
        // paths. Aligned to '/faculty' to match the admin dashboard's working endpoint —
        // if your FacultyController is actually mapped to '/faculties', switch this back
        // and let me know so I can align the admin side instead.
        const [enrollments, facultiesList] = await Promise.all([
            apiClient.get('/enrollments'), // GET /enrollments
            apiClient.get('/faculty')      // GET /faculty
        ]);

        // Welcome Header Banner Identity Map
        //
        // NOTE: this matches on facultyId === the logged-in user's id (from localStorage
        // 'user_id'). If your backend's User entity and Faculty entity have separate
        // primary keys (i.e. a Faculty row's id isn't the same as its linked User's id),
        // this lookup will never find a match and the welcome name will stay generic.
        // Worth double-checking against your FacultyResponseDTO shape.
        const currentFacultyProfile = (facultiesList || []).find(f => parseInt(f.facultyId || f.id) === loggedInFacultyUserId);
        if (currentFacultyProfile) {
            const name = currentFacultyProfile.facultyName || currentFacultyProfile.name || 'Instructor';
            document.getElementById('facultyWelcomeName').innerText = `Welcome, ${name}`;
        }

        const rosterTableBody = document.getElementById('facultyRosterTable');
        if (!rosterTableBody) return;

        if (enrollments && enrollments.length > 0) {
            rosterTableBody.innerHTML = enrollments.map(e => {
                const enrollmentId = e.enrollmentId || e.id;
                const studentName = e.studentName || `Student ID: ${e.studentId}`;
                const courseName = e.courseName || `Course ID: ${e.courseId}`;
                const termYear = e.academicYear || 'N/A';
                const currentGrade = e.grade;

                return `
                    <tr>
                        <td><strong>#EN-${enrollmentId}</strong></td>
                        <td><strong>${studentName}</strong></td>
                        <td>${courseName}</td>
                        <td><span class="badge badge-student">${termYear}</span></td>
                        <td>
                            <span class="badge ${currentGrade ? 'badge-admin' : 'badge-faculty'}">
                                ${currentGrade || 'Pending Grading Evaluation'}
                            </span>
                        </td>
                        <td>
                            <button class="action-btn btn-success open-grade-trigger" data-id="${enrollmentId}">
                                Grade Student
                            </button>
                        </td>
                    </tr>
                `;
            }).join('');
        } else {
            rosterTableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; color:#a0aec0; padding:20px;">No student enrollments found inside system tracking logs.</td></tr>`;
        }

    } catch (err) {
        console.error("Faculty workspace initialization failure:", err.message);
    }
}

/**
 * Intercept action click streams to trigger modal form pop-ups
 */
document.body.addEventListener('click', (e) => {
    if (e.target.classList.contains('open-grade-trigger')) {
        e.preventDefault();
        document.getElementById('modalEnrollId').value = e.target.dataset.id;
        document.getElementById('gradeModal').style.display = 'flex'; // Display modal
    }
});

/**
 * ➕ Submit Form: Update performance evaluation grading marks (PATCH /enrollments)
 */
document.getElementById('gradeForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const enrollId = document.getElementById('modalEnrollId').value;
    const gradeValue = encodeURIComponent(document.getElementById('studentGradeInput').value.trim());

    try {
        // Submits matching query parameter formatting variables to backend endpoint string
        await apiClient.patch(`/enrollments/${enrollId}/complete?grade=${gradeValue}`); // PATCH /enrollments/{id}/complete?grade=...

        document.getElementById('gradeModal').style.display = 'none';
        document.getElementById('gradeForm').reset();
        alert("Official terminal grade successfully assigned! 🎓");
        await renderFacultyWorkspace(); // Refresh metrics grid layout view instantly
    } catch (err) {
        alert("Grading transaction rejected: " + err.message);
    }
});

// 🚪 Close Grading Dialog Modal Box
document.getElementById('closeGradeModal').addEventListener('click', () => {
    document.getElementById('gradeModal').style.display = 'none';
});

// 🚪 Global Session Termination
document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    window.location.href = '/index.html';
});

// Initial workspace compilation sequencial launch call
renderFacultyWorkspace();