package prototype;

import java.util.ArrayList;
import java.util.List;

public class Transporte {
    private List<Soldado> ls;

    public Transporte() {
        this.ls = new ArrayList<Soldado>();
    }

    public Transporte(List<Soldado> l) {
        this.ls = l;
    }

    /**
     * Simulamos que acedemos a la base de datos
     */
    public void llenarTransporte() {
        for (int i = 0; i < 5; i++) {
            Soldado s = new STT();
            s.serialNumber = "QWE-00" + i;
            s.lugar = "Lugar " + i + "0";

            this.ls.add(s);
        }
    }

    public List<Soldado> getLs() {
        return ls;
    }

    public Transporte clonar() {
        List<Soldado> tmp = new ArrayList<>();

        for (Soldado s : this.ls) {
            tmp.add(s);
        }

        return new Transporte(tmp);
    }
}
