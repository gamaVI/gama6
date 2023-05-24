/* eslint-disable */
// import puppeter core
import puppeteer from "puppeteer-core";

export interface Listing {
  title: string | null;
  link: string | null;
  photoUrl: string | null;
  price: string | null;
  size: string | null;
  year: string | null;
  seller: string | null;
  location: string | null;

}

export class Scraper {
  async  autoScroll(page){
    await page.evaluate(async () => {
        await new Promise((resolve) => {
            var totalHeight = 0;
            var distance = 100;
            var timer = setInterval(() => {
                var scrollHeight = document.body.scrollHeight;
                window.scrollBy(0, distance);
                totalHeight += distance;

                if(totalHeight >= scrollHeight - window.innerHeight){
                    clearInterval(timer);
                    resolve();
                }
            }, 100);
        });
    });
}

  async scrape(
    pageNumber: number,
    minCost: number,
    maxCost: number,
    typeOfBuilding: string,
    location: string
  ): Promise<Listing[]> {
    const url = this.buildUrl(pageNumber, minCost, maxCost, typeOfBuilding, location);
    const browser = await puppeteer.launch({
      executablePath: process.env.CHROME_BIN || null, 
      args: ['--no-sandbox', '--disable-setuid-sandbox']
  });
    const page = await browser.newPage();
    page.setDefaultTimeout(10000)
    // wait for three seconds
   
    await page.goto(url);
    await page.waitForSelector('.col-md-6.col-md-12.position-relative');
    // scroll to the bottom of the page
    await this.autoScroll(page);

    const listingData: Listing[] = await page.evaluate(() => {
      const listings = Array.from(document.querySelectorAll('.col-md-6.col-md-12.position-relative'));
      return listings.map((listing) => {
        const  title = listing.querySelector('.font-roboto')?.textContent?.replaceAll('\n', '').replaceAll('  ', '').replace("Novo","").trim() || null;
        const link = listing.querySelector('a')?.getAttribute('href') || null;
        const photoUrl = listing.querySelector('img')?.getAttribute('src') || null;
        const price = listing.querySelector('h6')?.textContent || null;
        const size = listing.querySelector('img[src="https://www.nepremicnine.net/images/velikost.svg"]')?.parentElement?.textContent || null;
        const year = listing.querySelector('img[src="https://www.nepremicnine.net/images/leto.svg"]')?.parentElement?.textContent || null;
        const location  = listing.querySelector('.url-title-d')?.textContent.replaceAll('\n', '').replaceAll('  ', '') || null;
        const seller = listing.querySelector('.logo')?.previousElementSibling?.textContent?.replaceAll('\n', '').replaceAll('  ', '') || null;
        
        const formattedPrice =  price ? parseInt(price.replace('€', '').replace('.', '').split(",")[0].trim()) : null;
        const formattedSize =  size ? parseFloat(size.replace('m2', '').replace(',', '.').replaceAll(" ","").split(",")[0].trim()) : null;
        const formattedYear =  year ? parseInt(year.replace('leto', '').trim()) : null;
        
        
        
        return {
          title,
          link: link ? `https://www.nepremicnine.net/${link}` : null,
          photoUrl,
          price:formattedPrice,
          size : formattedSize,
          year : formattedYear,
          seller,
          location
        };
      });
    })
    
    const pageCount  = await page.evaluate(() => {
      return  parseInt(document.querySelector('.paging_last a')?.getAttribute('href')?.split("/?s")[0]?.split("/").slice(-1)[0]) || null;
   })

    
    await browser.close();
    return  {
      listings: listingData,
      pageCount : pageCount
    }
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
}

