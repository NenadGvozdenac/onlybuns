import axios from 'axios';

const API_URL = 'http://localhost:8080/post'; 

class CardService {

    async fetchPosts() {
        try {
            const response = await axios.get(`${API_URL}/all`);
            return response.data;
        } catch (error) {
            console.error("Error fetching cards:", error);
            throw error;
        }
    }

    async likePost(id) {
        try {
            // Retrieve the token from local storage
            const token = localStorage.getItem('token'); // Ensure the token is stored correctly during login
        
            // Set up the headers with the Bearer token
            const headers = {
                Authorization: `Bearer ${token}`,
            };
    
            // Make the request with headers
            const response = await axios.post(`${API_URL}/${id}/like`, {}, { headers });
    
            // Return the response data
            return response.data; 
        } catch (error) {
            console.error(`Error liking post with ID ${id}:`, error);
            throw error;
        }
    }
    

}

export default new CardService();