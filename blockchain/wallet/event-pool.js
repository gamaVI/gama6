const Event = require("./event");

class EventPool {
  constructor() {
    this.events = [];
  }
  /**
   * this method will add a event
   * it is possible that the transaction exists already
   * so it will replace the transaction with the new transaction
   * after checking the input id and adding new outputs if any
   * we call this method and replace the transaction in the pool
   */
  addEvent(event) {
    this.events.push(event);
  }

  /**
   * returns a existing transaction from the pool
   * if the inputs matches
   */

  /**
   * sends valid transactions to the miner
   */

  clear() {
    this.events = [];
  }
}

module.exports = EventPool;
