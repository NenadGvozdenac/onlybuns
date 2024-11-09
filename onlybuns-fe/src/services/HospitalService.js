import axios from 'axios';

const API_URL = 'http://localhost:8080/api/fetch-vets';

class HospitalService {
    async getHospitals() {
        try {
            let token = localStorage.getItem('token');

            if (!token) {
                throw new Error("No token saved!");
            }

            const headers = {
                Authorization: `Bearer ${token}`,
            }

            const response = await axios.get(`${API_URL}`, {
                headers,
            });

            return response.data;
        } catch (error) {
            console.error("Error fetching nearby hospitals:", error);
            throw error;
        }
    }
}

export default new HospitalService();