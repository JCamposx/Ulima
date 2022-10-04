package factory.method;

public class FabricaConcretaSTT extends Fabrica {
    @Override
    public Soldado crearSoldado() {
        return new STT();
    }
}
