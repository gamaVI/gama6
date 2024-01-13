const ChainUtil = require("../chain-util");
const { NUM_WORKERS } = require("../config.js");
const { Worker, SharedArrayBuffe } = require("worker_threads");
const deasync = require("deasync");

const {
  DIFFICULTY,
  MINE_RATE,
  DIFFICULTY_ADJUSTMENT_INTERVAL,
} = require("../config.js");

class Block {
  constructor(
    timestamp,
    lastHash,
    hash,
    data,
    nonce,
    difficulty,
    index,
    totalHashCount = 0
  ) {
    this.timestamp = timestamp;
    this.index = index || 0;
    this.lastHash = lastHash;
    this.hash = hash;
    this.data = data;
    this.nonce = nonce;
    this.difficulty = difficulty || DIFFICULTY;
    this.totalHashCount = totalHashCount || 0;
  }

  /**
   * returns what the object looks like
   * substring is used to make it look nice
   * hashes are too big to printed on command line
   */

  toString() {
    return `Block - 
        Timestamp : ${this.timestamp}
        Last Hash : ${this.lastHash.substring(0, 10)}
        Hash      : ${this.hash.substring(0, 10)}
        Nonce     : ${this.nonce}
        Data      : ${this.data}
        Difficulty: ${this.difficulty}`;
  }

  /**
   * function to create the first block or the genesis block
   */

  static genesis() {
    return new this("Genesis time", "----", "f1574-h4gh", [], 0, DIFFICULTY, 0);
  }

  /**
   * function to create new blocks or to mine new blocks
   *
   *
   */

  static mineBlock(lastBlock, data, difficulty) {
    let minedBlock;
    let error;
    let done = false;

    // Asynchronous mining logic (same as before)
    this.mineBlockAsync(lastBlock, data, difficulty)
      .then((block) => {
        minedBlock = block;
        done = true;
      })
      .catch((err) => {
        error = err;
        done = true;
      });

    // Deasync loop waits here until mining is complete
    deasync.loopWhile(() => !done);

    if (error) {
      throw error;
    }

    return minedBlock;
  }

  static mineBlockAsync(lastBlock, data, difficulty) {
    return new Promise((resolve, reject) => {
      const workers = [];
      const lastHash = lastBlock.hash;
      let nonceFound = false;

      // Shared buffer for hash count
      const sharedBuffer = new SharedArrayBuffer(4); // 4 bytes for an Int32
      const hashCount = new Int32Array(sharedBuffer);

      // Calculate the nonce range for each worker
      const maxNonce = 2 ** 32;
      const nonceRange = Math.floor(maxNonce / NUM_WORKERS);

      for (let i = 0; i < NUM_WORKERS; i++) {
        const startNonce = i * nonceRange;
        const endNonce = startNonce + nonceRange;
        const worker = new Worker("./mineWorker.js", {
          workerData: {
            lastHash,
            data,
            difficulty,
            startNonce,
            endNonce,
            sharedBuffer,
          },
        });

        worker.on("message", (result) => {
          if (!nonceFound && result.found) {
            nonceFound = true;
            workers.forEach((w) => w.terminate()); // Terminate all workers
            const totalHashCount = Atomics.load(hashCount, 0); // Read total hash count
            const newBlock = new Block(
              result.timestamp,
              lastHash,
              result.hash,
              data,
              result.nonce,
              difficulty,
              lastBlock.index + 1,
              totalHashCount // Add total hash count to the block
            );
            resolve(newBlock);
          }
        });

        worker.on("error", reject);
        worker.on("exit", (code) => {
          if (code !== 0 && !nonceFound) {
            reject(new Error(`Worker stopped with exit code ${code}`));
          }
        });

        workers.push(worker);
      }
    });
  }

  /**
   * function to create the hash value of the block data
   */

  static hash(timestamp, lastHash, data, nonce, difficulty) {
    return ChainUtil.hash(
      `${timestamp}${lastHash}${data}${nonce}${difficulty}`
    ).toString();
  }

  /**
   * return the hash value of the passed block
   */

  static blockHash(block) {
    //destructuring
    const { timestamp, lastHash, data, nonce, difficulty } = block;
    return Block.hash(timestamp, lastHash, data, nonce, difficulty);
  }

  /**
   * utility function to adjust difficulty
   */

  static adjustDifficulty(lastBlock, currentTime) {
    const timeTaken = currentTime - lastBlock.timestamp; // Čas, ki je bil potreben za najti zadnji blok

    if (lastBlock.index % DIFFICULTY_ADJUSTMENT_INTERVAL === 0) {
      // Preveri, če je čas za popravek težavnosti
      if (timeTaken < MINE_RATE / 2) {
        // Če je ustvarjanje trajalo manj kot polovico pričakovanega časa
        return lastBlock.difficulty + 1;
      } else if (timeTaken > MINE_RATE * 2) {
        // Če je ustvarjanje trajalo več kot dvojno pričakovanega časa
        return Math.max(1, lastBlock.difficulty - 1); // Težavnost ne sme pasti pod 1
      }
    }

    return lastBlock.difficulty; // Če ni potreben popravek, vrni trenutno težavnost
  }
}

// share this class by exporting it

module.exports = Block;
