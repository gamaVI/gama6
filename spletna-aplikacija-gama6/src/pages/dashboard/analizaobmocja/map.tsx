/* eslint-disable */
"use client";
import React, { useState } from "react";
import {
  MapContainer,
  TileLayer,
  FeatureGroup,
  Marker,
  Popup,
} from "react-leaflet";
import { EditControl } from "react-leaflet-draw";
import osm from "./osm-providers";
import { useRef } from "react";
import "leaflet/dist/leaflet.css";
import "leaflet-draw/dist/leaflet.draw.css";
import "leaflet-defaulticon-compatibility";
import "leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.css";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardTitle,
  CardHeader,
} from "@/components/ui/card";
import {
  TableRow,
  Table,
  TableCell,
  TableBody,
  TableHeader,
} from "@/components/ui/table";
const PolygonMap = ({ mapLayers, setMapLayers, transactions }) => {
  console.log(transactions);

  const [center, setCenter] = useState({ lat: 46.056946, lng: 14.505751 });

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
      <div
        className="row "
        style={{
          width: "100%",
        }}
      >
        <div className="col text-center">
          <div className="col">
            <MapContainer
              center={center}
              zoom={ZOOM_LEVEL}
              ref={mapRef}
              style={{
                height: "70vh",
                width: "100%",
                borderRadius: "10px",
              }}
            >
              <FeatureGroup>
                <EditControl
                  position="topright"
                  onCreated={_onCreate}
                  onEdited={_onEdited}
                  onDeleted={_onDeleted}
                  draw={{
                    rectangle: false,
                    polyline: false,
                    circle: false,
                    circlemarker: false,
                    marker: false,
                  }}
                />
              </FeatureGroup>

              <TileLayer
                url={osm.maptiler.url}
                attribution={osm.maptiler.attribution}
              />

              {transactions.map((transaction) => {
                return (
                  <Marker position={[transaction.gps.lat, transaction.gps.lng]}>
                   <Popup>
  <Card key={transaction.id}>
    <CardHeader>
      <CardTitle>{transaction.componentType}</CardTitle>
      <CardDescription>
        {transaction.address}
      </CardDescription>
    </CardHeader>
    <CardContent>
    <Table>
      <TableHeader>
        <TableRow>
          <TableCell>Velikost (m²)</TableCell>
          <TableCell>Cena na m²</TableCell>
          <TableCell>Leto gradnje</TableCell>
          <TableCell>Vrednost transakcije </TableCell>
          <TableCell></TableCell>
        </TableRow>
      </TableHeader>
      <TableBody>
          <TableRow key={transaction.id}>
          <TableCell>{transaction.unitRoomsSumSize}</TableCell>
            <TableCell>{transaction.estimatedAmountM2.toFixed(2)}</TableCell>
            <TableCell>{transaction.buildingYearBuilt}</TableCell>
            <TableCell>{transaction.transactionAmountGross?.toFixed(2) + "€"}</TableCell>
          </TableRow>
      </TableBody>
    </Table>
    </CardContent>
    <CardFooter className="flex flex-col items-start">
      <p>
        Datum transakcije:{" "}
        {new Date(transaction.transactionDate).toLocaleDateString()}
      </p>
      <p>
        V transakcijo so ključeni naslednji predmeti:{" "}
        {transaction.transactionItemsList.join(", ")}
        {transaction.unitRooms.replaceAll("|", ", ")}
      </p>
      <br/>
     
    </CardFooter>
  </Card>
</Popup>

                  </Marker>
                );
              })}
            </MapContainer>
          </div>
        </div>
      </div>
    </>
  );
};

export default PolygonMap;
