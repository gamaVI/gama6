import puppeteer from "puppeteer-extra";
import StealthPlugin from "puppeteer-extra-plugin-stealth";

export interface Listing {
  title: string | null;
  link: string | null;
  photoUrl: string | null;
  price: string | null;
  size: string | null;
  floor: string | null;
  year: string | null;
  sellerTitle: string | null;
}

export class Scraper {
  

  async scrape(
    pageNumber: number,
    minCost: number,
    maxCost: number,
    typeOfBuilding: string,
    location: string
  ): Promise<Listing[]> {
    const url = this.buildUrl(pageNumber, minCost, maxCost, typeOfBuilding, location);
    puppeteer.use(StealthPlugin())
      const browser = await puppeteer.launch({
        headless: true,
      });
    const page = await browser.newPage();
    await page.goto(url);
    await page.waitForSelector('.oglas_container');

    const listingData: Listing[] = await page.evaluate(() => {
      const listings = Array.from(document.querySelectorAll('.oglas_container'));
      return listings.map((listing) => {
        const title = listing.querySelector('.title')?.textContent || null;
        const link = listing.querySelector('a')?.getAttribute('href') || null;
        const photoUrl = listing.querySelector('a.slika img')?.getAttribute('data-src') || null;
        const price = listing.querySelector('.cena')?.textContent || null;
        const size = listing.querySelector('.velikost')?.textContent || null;
        const floor = listing.querySelector('.nadstropje')?.textContent || null;
        const year = listing.querySelector('.leto')?.textContent || null;
        const sellerTitle = listing.querySelector('.prodajalec_o')?.getAttribute('title') || null;

        return {
          title,
          link: link ? `https://www.nepremicnine.net/${link}` : null,
          photoUrl,
          price,
          size,
          floor,
          year,
          sellerTitle,
        };
      });
    })
    await browser.close();
    return listingData;
  }


    

  private buildUrl(
    pageNumber: number,
    minCost: number,
    maxCost: number,
    typeOfBuilding: string,
    location: string
  ): string {
    const pageSuffix = pageNumber > 1 ? `/${pageNumber}` : '';
    return `https://www.nepremicnine.net/oglasi-prodaja/${location}/${typeOfBuilding}/cena-od-${minCost}-do-${maxCost}-eur${pageSuffix}/?s=3&nadst%5B0%5D=vsa&nadst%5B1%5D=vsa`;
  }

  private printListing(listing: Listing): void {
    console.log('Title:', listing.title);
    console.log('Link:', listing.link);
    console.log('Photo URL:', listing.photoUrl);
    console.log('Price:', listing.price);
    console.log('Size:', listing.size);
    console.log('Floor:', listing.floor);
    console.log('Year:', listing.year);
    console.log('Seller Title:', listing.sellerTitle);
    console.log('------------');
  }
}

