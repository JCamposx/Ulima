package prototype;

public class STT extends Soldado {
    public STT() {
    }

    public STT(STT stt) {
        this.serialNumber = stt.serialNumber;
        this.lugar = stt.lugar;
    }

    @Override
    public void disparar() {
        System.out.println("STT disparando ...");;
    }

    @Override
    public Soldado clonar() {
        return new STT(this);
    }

    @Override
    public String toString() {
        return "STT{" +
                "serialNumber='" + serialNumber + '\'' +
                ", lugar='" + lugar + '\'' +
                '}';
    }
}
