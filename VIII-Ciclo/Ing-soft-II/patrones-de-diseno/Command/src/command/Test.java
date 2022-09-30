package command;

public class Test {
    public static void main(String[] args) {
        Invocador inv = new Invocador();
        Receptor rec = new Receptor();

        ComandoIF on = new ComandoEncender(rec);
        ComandoIF off = new ComandoApagar(rec);

        inv.setCmd(on);
        inv.ejecutarComando();

        inv.setCmd(off);
        inv.ejecutarComando();
    }
}
