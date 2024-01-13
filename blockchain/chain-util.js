const SHA256 = require("crypto-js/sha256");
const fs = require("fs");

class ChainUtil {
  static hash(data) {
    return SHA256(JSON.stringify(data)).toString();
  }

  static saveBlockchainToFile(blockchain, filename) {
    const jsonContent = JSON.stringify(blockchain, null, 2); // Beautify the JSON

    fs.writeFile(filename, jsonContent, "utf8", (err) => {
      if (err) {
        console.log("An error occurred while writing JSON to file.");
        return console.log(err);
      }

      console.log("Blockchain has been saved to file.");
    });
  }
}

module.exports = ChainUtil;
