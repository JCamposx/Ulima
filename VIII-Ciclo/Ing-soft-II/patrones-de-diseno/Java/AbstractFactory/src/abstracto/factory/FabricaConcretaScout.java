package abstracto.factory;

public class FabricaConcretaScout extends Fabrica {
    @Override
    public Soldado crearSoldado() {
        return new ScoutTrooper();
    }

    @Override
    public Vehiculo crearVehiculo() {
        return new Moto();
    }
}
