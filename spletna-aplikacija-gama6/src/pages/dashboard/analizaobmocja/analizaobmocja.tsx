"use client";
import dynamic from "next/dynamic";
import { useMemo } from "react";
import { DatePickerWithRange } from "./components/daterangepicker";
import { Checkboxes } from "./components/checkboxes";
import { Button } from "@/components/ui/button";
const AnalizaObmocja = () => {
  const PolygonMap = useMemo(
    () =>
      dynamic(() => import("./map"), {
        loading: () => <p>A map is loading</p>,
        ssr: false,
      }),
    []
  );
  return (
    <div className="flex flex-row gap-6">
      <PolygonMap />
      <div
        className="flex items-start p-4 border rounded-md"
    
      >
        <div className="flex flex-col items-start gap-5">
        <div className="flex flex-row items-center gap-2">
          <p>Obdobje:</p>
          <DatePickerWithRange />
        </div>
        <div className="flex flex-row items-center gap-2">
          <p>Tip :</p>
          <Checkboxes />
        </div>
        <Button variant="default" className="w-full">Prikaži</Button>
        </div>
       
        
      </div>
    </div>
  );
};

export default AnalizaObmocja;
