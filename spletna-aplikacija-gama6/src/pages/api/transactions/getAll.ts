import { NextApiRequest, NextApiResponse } from "next";
import { prisma } from "../../../server/db";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  if (req.method !== "GET") {
    return res.status(405).json({ message: "Method not allowed" });
  }
  const transactions = await prisma.transaction.findMany({
    take: 20,
    orderBy: {
      transactionDate: "desc",
    },
    select: {
      id: true,
      apiId: true,
      componentType: true,
      address: true,
      transactionAmountM2: true,
      estimatedAmountM2: true,
      isEstimatedAmount: true,
      transactionItemsList: false,
      transactionSumParcelSizes: true,
      transactionDate: true,
      transactionAmountGross: true,
      transactionTax: true,
      buildingYearBuilt: true,
      unitRoomCount: true,
      unitRoomsSumSize: true,
      unitRooms: false,
      gpsId: true,
      gps: {
        select: {
          lat: true,
          lng: true,
        },
      },
    },
  });

  try {
    return res.status(200).json(transactions);
  } catch (error) {
    return res.status(500).json({ message: "Error" });
  }
}
