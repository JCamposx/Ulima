package abstracto.factory;

public class FabricaConcretaStorm extends Fabrica {
    @Override
    public Soldado crearSoldado() {
        return new StormTrooper();
    }

    @Override
    public Vehiculo crearVehiculo() {
        return new Moto();
    }
}
