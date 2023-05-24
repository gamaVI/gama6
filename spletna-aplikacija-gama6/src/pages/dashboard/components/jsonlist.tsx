import React from 'react';
import { api } from '~/utils/api';
import {Card, CardContent,CardTitle} from "@/components/ui/card"


interface ListItem {
    id: string;
    [key: string]: any;
  }
  
  interface JsonListProps {
    list: ListItem[];
  }
const JsonList:React.FC<JsonListProps>  =({list}) => {

  return (
    <div>
      <ul className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
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

            <CardContent className="overflow-scroll">
            <pre>
             {JSON.stringify(listItem,null,2).replace("{","").replace("}","").replaceAll(" ","")}
             </pre>
            </CardContent>
          </Card>
          
        ))}
      </ul>
    </div>
  );
};

export default JsonList;
