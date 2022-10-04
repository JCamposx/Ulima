package facade;

public class Cuenta {
    public void comprobarSaldoDisponible() {
        System.out.println("Este es su saldo disponible ...");
    }

    public void bloquearCuenta() {
        System.out.println("Cuenta bloqueada exitosamente");
    }

    public void retirarSaldo() {
        System.out.println("Retirando saldo ...");
    }

    public void actualizarCuenta() {
        System.out.println("Cuenta actualizada");
    }
}
