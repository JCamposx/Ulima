package prototype;

import java.util.ArrayList;
import java.util.List;

public class Transporte {
    private List<SoldadoImperial> ls;

    public Transporte() {
        ls = new ArrayList<SoldadoImperial>();
    }

    public Transporte(List<SoldadoImperial> li) {
        this.ls = li;
    }

    /**
     * Simulamos que acedemos a la base de datos
     */
    public void abordarTransporte() {
        for (int i = 1; i <= 5; i++) {
            SoldadoImperial si = new SoldadoAsalto();
            si.serialNumber = "XYZ-00" + i;
            si.lugar = "Endor";

            ls.add(si);
        }
    }

    public List<SoldadoImperial> getLs() {
        return ls;
    }

    public Transporte clonar() {
        List<SoldadoImperial> tmp = new ArrayList<>();

        for (SoldadoImperial s : ls) {
            tmp.add(s);
        }

        return new Transporte(tmp);
    }
}
