import { NextApiRequest, NextApiResponse } from "next";
import { prisma } from "../../../server/db";
import { z } from "zod";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  if (req.method !== "POST") {
    return res.status(405).json({ message: "Method not allowed" });
  }

  const schema = z.object({
    name: z.string(),
    minCars: z.number(),
    maxCars: z.number(),
    latitude: z.number(),
    longitude: z.number(),
    updateFrequencySeconds: z.number(),
    lastUpdated: z.number(),
    simulation: z.boolean(),
    numCars: z.number(),
  });

  try {
    const input = schema.parse(req.body);

    const lokacija = await prisma.lokacija.upsert({
      where: {
        name: input.name, // Unique identifier
      },
      update: {
        // Fields to update if the record exists
        minCars: input.minCars,
        maxCars: input.maxCars,
        latitude: input.latitude,
        longitude: input.longitude,
        updateFrequencySeconds: input.updateFrequencySeconds,
        lastUpdated: input.lastUpdated,
        simulation: input.simulation,
        numCars: input.numCars, // Assuming you want to update this as well
      },
      create: {
        // Data to create if the record doesn't exist
        name: input.name,
        minCars: input.minCars,
        maxCars: input.maxCars,
        latitude: input.latitude,
        longitude: input.longitude,
        updateFrequencySeconds: input.updateFrequencySeconds,
        lastUpdated: input.lastUpdated,
        simulation: input.simulation,
        numCars: input.numCars,
      },
    });

    return res.status(200).json(lokacija);
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
