package builder;

public class Test {
    public static void main(String[] args) {
        Soldado s1 = new Soldado.Builder("XYZ-909").armadura("BLANCO").build();

        System.out.println(s1);

        Soldado s2 = new Soldado.Builder("XYZ-005")
                .arma("LASER")
                .botas("AZUL")
                .casco("BLANCO")
                .build();
        System.out.println(s2);
    }
}
