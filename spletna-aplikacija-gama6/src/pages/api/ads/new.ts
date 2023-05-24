import { NextApiRequest, NextApiResponse } from 'next';
import {prisma} from "../../../server/db"
import { z } from "zod";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method !== 'POST') {
    return res.status(405).json({ message: 'Method not allowed' });
  }

  const schema = z.object({
    title: z.string(),
    price: z.number(),
    url: z.string(),
    location: z.string(),
    seller: z.string(),
    size: z.number(),
    type: z.string(),
    photoUrl: z.string(),
  });

  try {
    const input = schema.parse(req.body);

    // Check if the record already exists 
    const existingOglas = await prisma.oglas.findFirst({
      where: { url: input.url }, 
    });
    if (existingOglas) {
      return res.status(200).json(existingOglas);
    }

    const newOglas = await prisma.oglas.create({
      data: {
        title: input.title,
        price: input.price,
        url: input.url,
        location: input.location,
        seller: input.seller,
        size: input.size,
        type: input.type,
        photoUrl: input.photoUrl,
      },
    });

    return res.status(200).json(newOglas);
  } catch (error) {
    if (error instanceof z.ZodError) {
      return res.status(400).json({ message: 'Invalid request body', errors: error.errors });
    } else {
      console.error(error);
      return res.status(500).json({ message: 'Server error' });
    }
  }
}
