package factory.method;

public class FabricaConcretaSCT extends Fabrica {
    @Override
    public Soldado crearSoldado() {
        return new SCT();
    }
}
