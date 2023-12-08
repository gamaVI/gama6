const Blockchain = require("./blockchain"); // Replace with your actual path
const Miner = require("./app/miner"); // Replace with your actual path

async function mineBlocks(numBlocks) {
  const blockchain = new Blockchain();

  console.log("Starting mining test...");

  let startTime = Date.now();

  for (let i = 0; i < numBlocks; i++) {
    console.log(`Mining block ${i + 1}`);
    blockchain.addBlock(`block ${i + 1}`);
  }

  let endTime = Date.now();
  let duration = (endTime - startTime) / 1000; // Duration in seconds

  console.log(`Mined ${numBlocks} blocks in ${duration} seconds.`);
}

const NUM_BLOCKS_TO_MINE = 10;
mineBlocks(NUM_BLOCKS_TO_MINE);
