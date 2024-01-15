const Event = require("../wallet/event");

class Miner {
  constructor(blockchain, eventPool, wallet, p2pServer) {
    this.blockchain = blockchain;
    this.p2pServer = p2pServer;
    this.wallet = wallet;
    this.eventPool = eventPool;
  }

  mine() {
    console.log("Inside mine");

    /**
     * 1. Grab events from the pool
     * 2. Create a block using these events
     * 3. Synchronize the chain to include the new block
     * 4. Clear the event pool
     */

    // Get events from the event pool
    const events = this.eventPool.events;

    // Create a block consisting of these events
    const block = this.blockchain.addBlock(events);

    // Synchronize the chains in the p2p server
    this.p2pServer.syncChain();

    // Clear the event pool
    this.eventPool.clear();

    // Optionally, broadcast to other miners/nodes to clear their pools
    this.p2pServer.broadcastClearEvents();

    return block;
  }
}

module.exports = Miner;
