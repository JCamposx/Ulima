package ejemplo.cliente;

import ejemplo.implementor.MotorIF;

public class Auto extends Vehiculo {
    public Auto(MotorIF m) {
        super(m);
    }

    @Override
    public void mover() {
        m.arrancar();
    }
}
