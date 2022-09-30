package ejemplo;

public class Receptor {
    private int cantidad;
    private String moneda;

    public Receptor(int cantidad, String moneda) {
        this.cantidad = cantidad;
        this.moneda = moneda;
    }

    public void comprar() {
        System.out.println("Comprar " + this.cantidad + " " + this.moneda);
    }

    public void vender() {
        System.out.println("Vender " + this.cantidad + " " + this.moneda);
    }
}
