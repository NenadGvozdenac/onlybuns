<template>
    <div style="height: 92vh; position: relative;">
        <div class="map" ref="mapContainer" style="width: 100%; height: 100%; padding: 1rem;"></div>
        <!-- Button to recenter the map -->
        <button @click="recenterMap" class="recenter-button">Recenter Map</button>
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
import CardService from '@/services/CardService';

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
                console.log('My location:', response.address);
                this.myLocation = response.address;
            });

            await CardService.fetchPosts().then((response) => {
                console.log('Posts locations: ', response);
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
        addPostPins() {
            // Loop through posts and add a marker for each post address
            this.posts.forEach(post => {
                if (post.address) {
                    const postCoordinates = fromLonLat([post.address.longitude, post.address.latitude]);

                    const postMarker = new Feature({
                        geometry: new Point(postCoordinates),
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
</style>
