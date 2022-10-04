package facade;

public class Facade {
    private EC1 ec1;
    private EC2 ec2;

    public Facade() {
    }

    public Facade(EC1 ec1, EC2 ec2) {
        this.ec1 = ec1;
        this.ec2 = ec2;
    }

    public int promedio() {
        return (ec1.promedioEC1() + ec2.promedioEC2()) / 2;
    }
}
