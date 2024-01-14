import { NextApiRequest, NextApiResponse } from "next";
import { prisma } from "../../../server/db";
import { z } from "zod";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  if (req.method !== "DELETE") {
    return res.status(405).json({ message: "Method not allowed" });
  }

  const schema = z.object({
    name: z.string(),
  });

  try {
    const { name } = schema.parse(req.body);
    await prisma.lokacija.delete({
      where: {
        name,
      },
    });
    return res.status(200).json({ message: "Location deleted" });
  } catch (error) {
    console.error(error);
    return res.status(500).json({ message: "Server error" });
  }
}
