import { z } from "zod";

import {
  createTRPCRouter,
  publicProcedure,
  protectedProcedure,
} from "~/server/api/trpc";

import { getComparison } from "~/utils/realEstateComparision";

export const oglasiRouter = createTRPCRouter({
  getAll: publicProcedure.query(({ ctx }) => {
    return ctx.prisma.oglas.findMany();
  }

    ),
    getOglas: publicProcedure
    .input(z.object({ id: z.number() }))
    .query(({ input }) => {
        return {
            greeting: `Hello ${input.id}`,
        };
        }
    ),

    addOglas: protectedProcedure.input(
        z.object({
            naslov: z.string(),
            opis: z.string(),
            cena: z.number(),
            url: z.string(),
            lokacija: z.string(),
            agencija: z.string(),
            velikost: z.number(),
            tip: z.string(),
        })).mutation(({ ctx, input }) => {
            return ctx.prisma.oglas.create({
                    data: {
                        naslov: input.naslov,
                        opis: input.opis,
                        cena: input.cena,
                        url: input.url,
                        lokacija: input.lokacija,
                        agencija: input.agencija,
                        velikost: input.velikost,
                        tip: input.tip,
                    },
                })
            }
        )
        
    ,
    getComparison: publicProcedure
    // accept two strings as input
    .input(z.array(z.string()))
    .mutation(async ({}) => {
      const url2 = "https://www.nepremicnine.net/oglasi-prodaja/lj-center-hisa_6542556/"
      const url1 =
        "https://www.nepremicnine.net/oglasi-prodaja/lj-center-stanovanje_6537956/";
      const data = await getComparison([url1, url2]);
      return data;
    }),

            



  
});
