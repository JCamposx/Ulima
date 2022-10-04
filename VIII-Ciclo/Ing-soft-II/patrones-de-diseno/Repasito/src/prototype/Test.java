package prototype;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        // SOLDADOS

        Soldado s1 = new STT();
        s1.serialNumber = "XYZ-999";
        s1.lugar = "Endor";
        System.out.println("Soldado 1: " + s1);

        // Generar clones del soldado ya creado

        Soldado s2 = s1.clonar();
        System.out.println("Soldado 2: " + s2);

        Soldado s3 = s1.clonar();
        System.out.println("Soldado 3: " + s3);

        ////////////////////////////////////////////////////////////////////////

        // TRANPORTES

        Transporte t1 = new Transporte();
        t1.llenarTransporte();

        List<Soldado> l1 = t1.getLs();

        System.out.println("\nTransporte 1:");
        for (Soldado s : l1) {
            System.out.println(s);
        }

        // Generar clon del transporte ya creado con todos sus soldados
        Transporte t2 = t1.clonar();
        List<Soldado> l2 = t2.getLs();

        System.out.println("\nTransporte 2:");
        for (Soldado s : l2) {
            System.out.println(s);
        }
    }
}
