import { prisma } from "../src/server/db";
import axios from "axios";
/* disable eslint */

async function postRequest(unit_type, unit_subtype, date_interval) {
  const url = "http://51.136.39.46:3000/api/scraping/posli"; // replace with your server url if needed

  try {
    const response = await axios.post(url, {
      unit_type,
      unit_subtype,
      date_interval_months: date_interval,
    });

    console.log(`Status: ${response.status}`);
    console.log("Body: ", response.data);
  } catch (err) {
    console.error(err);
  }
}

async function main() {
  // set the date interval to 6 months
  const date_interval = 6;

  // possible unit types and subtypes
  const unit_types = [1, 2];
  const subtypes = {
    1: [1, 2, 3],
    2: [1, 2, 3, 4, 5],
  };

  unit_types.forEach((unit_type) => {
    const unit_subtypes = subtypes[unit_type];

    unit_subtypes.forEach(async (unit_subtype) => {
      const results = await postRequest(unit_type, unit_subtype, date_interval);
      console.log(results);
    });
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
