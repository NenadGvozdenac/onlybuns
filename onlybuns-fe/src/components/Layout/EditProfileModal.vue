<!-- EditProfileModal.vue -->
<template>
    <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editProfileModalLabel">Edit Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form @submit.prevent="handleSubmit">
                        <div class="mb-3">
                            <label for="name" class="form-label">Name</label>
                            <input type="text" class="form-control" id="name" v-model="formData.name" required>
                        </div>
                        <div class="mb-3">
                            <label for="surname" class="form-label">Last Name</label>
                            <input type="text" class="form-control" id="surname" v-model="formData.surname" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" v-model="formData.password">
                        </div>
                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">Confirm Password</label>
                            <input type="password" class="form-control" id="confirmPassword"
                                v-model="formData.confirmPassword">
                        </div>
                        <div class="mb-3">
                            <label for="street" class="form-label">Street</label>
                            <input type="text" class="form-control" id="street" v-model="formData.address.street"
                                required>
                        </div>
                        <div class="row mb-3">
                            <div class="col">
                                <label for="number" class="form-label">Number</label>
                                <input type="number" class="form-control" id="number" v-model="formData.address.number"
                                    required>
                            </div>
                            <div class="col">
                                <label for="city" class="form-label">City</label>
                                <input type="text" class="form-control" id="city" v-model="formData.address.city"
                                    required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="country" class="form-label">Country</label>
                            <input type="text" class="form-control" id="country" v-model="formData.address.country"
                                required>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Save changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'EditProfileModal',
    props: {
        userData: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            formData: {
                username: '',
                name: '',
                surname: '',
                password: '',
                confirmPassword: '',
                address: {
                    street: '',
                    number: 0,
                    city: '',
                    country: ''
                }
            }
        }
    },
    watch: {
        userData: {
            handler(newValue) {
                this.formData = {
                    username: newValue.username,
                    name: newValue.name,
                    surname: newValue.surname,
                    password: '',
                    confirmPassword: '',
                    address: { ...newValue.address }
                }
            },
            immediate: true
        }
    },
    methods: {
        handleSubmit() {
            if (this.formData.password !== this.formData.confirmPassword) {
                alert('Passwords do not match!')
                return
            }

            const updateData = {
                ...this.formData,
                address: { ...this.formData.address }
            }

            delete updateData.username

            this.$emit('update-profile', updateData)
        },
        closeModal() {
            // Reset form data to initial values
            this.formData = {
                username: this.userData.username,
                name: this.userData.name,
                surname: this.userData.surname,
                password: '',
                confirmPassword: '',
                address: { ...this.userData.address }
            }

            // Close the modal using Bootstrap
            const modal = document.getElementById('editProfileModal')
            const bootstrapModal = bootstrap.Modal.getInstance(modal)
            if (bootstrapModal) {
                bootstrapModal.hide()
            }
        }
    }
}
</script>