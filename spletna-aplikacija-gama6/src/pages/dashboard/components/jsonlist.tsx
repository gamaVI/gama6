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
          <Card key={listItem?.id}>

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
