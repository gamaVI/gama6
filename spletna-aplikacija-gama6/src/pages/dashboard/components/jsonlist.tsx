/* eslint-disable react/no-array-index-key */
import React from 'react';
import { api } from '~/utils/api';
import {Card, CardContent,CardTitle} from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHeader, TableRow } from "@/components/ui/table";
import { CardDescription, CardFooter, CardHeader } from '@/components/ui/card';
import dynamic from "next/dynamic";
import { useMemo } from "react";
import TransactionMarkerMap from './transactionmarkermap';


interface ListItem {
    id: string;
    [key: string]: any;
  }
  
  interface JsonListProps {
    list: ListItem[];
  }
const TransactionList:React.FC<JsonListProps>  =({list}) => {
  console.log(list);
  const TransactionMarkerMap = useMemo(
    () =>
      dynamic(() => import("./transactionmarkermap"), {
        loading: () => <p>A map is loading</p>,
        ssr: false,
      }),
    []
  );

  return (
    <div>
      <ul className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-2">
        {list?.map((listItem) => (
         <Card key={listItem.id}>
         <CardHeader>
           <CardTitle>{"Stanovanjska hiša"}</CardTitle>
           <CardDescription>{listItem.address}</CardDescription>
         </CardHeader>
         <CardContent>
           <Table>
             <TableBody>
               <TableRow>
                 <TableCell>Transaction Amount M2</TableCell>
                 <TableCell>{"90" }</TableCell>
               </TableRow>
               <TableRow>
                 <TableCell>Estimated Amount M2</TableCell>
                 <TableCell>{"100"}</TableCell>
               </TableRow>
                <TableRow>
                  <TableCell>Leto gradnje</TableCell>
                  <TableCell>{"1991"}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>Leto gradnje</TableCell>
                  <TableCell>{"1991"}</TableCell>
                </TableRow>
                <TableRow>
                  <TableCell>{"Vrednost transakcije (bruto)"}</TableCell>
                  <TableCell>{"123,123€"}</TableCell>
                </TableRow>
                <TableRow>
                  <TransactionMarkerMap />
                </TableRow>


             </TableBody>
             
           </Table>
         </CardContent>
         <CardFooter>
           <p>Datum transakcije: {
            new Date(listItem.transactionDate).toLocaleDateString()
            }</p>
         </CardFooter>
       </Card>
          
        ))}
      </ul>
    </div>
  );
};

export default TransactionList;
