package command;

public class Invocador {
    // Simplemente le va a decir al Receptor que ejecute una determinada cosa

    private ComandoIF cmd;

    public void setCmd(ComandoIF cmd) {
        this.cmd = cmd;
    }

    public void ejecutarComando() {
        cmd.ejecutar();
    }
}
