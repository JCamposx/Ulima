package abstracto.factory;

public class FabricaConcretaSTT extends Fabrica {
    @Override
    public Soldado crearSoldado() {
        return new STT();
    }

    @Override
    public Vehiculo crearVehiculo() {
        return new Moto();
    }
}
