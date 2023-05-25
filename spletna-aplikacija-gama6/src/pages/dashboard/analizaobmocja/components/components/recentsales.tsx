import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Building } from "lucide-react";
export default function RecentSales() {
  return (
    <div className="space-y-8">
      <div className="flex items-center justify-center">
        <Avatar className="h-9 w-9">
          <AvatarImage src="/avatars/01.png" alt="Avatar" />
          <AvatarFallback>
            <Building className="h-5 w-5 " />
          </AvatarFallback>
        </Avatar>
        <div className="ml-4 space-y-1">
          <p className="text-sm font-medium leading-none">
            Izmišljena ulica 19
          </p>
          <p className="text-sm text-muted-foreground">Stanovanjska hiša</p>
        </div>
        <div className="ml-auto font-medium">€1,999,100</div>
      </div>
      <div className="flex items-center justify-center">
        <Avatar className="h-9 w-9">
          <AvatarImage src="/avatars/01.png" alt="Avatar" />
          <AvatarFallback>
            <Building className="h-5 w-5 " />
          </AvatarFallback>
        </Avatar>
        <div className="ml-4 space-y-1">
          <p className="text-sm font-medium leading-none">
            Izmišljena ulica 19
          </p>
          <p className="text-sm text-muted-foreground">Stanovanjska hiša</p>
        </div>
        <div className="ml-auto font-medium">€1,999,100</div>
      </div>
      <div className="flex items-center justify-center">
        <Avatar className="h-9 w-9">
          <AvatarImage src="/avatars/01.png" alt="Avatar" />
          <AvatarFallback>
            <Building className="h-5 w-5 " />
          </AvatarFallback>
        </Avatar>
        <div className="ml-4 space-y-1">
          <p className="text-sm font-medium leading-none">
            Izmišljena ulica 19
          </p>
          <p className="text-sm text-muted-foreground">Stanovanjska hiša</p>
        </div>
        <div className="ml-auto font-medium">€1,999,100</div>
      </div>
      <div className="flex items-center justify-center">
        <Avatar className="h-9 w-9">
          <AvatarImage src="/avatars/01.png" alt="Avatar" />
          <AvatarFallback>
            <Building className="h-5 w-5 " />
          </AvatarFallback>
        </Avatar>
        <div className="ml-4 space-y-1">
          <p className="text-sm font-medium leading-none">
            Izmišljena ulica 19
          </p>
          <p className="text-sm text-muted-foreground">Stanovanjska hiša</p>
        </div>
        <div className="ml-auto font-medium">€1,999,100</div>
      </div>
    </div>
  );
}
