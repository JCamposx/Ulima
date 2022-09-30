public class BDR_Estandar implements BDR {
    private boolean visito_cliente;

    public boolean get_visito_cliente() {
        return visito_cliente;
    }

    @Override
    public void si_visito_cliente() {
        this.visito_cliente = true;
    }

    @Override
    public void no_visito_cliente() {
        this.visito_cliente = false;
    }
}
