// assets/js/apiClient.js
const BASE_URL = "http://localhost:9092";

async function request(endpoint, method = "GET", body = null) {
    const token = localStorage.getItem("jwt_token");
    const headers = { "Content-Type": "application/json" };
    
    if (token) {
        headers["Authorization"] = `Bearer ${token}`; 
    }

    const config = { method, headers };
    if (body) {
        config.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, config);
        
        let result = {};
        const text = await response.text();
        if (text) {
            result = JSON.parse(text);
        }

        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                localStorage.clear();
                if (!window.location.pathname.endsWith("index.html") && window.location.pathname !== "/") {
                    window.location.href = "/index.html";
                }
            }
            throw new Error(result.message || `HTTP Error ${response.status}`);
        }
        
        return result.data !== undefined ? result.data : result;
    } catch (error) {
        console.error(`API Error [${method} ${endpoint}]:`, error.message);
        throw error;
    }
}

export const apiClient = {
    get: (url) => request(url, "GET"),
    post: (url, body) => request(url, "POST", body),
    put: (url, body) => request(url, "PUT", body),
    patch: (url, body) => request(url, "PATCH", body),
    delete: (url) => request(url, "DELETE")
};
