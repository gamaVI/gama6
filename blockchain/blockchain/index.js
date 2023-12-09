const Block = require("./block");
const { DIFFICULTY_ADJUSTMENT_INTERVAL, MINE_RATE } = require("../config.js");

class Blockchain {
  constructor() {
    this.chain = [Block.genesis()];
  }
  /**
   * utility function to add block to the blockchain
   * returns the added block
   */

  addBlock(data) {
    const lastBlock = this.chain[this.chain.length - 1];
    const difficulty = Blockchain.adjustDifficulty(lastBlock, this);
    const newBlock = Block.mineBlock(lastBlock, data, difficulty);
    this.chain.push(newBlock);
    return newBlock;
  }

  /**
   * checks if the chain recieved from another miner is valid or not
   */

  isValidChain(chain) {
    if (JSON.stringify(chain[0]) !== JSON.stringify(Block.genesis()))
      return false;

    for (let i = 1; i < chain.length; i++) {
      const block = chain[i];
      const lastBlock = chain[i - 1];
      if (
        block.lastHash !== lastBlock.hash ||
        block.hash !== Block.blockHash(block)
      )
        return false;
    }

    return true;
  }
  /**
   * replace the chain if the chain recieved from another miner
   * is longer and valid
   */

  static adjustDifficulty(lastBlock, blockchain) {
    // Preveri, ali je čas za prilagoditev težavnosti
    if (
      lastBlock.index % DIFFICULTY_ADJUSTMENT_INTERVAL === 0 &&
      lastBlock.index !== 0
    ) {
      return this.calculateNewDifficulty(lastBlock, blockchain);
    }

    return lastBlock.difficulty;
  }

  static calculateNewDifficulty(lastBlock, blockchain) {
    const lastAdjustmentBlock =
      blockchain.chain[
        blockchain.chain.length - DIFFICULTY_ADJUSTMENT_INTERVAL
      ];

    const timeExpected = MINE_RATE * DIFFICULTY_ADJUSTMENT_INTERVAL; // Pričakovani čas za zadnjih N blokov
    const timeTaken = lastBlock.timestamp - lastAdjustmentBlock.timestamp; // Dejanski čas za zadnjih N blokov

    if (timeTaken < timeExpected / 2) {
      return lastAdjustmentBlock.difficulty + 1;
    } else if (timeTaken > timeExpected * 2) {
      return Math.max(1, lastAdjustmentBlock.difficulty - 1); // Prepreči, da bi težavnost padla pod 1
    }

    return lastAdjustmentBlock.difficulty;
  }

  replaceChain(newChain) {
    if (newChain.length <= this.chain.length) {
      console.log("Recieved chain is not longer than the current chain");
      return;
    } else if (!this.isValidChain(newChain)) {
      console.log("Recieved chain is invalid");
      return;
    }

    console.log("Replacing the current chain with new chain");
    this.chain = newChain;
  }
}

module.exports = Blockchain;
