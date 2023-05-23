"use client";
import dynamic from "next/dynamic";
import { useMemo } from "react";
import { DatePickerWithRange } from "./components/daterangepicker";
import { Checkboxes } from "./components/checkboxes";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
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
        <div className="flex row gap-2">
        <div className="grid w-full max-w-sm items-center gap-1.5">
      <Label htmlFor="email">Cena od</Label>
      <Input type="email" id="email" placeholder="10000" />
    </div>
    <div className="grid w-full max-w-sm items-center gap-1.5">
      <Label htmlFor="email">Cena od</Label>
      <Input type="email" id="email" placeholder="1000000" />
    </div>
    </div>
        <Button variant="default" className="w-full">Prikaži</Button>
        </div>
       
        
      </div>
    </div>
  );
};

export default AnalizaObmocja;
