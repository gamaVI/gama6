import { type NextPage } from "next";
import { api } from "~/utils/api";





const Primerjava: NextPage = () => {

    const {data,isLoading, refetch} = api.example.getComparison.useQuery();
  

  



  return (
     <main className="flex min-h-screen flex-col items-center justify-center bg-gradient-to-b from-[#2e026d] to-[#15162c]">
        <h1 className="text-pink-500">
            <pre>
            {JSON.stringify(data,null,2)}
            </pre>
        </h1>
    </main>
  );
};

export default Primerjava;
