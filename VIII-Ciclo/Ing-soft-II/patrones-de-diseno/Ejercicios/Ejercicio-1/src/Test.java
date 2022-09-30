public class Test {
    public static void main(String[] args) {
        BDR_Especializado bdr1 = new BDR_Especializado();
        bdr1.si_visito_cliente();
        System.out.println(bdr1.get_visito_cliente());

        BDR_Estandar bdr2 = new BDR_Estandar();
        bdr2.no_visito_cliente();
        System.out.println(bdr2.get_visito_cliente());
    }
}
