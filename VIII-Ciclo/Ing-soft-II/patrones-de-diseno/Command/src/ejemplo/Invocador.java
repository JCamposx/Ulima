package ejemplo;

import java.util.ArrayList;
import java.util.List;

public class Invocador {
    List<ejemplo.ComandoIF> cola = new ArrayList<>();

    public void recibirComando(ComandoIF c) {
        cola.add(c);
    }

    public void ejecutarEnBloque() {
        for (ComandoIF c : cola) {
            c.ejecutar();
        }
    }
}
