<template>
    <div style="height: 100%;">
        <div class="map" ref="mapContainer" style="width: 100%; height: 100%;"></div>
        <div v-if="locationInfo" class="location-info">
            {{ locationInfo.street }}, {{ locationInfo.number }}, {{ locationInfo.city }}, {{ locationInfo.country }}
        </div>
    </div>
</template>

<script>
import 'ol/ol.css'; // Import OpenLayers CSS
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { fromLonLat, toLonLat } from 'ol/proj'; // Import projection functions
import { Feature } from 'ol';
import { Point } from 'ol/geom';
import { Icon, Style } from 'ol/style';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';

export default {
    name: 'MapComponent',
    data() {
        return {
            locationInfo: null, // Store location info for display
            markerCoordinates: null, // Store the coordinates of the marker
            vectorSource: new VectorSource(), // Source for vector layer
            marker: null, // Reference to the marker feature
            map: null, // Store the map instance
        };
    },
    mounted() {
        // Initialize the map when the component is mounted
        this.initMap();
    },
    beforeDestroy() {
        // Clean up resources when the component is destroyed
        if (this.map) {
            // Remove any click event listener
            this.map.un('click', this.handleMapClick);
        }
    },
    methods: {
        initMap() {
            this.map = new Map({
                target: this.$refs.mapContainer,
                layers: [
                    new TileLayer({
                        source: new OSM(),
                    }),
                    new VectorLayer({
                        source: this.vectorSource,
                    }),
                ],
                view: new View({
                    center: fromLonLat([19.8227, 45.2396]),
                    zoom: 12,
                }),
            });

            // Set cursor to point when hovering over the map
            this.$refs.mapContainer.style.cursor = 'pointer';

            // Store the click event handler for cleanup
            this.handleMapClick = (event) => {
                this.addMarker(event.coordinate);
                this.getLocationInfo(event.coordinate); // Get location info on click
            };

            // Add a click event listener to the map
            this.map.on('click', this.handleMapClick);
        },
        addMarker(coordinate) {
            // Convert the coordinate to lon/lat
            const lonLat = toLonLat(coordinate);
            this.markerCoordinates = lonLat; // Store the coordinates

            if (this.marker) {
                // If the marker already exists, update its position
                this.marker.setGeometry(new Point(coordinate));
            } else {
                // Create a feature for the marker
                this.marker = new Feature({
                    geometry: new Point(coordinate),
                });

                // Set the marker style with a Pin icon
                this.marker.setStyle(new Style({
                    image: new Icon({
                        src: '/pin.png',
                        scale: 0.20,
                    }),
                }));

                // Add the marker to the vector source
                this.vectorSource.addFeature(this.marker);
            }
        },
        emitLocationInfo(lonLat) {
            this.$emit('location-updated', {
                longitude: lonLat[0],
                latitude: lonLat[1],
                street: this.locationInfo.street,
                number: this.locationInfo.number,
                city: this.locationInfo.city,
                country: this.locationInfo.country,
            });
        },
        async getLocationInfo(coordinate) {
            const lonLat = toLonLat(coordinate);
            try {
                const response = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lon=${lonLat[0]}&lat=${lonLat[1]}`);
                if (response.ok) {
                    const data = await response.json();
                    this.locationInfo = {
                        street: data.address.road || 'N/A',
                        number: data.address.house_number || 'N/A',
                        city: data.address.city || data.address.town || data.address.village || 'N/A',
                        country: data.address.country || 'N/A',
                        longitude: lonLat[0],
                        latitude: lonLat[1],
                    };

                    this.$emit('location-updated', this.locationInfo);
                } else {
                    console.error('Error fetching location info:', response.status);
                }
            } catch (error) {
                console.error('Error fetching location info:', error);
            }
        },
    },
};
</script>

<style scoped>
.map {
    border: 1px dashed #000000;
}

.location-info {
    margin-top: 10px;
    font-size: 1rem;
}
</style>
