<template>
    <div style="height: 92vh; position: relative;">
        <div class="map" ref="mapContainer" style="width: 100%; height: 100%; padding: 1rem;"></div>
        <button @click="recenterMap" class="recenter-button">Recenter Map</button>

        <Transition name="fade">
            <div v-if="popupVisible" class="popup" :style="{ left: popupPosition[0] + 'px', top: popupPosition[1] + 'px' }">
                <h4>{{ popupData.username }}</h4>
                <p>{{ popupData.description }}</p>
            </div>
        </Transition>

        <Transition name="fade">
            <div v-if="!hospitals.length" class="loading-hospitals">
                <div class="loading-spinner"></div>
                <span>Loading hospitals...</span>
            </div>
        </Transition>
    </div>
</template>

<script>
import 'ol/ol.css'; // Import OpenLayers CSS
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { fromLonLat } from 'ol/proj';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import Style from 'ol/style/Style';
import Icon from 'ol/style/Icon';

import ProfileService from '@/services/ProfileService';
import PostService from '@/services/PostService';
import HospitalService from '@/services/HospitalService';

export default {
    name: 'ReadOnlyMapComponent',
    data() {
        return {
            map: null,
            posts: [],
            hospitals: [],
            myLocation: {
                longitude: 0,
                latitude: 0,
            },
            popupVisible: false,               // Track popup visibility
            popupData: {},                     // Data to display in the popup
            popupPosition: { x: 0, y: 0 },     // Position of the popup
            vectorSource: new VectorSource() // Source to hold all markers
        };
    },
    mounted() {
        // Initialize the map first
        this.initMap();

        // Fetch all the data in sequence
        this.fetchData();
    },
    beforeDestroy() {
        if (this.map) {
            this.map.setTarget(null);
        }
    },
    methods: {
        async fetchData() {
            try {
                await this.findMyLocation();
                await this.addPostPins();
                await this.fetchHospitals();
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        },
        
        // Get user's location and set marker
        async findMyLocation() {
            const profileResponse = await ProfileService.getMyProfile();
            this.myLocation = profileResponse.address;

            this.addMyLocationMarker();

            // Recenter the map to user's location
            const centerCoordinates = fromLonLat([this.myLocation.longitude, this.myLocation.latitude]);
            this.map.getView().setCenter(centerCoordinates);
            
            const postsResponse = await PostService.getPostsNearby(this.myLocation.latitude, this.myLocation.longitude);
            this.posts = postsResponse;
        },

        // Add a marker for the user's location
        addMyLocationMarker() {
            const centerCoordinates = fromLonLat([this.myLocation.longitude, this.myLocation.latitude]);

            // Add marker at myLocation
            const myLocationMarker = new Feature({
                geometry: new Point(centerCoordinates),
                data: {  // Store relevant data in the feature
                    username: 'My Location',
                    description: 'This is where you are',
                    isHouse: true
                }
            });

            myLocationMarker.setStyle(
                new Style({
                    image: new Icon({
                        src: 'house.png',
                        anchor: [0.5, 1],
                        scale: 0.1
                    }),
                })
            );

            this.vectorSource.addFeature(myLocationMarker); // Add my location marker to vector source
        },

        // Add post markers to the map
        async addPostPins() {
            for (let post of this.posts) {
                if (post.address) {
                    const postCoordinates = fromLonLat([post.address.longitude, post.address.latitude]);

                    const postMarker = new Feature({
                        geometry: new Point(postCoordinates),
                        data: {  // Store relevant data in the feature
                            username: post.username,
                            description: post.description,
                            isHouse: false
                        }
                    });

                    postMarker.setStyle(
                        new Style({
                            image: new Icon({
                                src: 'pin.png',
                                anchor: [0.5, 1],
                                scale: 0.3
                            }),
                        })
                    );

                    this.vectorSource.addFeature(postMarker); // Add post marker to vector source
                }
            }
        },

        // Fetch hospitals and add markers
        async fetchHospitals() {
            const response = await HospitalService.getHospitals();
            this.hospitals = response;

            console.log("Hospitals:", this.hospitals);
            this.addHospitalPins();
        },

        // Add hospital markers to the map
        addHospitalPins() {
            for (let hospital of this.hospitals) {
                if (hospital.location) {
                    const hospitalCoordinates = fromLonLat([hospital.location.longitude, hospital.location.latitude]);

                    const hospitalMarker = new Feature({
                        geometry: new Point(hospitalCoordinates),
                        data: {  // Store relevant data in the feature
                            username: hospital.name,
                            description: hospital.description,
                            isHouse: false
                        }
                    });

                    hospitalMarker.setStyle(
                        new Style({
                            image: new Icon({
                                src: 'veterinarian.png',
                                anchor: [0.5, 1],
                                scale: 0.07
                            }),
                        })
                    );

                    this.vectorSource.addFeature(hospitalMarker); // Add hospital marker to vector source
                }
            }
        },

        // Initialize the map
        initMap() {
            const centerCoordinates = fromLonLat([this.myLocation.longitude, this.myLocation.latitude]);

            // Initialize the map with OSM layer
            this.map = new Map({
                target: this.$refs.mapContainer,
                layers: [
                    new TileLayer({
                        source: new OSM(),
                    }),
                    new VectorLayer({
                        source: this.vectorSource, // Layer to hold all markers
                    }),
                ],
                view: new View({
                    center: centerCoordinates,
                    zoom: 15,
                }),
            });

            // Add pointermove event to handle hover effects
            this.map.on('pointermove', (event) => {
                const feature = this.map.forEachFeatureAtPixel(event.pixel, (feature) => feature);
                // Show popup if it's not house
                if (feature && !feature.get('data').isHouse) {
                    this.showPopup(feature.get('data'), event.pixel);
                } else {
                    this.hidePopup();
                }
            });
        },

        // Recenter the map to user's location
        recenterMap() {
            const centerCoordinates = fromLonLat([this.myLocation.longitude, this.myLocation.latitude]);
            this.map.getView().animate({
                center: centerCoordinates,
                duration: 500,
            });
        },

        // Show the popup with post details
        showPopup(post, coordinates) {
            // Set popup data and position
            this.popupData = {
                username: post.username,
                description: post.description,
            };
            this.popupPosition = this.map.getCoordinateFromPixel(this.map.getPixelFromCoordinate(coordinates));
            this.popupVisible = true;
        },

        // Hide the popup
        hidePopup() {
            this.popupVisible = false;
        },
    },
};
</script>

<style>
.recenter-button {
    position: absolute;
    bottom: 2rem;
    left: 2rem;
    z-index: 1000;
    padding: 8px 16px;
    font-size: 14px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.recenter-button:hover {
    background-color: #0056b3;
}

.popup h4 {
    font: 18px/1.4 'Helvetica Neue', Arial, Helvetica, sans-serif;
}

.popup {
    position: absolute;
    background: white;
    border: 1px solid #ccc;
    border-radius: 4px;
    padding: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    z-index: 1001;
    max-width: 20rem;
    cursor: default;
    font: 14px/1.4 'Helvetica Neue', Arial, Helvetica, sans-serif;
    user-select: none;
}

/* Loading indicator styles */
.loading-hospitals {
    position: absolute;
    bottom: 3rem;
    right: 2rem;
    z-index: 1000;
    background: rgba(255, 255, 255, 0.9);
    padding: 0.75rem 1.25rem;
    border-radius: 4px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-size: 14px;
    color: #6c757d;
}

.loading-spinner {
    width: 1rem;
    height: 1rem;
    border: 2px solid #f3f3f3;
    border-top: 2px solid #007bff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}

.fade-enter-active {
    transition: opacity 0.5s;
}

.fade-leave-active {
    transition: opacity 1.5s;
}

.fade-enter-from, .fade-leave-to {
    opacity: 0;
}

.fade-enter-to, .fade-leave-from {
    opacity: 1;
}
</style>