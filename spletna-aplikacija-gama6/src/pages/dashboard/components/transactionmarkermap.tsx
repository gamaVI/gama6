/* eslint-disable */
import React, { useState } from "react";
import { MapContainer, TileLayer, FeatureGroup } from "react-leaflet";
import { EditControl } from "react-leaflet-draw";
import osm from "../analizaobmocja/osm-providers"
import { useRef } from "react";
import "leaflet/dist/leaflet.css";
import "leaflet-draw/dist/leaflet.draw.css";

const TransactionMarkerMap = () => {

    
  const [center, setCenter] = useState({ lat:46.056946, lng: 14.505751});
  const [mapLayers, setMapLayers] = useState([]);

  const ZOOM_LEVEL = 13;
  const mapRef = useRef();

  const _onCreate = (e) => {
    console.log(e);

    const { layerType, layer } = e;
    if (layerType === "polygon") {
      const { _leaflet_id } = layer;

      setMapLayers((layers) => [
        ...layers,
        { id: _leaflet_id, latlngs: layer.getLatLngs()[0] },
      ]);
    }
  };

  const _onEdited = (e) => {
    console.log(e);
    const {
      layers: { _layers },
    } = e;

    Object.values(_layers).map(({ _leaflet_id, editing }) => {
      setMapLayers((layers) =>
        layers.map((l) =>
          l.id === _leaflet_id
            ? { ...l, latlngs: { ...editing.latlngs[0] } }
            : l
        )
      );
    });
  };

  const _onDeleted = (e) => {
    console.log(e);
    const {
      layers: { _layers },
    } = e;

    Object.values(_layers).map(({ _leaflet_id }) => {
      setMapLayers((layers) => layers.filter((l) => l.id !== _leaflet_id));
    });
  };

  return (
    <>

      <div className="row " style={{
        width: "100%",
      }}>
        <div className="col text-center">

          <div className="col">
            <MapContainer  center={center} zoom={ZOOM_LEVEL} ref={mapRef} style={{
                height: "200px",
                width: "100%",
                borderRadius: "5px",
                
            }}>
              

              <TileLayer
                url={osm.maptiler.url}
                attribution={osm.maptiler.attribution}
              />
            </MapContainer>
            
          </div>
        </div>
      </div>
    </>
  );
};

export default TransactionMarkerMap;