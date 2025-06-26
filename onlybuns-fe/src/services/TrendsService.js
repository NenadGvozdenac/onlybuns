import axios from 'axios';

const API_URL = 'http://localhost:8080';

class TrendsService {
    // Helper method to map backend response properties to frontend expected properties
    mapPostData(post) {
        if (post && typeof post.markedForAdvertisement !== 'undefined') {
            return {
                ...post,
                isMarkedForAdvertisement: post.markedForAdvertisement
            };
        }
        return post;
    }

    // Helper method to map trends data containing arrays of posts
    mapTrendsData(trends) {
        if (trends) {
            const mappedTrends = { ...trends };
            if (trends.mostPopularPostsLastSevenDays && Array.isArray(trends.mostPopularPostsLastSevenDays)) {
                mappedTrends.mostPopularPostsLastSevenDays = trends.mostPopularPostsLastSevenDays.map(post => this.mapPostData(post));
            }
            if (trends.mostPopularPosts && Array.isArray(trends.mostPopularPosts)) {
                mappedTrends.mostPopularPosts = trends.mostPopularPosts.map(post => this.mapPostData(post));
            }
            return mappedTrends;
        }
        return trends;
    }

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

            return this.mapTrendsData(response.data);
        } catch (error) {
            throw error.response.data;
        }
    }
}

export default new TrendsService();