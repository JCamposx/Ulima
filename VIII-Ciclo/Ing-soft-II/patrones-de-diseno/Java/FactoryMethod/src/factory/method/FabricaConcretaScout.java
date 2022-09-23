package factory.method;

public class FabricaConcretaScout extends Fabrica_2 {
    Soldado s = null;

    @Override
    public Soldado crearSoldado() {
        s = new ScoutTrooper();

        return s;
    }
}
