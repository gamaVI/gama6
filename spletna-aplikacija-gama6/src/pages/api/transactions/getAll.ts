import { NextApiRequest, NextApiResponse } from "next";
import { prisma } from "../../../server/db";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  if (req.method !== "POST") {
    return res.status(405).json({ message: "Method not allowed" });
  }

  const transactions = await prisma.transaction.findMany({
    take: 20,
    orderBy: {
      transactionDate: "desc",
    },
  });

  try {
    return res.status(200).json(transactions);
  } catch (error) {
    return res.status(500).json({ message: "Error" });
  }
}
