import { prisma } from "../src/server/db";
import { getAuthToken } from "./utils/tokenUtils";
import axios from "axios";
/* disable eslint */

const scrapeAndSaveToDB = async (token, priceFrom, priceTo) => {
  try {
    const response = await axios({
      method: 'post',
      url: 'https://sparkasse.arvio.si/api/v1/transaction-map/search',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Token ${token}`
      },
      data: {
        transaction_amount_gross__gte: priceFrom,
        transaction_amount_gross__lte: priceTo,
        date_interval_months: 100,
        bbox: '14.41011428833008,46.01043551674467,14.602031707763674,46.10573209804895',
      },
    });
    
    const deals = response.data;
    const detailedDeals = [];
    for (const deal of deals) {
      const dealDetails = await axios(
        {
          method: "get",
          url : `https://sparkasse.arvio.si/api/v1/transaction-map/search?id=${deal.id}`,
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Token ${token}`,
          }
        }
      );
      const dealDetailsJson = dealDetails.data;
      detailedDeals.push(dealDetailsJson);
    }

    // save to db
    for (const deal of detailedDeals) {
      await axios({
        method: "post",
        url: "http://localhost:3000/api/transactions/new",
        data: {
          apiId: deal.id,
          componentType: deal.get_component_type_display,
          address: deal.address,
          transactionAmountM2: deal.transaction_amount_m2,
          estimatedAmountM2: deal.estimated_amount_m2,
          isEstimatedAmount: deal.is_estimated_amount,
          gps: deal.gps,
          transactionItemsList: deal.transaction_items_list,
          transactionSumParcelSizes: deal.transaction_sum_parcel_sizes,
          transactionDate: deal.transaction_date,
          transactionAmountGross: deal.transaction_amount_gross,
          transactionTax: deal.transaction_tax,
          buildingYearBuilt: deal.building_year_built,
          unitRoomCount: deal.unit_room_count,
          unitRoomsSumSize: deal.unit_rooms_sum_size,
          unitRooms: deal.unit_rooms,

        },
      });
          
          
    
  }
}
  catch (error) {
    console.error(error);
  }
};


      



async function main() {
  let priceFrom = 10000;
  let priceTo = 11000;
 

  const token = await getAuthToken();
  


  while(priceTo < 5000000) {
    const deals = await scrapeAndSaveToDB(token, priceFrom, priceTo)
    priceFrom = priceTo;
    priceTo = priceTo + 5000;
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
