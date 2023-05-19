
import puppeteer from "puppeteer-extra";
import StealthPlugin from "puppeteer-extra-plugin-stealth";
// eslint-disable-next-line @typescript-eslint/no-unsafe-call

import  { Configuration, OpenAIApi } from "openai";
import { env } from "@/env.mjs";





export const getDataFromUrl = async (urls: string[]) => {
    
    try {
      // extracd id from url : https://www.nepremicnine.net/oglasi-prodaja/lj-center-stanovanje_6537956/
      const results = []
      puppeteer.use(StealthPlugin())
      const browser = await puppeteer.launch({
        headless: true,
      });
      const page = await browser.newPage();
  
  
      for(const url of urls){
        const id = url?.split("_")[1]?.split("/")[0] || "Ni podatka";
        const printURL = `https://www.nepremicnine.net/prt-oglas.php?id=${id}`;
        await page.goto(printURL);
        await page.waitForSelector('h1 strong');
        const data = await page.evaluate(() => {
            const getTextFromSibling = (searchText:string) => {
                const thElement = Array.from(document.querySelectorAll('th')).find((th) => th.textContent?.trim().includes(searchText));
                return thElement?.nextElementSibling?.textContent?.trim() || '';
              };
          const naslovOglasa = document.querySelector('h1 strong')?.textContent?.trim() || '';
          const cena = document.querySelector('.cena')?.textContent?.replace(/[^\d,]/g, '') || '';
          // Vrsta is in td next to a th with text Posredovanje
          const tip = getTextFromSibling("Vrsta");
          const regija = getTextFromSibling("Regija");
          const upravnaEnota = getTextFromSibling("Upravna enota");
          const velikost = getTextFromSibling("Velikost");
          const leto = getTextFromSibling("Leto");
          const energetskiRazred = getTextFromSibling("Energ. razred");
          const obcina = getTextFromSibling("Občina");
            const opisParagraphs = document.querySelectorAll('.web-opis p');
            const opis = Array.from(opisParagraphs).map((p) => p.textContent).join('\n');




          return {
            naslovOglasa,
            cena,
            tip,
            regija,
            upravnaEnota,
            velikost,
            leto,
            energetskiRazred,
            opis,
            obcina
          };
        });
        console.log(data);
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


  export const getComparison = async (urls:string[]) => {
    try{

    
    const realEstates = await getDataFromUrl(urls);
    const configuration = new Configuration({
        apiKey: env.OPENAI_API_KEY,
      });

        const openai = new OpenAIApi(configuration);
        const prompt = `Primerjaj dve nepremičnini ki ti jih bom podal. Povej kaj so glavne razlike med njima( prednosti slabosti) in povej katero se ti zdi boljše in pojasni svoj odgovor. To so podatki o nepremičninah: 
        Nepremičnina 1:
        Naslov oglasa: ${realEstates?.results[0]?.naslovOglasa || "Ni podatka"}
        Cena: ${realEstates?.results[0]?.cena || "Ni podatka"}
        Tip: ${realEstates?.results[0]?.tip || "Ni podatka"}
        Regija: ${realEstates?.results[0]?.regija || "Ni podatka"}
        Upravna enota: ${realEstates?.results[0]?.upravnaEnota || "Ni podatka"}
        Velikost: ${realEstates?.results[0]?.velikost || "Ni podatka"}
        Leto: ${realEstates?.results[0]?.leto || "Ni podatka"}
        Energetski razred: ${realEstates?.results[0]?.energetskiRazred || "Ni podatka"}
        Občina: ${realEstates?.results[0]?.obcina || "Ni podatka"}
        Opis: ${realEstates?.results[0]?.opis || "Ni podatka"}
        -----------------------------------------------
        Nepremičnina 2:
        Naslov oglasa: ${realEstates?.results[1]?.naslovOglasa || "Ni podatka"}
        Cena: ${realEstates?.results[1]?.cena || "Ni podatka"}
        Tip: ${realEstates?.results[1]?.tip || "Ni podatka"}
        Regija: ${realEstates?.results[1]?.regija || "Ni podatka"}
        Upravna enota: ${realEstates?.results[1]?.upravnaEnota || "Ni podatka"}
        Velikost: ${realEstates?.results[1]?.velikost || "Ni podatka"}
        Leto: ${realEstates?.results[1]?.leto || "Ni podatka"}
        Energetski razred: ${realEstates?.results[1]?.energetskiRazred || "Ni podatka"}
        Občina: ${realEstates?.results[1]?.obcina || "Ni podatka"}
        Opis: ${realEstates?.results[1]?.opis || "Ni podatka"}
        `

        


    const result = "asd"

    const completion = await openai.createChatCompletion({
        model: "gpt-3.5-turbo",
        messages: [
            {
                "role": "system",
                "content": "Si poznavalec nepremičnin v sloveniji in odgovoriš na vsako vprašanje čeprav imaš malo informacij o nepremičninah"
            },
            {
                "role": "user",
                "content": prompt
            }
            ], 
    })

    const comparisonText = completion.data.choices[0]?.message?.content || "Ni podatka";
    return {
        comparisonText,
        realEstates: realEstates?.results || []
    }; 
}catch(error){
    console.log(error);
}
  }



  