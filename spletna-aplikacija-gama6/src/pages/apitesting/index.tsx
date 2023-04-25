import { type NextPage } from "next";
import {useState,useEffect} from "react";
import Head from "next/head";
import Link from "next/link";
import { signIn, signOut,getProviders, useSession, type ClientSafeProvider, type LiteralUnion} from "next-auth/react";
import { type BuiltInProviderType } from "next-auth/providers";

import { api } from "~/utils/api";
import OglasList from "./components/OglasiList";

const ApiTesting: NextPage = () => {
  const {data: sessionData} = useSession();
  const [activeTab,setActiveTab] = useState<"oglasi" | "posli" | "form">("oglasi");

  

  return (
    <>
      <Head>
        <title>Gama6 api testing</title>
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <main className="flex min-h-screen flex-col items-center justify-center bg-gradient-to-b from-[#2e026d] to-[#15162c]">
        <div className="container flex flex-col items-center justify-center gap-12 px-4 py-16 ">
                <div className="flex gap-4">
                    <button 
                        className="bg-pink-500 hover:bg-white hover:text-pink-500 text-white font-semibold py-2 px-4 rounded-xl"
                     onClick={() => setActiveTab("oglasi")}>Oglasi</button>
                     <button 
                        className="bg-pink-500 hover:bg-white hover:text-pink-500 text-white font-semibold py-2 px-4 rounded-xl"
                     onClick={() => setActiveTab("oglasi")}>Posli</button>
                      <button 
                        className="bg-pink-500 hover:bg-white hover:text-pink-500 text-white font-semibold py-2 px-4 rounded-xl"
                     onClick={() => setActiveTab("oglasi")}>Objavi oglas</button>
                      <button 
                        className="bg-pink-500 hover:bg-white hover:text-pink-500 text-white font-semibold py-2 px-4 rounded-xl"
                     onClick={() => setActiveTab("oglasi")}>Objavi posel</button>
                </div>
           <OglasList />
        </div>
      </main>
    </>
  );
};



export default ApiTesting;

