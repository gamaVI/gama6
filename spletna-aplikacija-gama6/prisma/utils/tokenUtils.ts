import puppeteer from "puppeteer";
export const getAuthToken = async () => {
    let authToken = "";
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
  