package ejemplo.contexto;

import ejemplo.estrategia.EstrategiaIF;

public class Contexto {
    EstrategiaIF e;

    public void setE(EstrategiaIF e) {
        this.e = e;
    }

    public int procesa(int a, int b) {
        return e.calcula(a, b);
    }
}
