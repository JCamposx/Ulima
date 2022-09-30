package ejemplo;

public class ComandoComprar implements ComandoIF {
    private Receptor r;

    public ComandoComprar(Receptor r) {
        this.r = r;
    }

    @Override
    public void ejecutar() {
        r.comprar();
    }
}
