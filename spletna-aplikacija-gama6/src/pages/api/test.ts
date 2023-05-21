import { NextApiRequest, NextApiResponse } from 'next';
import { Scraper, Listing } from '~/utils/scraper';

export default  function handler(req: NextApiRequest, res: NextApiResponse) {
  res.send('Hello');
}
