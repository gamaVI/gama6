import { z } from "zod";
import puppeteer from "puppeteer";
import {
  createTRPCRouter,
  publicProcedure,
  protectedProcedure,
} from "~/server/api/trpc";

const delay = (ms: number) => new Promise((res) => setTimeout(res, ms));

const getDataFromUrl = async (urls: string[]) => {
  try {
    // extracd id from url : https://www.nepremicnine.net/oglasi-prodaja/lj-center-stanovanje_6537956/
    const results = []
    const browser = await puppeteer.launch({
      headless: false,
    });
    const page = await browser.newPage();


    for(const url of urls){
      const id = url?.split("_")[1]?.split("/")[0] || "";
      console.log("ID", id);
      const printURL = `https://www.nepremicnine.net/prt-oglas.php?id=${id}`;
      await page.goto(printURL);
      const data = await page.evaluate(() => {
        const naslovOglasa = document.querySelector('h1 strong')?.textContent?.trim() || '';
        const cena = document.querySelector('.cena')?.textContent?.replace(/[^\d,]/g, '') || '';
        const tip = document.querySelector('#podrobnosti table tr:nth-child(2) td')?.textContent?.trim() || '';
        const regija = document.querySelector('#podrobnosti table tr:nth-child(3) td')?.textContent?.trim() || '';
        const upravnaEnota = document.querySelector('#podrobnosti table tr:nth-child(4) td')?.textContent?.trim() || '';
        const velikost = document.querySelector('#podrobnosti table tr:nth-child(7) td')?.textContent?.replace(/[^\d]/g, '') || '';
        const leto = document.querySelector('#podrobnosti table tr:nth-child(8) td')?.textContent?.replace(/[^\d]/g, '') || '';
        const energetskiRazred = document.querySelector('#podrobnosti table tr:nth-child(9) td .icon16')?.textContent?.trim() || '';
    
        return {
          naslovOglasa,
          cena,
          tip,
          regija,
          upravnaEnota,
          velikost,
          leto,
          energetskiRazred
        };
      });
      results.push(data);
    }


  
    await browser.close();
    return {
      results
    };
  }catch (error) {
    console.log(error);
  }
};

export const exampleRouter = createTRPCRouter({
  hello: publicProcedure
    .input(z.object({ text: z.string() }))
    .query(({ input }) => {
      return {
        greeting: `Hello ${input.text}`,
      };
    }),

  getAll: publicProcedure.query(({ ctx }) => {
    return ctx.prisma.example.findMany();
  }),

  getSecretMessage: protectedProcedure.query(() => {
    return "you can now see this secret message!";
  }),
  getComparison: publicProcedure
    // accept array of two strings as input
    //.input(z.array(z.string()).nonempty().length(2))
    .query(async ({}) => {
      const url2 = "https://www.nepremicnine.net/oglasi-prodaja/lj-center-hisa_6542556/"
      const url1 =
        "https://www.nepremicnine.net/oglasi-prodaja/lj-center-stanovanje_6537956/";
      const data1 = await getDataFromUrl([url1,url2]);
      return data1;
    }),
});
