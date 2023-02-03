public class deneme {

    public static boolean icerikKontrol ( String yazi1, String yazi2) {

        boolean dogruMu = false;
        if ( yazi1.equals(yazi2) ) {
            dogruMu = true;
        }
        return dogruMu;

    }

    public static void main(String[] args) {

        String str = "emre";
        String str2 = "emre";
        boolean bln = icerikKontrol(str, str2);
        System.out.println(bln);
    }

}
