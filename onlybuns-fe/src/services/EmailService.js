import axios from 'axios';

const API_URL = 'http://localhost:8080/email';

class EmailService {
    // Send an email
    async verifyEmail(token) {
        try {
            const response = await axios.post(`${API_URL}/verify/${token}`);
            return response; // Return the response data
        } catch (error) {
            throw error.response; // Handle errors appropriately
        }
    }
    async sendSpam(){
        try {
            const response = await axios.post(`${API_URL}/sendSpamEmail`);
            return response; // Return the response data
        } catch (error) {
            throw error.response; // Handle errors appropriately
        }
    }
}

export default new EmailService();