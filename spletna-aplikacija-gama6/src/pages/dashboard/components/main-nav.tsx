import Link from "next/link";

import { cn } from "~/lib/utils";

export default function MainNav({
  className,
  ...props
}: React.HTMLAttributes<HTMLElement>) {
  return (
    <nav
      // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment, @typescript-eslint/no-unsafe-call
      className={cn("flex items-center space-x-4 lg:space-x-6", className)}
      {...props}
    >
      <h1 className=" text-2xl font-extrabold tracking-tight text-white">
          🏘️Gama<span className="text-[hsl(280,100%,70%)]">6</span>
          </h1>
      <Link
        href="/examples/dashboard"
        className="text-sm font-medium transition-colors hover:text-primary"
      >
        Analiza območja
      </Link>
      <Link
        href="/examples/dashboard"
        className="text-sm font-medium text-muted-foreground transition-colors hover:text-primary"
      >
        AI asistent
      </Link>
    </nav>
  );
}
