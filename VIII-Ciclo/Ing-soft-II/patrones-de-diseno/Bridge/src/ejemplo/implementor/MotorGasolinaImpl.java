package ejemplo.implementor;

public class MotorGasolinaImpl implements MotorIF {
    @Override
    public void arrancar() {
        System.out.println("Encenciendo motor a gasolina ...");
    }
}
