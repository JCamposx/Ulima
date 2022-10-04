package abstracto.factory;

public class FabricaConcretaSCT extends Fabrica {
    @Override
    public Soldado crearSoldado() {
        return new SCT();
    }

    @Override
    public Vehiculo crearVehiculo() {
        return new Auto();
    }
}
