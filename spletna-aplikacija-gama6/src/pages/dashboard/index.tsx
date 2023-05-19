import Image from "next/image"
 import { type NextPage } from "next";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { MainNav } from "./components/main-nav"
import { UserNav } from "./components/user-nav"
import { api } from "~/utils/api";
import { useSession } from "next-auth/react";
import JsonList from "./components/jsonlist"
import OverviewPage from "./overview/overview"

const DashboardPage: NextPage =()=>{

  const { data: sessionData } = useSession();
  const {data: oglasi,refetch:refetchOglasi} = api.oglasi.getAll.useQuery();
  const {data: posli} = api.transactions.getAllTransactions.useQuery();

  const addOglasMutation = api.oglasi.addOglas.useMutation({
    onSuccess: (data) => {
        console.log(data);
        void refetchOglasi();
        }
  });
  console.log(posli)
  console.log(oglasi)
  return (
    <>
      <div className="md:hidden">
        <Image
          src="/examples/dashboard-light.png"
          width={1280}
          height={866}
          alt="Dashboard"
          className="block dark:hidden"
        />
        <Image
          src="/examples/dashboard-dark.png"
          width={1280}
          height={866}
          alt="Dashboard"
          className="hidden dark:block"
        />
      </div>
      <div className="hidden flex-col md:flex">
        <div className="border-b">
          <div className="flex h-16 items-center px-4">
            <MainNav className="mx-6" />
            <div className="ml-auto flex items-center space-x-4">
              <UserNav />
            </div>
          </div>
        </div>
        <div className="flex-1 space-y-4 p-8 pt-6">
          <div className="flex items-center justify-between space-y-2">
            <h2 className="text-3xl font-bold tracking-tight">Nadzorna plošča</h2>
            <div className="flex items-center space-x-2">
             
            </div>
          </div>
          <Tabs defaultValue="posli" className="space-y-4">
            <TabsList>
              <TabsTrigger value="posli">Posli</TabsTrigger>
              <TabsTrigger value="objavaposla">Objavi posel</TabsTrigger>
              <TabsTrigger value="oglasi">Oglasi</TabsTrigger>
              <TabsTrigger value="objavaoglasa">Objavi oglas</TabsTrigger>
              <TabsTrigger value="splosnipregled">Splošni pregled</TabsTrigger>
            </TabsList>
            <TabsContent value="posli" className="space-y-4">
            <JsonList 
              list={posli || []}
              />
            </TabsContent>
            <TabsContent value="oglasi" className="space-y-4">
            <JsonList 
              list={oglasi || []}
              />
            </TabsContent>
            <TabsContent value="objavaoglasa" className="space-y-4">

            </TabsContent>
            <TabsContent value="splosnipregled" className="space-y-4">
              <OverviewPage/>
            </TabsContent>
          </Tabs>
        </div>
      </div>
      </>
  )
}


export default DashboardPage;