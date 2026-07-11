// assets/js/login.js
import { apiClient } from './apiClient.js';

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const errorBox = document.getElementById('errorBox');
    if (errorBox) errorBox.style.display = 'none'; // Clear previous error messages

    const payload = {
        email: document.getElementById('email').value.trim(),
        password: document.getElementById('password').value
    };

    try {
        // 📞 Calls your backend API via the apiClient engine on port 9092
        const data = await apiClient.post('/auth/login', payload);

        console.log("Login Response:", data);

        // 💾 Save session tokens and metadata to browser storage
        localStorage.setItem('jwt_token', data.token);
        localStorage.setItem('user_role', data.role); 
        localStorage.setItem('user_id', data.userId); 

        // 🔀 Route dynamically to separate dashboard folders based on Spring Security Roles
      // assets/js/login.js
// Find your switch statement and modify it to look exactly like this:

switch(data.role) {
    case 'ROLE_ADMIN':
    case 'ADMIN': 
        window.location.href = '/dashboards/admin.html'; 
        break;
        
    case 'ROLE_FACULTY':
    case 'FACULTY': 
        window.location.href = '/dashboards/faculty.html'; 
        break;
        
    case 'ROLE_STUDENT':
    case 'STUDENT': 
        window.location.href = '/dashboards/student.html'; 
        break;
        
    default: 
        throw new Error(`Assigned user role configuration [${data.role}] is not supported.`);
}

    } catch (err) {
        if (errorBox) {
            errorBox.innerText = err.message;
            errorBox.style.display = 'block';
        } else {
            alert(err.message);
        }
    }
});
