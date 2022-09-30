package ejemplo.implementor;

public class MotorDieselImpl implements MotorIF {
    @Override
    public void arrancar() {
        System.out.println("Encenciendo motor diesel ...");
    }
}
