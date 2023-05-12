import { NextApiRequest, NextApiResponse } from 'next';
import { Scraper, Listing } from '~/utils/scraper';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  if (req.method !== 'POST') {
    res.status(405).json({ error: 'Method not allowed'});
    return;
  }

  // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
  const { pageNumber, minCost, maxCost, typeOfBuilding, location } = req.body;


  try {
   

    const scraper = new Scraper();
   
    // eslint-disable-next-line @typescript-eslint/no-unsafe-argument
    const listings = await scraper.scrape(pageNumber, minCost, maxCost, typeOfBuilding, location);


    res.status(200).json(listings);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
}
