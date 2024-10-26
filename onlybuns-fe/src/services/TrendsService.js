import axios from 'axios';

const API_URL = 'http://localhost:8080';

class TrendsService {
    async getTrends() {
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

            const response = await axios.get(`${API_URL}/trends`, authorization);

            return response.data;
        } catch (error) {
            throw error.response.data;
        }
    }
}

export default new TrendsService();