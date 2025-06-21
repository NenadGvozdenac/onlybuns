import axios from 'axios';

const API_URL = 'http://localhost:8080/post'; 

class CardService {

    async fetchPosts() {
        try {
            const response = await axios.get(`${API_URL}/all`);
            console.log("Response:", response.data);
            return response.data;
        } catch (error) {
            console.error("Error fetching cards:", error);
            throw error;
        }
    }

    async fetchFollowingPosts() {
        try {
            const token = localStorage.getItem('token');
        
            const headers = {
                Authorization: `Bearer ${token}`,
            };

            const response = await axios.get(`${API_URL}/following`, { headers });
            return response.data;
        } catch (error) {
            console.error("Error fetching following cards:", error);
            throw error;
        }
    }

    async likePost(id) {
        try {

            const token = localStorage.getItem('token'); 
        
            const headers = {
                Authorization: `Bearer ${token}`,
            };
    
            const response = await axios.post(`${API_URL}/${id}/like`, {}, { headers });
    
            return response.data; 
        } catch (error) {
            console.error(`Error liking post with ID ${id}:`, error);
            throw error;
        }
    }
    
    async unlikePost(id) {
        try {
            const token = localStorage.getItem('token');
        
            const headers = {
                Authorization: `Bearer ${token}`,
            };
    
            const response = await axios.delete(`${API_URL}/${id}/unlike`, { headers });
    
            return response.data; 
        } catch (error) {
            console.error(`Error unliking post with ID ${id}:`, error);
            throw error;
        }
    }

    async deletePost(id){
        try {
            const token = localStorage.getItem('token');
        
            const headers = {
                Authorization: `Bearer ${token}`,
            };
    
            const response = await axios.delete(`${API_URL}/${id}`, { headers });
    
            return response.data; 
        } catch (error) {
            console.error(`Error deleting post with ID ${id}:`, error);
            throw error;
        }
    }

    async updatePost(id, description) {
        try {
            const token = localStorage.getItem('token');
        
            const headers = {
                Authorization: `Bearer ${token}`,
            };

            let post = {
                id: id,
                description: description
            }
            
            const response = await axios.put(`${API_URL}`, post, { headers });
    
            return response.data; 
        } catch (error) {
            console.error(`Error updating post with ID ${id}:`, error);
            throw error;
        }
    }
    async postComment(id, commentText){
        try {
            const token = localStorage.getItem('token');
        
            const headers = {
                Authorization: `Bearer ${token}`,
            };

            let comment = {
                text: commentText
            }
    
            const response = await axios.post(`${API_URL}/${id}/comment`, comment, { headers });
    
            return response.data; 
        } catch (error) {
            console.error(`Error posting comment on post with ID ${id}:`, error);
            throw error;
        }
    }
}

export default new CardService();