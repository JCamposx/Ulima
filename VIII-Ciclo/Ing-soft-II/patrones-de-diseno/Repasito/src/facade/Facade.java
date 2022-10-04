package facade;

public class Facade {
    private Autenticacion autenticacion;
    private Cajero cajero;
    private Cuenta cuenta;

    public Facade() {
    }

    public Facade(Autenticacion autenticacion, Cajero cajero, Cuenta cuenta) {
        this.autenticacion = autenticacion;
        this.cajero = cajero;
        this.cuenta = cuenta;
    }

    public void retirarDineroCajero() {
        this.autenticacion.leerTarjeta();
        this.autenticacion.introducirClave();
        this.autenticacion.comprobarClave();
        this.autenticacion.obtenerCuenta();
        this.cajero.introducirCantidad();
        this.cajero.verificarSaldo();
        this.cajero.expedirDinero();
        this.cajero.imprimirTicket();
    }
}
