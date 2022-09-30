package ejemplo;

import ejemplo.contexto.Contexto;
import ejemplo.estrategia.*;

public class Test {
    public static void main(String[] args) {
        Contexto ctx = new Contexto();

        int a = 9;
        int b = 6;

        ctx.setE(new Suma());
        System.out.println("La suma es " + ctx.procesa(a, b));

        ctx.setE(new Resta());
        System.out.println("La resta es " + ctx.procesa(a, b));

        ctx.setE(new Multiplicacion());
        System.out.println("La multiplicacion es " + ctx.procesa(a, b));

        ctx.setE(new Division());
        System.out.println("La division es " + ctx.procesa(a, b));
    }
}
