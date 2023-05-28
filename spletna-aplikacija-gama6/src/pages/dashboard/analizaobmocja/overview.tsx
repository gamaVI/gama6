import { Metadata } from "next";
import Image from "next/image";
import {
  Activity,
  CreditCard,
  DollarSign,
  Download,
  Users,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import Overview from "./components/components/overviewgraph";
import RecentSales from "./components/components/recentsales";
import { Building } from "lucide-react";
import TransactionsByDayGraph from "./components/components/transactionsbydaygraph";
import ComponentTypePieChart from "./components/components/piecharts";
import dayjs from "dayjs";
import TransactionScatterPlot from "./components/components/scatterchart";
import TransactionHistogram from "./components/components/histogram";
import ParcelSizePriceScatterChart from "./components/components/corelationchart";
import YearBuiltSizeScatterChart from "./components/components/correlationyearsize";

export default function OverviewPage({transactions,dateFrom,dateTo}) {
  // Calculate total transaction amount
  const totalTransactionAmount = parseInt(transactions.reduce((total, transaction) => total + transaction.transactionAmountGross, 0));

  // Get number of transactions
  const numberOfTransactions = transactions.length;

  // Calculate average transaction value
  const averageTransactionValue = totalTransactionAmount / numberOfTransactions;

  // Calculate average price per square meter
  const totalSquareMeters = transactions.reduce((total, transaction) => total + transaction.unitRoomsSumSize, 0);
  const averagePricePerSquareMeter = totalTransactionAmount / totalSquareMeters;

  return (
    <>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Skupni znesek izvedenih poslov
            </CardTitle>
            <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{`€${totalTransactionAmount}`}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Število transakcij  v obdobju
            </CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{numberOfTransactions}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Povprečna vrednost izvedenega posla
            </CardTitle>
            <CreditCard className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{`€${averageTransactionValue.toFixed(2)}`}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">
              Povprečna cena na kvadratni meter
            </CardTitle>
            <Activity className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{`${averagePricePerSquareMeter.toFixed(2)} €/m2`}</div>
          </CardContent>
        </Card>
      </div>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
        <Card className="col-span-4">
          <CardHeader>
            <CardTitle>Povpečna cena na kvadratni meter</CardTitle>
          </CardHeader>
          <CardContent className="pl-2">
          <Overview transactions={transactions} />
          </CardContent>
        </Card>
        <Card className="col-span-3">
          <CardHeader>
            <CardTitle>Največji izvedeni posli v mesecu</CardTitle>
            <CardDescription>
             V obdobju od {dayjs(date.from).format("DD.MM.YYYY")} do {dayjs(date.to).format("DD.MM.YYYY")} je bilo izvedenih {numberOfTransactions} transakcij v skupni vrednosti {totalTransactionAmount} €.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <RecentSales transactions={transactions}/>
          </CardContent>
        </Card>
      </div>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
        <Card className="col-span-4">
          <CardHeader>
            <CardTitle>Število izvedenih transakcij po dnevih:</CardTitle>
          </CardHeader>
          <CardContent className="pl-2">
          <TransactionsByDayGraph dateFrom={dateFrom} dateTo={dateTo} transactions={transactions} />
          </CardContent>
        </Card>
        <Card className="col-span-3">
          <CardHeader>
            <CardTitle>Tipi prodanih nepremičnin</CardTitle>
          </CardHeader>
          <CardContent className="pl-2">
            <ComponentTypePieChart transactions={transactions}/>
          </CardContent>
        </Card>
      </div>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
        <Card className="col-span-4">
          <CardHeader>
            <CardTitle>Cene transakcij glede na leto gradnje:</CardTitle>
          </CardHeader>
          <CardContent className="pl-2">
          <TransactionScatterPlot transactions={transactions} />
          </CardContent>
        </Card>
        <Card className="col-span-3">
          <CardHeader>
            <CardTitle>Hisogram cen transakcij:</CardTitle>
          </CardHeader>
          <CardContent className="pl-2">
          <TransactionHistogram transactions={transactions} />
          </CardContent>
        </Card>
      </div>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
        <Card className="col-span-4">
          <CardHeader>
            <CardTitle>Korelacija cene nepremičnine glede na velikost:</CardTitle>
          </CardHeader>
          <CardContent className="pl-2">
          <ParcelSizePriceScatterChart transactions={transactions} />
          </CardContent>
        </Card>
        <Card className="col-span-3">
          <CardHeader>
            <CardTitle>Korelacija velikosti nepremičnine glede na leto gradnje:</CardTitle>
          </CardHeader>
          <CardContent className="pl-2">
          <YearBuiltSizeScatterChart transactions={transactions} />
          </CardContent>
        </Card>
       
      </div>
    </>
  );
}

