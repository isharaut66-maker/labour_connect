// Base URL for the Spring Boot backend
const API_BASE_URL = window.API_BASE_URL 
    || localStorage.getItem('API_BASE_URL')
    || (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1' 
        ? 'http://localhost:8080/api' 
        : '/api');

/**
 * Global wrapper for API requests.
 */
async function apiRequest(endpoint, options = {}) {
    // Add default headers, like Content-Type
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    // Include credentials (cookies) to support Spring Boot HttpSession
    const fetchOptions = {
        ...options,
        headers,
        credentials: 'include'
    };

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, fetchOptions);
        
        // Handle 401 Unauthorized
        if (response.status === 401 && !endpoint.includes('/auth/login')) {
            const isPagesDir = window.location.pathname.includes('/pages/');
            const hasFrontendPrefix = window.location.pathname.includes('/frontend/');
            window.location.href = hasFrontendPrefix 
                ? (isPagesDir ? '/frontend/login.html' : '/frontend/login.html')
                : (isPagesDir ? '../login.html' : 'login.html');
            throw new Error('Unauthorized. Redirecting to login...');
        }

        // Handle empty responses
        if (response.status === 204 || response.headers.get('content-length') === '0') {
            return null;
        }

        const text = await response.text();
        const data = text ? JSON.parse(text) : {};
        
        if (!response.ok) {
            throw new Error(data.message || 'An error occurred');
        }
        
        return data;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// Ensure the user is logged in for protected pages
async function requireAuth() {
    try {
        const user = await apiRequest('/users/me');
        return user;
    } catch (error) {
        // Redirection happens in apiRequest
        return null;
    }
}
