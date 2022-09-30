package command;

public class ComandoApagar implements ComandoIF {
    private Receptor r;

    public ComandoApagar(Receptor r) {
        this.r = r;
    }

    @Override
    public void ejecutar() {
        r.apagar();
    }
}
