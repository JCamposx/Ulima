package facade;

public class Cajero {
    int cantidad;

    public Cajero(int cantidad) {
        this.cantidad = cantidad;
    }

    public void introducirCantidad() {
        System.out.println("Cantidad introducida: " + this.cantidad);
    }

    public void verificarSaldo() {
        System.out.println("Usted sí tiene saldo disponible");
    }

    public void expedirDinero() {
        System.out.println("Expediendo dinero ...");
    }

    public void imprimirTicket() {
        System.out.println("Imprimiendo ticket ...");
    }
}
