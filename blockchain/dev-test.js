const Blockchain = require("./blockchain");
const ChainUtil = require("./chain-util");
const { NUM_WORKERS } = require("./config.js");
const fs = require("fs");

const blockchain = new Blockchain();

// Start the timer
const startTime = Date.now();

let totalHashCount = 0;
const NUMBLOCKS = 10;

for (let i = 0; i < NUMBLOCKS; i++) {
  const newBlock = blockchain.addBlock(`foo ${i}`);
  console.log(newBlock.toString());

  // Accumulate total hash count
  totalHashCount += newBlock.totalHashCount;
}

// Stop the timer
const endTime = Date.now();
const duration = (endTime - startTime) / 1000; // Duration in seconds

if (duration > 0) {
  const hashesPerSecond = totalHashCount / duration;
  console.log(`Total Hashes: ${totalHashCount}`);
  console.log(`Total Duration: ${duration} seconds`);
  console.log(`Hashes per Second: ${hashesPerSecond}`);
} else {
  console.log("Duration too short to calculate hashes per second");
}

const minignInfo = {
  totalHashCount,
  duration,
  threads: NUM_WORKERS,
  blocks: NUMBLOCKS,
  hashesPerSecond: totalHashCount / duration,
};

fs.writeFileSync(
  "./stats/miningInfo_" + NUMBLOCKS + "blocks_" + NUM_WORKERS + "threads.json",
  JSON.stringify(minignInfo)
);

console.log(minignInfo);

ChainUtil.saveBlockchainToFile(blockchain, "./stats/bc_3thread_10blocks.json");
