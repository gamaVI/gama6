const axios = require("axios");

async function sendMessage(apiUrl, type, message) {
  try {
    const response = await axios.post(apiUrl, {
      type: type,
      message: message,
    });

    console.log("Response data:", response.data);
    return response.data;
  } catch (error) {
    console.error(
      "Error sending message:",
      error.response ? error.response.data : error.message
    );
  }
}
