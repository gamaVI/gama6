const ChainUtil = require("../chain-util");
class Event {
  constructor(type, data) {
    this.id = ChainUtil.id(); // Unique identifier for the event
    this.type = type; // Type of event, e.g., 'earthquake', 'flood', etc.
    this.data = data; // Detailed information about the event
    this.timestamp = Date.now(); // Timestamp of the event creation
  }

  /**
   * Update the event data.
   */
  update(newData) {
    this.data = newData;
    this.timestamp = Date.now(); // Update timestamp
  }

  /**
   * Create a new event.
   */
  static newEvent(type, data) {
    return new Event(type, data);
  }
}

module.exports = Event;