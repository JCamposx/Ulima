class POC {
  constructor(tipo, canal) {
    this._tipo = tipo; // Sel in / sell out
    this._canal = canal;

    // Sell in: on tradicional / moderno. off tradicional / moderno
    // Sell out: bodega / cantina
  }

  get tipo() {
    return this._tipo;
  }

  get canal() {
    return this._canal;
  }

  obtener_descripcion() {
    return `soy de tipo "${this._tipo}" y canal "${this._canal}!"`;
  }
}

export default POC;
