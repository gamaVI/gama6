var mqtt = require("mqtt");

var options = {
  host: "800979d8ad28412c984565b6102b58a1.s2.eu.hivemq.cloud",
  port: 8883,
  protocol: "mqtts",
  username: "galjeza55@gmail.com",
  password: "Geslo123.",
};

// initialize the MQTT client
var client = mqtt.connect(options);

client.on("connect", function () {
  // Once connected, publish a message
  client.publish("", "Hello from JS script");
  client.end(); // Close the connection after publishing
});

client.on("error", function (error) {
  console.log(error);
});
