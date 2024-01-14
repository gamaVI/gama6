// mqttClient.js
const mqtt = require("mqtt");

const client = mqtt.connect("mqtt://your_mqtt_broker:1883");

client.on("connect", () => {
  console.log("Connected to MQTT Broker");
  client.subscribe("your/topic", (err) => {
    if (!err) {
      console.log("Subscribed to topic: your/topic");
    } else {
      console.error("Subscription error:", err);
    }
  });
});

client.on("message", (topic, message) => {
  console.log("Received message:", message.toString());
});
