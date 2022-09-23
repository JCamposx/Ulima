package prototype;

public class SoldadoAsalto extends SoldadoImperial {
    public SoldadoAsalto() {

    }

    public SoldadoAsalto(SoldadoAsalto s) {
        this.serialNumber = s.serialNumber;
        this.lugar = s.lugar;
    }

    @Override
    public void disparar() {
        System.out.println("Pium pium ...");
    }

    @Override
    public SoldadoImperial clonar() {
        return new SoldadoAsalto(this);
    }

    @Override
    public String toString() {
        return "SoldadoAsalto{" + serialNumber + " " + lugar + "}";
    }
}
