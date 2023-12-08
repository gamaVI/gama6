const Blockchain = require("./blockchain"); // Replace with your actual path
const Miner = require("./app/miner"); // Replace with your actual path
const ChainUtil = require("./chain-util"); // Replace with your actual path
async function mineBlocks(numBlocks) {
  const blockchain = new Blockchain();

  console.log("Starting mining test...");

  let startTime = Date.now();

  for (let i = 0; i < numBlocks; i++) {
    console.log(`Mining block ${i + 1}`);
    blockchain.addBlock(`block ${i + 1}`);
  }

  let endTime = Date.now();
  let duration = (endTime - startTime) / 1000;

  console.log(`Mined ${numBlocks} blocks in ${duration} seconds.`);
  ChainUtil.saveBlockchainToFile(blockchain, "bc.json");
}

const NUM_BLOCKS_TO_MINE = 22;
mineBlocks(NUM_BLOCKS_TO_MINE);
