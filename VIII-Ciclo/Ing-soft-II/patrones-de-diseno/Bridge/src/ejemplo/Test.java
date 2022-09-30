package ejemplo;

import ejemplo.cliente.*;
import ejemplo.implementor.*;

public class Test {
    public static void main(String[] args) {
        MotorIF m1 = new MotorDieselImpl();
        MotorIF m2 = new MotorGasolinaImpl();

        Vehiculo v1 = new Omnibus(m1);
        v1.mover();

        Vehiculo v2 = new Omnibus(m2);
        v2.mover();
    }
}
