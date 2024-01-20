const express = require("express");
const Blockchain = require("../blockchain");
const bodyParser = require("body-parser");
const P2pserver = require("./p2p-server");

//get the port from the user or set the default port
const HTTP_PORT = process.env.HTTP_PORT || 3001;

//create a new app
const app = express();

//using the blody parser middleware
app.use(bodyParser.json());

const blockchain = new Blockchain();
const p2pserver = new P2pserver(blockchain);

app.get("/blocks", (req, res) => {
  res.json(blockchain.chain);
});

app.post("/mine", (req, res) => {
  console.log(req.body);
  const block = blockchain.addBlock(req.body);
  p2pserver.syncChain();
  res.redirect("/blocks");
});

// app server configurations
app.listen(HTTP_PORT, () => {
  console.log(`listening on port ${HTTP_PORT}`);
});

blockchain.addBlock(
  {
    type:"Information",
    message:"Test message"

  }
)
blockchain.addBlock(
  {
    type:"Information",
    message:"Test message 2"

  }
)
blockchain.addBlock(
  {
    type:"Information",
    message:"Test message 3"

  }
)

p2pserver.listen();
