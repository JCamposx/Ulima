package abstracto.factory;

public class Transporte implements Vehiculo {
    @Override
    public void moverse() {
        System.out.println("Transporte moviéndose!");
    }
}
