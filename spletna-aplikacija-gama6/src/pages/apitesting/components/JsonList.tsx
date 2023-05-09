import React from 'react';
import { api } from '~/utils/api';


interface ListItem {
    id: string;
    [key: string]: any;
  }
  
  interface JsonListProps {
    list: ListItem[];
  }
const JsonList:React.FC<JsonListProps>  =({list}) => {

  return (
    <div className="p-4">
      <ul className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {list?.map((listItem) => (
          <li key={listItem?.id} className="col-span-1 bg-white rounded-lg shadow  overflow-x-scroll overflow-y-scrol">
            <div className="w-full p-6 flex flex-col"><pre>
             {JSON.stringify(listItem,null,2)}
             </pre>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default JsonList;
