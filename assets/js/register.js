// assets/js/register.js
import { apiClient } from './apiClient.js';

document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const errorBox = document.getElementById('errorBox');
    if (errorBox) errorBox.style.display = 'none';

    const selectedRole = document.getElementById('regRole').value; // 'STUDENT' or 'FACULTY'

    const payload = {
        name: document.getElementById('regName').value.trim(),
        email: document.getElementById('regEmail').value.trim(),
        password: document.getElementById('regPassword').value,
        role: selectedRole
    };

    try {
        let endpoint = '';
        
        // 🔀 Route to the exact path specified in your UserController
        if (selectedRole === 'STUDENT') {
            endpoint = '/auth/register/student';
        } else if (selectedRole === 'FACULTY') {
            // Note: Per your @PreAuthorize config, only a logged-in ADMIN can call this successfully!
            endpoint = '/auth/register/faculty'; 
        } else {
            throw new Error("Invalid role configuration selected.");
        }

        await apiClient.post(endpoint, payload);
        alert(`${selectedRole.toLowerCase()} profile provisioned successfully! Proceeding to login workspace.`);
        window.location.href = '/index.html';
    } catch (err) {
        if (errorBox) {
            errorBox.innerText = err.message;
            errorBox.style.display = 'block';
        } else {
            alert(err.message);
        }
    }
});
