const ChainUtil = require("../chain-util");
const {
  DIFFICULTY,
  MINE_RATE,
  DIFFICULTY_ADJUSTMENT_INTERVAL,
} = require("../config.js");

class Block {
  constructor(timestamp, lastHash, hash, data, nonce, difficulty, index) {
    this.timestamp = timestamp;
    this.index = index || 0;
    this.lastHash = lastHash;
    this.hash = hash;
    this.data = data;
    this.nonce = nonce;
    this.difficulty = difficulty || DIFFICULTY;
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
   */

  static mineBlock(lastBlock, data, difficulty) {
    let hash, timestamp;
    const lastHash = lastBlock.hash;

    let { index } = lastBlock;
    index = index + 1;

    let nonce = 0;
    do {
      nonce++;
      timestamp = Date.now();
      hash = Block.hash(timestamp, lastHash, data, nonce, difficulty);
      // checking if we have the required no of leading number of zeros
    } while (hash.substring(0, difficulty) !== "0".repeat(difficulty));

    return new this(timestamp, lastHash, hash, data, nonce, difficulty, index);
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
