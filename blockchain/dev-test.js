const Blockchain = require("./blockchain");

const blockchain = new Blockchain();

// Start the timer
const startTime = Date.now();

let totalHashCount = 0;

for (let i = 0; i < 10; i++) {
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
