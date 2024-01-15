var mqtt = require("mqtt");
const { sendMessage } = require("./util");
const API_URL = "http://localhost:3001/mine";

var options = {
  host: "800979d8ad28412c984565b6102b58a1.s2.eu.hivemq.cloud",
  port: 8883,
  protocol: "mqtts",
  username: "galjeza55@gmail.com",
  password: "Geslo123.",
};

// initialize the MQTT client
var client = mqtt.connect(options);

// setup the callbacks
client.on("connect", function () {
  console.log("Connected");

  client.subscribe("Information");
  client.subscribe("Warning");
  client.subscribe("Alert");
});

client.on("error", function (error) {
  console.log(error);
});

client.on("message", function (topic, message) {
  console.log(
    "Received message on topic '" + topic + "': " + message.toString()
  );
  sendMessage(API_URL, topic, message.toString());
});
