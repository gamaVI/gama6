"use client";
import dynamic from "next/dynamic";
import { DatePickerWithRange } from "./components/daterangepicker";
import { Checkboxes } from "./components/checkboxes";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { addDays } from "date-fns";
import { DateRange } from "react-day-picker";
import { useState,useEffect,useMemo } from "react";
import { api } from "~/utils/api";
import { get } from "http";
import Overview from "./overview";
import dayjs from "dayjs";
/* eslint-disable  */

const AnalizaObmocja = () => {

  const [date, setDate] = useState<DateRange | undefined>({
    from: new Date(2021, 0, 20),
    to: addDays(new Date(2023, 0, 20), 20),
  })

  const [priceFrom, setPriceFrom] = useState<number | undefined>(100000)
  const [priceTo, setPriceTo] = useState<number | undefined>(200000)
  const [enabledTypes, setEnabledTypes] = useState<string[]>([])
  const [mapLayers, setMapLayers] = useState([]);
  const [transactions , setTransactions] = useState<any[]>([])

  const {data: componentTypeList} = api.transactions.getAllComponentTypes.useQuery();

 console.log("Enabled types: ", enabledTypes)

  const getTransactionsInPolygon = api.transactions.getTransactionsInPolygon.useMutation({
    onSuccess: (data) => {
      setTransactions(data || [])
    }

  });

  const submitSearch = async () => {
      await getTransactionsInPolygon.mutate({
        polygon: mapLayers[0].latlngs,
        startDate: date.from,
        endDate: date.to,
        priceFrom: priceFrom,
        priceTo: priceTo,
        componentTypes: enabledTypes
      })
  }   



  
  const PolygonMap = useMemo(
    () =>
      dynamic(() => import("./map"), {
        loading: () => <p>A map is loading</p>,
        ssr: false,
      }),
    []
  );
  return (
    <>
    <div className="flex flex-row gap-6">
      <PolygonMap transactions={transactions} mapLayers={mapLayers} setMapLayers={setMapLayers}/>
      <div
        className="flex items-start p-4 border rounded-md " style={{
          height:"70vh"
        }} 
    
      >
        <div className="flex flex-col items-start gap-5" style={{

        height:"100%"
        }}>
        <div className="flex flex-row items-center gap-2">
          <p>Obdobje:</p>
          <DatePickerWithRange className="date-picker" date={date} setDate={setDate} />
        </div>
        <div className="flex row gap-2">
        <div className="grid w-full max-w-sm items-center gap-1.5">
      <Label htmlFor="email">Cena od</Label>
      <Input type="number" id="email"   value={priceFrom} onChange={(e)=>{
        setPriceFrom(parseInt(e.target.value))
      }} />
    </div>
    <div className="grid w-full max-w-sm items-center gap-1.5">
      <Label htmlFor="email">Cena od</Label>
      <Input type="number" id="email"  value={priceTo} onChange={(e)=>{
        setPriceTo(parseInt(e.target.value))
      }}  />
    </div>
    
    </div>
    <div className="flex flex-row items-start justify-start gap-2">
          <p>Tip :</p>
          <div>
          <Checkboxes  types={componentTypeList} enabledTypes={enabledTypes}
          setEnabledTypes={setEnabledTypes}
          />
          </div>
        </div>
        <Button variant="default" className="w-full mt-auto" onClick={()=>{
          void submitSearch()
        }}>Prikaži</Button>
        </div>
       
        
      </div>
    </div>
    {
      transactions.length>0 && <Overview transactions={transactions} dateFrom={date.from} dateTo={date.to} />
    }
    
  
    </>
  );
};

export default AnalizaObmocja;
