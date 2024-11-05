<template>
    <div style="height: 92vh; position: relative;">
        <div class="map" ref="mapContainer" style="width: 100%; height: 100%; padding: 1rem;"></div>
        <button @click="recenterMap" class="recenter-button">Recenter Map</button>

        <div v-if="popupVisible" class="popup" :style="{ left: popupPosition[0] + 'px', top: popupPosition[1] + 'px' }">
            <h4>{{ popupData.username }}</h4>
            <p>{{ popupData.description }}</p>
        </div>
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

export default {
    name: 'ReadOnlyMapComponent',
    data() {
        return {
            map: null,
            posts: [],
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
        this.findMyLocation().then(() => {
            this.initMap();
            this.addPostPins();
        });
    },
    beforeDestroy() {
        if (this.map) {
            this.map.setTarget(null);
        }
    },
    methods: {
        async findMyLocation() {
            await ProfileService.getMyProfile().then((response) => {
                this.myLocation = response.address;
            });

            await PostService.getPostsNearby(this.myLocation.latitude, this.myLocation.longitude).then((response) => {
                this.posts = response;
            });
        },
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

            // Add pointermove event to handle hover effects
            this.map.on('pointermove', (event) => {
                const feature = this.map.forEachFeatureAtPixel(event.pixel, (feature) => feature);
                // Show popup if its not house

                if (feature && !feature.get('data').isHouse) {
                    this.showPopup(feature.get('data'), event.pixel);
                } else {
                    this.hidePopup();
                }
            });
        },
        addPostPins() {
            // Loop through posts and add a marker for each post address
            this.posts.forEach(post => {
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

                    // Set pin style to use pin.png
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
            });
        },
        recenterMap() {
            const centerCoordinates = fromLonLat([this.myLocation.longitude, this.myLocation.latitude]);
            this.map.getView().animate({
                center: centerCoordinates,
                duration: 500,
            });
        },
        showPopup(post, coordinates) {
            // Set popup data and position
            this.popupData = {
                username: post.username,
                description: post.description,
            };
            console.log('Popup data:', this.popupData);
            this.popupPosition = this.map.getCoordinateFromPixel(this.map.getPixelFromCoordinate(coordinates));
            this.popupVisible = true;
        },
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
</style>
