package abstracto.factory;

public class Test {
    public static void main(String[] args) {
        Fabrica f1 = new FabricaConcretaSTT();
        Fabrica f2 = new FabricaConcretaSCT();

        Soldado s1 = f1.crearSoldado();
        Vehiculo v1 = f1.crearVehiculo();
        s1.disparar();
        v1.moverse();

        Soldado s2 = f2.crearSoldado();
        Vehiculo v2 = f2.crearVehiculo();
        s2.disparar();
        v2.moverse();
    }
}
