public class BDR_Distribuidor implements BDR {
    private boolean existe_visita;

    public boolean get_existe_visita() {
        return existe_visita;
    }

    @Override
    public void visita_valida() {
        this.existe_visita = true;
    }

    @Override
    public void visita_invalida() {
        this.existe_visita = false;
    }
}
