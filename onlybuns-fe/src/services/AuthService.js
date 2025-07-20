import axios from 'axios';

const API_URL = 'http://localhost:8080/auth';

class AuthService {
    // Register a new user
    async register(userData) {
        try {
            const response = await axios.post(`${API_URL}/register`, userData);
            return response.data; // Return the response data
        } catch (error) {
            throw error.response.data; // Handle errors appropriately
        }
    }

    // Login user
    async login(credentials) {
        const response = await axios.post(`${API_URL}/login`, credentials);

        // Store the token in local storage or any other storage mechanism
        localStorage.setItem('token', response.data.accessToken);
        return response.data; // Return the response data
    }

    // Logout user
    logout() {
        localStorage.removeItem('token'); // Remove token from local storage
    }

    isLoggedIn() {
        return localStorage.getItem('token') !== null; // Check if token is present
    }
    isAdmin() {
        const role = this.getLoggedInUserRole();
        return role === 'ADMIN'; // Check if the user has admin role
    }

    getLoggedInUsername() {
        let username = '';

        if (this.isLoggedIn()) {
            const token = localStorage.getItem('token');
            const payload = token.split('.')[1];
            const decoded = atob(payload);
            const user = JSON.parse(decoded);
            username = user.sub;
        }

        return username;
    }

    getLoggedInUserRole() {
        let role = '';
    
        if (this.isLoggedIn()) {
            const token = localStorage.getItem('token');
            const payload = token.split('.')[1];
            const decoded = atob(payload);
            const user = JSON.parse(decoded);
    
            role = user.role; // Assuming 'role' is the key you used in the JWT payload
        }
    
        return role;
    }
}

export default new AuthService();