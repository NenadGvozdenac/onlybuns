import axios from 'axios';

const API_URL = 'http://localhost:8080/chat';

class ChatService {

    async fetchMyRooms() {
        try {
            let token = localStorage.getItem('token');

            if (!token) {
                throw new Error("No token saved!");
            }

            const headers = {
                Authorization: `Bearer ${token}`,
            }

            const response = await axios.get(`${API_URL}/myRooms`, { headers });
            return response.data;
        } catch (error) {
            console.error("Error fetching my rooms:", error);
            throw error;
        }
    }

    async fetchRoom(id) {
        try {
            let token = localStorage.getItem('token');

            if (!token) {
                throw new Error("No token saved!");
            }

            const headers = {
                Authorization: `Bearer ${token}`,
            };

            const response = await axios.get(`${API_URL}/room?id=${id}`, { headers });

            return response.data;
        } catch (error) {
            console.error("Error fetching room:", error);
            throw error;
        }
    }

    async addUserToRoom(roomId, username) {
        try {
            let token = localStorage.getItem('token');
            
            if (!token) {
                throw new Error("No token saved!");
            }
    
            const headers = {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json', 
            };
    
            const data = {
                id: roomId,          
                username: username,  
            };
    
            const response = await axios.post(`${API_URL}/addUser`, data, { headers });
    
            return response.data;
        } catch (error) {
            console.error("Error adding user to chat room:", error);
            throw error;
        }
    }

    async removeUserFromRoom(roomId, username) {
        try {
            let token = localStorage.getItem('token');
            
            if (!token) {
                throw new Error("No token saved!");
            }
    
            const headers = {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json', 
            };
    
            const data = {
                id: roomId,          
                username: username,  
            };
    
            const response = await axios.post(`${API_URL}/removeUser`, data, { headers });
    
            return response.data;
        } catch (error) {
            console.error("Error removing user from chat room:", error);
            throw error;
        }
    }
    

    async fetchMessages(roomId) {
        try {
            let token = localStorage.getItem('token');
            
            if (!token) {
                throw new Error("No token saved!");
            }
    
            const headers = {
                Authorization: `Bearer ${token}`,
            };
    
            const response = await axios.get(`${API_URL}/messages?id=${roomId}`, { headers });
    
            return response.data;
        } catch (error) {
            console.error("Error getting messages:", error);
            throw error;
        }
    }

    async createRoom(name) {
        try {
            let token = localStorage.getItem('token');
            
            if (!token) {
                throw new Error("No token saved!");
            }
    
            const headers = {
                Authorization: `Bearer ${token}`,
            };

            const response = await axios.post(
                `${API_URL}/create`, 
                { name: name },  // 'name' se šalje kao JSON objekat
                { headers }
            );
    
            return response.data;
        } catch (error) {
            console.error("Error creating room:", error);
            throw error;
        }
    }
}

export default new ChatService();