package facade;

public class Test {
    public static void main(String[] args) {
        EC1 ec1 = new EC1(15, 16, 17);

        EC2 ec2 = new EC2(11, 10, 9);

        // Sin Facade
        int promEC1 = ec1.promedioEC1();
        int promEC2 = ec2.promedioEC2();
        int promFinal = (promEC1 + promEC2) / 2;
        System.out.println("El promedio final es " + promFinal);

        // Con Facade
        Facade f = new Facade(ec1, ec2);
        System.out.println("El promedio final es " + f.promedio());
    }
}
