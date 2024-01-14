const express = require("express");
const Blockchain = require("../blockchain");
const bodyParser = require("body-parser");
const P2pserver = require("./p2p-server");
const Miner = require("./miner");

//get the port from the user or set the default port
const HTTP_PORT = process.env.HTTP_PORT || 3001;

const Wallet = require("../wallet");
const EventPool = require("../wallet/event-pool");

//create a new app
const app = express();

//using the blody parser middleware
app.use(bodyParser.json());

// create a new blockchain instance
const blockchain = new Blockchain();

// create a new wallet
const wallet = new Wallet();

// create a new event pool which will be later
// decentralized and synchronized using the peer to peer server
const eventPool = new EventPool();

// create a p2p server instance with the blockchain and the event pool
const p2pserver = new P2pserver(blockchain, eventPool);

// create a miner
const miner = new Miner(blockchain, eventPool, wallet, p2pserver);
//EXPOSED APIs

app.get("/blocks", (req, res) => {
  res.json(blockchain.chain);
});

app.post("/mine", (req, res) => {
  const block = blockchain.addBlock(req.body.data);
  p2pserver.syncChain();
  res.redirect("/blocks");
});

// api to start mining
app.get("/mine-events", (req, res) => {
  const block = miner.mine();
  console.log(`New block added: ${block.toString()}`);
  res.redirect("/blocks");
});

// api to view events in the event pool
app.get("/events", (req, res) => {
  res.json(eventPool.events);
});

// create events
app.post("/event", (req, res) => {
  const { type, data } = req.body;
  const event = wallet.createEvent(type, data, eventPool);
  res.redirect("/events");
});

// app server configurations
app.listen(HTTP_PORT, () => {
  console.log(`listening on port ${HTTP_PORT}`);
});

// p2p server configuration
p2pserver.listen();
