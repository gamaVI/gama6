import { type NextPage } from "next";
import { useState, useEffect } from "react";
import Head from "next/head";
import CustomButton from "./components/CustomButton";
import Link from "next/link";
import {
  signIn,
  signOut,
  getProviders,
  useSession,
  type ClientSafeProvider,
  type LiteralUnion,
} from "next-auth/react";
import { type BuiltInProviderType } from "next-auth/providers";

import { api } from "~/utils/api";
import JsonList from "./components/JsonList";
import AddOglasForm from "./components/OglasiForm";

interface Oglas {
    naslov: string;
    opis: string;
    cena: number;
    tip: string;
    velikost: number;
    agencija: string;
    lokacija: string;
    url: string;
    }


const ApiTesting: NextPage = () => {
  const { data: sessionData } = useSession();
  const [activeTab, setActiveTab] = useState<"oglasi" | "posli" | "form">(
    "oglasi"
  );
  const {data: oglasi,isLoading, refetch} = api.oglasi.getAll.useQuery();
  const addOglasMutation = api.oglasi.addOglas.useMutation({
    onSuccess: (data) => {
        console.log(data);
        void refetch();
        }
  });

  console.log(oglasi);
    const handleSubmit =  (
        values: Oglas
    ) => {
        addOglasMutation.mutate(
            values
        );
    }



  return (
    

    <>
      <Head>
        <title>Gama6 api testing</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <main className="flex min-h-screen flex-col items-center justify-center bg-gradient-to-b from-[#2e026d] to-[#15162c]">
        <div className="container flex flex-col items-center justify-center gap-12 px-4 py-16 ">
          <div className="flex gap-4">
            <CustomButton
                label = "Oglasi"
                isSelected = {activeTab === "oglasi"}
                onClick = {() => setActiveTab("oglasi")}
            />

            <CustomButton
                label = "Objavi oglas"
                isSelected = {activeTab === "form"}
                onClick = {() => setActiveTab("form")}
            />
          </div>
          <h1 className=" text-4xl font-bold tracking-tight text-white ">
            {activeTab}
          </h1>
          {activeTab === "oglasi" && <>
          <AddOglasForm 
            onSubmit={(values) => {
               void handleSubmit(values);
            }}

          />
        <JsonList list={oglasi || []} /></>}
        </div>
      </main>
    </>
  );
};

export default ApiTesting;
