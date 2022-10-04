package facade;

public class Test {
    public static void main(String[] args) {
        Autenticacion autenticacion = new Autenticacion();
        Cajero cajero = new Cajero(9000);
        Cuenta cuenta = new Cuenta();

        Facade f = new Facade(autenticacion, cajero, cuenta);
        f.retirarDineroCajero();
    }
}
