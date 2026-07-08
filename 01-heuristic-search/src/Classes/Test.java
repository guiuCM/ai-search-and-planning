package Classes;


public class Test {
    public static void main(String[] var0) {
        Gasolineras var1 = new Gasolineras(100, 1234);
        CentrosDistribucion var2 = new CentrosDistribucion(10, 1, 1234);
        double[] var3 = new double[]{(double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F};

        for(int var4 = 0; var4 < var1.size(); ++var4) {
            System.out.println("Gasolinera " + var4 + ": " + ((Gasolinera)var1.get(var4)).getCoordX() + " " + ((Gasolinera)var1.get(var4)).getCoordY());
            int var5 = 0;
            if (((Gasolinera)var1.get(var4)).getPeticiones().isEmpty()) {
                System.out.println("-> Sin Peticiones <-");
            }

            for(Integer var7 : ((Gasolinera)var1.get(var4)).getPeticiones()) {
                System.out.println("Peticion " + var5 + ": Dias " + var7);
                ++var5;
                ++var3[var7];
            }
        }

        System.out.println();

        for(int var8 = 0; var8 < 4; ++var8) {
            System.out.println(var3[var8] + " de " + var8 + " dias");
        }

        System.out.println();

        for(int var9 = 0; var9 < var2.size(); ++var9) {
            System.out.println("Centro " + var9 + ": " + ((Distribucion)var2.get(var9)).getCoordX() + " " + ((Distribucion)var2.get(var9)).getCoordY());
        }

    }
}
