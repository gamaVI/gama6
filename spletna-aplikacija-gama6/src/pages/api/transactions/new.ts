import { NextApiRequest, NextApiResponse } from 'next';
import {prisma} from "../../../server/db"

import { z } from "zod";
import dayjs from "dayjs";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method !== 'POST') {
    return res.status(405).json({ message: 'Method not allowed' });
  }

  const schema = z.object({
    apiId: z.string(),
    componentType: z.string(),
    address: z.string(),
    transactionAmountM2: z.number().nullable(),
    estimatedAmountM2: z.number().nullable(),
    isEstimatedAmount: z.boolean(),
    gps: z.object({
      lat: z.number(),
      lng: z.number(),
    }),
    transactionItemsList: z.array(z.string()),
    transactionSumParcelSizes: z.number(),
    transactionDate: z.string(),
    transactionAmountGross: z.number(),
    transactionTax: z.number().nullable(),
    buildingYearBuilt: z.number(),
    unitRoomCount: z.number().nullable(),
    unitRoomsSumSize: z.number(),
    unitRooms: z.string(),
  });

  try {
    const input = schema.parse(req.body);

    // Convert transactionDate from string to JavaScript Date
    if (input.transactionDate) {
      input.transactionDate = dayjs(input.transactionDate, 'DD.MM.YYYY').toDate();
    }
    
    // check if it already exists 
    const existingTransaction = await prisma.transaction.findUnique({
      where: { apiId: input.apiId },
    });
    if (existingTransaction) {
      return res.status(200).json(existingTransaction);
    }

    const gps = await prisma.gps.create({ data: input.gps });
    const transaction = await prisma.transaction.create({
      data: { ...input, gps: { connect: { id: gps.id } } },
    });
    
    return res.status(200).json(transaction);
  } catch (error) {
    if (error instanceof z.ZodError) {
      return res.status(400).json({ message: 'Invalid request body', errors: error.errors });
    } else {
      console.error(error);
      return res.status(500).json({ message: 'Server error' });
    }
  }
}
