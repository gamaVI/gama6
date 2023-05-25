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
    from: new Date(2022, 0, 20),
    to: addDays(new Date(2022, 0, 20), 20),
  })

  const [priceFrom, setPriceFrom] = useState<number | undefined>(10)
  const [priceTo, setPriceTo] = useState<number | undefined>(10000000)
  const [enabledTypes, setEnabledTypes] = useState<number[]>([])
  const [mapLayers, setMapLayers] = useState([]);
  const [transactions , setTransactions] = useState<any[]>([])

  const getTransactionsInPolygon = api.transactions.getTransactionsInPolygon.useMutation({
    onSuccess: (data) => {

      const filteredTransactions = data.filter((transaction) => {
        const transactionDate = dayjs(transaction.transactionDate);
        const transactionPrice = transaction.transactionAmountGross;
  
        return (
          (transactionDate.isSame(date.from, 'day') ||  transactionDate.isAfter(date.from, 'day'))
          &&
          transactionDate.isSame(date.to, 'day') ||  transactionDate.isBefore(date.to, 'day')
          &&
          transactionPrice >= priceFrom &&
          transactionPrice <= priceTo
        );
      });
      

      setTransactions(filteredTransactions || [])
    }

  });

  const submitSearch = async () => {
    
      await getTransactionsInPolygon.mutate(
       mapLayers[0].latlngs)
      
      
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
        <div className="flex flex-row items-center gap-2">
          <p>Tip :</p>
          <Checkboxes  enabledTypes={enabledTypes} 
          setEnabledTypes={setEnabledTypes}
          />
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
        <Button variant="default" className="w-full mt-auto" onClick={()=>{
          void submitSearch()
        }}>Prikaži</Button>
        </div>
       
        
      </div>
    </div>
    {
      transactions && <Overview transactions={transactions} dateFrom={date.from} dateTo={date.to} />
    }
    
  
    </>
  );
};

export default AnalizaObmocja;
