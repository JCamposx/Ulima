import POC from "./POC.js";

class SellInOnTradicional extends POC {
  constructor() {
    super("sell in", "on tradicional");
  }
}

class SellInOnModerno extends POC {
  constructor() {
    super("sell in", "on moderno");
  }
}

class SellInOffTradicional extends POC {
  constructor() {
    super("sell in", "off tradicional");
  }
}

class SellInOffModerno extends POC {
  constructor() {
    super("sell in", "off moderno");
  }
}

export {
  SellInOnTradicional,
  SellInOnModerno,
  SellInOffTradicional,
  SellInOffModerno,
};
