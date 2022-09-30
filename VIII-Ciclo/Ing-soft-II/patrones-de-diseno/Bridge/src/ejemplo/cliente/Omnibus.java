package ejemplo.cliente;

import ejemplo.implementor.MotorIF;

public class Omnibus extends Vehiculo {
    public Omnibus(MotorIF m) {
        super(m);
    }

    @Override
    public void mover() {
        m.arrancar();
    }
}
