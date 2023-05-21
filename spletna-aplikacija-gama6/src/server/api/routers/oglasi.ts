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
  }),
  getOglas: publicProcedure
    .input(z.object({ id: z.number() }))
    .query(({ input }) => {
      return {
        greeting: `Hello ${input.id}`,
      };
    }),

  addOglas: protectedProcedure
    .input(
      z.object({
        naslov: z.string(),
        opis: z.string(),
        cena: z.number(),
        url: z.string(),
        lokacija: z.string(),
        agencija: z.string(),
        velikost: z.number(),
        tip: z.string(),
      })
    )
    .mutation(({ ctx, input }) => {
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
      });
    }),

  getComparison: publicProcedure
    .input(z.array(z.string()))
    .mutation(async ({}) => {
      const url2 =
        "https://www.nepremicnine.net/oglasi-prodaja/lj-center-hisa_6542556/";
      const url1 =
        "https://www.nepremicnine.net/oglasi-prodaja/lj-center-stanovanje_6537956/";
      const data = await getComparison([url1, url2]);
      return data;
    }),
  searchOglasi: publicProcedure
    .input(
      z.object({
        query: z.string(),
        location: z.optional(z.string()),
        minPrice: z.optional(z.number()),
        maxPrice: z.optional(z.number()),
        propertyType: z.optional(z.string()),
      })
    )
    .query(async ({ ctx, input }) => {
      const { query, location, minPrice, maxPrice, propertyType } = input;
      const searchResults = await ctx.prisma.oglas.findMany({
        where: {
          AND: [
            { naslov: { contains: query } },
            location ? { lokacija: location } : {},
            minPrice ? { cena: { gte: minPrice } } : {},
            maxPrice ? { cena: { lte: maxPrice } } : {},
            propertyType ? { tip: propertyType } : {},
          ],
        },
      });
      return searchResults;
    }),

  updateOglas: protectedProcedure
    .input(
      z.object({
        id: z.string(),
        newData: z.object({
          naslov: z.optional(z.string()),
          opis: z.optional(z.string()),
          cena: z.optional(z.number()),
          url: z.optional(z.string()),
          lokacija: z.optional(z.string()),
          agencija: z.optional(z.string()),
          velikost: z.optional(z.number()),
          tip: z.optional(z.string()),
        }),
      })
    )
    .mutation(async ({ ctx, input }) => {
      const { id, newData } = input;
      const updatedOglas = await ctx.prisma.oglas.update({
        where: { id },
        data: newData,
      });
      return updatedOglas;
    }),

  deleteOglas: protectedProcedure
    .input(z.object({ id: z.string() }))
    .mutation(async ({ ctx, input }) => {
      const { id } = input;
      const deletedOglas = await ctx.prisma.oglas.delete({ where: { id } });
      return deletedOglas;
    }),

  getAveragePriceByLocation: publicProcedure
    .input(z.object({ location: z.string() }))
    .query(async ({ ctx, input }) => {
      const { location } = input;
      const oglasi = await ctx.prisma.oglas.findMany({
        where: { lokacija: location },
      });
      const total = oglasi.reduce((acc, oglas) => acc + oglas.cena, 0);
      const average = total / oglasi.length;
      return average;
    }),

  getMostPopularAgencies: publicProcedure
    .input(z.optional(z.number()))
    .query(async ({ ctx, input }) => {
      const limit = input || 5;
      const oglasi = await ctx.prisma.oglas.findMany();
      const agencies: Record<string, number> = oglasi.reduce<
        Record<string, number>
      >((acc, oglas) => {
        const agency = oglas.agencija;
        if (!acc[agency]) {
          acc[agency] = 1;
        } else {
          acc[agency]++;
        }
        return acc;
      }, {});

      const sortedAgencies = Object.entries(agencies).sort(
        (a, b) => b[1] - a[1]
      );
      const topAgencies = sortedAgencies.slice(0, limit);
      return topAgencies;
    }),
});
