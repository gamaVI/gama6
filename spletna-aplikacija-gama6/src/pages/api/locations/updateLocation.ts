import { NextApiRequest, NextApiResponse } from "next";
import { prisma } from "../../../server/db";
import { z } from "zod";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  if (req.method !== "PUT") {
    return res.status(405).json({ message: "Method not allowed" });
  }

  const schema = z.object({
    name: z.string(),
    numCars: z.number(),
  });

  try {
    const input = schema.parse(req.body);

    const updatedLokacija = await prisma.lokacija.update({
      where: {
        name: input.name, // Identifying the record by name
      },
      data: {
        numCars: input.numCars, // Updating only the numCars field
      },
    });

    return res.status(200).json(updatedLokacija);
  } catch (error) {
    if (error instanceof z.ZodError) {
      return res
        .status(400)
        .json({ message: "Invalid request body", errors: error.errors });
    } else {
      console.error(error);
      return res.status(500).json({ message: "Server error" });
    }
  }
}
