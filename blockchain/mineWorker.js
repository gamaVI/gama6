const { parentPort, workerData } = require("worker_threads");
const ChainUtil = require("./chain-util");

function mineBlock(
  lastHash,
  data,
  difficulty,
  startNonce,
  endNonce,
  sharedBuffer
) {
  let hash, timestamp;
  const hashCount = new Int32Array(sharedBuffer);

  for (let nonce = startNonce; nonce < endNonce; nonce++) {
    timestamp = Date.now();
    hash = ChainUtil.hash(
      `${timestamp}${lastHash}${data}${nonce}${difficulty}`
    ).toString();
    Atomics.add(hashCount, 0, 1); // Increment the shared hash count

    if (hash.substring(0, difficulty) === "0".repeat(difficulty)) {
      parentPort.postMessage({ nonce, timestamp, hash, found: true });
      return;
    }
  }
  parentPort.postMessage({ found: false });
}

mineBlock(
  workerData.lastHash,
  workerData.data,
  workerData.difficulty,
  workerData.startNonce,
  workerData.endNonce,
  workerData.sharedBuffer
);
