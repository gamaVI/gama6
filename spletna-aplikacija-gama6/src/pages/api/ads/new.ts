import { NextApiRequest, NextApiResponse } from 'next';
import {prisma} from "../../../server/db"
import { z } from "zod";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method !== 'POST') {
    return res.status(405).json({ message: 'Method not allowed' });
  }

  const schema = z.object({
    naslov: z.string(),
    opis: z.string(),
    cena: z.number(),
    url: z.string(),
    lokacija: z.string(),
    agencija: z.string(),
    velikost: z.number(),
    tip: z.string(),
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
        naslov: input.naslov,
        opis: input.opis,
        cena: input.cena,
        url: input.url,
        lokacija: input.lokacija,
        agencija: input.agencija,
        velikost: input.velikost,
        tip: input.tip,
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