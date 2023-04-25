import { prisma } from "../src/server/db";
import seedData from "./seeddata.json";



async function main() {
    // Create posel data
    for (const poselData of seedData.posel) {
      await prisma.posel.create({
        data: poselData
      })
    }
  
    // Create oglas data
    for (const oglasData of seedData.oglas) {
      await prisma.oglas.create({
        data: oglasData
      })
    }
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
