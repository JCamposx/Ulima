package factory.method;

public class StormTrooper implements Soldado {
    @Override
    public void disparar() {
        System.out.println("STT: Pium pium pium ...");
    }
}
