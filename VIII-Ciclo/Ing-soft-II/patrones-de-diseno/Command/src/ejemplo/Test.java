package ejemplo;

public class Test {
    public static void main(String[] args) {
        Receptor r1 = new Receptor(100, "USD");
        Receptor r2 = new Receptor(200, "EUR");
        Receptor r3 = new Receptor(300, "PEN");
        Receptor r4 = new Receptor(400, "COP");
        Receptor r5 = new Receptor(500, "YEN");

        ComandoIF c1 = new ComandoComprar(r1);
        ComandoIF c2 = new ComandoVender(r2);
        ComandoIF c3 = new ComandoComprar(r3);
        ComandoIF c4 = new ComandoVender(r4);
        ComandoIF c5 = new ComandoComprar(r5);

        Invocador inv = new Invocador();
        inv.recibirComando(c1);
        inv.recibirComando(c4);
        inv.recibirComando(c2);
        inv.recibirComando(c5);
        inv.recibirComando(c3);

        inv.ejecutarEnBloque();
    }
}
