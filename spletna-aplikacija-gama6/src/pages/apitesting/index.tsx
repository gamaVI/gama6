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
import OglasList from "./components/OglasiList";

const ApiTesting: NextPage = () => {
  const { data: sessionData } = useSession();
  const [activeTab, setActiveTab] = useState<"oglasi" | "posli" | "form">(
    "oglasi"
  );

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
          {activeTab === "oglasi" && <OglasList />}
        </div>
      </main>
    </>
  );
};

export default ApiTesting;
