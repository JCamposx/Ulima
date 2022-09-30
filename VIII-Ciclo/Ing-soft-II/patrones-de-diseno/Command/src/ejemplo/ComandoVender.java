package ejemplo;

public class ComandoVender implements ComandoIF {
    private Receptor r;

    public ComandoVender(Receptor r) {
        this.r = r;
    }

    @Override
    public void ejecutar() {
        r.vender();
    }
}
