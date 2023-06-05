import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Building } from "lucide-react";

export default function RecentSales({ transactions }) {

  if (!transactions) {
    return null;
  }
  // sort the transactions by transactionAmountGross in descending order and get the top 5
  const topTransactions = transactions
    .sort((a, b) => b.transactionAmountGross - a.transactionAmountGross)
    .slice(0, 5);

  return (
    <div className="space-y-8">
      {topTransactions.map((transaction, index) => (
        <div key={index} className="flex items-center justify-center">
          <Avatar className="h-9 w-9">
            <AvatarImage src="/avatars/01.png" alt="Avatar" />
            <AvatarFallback>
              <Building className="h-5 w-5 " />
            </AvatarFallback>
          </Avatar>
          <div className="ml-4 space-y-1">
            <p className="text-sm font-medium leading-none">
              {transaction.address}
            </p>
            <p className="text-sm text-muted-foreground">
              {transaction.componentType}
            </p>
          </div>
          <div className="ml-auto font-medium">
            €{transaction.transactionAmountGross.toFixed(2)}
          </div>
        </div>
      ))}
    </div>
  );
}
