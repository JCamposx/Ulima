package factory.method;

public class Test {
    public static void main(String[] args) {
        Fabrica f1 = new FabricaConcretaSTT();
        Soldado stt = f1.crearSoldado();
        stt.disparar();

        Fabrica f2 = new FabricaConcretaSCT();
        Soldado sct = f2.crearSoldado();
        sct.disparar();
    }
}
