const {getAuthToken} = require("./utils/token");

async function scrapeTransactions() {
    const token = await getAuthToken();
    
    
}


scrapeTransactions().then(() => {
    console.log("Done");
}
).catch((e) => {
    console.error(e);
    process.exit(1);
}
);