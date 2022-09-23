import FactoryProvider from "./FactoryProvider.js"

const abstractFactory = new FactoryProvider()
const sellin = abstractFactory.crearTipo('sell in')
const sellinOnTradicional = sellin.crear_canal('on tradicional')
console.log(sellinOnTradicional.obtener_descripcion())
