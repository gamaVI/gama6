import { z } from "zod";

import {
  createTRPCRouter,
  publicProcedure,
  protectedProcedure,
} from "~/server/api/trpc";
import { isPointInsidePolygon } from "~/utils/mapUtil";

export const transactionRouter = createTRPCRouter({
  

  getAllTransactions: publicProcedure.query(({ ctx }) => {
    return ctx.prisma.transaction.findMany();
  }),


  getTransactionById: publicProcedure
    .input(z.object({ id: z.string() }))
    .query(async ({ ctx, input }) => {
      const transaction = await ctx.prisma.transaction.findUnique({
        where: { id: input.id },
      });
      return transaction;
    }),

    addTransaction: publicProcedure
    .input(
      z.object({
        apiId: z.string(),
        componentType: z.string(),
        address: z.string(),
        transactionAmountM2: z.optional(z.number()),
        estimatedAmountM2: z.optional(z.number()),
        isEstimatedAmount: z.boolean(),
        gps: z.object({
          lat: z.number(),
          lng: z.number(),
        }),
        transactionItemsList: z.array(z.string()),
        transactionSumParcelSizes: z.number(),
        transactionDate: z.string(),
        transactionAmountGross: z.number(),
        transactionTax: z.optional(z.number()),
        buildingYearBuilt: z.number(),
        unitRoomCount: z.optional(z.number()),
        unitRoomsSumSize: z.number(),
        unitRooms: z.string(),
      })
    )
    .mutation(async ({ ctx, input }) => {
      // check if it already exists 
      const existingTransaction = await ctx.prisma.transaction.findUnique({
        where: { apiId: input.apiId },
      });
      if (existingTransaction) {
        return existingTransaction;
      }
      const gps = await ctx.prisma.gps.create({ data: input.gps });
      const transaction = await ctx.prisma.transaction.create({
        data: { ...input, gps: { connect: { id: gps.id } } },
      });
      return transaction;
    }),


    updateTransaction: protectedProcedure
    .input(
      z.object({
        id: z.string(),
        newData: z.object({
          // ... all fields from addTransaction with each wrapped in z.optional
        }),
      })
    )
    .mutation(async ({ ctx, input }) => {
      const { id, newData } = input;
      const updatedTransaction = await ctx.prisma.transaction.update({
        where: { id },
        data: newData,
      });
      return updatedTransaction;
    }),

    deleteTransaction: protectedProcedure
    .input(z.object({ id: z.string() }))
    .mutation(async ({ ctx, input }) => {
      const { id } = input;
      const deletedTransaction = await ctx.prisma.transaction.delete({ where: { id } });
      return deletedTransaction;
    }),



    getTransactionsInPolygon: publicProcedure
    .input(z.array(z.object({ lat: z.number(), lng: z.number() })))
    .mutation(async ({ ctx, input }) => {
      const polygon = input;
      const transactions = await ctx.prisma.transaction.findMany({
        include: { gps: true },
      });
      const result = transactions.filter((transaction) =>
        isPointInsidePolygon(
          { lat: transaction.gps.lat, lng: transaction.gps.lng },
          polygon
        )
      );
      return result;
    }),

    getAllComponentTypes: publicProcedure.query(async ({ ctx }) => {
      // find all diffferent component types in transactions
      const componentTypes  = await ctx.prisma.transaction.findMany({
        select: { componentType: true },
      });
      return [...new Set(componentTypes.map((item) => item.componentType))];
    }),



    }
    );


