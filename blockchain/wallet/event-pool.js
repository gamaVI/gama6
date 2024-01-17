const Event = require("./event");

class EventPool {
  constructor() {
    this.events = [];
  }

  addEvent(event) {
    this.events.push(event);
  }

  clear() {
    this.events = [];
  }
}

module.exports = EventPool;
