import { prisma } from "../src/server/db";
import {  faker } from '@faker-js/faker';
/*disable-eslint*/

function generateRandomGps() {
  return {
    lat: faker.location.latitude({
      min: 46.05,
      max: 46.06,
    }),
    lng: faker.location.longitude(
      {
        min: 14.45,
        max: 14.52,
      }
    ),
  };
}


function generateRandomTransaction(gpsId :string) {
  const transactionAmountM2 = faker.datatype.float({ min: 50.0, max: 1000.0 });
  const estimatedAmountM2 = faker.datatype.float({ min: 50.0, max: 1000.0 });
  const transactionSumParcelSizes = faker.datatype.number({ min: 1, max: 10 });
  const transactionAmountGross = faker.datatype.number({ min: 10000, max: 1000000 });
  const buildingYearBuilt = faker.datatype.number({ min: 1900, max: 2023 });
  const unitRoomsSumSize = faker.datatype.number({ min: 10, max: 200 });
  const transactionDate = faker.date.past().toISOString();

  // Modify the return object to match the required shape
  return {
    apiId: faker.string.uuid(),
    componentType: faker.random.word(),
    address: faker.address.streetAddress(),
    transactionAmountM2: transactionAmountM2,
    estimatedAmountM2: estimatedAmountM2,
    isEstimatedAmount: faker.datatype.boolean(),
    transactionItemsList: [faker.random.word()],
    transactionSumParcelSizes: transactionSumParcelSizes,
    transactionDate: transactionDate,
    transactionAmountGross: transactionAmountGross,
    transactionTax: faker.datatype.number({ min: 100, max: 10000 }),
    buildingYearBuilt: buildingYearBuilt,
    unitRoomCount: faker.datatype.number({ min: 1, max: 10 }),
    unitRoomsSumSize: unitRoomsSumSize,
    unitRooms: faker.random.word(),
    gpsId: gpsId,
  };
}

function generateRandomOglas() {
  const oglas = {
      url: faker.internet.url(),
      naslov: faker.lorem.sentence(),
      tip: faker.lorem.word(),
      opis: faker.lorem.paragraph(),
      velikost: faker.number.float({ min: 20.0, max: 300.0 }),
      cena: faker.number.int({ min: 50000, max: 500000 }),
      agencija: faker.company.name(),
      lokacija: faker.address.city()
  };

  return oglas;
}




async function main() {
  for (let i = 0; i < 1000; i++) {
    const gpsData = generateRandomGps();
    const createdGps = await prisma.gps.create({
      data: gpsData,
    });
    const transaction = generateRandomTransaction(createdGps.id);
    await prisma.transaction.create({
      data: transaction,
    });
  }
  
  const oglasi = Array.from({ length: 1000 }, generateRandomOglas);
  await prisma.oglas.createMany({
    data: oglasi,
  });
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
