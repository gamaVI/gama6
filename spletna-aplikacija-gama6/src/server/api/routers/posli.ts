import { z } from "zod";

import {
  createTRPCRouter,
  publicProcedure,
  protectedProcedure,
} from "~/server/api/trpc";

export const posliRouter = createTRPCRouter({
  hello: publicProcedure
    .input(z.object({ text: z.string() }))
    .query(({ input }) => {
      return {
        greeting: `Hello ${input.text}`,
      };
    }),

  getAll: publicProcedure.query(({ ctx }) => {
    return ctx.prisma.example.findMany();
  }),

  getSecretMessage: protectedProcedure.query(() => {
    return "you can now see this secret message!";
  }),

  getPoselById: publicProcedure
    .input(z.object({ id: z.string() }))
    .query(async ({ ctx, input }) => {
      const posel = await ctx.prisma.posel.findUnique({ where: { id: input.id } });
      return posel;
    }),

  addPosel: protectedProcedure
    .input(z.object({
      skupnaPovrsina: z.number(),
      cena: z.number(),
      cenaNaM2: z.number(),
      lokacija: z.string(),
      datumPosla: z.string(),
      letoGradnje: z.number(),
      tipPosla: z.string(),
      tipNepremicnine: z.string(),
      koordinataX: z.number(),
      koordinataY: z.number(),
    }))
    .mutation(async ({ ctx, input }) => {
      const posel = await ctx.prisma.posel.create({ data: input });
      return posel;
    }),

  updatePosel: protectedProcedure
    .input(z.object({
      id: z.string(),
      newData: z.object({
        skupnaPovrsina: z.optional(z.number()),
        cena: z.optional(z.number()),
        cenaNaM2: z.optional(z.number()),
        lokacija: z.optional(z.string()),
        datumPosla: z.optional(z.string()),
        letoGradnje: z.optional(z.number()),
        tipPosla: z.optional(z.string()),
        tipNepremicnine: z.optional(z.string()),
        koordinataX: z.optional(z.number()),
        koordinataY: z.optional(z.number()),
      }),
    }))
    .mutation(async ({ ctx, input }) => {
      const { id, newData } = input;
      const updatedPosel = await ctx.prisma.posel.update({
        where: { id },
        data: newData,
      });
      return updatedPosel;
    }),

  deletePosel: protectedProcedure
    .input(z.object({ id: z.string() }))
    .mutation(async ({ ctx, input }) => {
      const { id } = input;
      const deletedPosel = await ctx.prisma.posel.delete({ where: { id } });
      return deletedPosel;
    }),
});

