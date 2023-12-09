const { parentPort } = require("worker_threads");
const ChainUtil = require("./chain-util");

function mineBlock(lastHash, data, difficulty, startNonce, endNonce) {
  let hash, timestamp;
  for (let nonce = startNonce; nonce < endNonce; nonce++) {
    timestamp = Date.now();
    hash = ChainUtil.hash(
      `${timestamp}${lastHash}${data}${nonce}${difficulty}`
    ).toString();
    if (hash.substring(0, difficulty) === "0".repeat(difficulty)) {
      parentPort.postMessage({ nonce, timestamp, hash });
      return;
    }
  }
}

parentPort.on("message", (data) => {
  mineBlock(
    data.lastHash,
    data.data,
    data.difficulty,
    data.startNonce,
    data.endNonce
  );
});
