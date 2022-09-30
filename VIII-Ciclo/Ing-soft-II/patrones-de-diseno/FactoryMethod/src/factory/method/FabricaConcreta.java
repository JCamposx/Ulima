package factory.method;

public class FabricaConcreta extends Fabrica_1 {
    Soldado s = null;

    public Soldado crearSoldado(String tipo) {
        if (tipo.equals("Storm")) {
            s = new StormTrooper();
        } else if (tipo.equals("Scout")) {
            s = new ScoutTrooper();
        }

        return s;
    }
}
