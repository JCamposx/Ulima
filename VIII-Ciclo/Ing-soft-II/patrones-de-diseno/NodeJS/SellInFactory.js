import {
  SellInOnTradicional,
  SellInOnModerno,
  SellInOffTradicional,
  SellInOffModerno,
} from "./SellIn.js";

class SellInFactory {
  crear_canal(canal) {
    switch (canal) {
      case "on tradicional":
        return new SellInOnTradicional();

      case "on moderno":
        return new SellInOnModerno();

      case "off tradicional":
        return new SellInOffTradicional();

      case "off moderno":
        return new SellInOffModerno();

      default:
        break;
    }
  }
}

export default SellInFactory;
