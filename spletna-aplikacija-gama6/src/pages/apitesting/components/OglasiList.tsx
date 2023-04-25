import React from 'react';
import { BsHouse } from 'react-icons/bs';
import { api } from '~/utils/api';



const OglasList =({}) => {
    const oglasi = api.oglasi.getAll.useQuery();

  return (
    <div className="p-4">
      <ul className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        {oglasi.data?.map((oglas) => (
          <li key={oglas.id} className="col-span-1 bg-white rounded-lg shadow">
            <div className="w-full p-6 flex flex-col">
              <div className="flex-1">
                <a href={oglas.url} className="block">
                  <h3 className="mt-2 text-xl font-semibold text-gray-900">
                    {oglas.naslov}
                  </h3>
                  <p className="mt-3 text-base text-gray-500 h-20  overflow-scroll">{oglas.opis}</p>
                </a>
              </div>
              <div className="mt-6 flex items-center">
                <BsHouse className="text-gray-400 h-5 w-5" />
                <div className="ml-3">
                  <p className="text-sm font-medium text-gray-900">{oglas.agencija}</p>
                  <div className="flex space-x-2 mt-1 text-sm text-gray-500">
                    <span>{oglas.velikost} m²</span>
                    <span>&middot;</span>
                    <span>{oglas.tip}</span>
                    <span>&middot;</span>
                    <span>{oglas.lokacija}</span>
                  </div>
                </div>
              </div>
              <div className="mt-4">
                <span className="text-xl font-semibold text-gray-900">{oglas.cena} €</span>
              </div>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default OglasList;
