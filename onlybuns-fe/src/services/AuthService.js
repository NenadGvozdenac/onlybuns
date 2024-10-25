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
        localStorage.setItem('token', response.data.token);
        return response.data; // Return the response data
    }

    // Logout user
    logout() {
        localStorage.removeItem('token'); // Remove token from local storage
    }
}

export default new AuthService();