package builder;

public class Test {
    public static void main(String[] args) {
        Soldado s1 = new Soldado.Builder("XYZ-999").arma("STG-44").build();
        System.out.println(s1);

        Soldado s2 = new Soldado.Builder("XYZ-005")
                .botas("AZUL")
                .casco("BLANCO")
                .build();
        System.out.println(s2);

        Soldado s3 = new Soldado.Builder("ASD-123").botas("verde").build();
        System.out.println(s3);
    }
}
