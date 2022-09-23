package abstracto.factory;

public class Test {
    public static void main(String[] args) {
        Fabrica f = new FabricaConcretaScout();

        Soldado s = f.crearSoldado();
        Vehiculo v = f.crearVehiculo();

        s.disparar();
        v.moverse();
    }
}
