package abstracto.factory;

public class Auto implements Vehiculo {
    @Override
    public void moverse() {
        System.out.println("Auto moviendose ...");
    }
}
