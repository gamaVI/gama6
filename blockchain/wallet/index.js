const ChainUtil = require("../chain-util");
const { INITIAL_BALANCE } = require("../config");
const Event = require("./event");

class Wallet {
  constructor() {}

  toString() {}

  /**
   * combines the functionality to create a new transaction
   * update a transaction into one and also update the transaction
   * pool if the transaction exists already.
   */

  createEvent(type, data, eventPool) {
    let event = Event.newEvent(type, data);
    eventPool.addEvent(event);
    return event;
  }

  static blockchainWallet() {
    const blockchainWallet = new this();
    blockchainWallet.address = "blockchain-wallet";
    return blockchainWallet;
  }
}

module.exports = Wallet;
