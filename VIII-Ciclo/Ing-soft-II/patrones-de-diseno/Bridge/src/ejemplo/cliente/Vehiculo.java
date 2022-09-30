package ejemplo.cliente;

import ejemplo.implementor.MotorIF;

public abstract class Vehiculo {
    protected MotorIF m;

    public Vehiculo(MotorIF m) {
        this.m = m;
    }

    public abstract void mover();
}
