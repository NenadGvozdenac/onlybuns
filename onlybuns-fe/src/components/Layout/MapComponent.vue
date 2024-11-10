<template>
    <div style="height: 100%;">
        <div class="map" ref="mapContainer" style="width: 100%; height: 100%;"></div>
    </div>
</template>

<script>
import 'ol/ol.css';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import { fromLonLat, toLonLat } from 'ol/proj';
import { Feature } from 'ol';
import { Point } from 'ol/geom';
import { Icon, Style } from 'ol/style';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';

export default {
    name: 'MapComponent',
    props: {
        address: {
            type: Object,
            default: null,
            validator: function (value) {
                if (!value) return true;
                return [
                    'street',
                    'city',
                    'number',
                    'country',
                    'longitude',
                    'latitude'
                ].every(key => key in value);
            }
        },
        initialZoom: {
            type: Number,
            default: 12
        }
    },
    data() {
        return {
            locationInfo: null,
            markerCoordinates: null,
            vectorSource: new VectorSource(),
            marker: null,
            map: null,
            isInitialLoad: true
        };
    },
    mounted() {
        this.initMap();
    },
    beforeDestroy() {
        this.destroyMap();
    },
    methods: {
        destroyMap() {
            if (this.map) {
                // Remove any event listeners
                this.map.off();

                // Remove any markers
                if (this.marker) {
                    this.marker.remove();
                }

                // Remove the map
                this.map.remove();

                // Clear the references
                this.map = null;
                this.marker = null;
            }
        },
        initMap() {
            // Use default coordinates if no address is provided
            const defaultCoords = [19.8227, 45.2396];
            const initialCoords = this.address
                ? [this.address.longitude, this.address.latitude]
                : defaultCoords;

            const initialCenter = fromLonLat(initialCoords);

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
                    center: initialCenter,
                    zoom: this.initialZoom,
                }),
            });

            this.$refs.mapContainer.style.cursor = 'pointer';

            this.handleMapClick = (event) => {
                this.addMarker(event.coordinate);
                this.getLocationInfo(event.coordinate);
            };

            this.map.on('click', this.handleMapClick);

            // Add initial marker if address is provided
            if (this.address) {
                this.locationInfo = { ...this.address };
                this.addMarker(initialCenter);
                this.isInitialLoad = false;
            }
        },
        addMarker(coordinate) {
            const lonLat = toLonLat(coordinate);
            this.markerCoordinates = lonLat;

            if (this.marker) {
                this.marker.setGeometry(new Point(coordinate));
            } else {
                this.marker = new Feature({
                    geometry: new Point(coordinate),
                });

                this.marker.setStyle(new Style({
                    image: new Icon({
                        src: '/pin.png',
                        scale: 0.20,
                    }),
                }));

                this.vectorSource.addFeature(this.marker);
            }
        },
        emitLocationInfo(lonLat) {
            this.$emit('location-updated', {
                longitude: lonLat[0],
                latitude: lonLat[1],
                street: this.locationInfo?.street,
                number: this.locationInfo?.number,
                city: this.locationInfo?.city,
                country: this.locationInfo?.country,
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
        }
    },
    watch: {
        address: {
            handler(newAddress) {
                if (newAddress && this.map) {
                    const coordinate = fromLonLat([newAddress.longitude, newAddress.latitude]);

                    // Only center the map on the first load
                    if (this.isInitialLoad) {
                        this.map.getView().setCenter(coordinate);
                        this.isInitialLoad = false;
                    }

                    this.addMarker(coordinate);
                    this.locationInfo = { ...newAddress };
                }
            },
            immediate: true
        }
    }
};
</script>

<style scoped>
.map {
    border: 1px dashed #000000;
}
</style>