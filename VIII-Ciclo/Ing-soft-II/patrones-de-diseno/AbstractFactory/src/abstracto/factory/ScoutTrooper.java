package abstracto.factory;

public class ScoutTrooper implements Soldado {
    @Override
    public void disparar() {
        System.out.println("SCT: Miau ...");
    }
}
