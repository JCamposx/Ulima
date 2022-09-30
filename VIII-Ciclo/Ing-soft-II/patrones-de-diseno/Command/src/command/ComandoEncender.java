package command;

public class ComandoEncender implements ComandoIF {
    private Receptor r;

    public ComandoEncender(Receptor r) {
        this.r = r;
    }

    @Override
    public void ejecutar() {
        r.encender();
    }
}
