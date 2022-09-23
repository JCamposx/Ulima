package prototype;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        // SOLDADOS

        SoldadoImperial si1 = new SoldadoAsalto();
        si1.serialNumber = "XYZ-001";
        si1.lugar = "Endor";

        System.out.println("Soldado 1 " + si1);

        // Generar clones
        SoldadoImperial si2 = si1.clonar();
        System.out.println("Soldado 2 " + si2);

        // Generar clones
        SoldadoImperial si3 = si1.clonar();
        System.out.println("Soldado 3 " + si3);

        ////////////////////////////////////////////////////////////////////////

        // TRANSPORTES

        Transporte t1 = new Transporte();
        t1.abordarTransporte();

        System.out.println("\nTransporte 1");

        List<SoldadoImperial> ls1 = t1.getLs();

        for (SoldadoImperial s : ls1) {
            System.out.println(s);
        }

        // Clonar todo el transporte incluido lo que lleva adentro
        System.out.println("\nTransporte 2");

        Transporte t2 = t1.clonar();
        List<SoldadoImperial> ls2 = t1.getLs();

        for (SoldadoImperial s : ls2) {
            System.out.println(s);
        }
    }
}
