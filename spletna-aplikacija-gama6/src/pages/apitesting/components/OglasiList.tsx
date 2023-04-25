import React from 'react';
import { BsHouse } from 'react-icons/bs';
import { api } from '~/utils/api';



const OglasList =({}) => {
    const oglasi = api.oglasi.getAll.useQuery();

  return (
    <div className="p-4">
      <ul className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {oglasi.data?.map((oglas) => (
          <li key={oglas.id} className="col-span-1 bg-white rounded-lg shadow  overflow-x-scroll overflow-y-scrol">
            <div className="w-full p-6 flex flex-col"><pre>
             {JSON.stringify(oglas,null,2)}
             </pre>
            </div>
          </li>
        ))}
      </ul>
    </div>
    
  );
};

export default OglasList;
