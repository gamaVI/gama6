import { NextApiRequest, NextApiResponse } from "next";
import { prisma } from "../../../server/db";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  if (req.method !== "GET") {
    return res.status(405).json({ message: "Method not allowed" });
  }

  try {
    const allLocations = await prisma.lokacija.findMany();
    return res.status(200).json(allLocations);
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: "Server error" });
  }
}
