package factory.method;

public class FabricaConcretaStorm extends Fabrica_2 {
    Soldado s = null;

    @Override
    public Soldado crearSoldado() {
        s = new StormTrooper();

        return s;
    }
}
