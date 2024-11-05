<template>
    <div style="height: 92vh;">
        <div class="map" ref="mapContainer" style="width: 100%; height: 100%;"></div>
    </div>
</template>

<script>
import 'ol/ol.css'; // Import OpenLayers CSS
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { fromLonLat } from 'ol/proj'; // Import projection functions

export default {
    name: 'ReadOnlyMapComponent',
    data() {
        return {
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
            // Remove the map from the DOM
            this.map.setTarget(null);
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
                ],
                view: new View({
                    center: fromLonLat([19.8227, 45.2396]),
                    zoom: 15,
                }),
            });
        },
    },
};

</script>

<style>
</style>