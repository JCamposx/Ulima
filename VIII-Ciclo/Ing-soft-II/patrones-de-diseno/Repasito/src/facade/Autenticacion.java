package facade;

public class Autenticacion {
    public void leerTarjeta() {
        System.out.println("Leyendo tarjeta ...");
    }

    public void introducirClave() {
        System.out.println("Clave introducida");
    }

    public void comprobarClave() {
        System.out.println("Clave comprobada - Estado: correcta");
    }

    public void obtenerCuenta() {
        System.out.println("Retornando cuenta ...");
    }
}
