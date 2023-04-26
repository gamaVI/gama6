import { type NextPage } from "next";
import { api } from "~/utils/api";
import { useState } from "react";

interface RealEstate {
  naslovOglasa: string,
            cena : string,
            tip: string,
            regija: string,
            upravnaEnota : string,
            velikost : string,
            leto : string,
            energetskiRazred : string,
            opis : string,
            obcina : string,
}


interface DataInterface {
  comparisonText: string | undefined;
  realEstates: RealEstate[] | undefined;
}


const Primerjava: NextPage = () => {
  const [loading , setLoading] = useState<boolean>(false);

  const mutation = api.oglasi.getComparison.useMutation({
    onSuccess: (data ) => {
      console.log(data);
      if(data){
        setData(data);
        setLoading(false);
      }
      
    },
  });

  // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
  const [data, setData] = useState<DataInterface>(
    {
      comparisonText: "",
      realEstates: [],
    }
  );
  const [url1, setUrl1] = useState<string>(
    "https://www.nepremicnine.net/oglasi-prodaja/lj-center-hisa_6542556/"
  );
  const [url2, setUrl2] = useState<string>(
    "https://www.nepremicnine.net/oglasi-prodaja/lj-center-stanovanje_6537956/"
  );

  const handleSubmit = () => {
    setLoading(true);
    mutation.mutate([url1, url2]);
   
  };

  return (
    <main className="flex min-h-screen flex-col items-center justify-center bg-gradient-to-b from-[#2e026d] to-[#15162c]">

      <div
        className="
        mb-3 flex flex-row gap-3 
        "
      >
        <input
          type="text"
          value={url1}
          onChange={(e) => setUrl1(e.target.value)}
          className="rounded-md bg-gray-100 p-2 text-black"
        />

        <input
          type="text"
          value={url1}
          onChange={(e) => setUrl1(e.target.value)}
          className="rounded-md bg-gray-100 p-2 text-black"
        />
        <button
          onClick={handleSubmit}
          className="rounded-md bg-pink-500 p-2 text-white"
        >
          Primerjaj
        </button>
      </div>
      <h1 className="text-white font-bold my-3">Primerjava</h1>
      <div className="flex flex-row gap-2 p-5">
        {
          loading && <h1 className="text-white  mt-3 font-bold text-xl">Analiziram nepremicnine...</h1>
        }
        {
          data.realEstates?.map((realEstate) => (
            <div key={realEstate.cena} className="flex flex-col gap-2 bg-slate-600 rounded-xl p-4">
              <h1 className="text-white">Naslov: {realEstate.naslovOglasa}</h1>
              <h1 className="text-white">Cena: {realEstate.cena}</h1>
              <h1 className="text-white">Tip: {realEstate.tip}</h1>
              <h1 className="text-white">Regija: {realEstate.regija}</h1>
              <h1 className="text-white">Upravna enota: {realEstate.upravnaEnota}</h1>
              <h1 className="text-white">Velikost {realEstate.velikost}</h1>
              <h1 className="text-white">Leto: {realEstate.leto}</h1>
              <h1 className="text-white">Energetski razred: {realEstate.energetskiRazred}</h1>
              <h1 className="text-white">Obcina: {realEstate.obcina}</h1>
            </div>
          ))
        }
        <div className="bg-white rounded-xl p-3 w-1/2">
          <h1 className="text-black">{data.comparisonText}</h1>
        </div>
      </div>
    </main>
  );
};

export default Primerjava;
