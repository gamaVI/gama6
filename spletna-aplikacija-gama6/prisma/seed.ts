import { prisma } from "../src/server/db";

async function main() {
  const posel = await prisma.posel.create({
    data: {
      skupnaPovrsina: 1456.67,
      cena: 123456,
      cenaNaM2: 13.5,
      lokacija: "Ljubljana",
      datumPosla: new Date(),
      letoGradnje: 1999,
      tipPosla: "Prodaja",
      tipNepremicnine: "Stanovanje",
      koordinataX: 46.056946,
      koordinataY: 14.505751,
    },
  });
  const oglas = await prisma.oglas.create({
    data: {
      url: "https://www.nepremicnine.net/oglasi-prodaja/ljubljana-mesto-stanovanje_6117937/",
      naslov: "Ljubljana, Center, 2-sobno stanovanje, 50 m2",
      tip: "Stanovanje",
      opis: "Prodamo 2-sobno stanovanje v centru Ljubljane, na odlični lokaciji, v bližini vse infrastrukture. Stanovanje se nahaja v 1. nadstropju urejene večstanovanjske stavbe zgrajene leta 1930. Stanovanje je bilo v celoti obnovljeno leta 2018. Stanovanje obsega: hodnik, kopalnico, spalnico, kuhinjo z jedilnico in dnevno sobo. Stanovanje je svetlo in funkcionalno razporejeno. Stanovanje je opremljeno in vseljivo po dogovoru. Vseljivo takoj. Vabljeni na ogled!",
      velikost: 50,
      cena: 123456,
      agencija: "ABC nepremičnine",
      lokacija: "Ljubljana",
    },
  });
  console.log({ posel, oglas });
}

main()
  .then(async () => {
    await prisma.$disconnect();
  })
  .catch(async (e) => {
    console.error(e);
    await prisma.$disconnect();
    process.exit(1);
  });
