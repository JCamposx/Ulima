import SellInFactory from "./SellInFactory.js";

class FactoryProvider {
  crearTipo(tipo) {
    switch (tipo) {
      case "sell in":
        return new SellInFactory();

      default:
        console.log("String ingresado no válido");
        break
    }
  }
}

export default FactoryProvider;
