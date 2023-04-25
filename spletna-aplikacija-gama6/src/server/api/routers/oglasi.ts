import { z } from "zod";

import {
  createTRPCRouter,
  publicProcedure,
  protectedProcedure,
} from "~/server/api/trpc";

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

            



  
});
