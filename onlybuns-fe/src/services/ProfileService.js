import axios from 'axios';

const API_URL = 'http://localhost:8080';

class ProfileService {
    async getMyProfile() {
        try {
            const token = localStorage.getItem('token');

            if (!token) {
                throw new Error('No token saved!');
            }

            const authorization = {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            };

            const response = await axios.get(`${API_URL}/profile`, authorization);

            return response.data;
        } catch (error) {
            throw error.response.data;
        }
    }

    async getProfile(username) {
        const response = await axios.get(`${API_URL}/profile?username=${username}`);
        return response.data;
    }

    async updateProfile(profile) {
        try {
            const token = localStorage.getItem('token');

            if (!token) {
                throw new Error('No token saved!');
            }

            const authorization = {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            };

            const response = await axios.put(`${API_URL}/profile`, profile, authorization);

            return response.data;
        } catch (error) {
            throw error.response.data;
        }
    }

    async getVerifiedProfiles() {
        try {
            const token = localStorage.getItem('token');

            if (!token) {
                throw new Error('No token saved!');
            }

            const authorization = {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            };

            const response = await axios.get(`${API_URL}/profile/verifiedProfiles`, authorization);

            return response.data;
        } catch (error) {
            throw error.response.data

        }
    }
}

export default new ProfileService();