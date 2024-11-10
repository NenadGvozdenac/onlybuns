<template>
    <NavbarAuthorized />
    <div class="container mt-4">
        <div class="card border-0 shadow-sm">
            <div class="card-header bg-white border-0 py-3">
                <div class="d-flex align-items-center">
                    <div class="bg-primary bg-opacity-10 p-2 rounded-circle me-3">
                        <i class="bi bi-plus-circle text-primary"></i>
                    </div>
                    <h5 class="mb-0">Create New Post</h5>
                </div>
            </div>
            <div class="card-body p-4">
                <!-- Description Input -->
                <div class="mb-4">
                    <label class="form-label small text-muted fw-medium">Description</label>
                    <textarea v-model="description" class="form-control shadow-none" rows="3"
                        placeholder="What's on your mind?"></textarea>
                </div>

                <!-- Image Upload -->
                <div class="mb-4">
                    <label class="form-label small text-muted fw-medium">Upload Image</label>
                    <div class="d-flex align-items-center">
                        <div class="position-relative" style="width: 100%;">
                            <input type="file" ref="fileInput" @change="handleImageUpload" accept="image/*"
                                class="d-none">
                            <div @click="$refs.fileInput.click()"
                                class="upload-area border rounded-3 p-4 text-center cursor-pointer"
                                :class="{ 'has-image': imagePreview }">
                                <template v-if="!imagePreview">
                                    <i class="bi bi-cloud-upload fs-2 text-primary mb-2"></i>
                                    <p class="mb-0">Click to upload an image</p>
                                    <small class="text-muted">Maximum file size: 5MB</small>
                                </template>
                                <img v-else :src="imagePreview" alt="Preview" class="img-preview">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Location Picker -->
                <div class="mb-4">
                    <label class="form-label small text-muted fw-medium">Location</label>
                    <MapComponent class="border rounded-3" style="height: 300px;" @location-updated="updateLocation" />
                </div>

                <!-- Error Alert -->
                <div v-if="error" class="alert alert-danger d-flex align-items-center" role="alert">
                    <i class="bi bi-exclamation-circle-fill me-2"></i>
                    {{ error }}
                </div>

                <!-- Submit Button -->
                <div class="d-flex justify-content-end">
                    <button @click="createPost" class="btn btn-primary px-4 d-flex align-items-center gap-2"
                        :disabled="isLoading">
                        <span v-if="isLoading" class="spinner-border spinner-border-sm"></span>
                        <i v-else class="bi bi-send"></i>
                        Create Post
                    </button>
                </div>
            </div>
        </div>
    </div>
    <Footer />
</template>

<script>
import MapComponent from '../Layout/MapComponent.vue';
import NavbarAuthorized from '../Authorized/Navbar.vue';
import PostService from '../../services/PostService';
import Footer from '../Unauthorized/Footer.vue';
export default {
    name: 'CreatePost',
    components: {
        MapComponent,
        NavbarAuthorized,
        Footer
    },
    data() {
        return {
            description: '',
            selectedImage: null,
            imagePreview: null,
            location: {
                lat: null,
                lng: null
            },
            street: '',
            number: '',
            city: '',
            country: '',
            longitude: null,
            latitude: null,
            error: '',
            isLoading: false
        };
    },
    methods: {
        handleImageUpload(event) {
            const file = event.target.files[0];
            if (!file) return;
            if (file.size > 5 * 1024 * 1024) {
                this.error = 'Image size should be less than 5MB';
                return;
            }

            const reader = new FileReader();
            reader.onload = (e) => {
                this.imagePreview = e.target.result;
                this.selectedImage = file;
            };
            reader.readAsDataURL(file);
            this.error = '';
        }, async updateLocation(location) {
            this.location.lat = location.latitude;
            this.location.lng = location.longitude;

            try {
                // Make a fetch request to get location info (e.g., reverse geocoding)
                const locationInfo = await this.getLocationInfo(this.location.lat, this.location.lng);

                // Update location info after fetching from API
                this.locationInfo = locationInfo;
                // Emit the updated location info
                this.$emit('location-updated', this.locationInfo);
            } catch (error) {
                console.error("Error fetching location info:", error);
                this.error = "Failed to fetch location information.";
            }
        },
        async getLocationInfo(lat, lng) {
            try {
                const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lon=${lng}&lat=${lat}`);
                if (response.ok) {
                    const data = await response.json();
                    this.street= data.address.road || 'N/A',
                    this.number=data.address.house_number || 'N/A',
                    this.city= data.address.city || data.address.town || data.address.village || 'N/A',
                    this.country= data.address.country || 'N/A',
                    this.longitude= lng,
                    this.latitude= lat,
                    console.log("aaa",this.street);
                } else {
                    console.error('Error fetching location info:', response.status);
                }
            } catch (error) {
                console.error('Error fetching location info:', error);
            }
        },
        async createPost() {
            // Validation checks
            if (!this.description.trim()) {
                this.error = 'Please add a description';
                return;
            }
            if (!this.selectedImage) {
                this.error = 'Please select an image';
                return;
            }
            if (!this.location.lat || !this.location.lng) {
                this.error = 'Please select a location on the map';
                return;
            }

            try {
                this.isLoading = true;

                const formData = new FormData();
                formData.append('description', this.description);
                formData.append('image', this.selectedImage);

                console.log(this.street);
                console.log(this.number);
                console.log(this.city);
                console.log(this.country);
                console.log(this.latitude);
                console.log(this.longitude);

                formData.append('address', JSON.stringify({
                    street: this.street ,
                    number: this.number,
                    city: this.city,
                    country: this.country,
                    latitude: this.latitude,
                    longitude: this.longitude
                }));
                console.log(formData);
                await PostService.createPost(formData);

                this.description = '';
                this.selectedImage = null;
                this.imagePreview = null;
                this.location = { lat: null, lng: null };
                this.error = '';

                this.$router.push('/');

            } catch (error) {
                this.error = 'Failed to create post. Please try again.';
                console.error('Error creating post:', error);
            } finally {
                this.isLoading = false;
            }
        }
    }
};
</script>

<style scoped>
.upload-area {
    transition: all 0.3s ease;
    cursor: pointer;
}

.upload-area:hover {
    background-color: #f8f9fa;
}

.upload-area.has-image {
    padding: 0;
}

.img-preview {
    width: 100%;
    max-height: 300px;
    object-fit: cover;
    border-radius: 0.3rem;
}

:deep(.leaflet-container) {
    height: 100%;
    width: 100%;
    border-radius: 0.3rem;
}
</style>