import { NextApiRequest, NextApiResponse } from "next";
import { Scraper, Listing } from "~/utils/scraper";
import puppeteer from "puppeteer-extra";
import StealthPlugin from "puppeteer-extra-plugin-stealth";
import { de } from "date-fns/locale";
/*eslint-disable*/



const getAuthToken = async () => {
  let authToken = "";
  puppeteer.use(StealthPlugin());
  const browser = await puppeteer.launch({
    headless: true,
  });
  const page = await browser.newPage();
  await page.setRequestInterception(true);
  page.on("request", (request) => {
    if (
      request
        .url()
        .includes("https://sparkasse.arvio.si/api/v1/transaction-map/search")
    ) {
      const token = request.headers().authorization;
      if (token?.includes("Token")) {
        authToken = token.split(" ")[1];
      }
    }

    request.continue();
  });
  await page.goto(
    "https://www.sparkasse.si/sl/prebivalstvo/orodja/izracun-vrednosti-nepremicnin"
  );
  while (authToken === "") {
    await page.waitForTimeout(1000);
  }
  await browser.close();
  return authToken;
};




export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  const AUTHTOKEN = await getAuthToken();
  try {
    
  
    const { unit_type,unit_subtype,date_interval_months}  = req.body;
    const response = await fetch(
      "https://sparkasse.arvio.si/api/v1/transaction-map/search",
      {
        method: "POST",
        headers: new Headers({
          "Content-Type": "application/json",
          Authorization: `Token ${AUTHTOKEN}`,
        }),
        body: JSON.stringify({
          unit_type: unit_type,
          unit_subtype: unit_subtype,
          date_interval_months: date_interval_months,
          bbox: "14.41011428833008,46.01043551674467,14.602031707763674,46.10573209804895",
        }),
      }
    );
    const deals = await response.json();
    const detailedDeals = []

  
    for (const deal of deals) {
      const dealDetails = await fetch(
        `https://sparkasse.arvio.si/api/v1/transaction-map/search?id=${deal.id}`,
        {
          method: "GET",
          headers: new Headers({
            "Content-Type": "application/json",
            Authorization: `Token ${AUTHTOKEN}`,
          }),
        }
      );
      const dealDetailsJson = await dealDetails.json();
      detailedDeals.push(dealDetailsJson);
    }

    res.status(200).json(detailedDeals);

  } catch (error) {
    console.error(error);
    res.status(500).json({ error: "Internal Server Error" });
  }
}
