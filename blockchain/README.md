# Gama6 blockchain

## Installation

npm install

## Run

Node 0: npm run dev
Node 1: HTTP_PORT=3001 P2P_PORT=6001 npm run dev
Node 2: HTTP_PORT=3001 P2P_PORT=6002 PEERS=ws://localhost:6001 npm run dev
Node 3: HTTP_PORT=3001 P2P_PORT=6003 PEERS=ws://localhost:6001,ws://localhost:6002 npm run dev

## API

### Get blockchain

```
curl http://localhost:3001/blocks
```

### Mine block

```
curl -X POST http://localhost:3001/mineBlock
```
