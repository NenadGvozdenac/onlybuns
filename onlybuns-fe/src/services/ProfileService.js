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

            console.log('My profile:', response.data);

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

    async getVerifiedProfiles(filters = {}) {
        try {
            const token = localStorage.getItem('token');
            if (!token) throw new Error('No token saved!');

            const authorization = {
                headers: { Authorization: `Bearer ${token}` }
            };

            // Filter out undefined or null values from the filters object
            const validFilters = Object.fromEntries(
                Object.entries(filters).filter(([key, value]) => value !== undefined && value !== null && value !== '')
            );

            const queryParams = new URLSearchParams(validFilters).toString();

            const response = await axios.get(`${API_URL}/profile/verifiedProfiles?${queryParams}`, authorization);
            return response.data;
        } catch (error) {
            console.error('Error fetching verified profiles:', error);
            throw error.response ? error.response.data : error;
        }
    }

    async followProfile(username) {
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
    
            const response = await axios.put(`${API_URL}/profile/follow?username=${username}`, {}, authorization);
    
            return response.data;
        } catch (error) {
            console.error('Error following profile:', error);
            if (error.response) {
                throw error.response;
            } else {
                throw error;
            }
        }
    }

    async unfollowProfile(username) {
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

            const response = await axios.put(`${API_URL}/profile/unfollow?username=${username}`, {}, authorization);
            return response.data;
        } catch (error) {
            console.error('Error unfollowing profile:', error);
            throw error.response ? error.response.data : error;
        }
    }

}

export default new ProfileService();