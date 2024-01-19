const WebSocket = require("ws");

const P2P_PORT = process.env.P2P_PORT || 5001;
const peers = process.env.PEERS ? process.env.PEERS.split(",") : [];

const MESSAGE_TYPE = {
  chain: "CHAIN",
  event: "EVENT",
  clear_events: "CLEAR_EVENTS",
};

class P2pserver {
  constructor(blockchain) {
    this.blockchain = blockchain;
    this.sockets = [];
  }

  listen() {
    const server = new WebSocket.Server({ port: P2P_PORT });
    server.on("connection", (socket) => this.connectSocket(socket));
    this.connectToPeers();
    console.log(`Listening for peer to peer connection on port: ${P2P_PORT}`);
  }

  connectSocket(socket) {
    this.sockets.push(socket);
    console.log("Socket connected");
    this.messageHandler(socket);
    this.sendChain(socket);
  }

  connectToPeers() {
    peers.forEach((peer) => {
      const socket = new WebSocket(peer);
      socket.on("open", () => this.connectSocket(socket));
    });
  }

  messageHandler(socket) {
    socket.on("message", (message) => {
      const data = JSON.parse(message);
      console.log("data ", data);

      switch (data.type) {
        case MESSAGE_TYPE.chain:
          this.blockchain.replaceChain(data.chain);
          break;
      }
    });
  }

  sendChain(socket) {
    socket.send(
      JSON.stringify({
        type: MESSAGE_TYPE.chain,
        chain: this.blockchain.chain,
      })
    );
  }

  syncChain() {
    this.sockets.forEach((socket) => {
      this.sendChain(socket);
    });
  }

  broadcastEvent(event) {
    this.sockets.forEach((socket) => {
      this.sendEvent(socket, event);
    });
  }

  sendEvent(socket, event) {
    socket.send(
      JSON.stringify({
        type: MESSAGE_TYPE.event,
        event: event,
      })
    );
  }

  broadcastClearEvents() {
    this.sockets.forEach((socket) => {
      socket.send(
        JSON.stringify({
          type: MESSAGE_TYPE.clear_events,
        })
      );
    });
  }
}

module.exports = P2pserver;
