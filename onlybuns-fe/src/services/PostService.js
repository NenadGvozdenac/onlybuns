import axios from 'axios';

const API_URL = 'http://localhost:8080/post';

class PostService {
    async getPostsNearby(latitude, longitude, radius) {
        try {
            let token = localStorage.getItem('token');

            if (!token) {
                throw new Error("No token saved!");
            }

            const headers = {
                Authorization: `Bearer ${token}`,
            }

            const response = await axios.get(`${API_URL}/nearby`, {
                headers,
                params: {
                    latitude,
                    longitude,
                    radius,
                }
            });

            return response.data;
        } catch (error) {
            console.error("Error fetching nearby posts:", error);
            throw error;
        }
    }
    // Add createPost method to PostService
    async createPost(postData) {
        try {
            let token = localStorage.getItem('token');

            if (!token) {
                throw new Error("No token saved!");
            }

            const headers = {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'multipart/form-data', // Ensure content type is multipart
            };

            // Send the POST request with form data
            console.log("Post data:", postData);
            const response = await axios.post(`${API_URL}/create`, postData, { headers });
            return response.data;
        } catch (error) {
            console.error("Error creating post:", error);
            throw error;
        }
    }
}

export default new PostService();