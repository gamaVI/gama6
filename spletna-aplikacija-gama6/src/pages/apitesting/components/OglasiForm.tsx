import React, { useState } from 'react';

interface Oglas {
  url: string;
  naslov: string;
  tip: string;
  opis: string;
  velikost: number;
  cena: number;
  agencija: string;
  lokacija: string;
}

interface AddOglasFormProps {
  onSubmit: (newOglas: Oglas) => void;
}

const AddOglasForm: React.FC<AddOglasFormProps> = ({ onSubmit }) => {
  const [url, setUrl] = useState('');
  const [naslov, setNaslov] = useState('');
  const [tip, setTip] = useState('');
  const [opis, setOpis] = useState('');
  const [velikost, setVelikost] = useState(0);
  const [cena, setCena] = useState(0);
  const [agencija, setAgencija] = useState('');
  const [lokacija, setLokacija] = useState('');

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const newOglas: Oglas = {
      url,
      naslov,
      tip,
      opis,
      velikost,
      cena,
      agencija,
      lokacija,
    };
    onSubmit(newOglas);
    setUrl('');
    setNaslov('');
    setTip('');
    setOpis('');
    setVelikost(0);
    setCena(0);
    setAgencija('');
    setLokacija('');
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-row gap-4">
      <div>
        <input
        placeholder='URL'
          type="text"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        />
      </div>
      <div>
        <input
          placeholder='Cena'
          type="text"
          value={cena}
          onChange={(e) => setCena(parseInt(e.target.value))}
          className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        />
      </div>
      <div>
        <input
        placeholder='Tip'
          type="text"
          value={tip}
          onChange={(e) => setTip(e.target.value)}
          className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        />
      </div>
      <div>
        <input
        placeholder='Lokacija'
          type="text"
          value={lokacija}
          onChange={(e) => setLokacija(e.target.value)}
          className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        />
      </div>
      <div>
        <input
        placeholder='Agencija'
          type="text"
          value={agencija}
          onChange={(e) => setAgencija(e.target.value)}
          className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        />
      </div>
      <div>
        <input
        placeholder='Opis'
          type="text"
          value={opis}
          onChange={(e) => setOpis(e.target.value)}
          className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        />
      </div>
      <div>
        <input
        placeholder='Velikost'
          type="text"
          value={velikost}
          onChange={(e) => setVelikost(parseInt(e.target.value))}
          className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        />
      </div>
      <div>
        <input
        placeholder='Naslov'
          type="text"
          value={naslov}
          onChange={(e) => setNaslov(e.target.value)}
          className="mt-1 block w-full py-2 px-3 border border-gray-300 bg-white rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
        />
      </div>
      
      {/* Add more form fields for other properties here */}
      {/* ... */}
      <button
        type="submit"
        className="w-60 flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
      >
        Dodaj oglas
      </button>
    </form>
  );
};

export default AddOglasForm;
