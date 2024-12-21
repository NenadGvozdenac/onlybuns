import axios from 'axios';

const API_URL = 'http://localhost:8080/analytics';

class AnalyticsService {

    async fetchAnalyticsData() {
        try {
            let token = localStorage.getItem('token');

            if (!token) {
                throw new Error("No token saved!");
            }

            const headers = {
                Authorization: `Bearer ${token}`,
            }

            const response = await axios.get(`${API_URL}`, { headers });
            return response.data;
        } catch (error) {
            console.error("Error fetching data:", error);
            throw error;
        }
    }
}

export default new AnalyticsService();