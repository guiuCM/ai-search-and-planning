package Classes;


import java.util.ArrayList;
import java.util.Random;

public class Gasolineras extends ArrayList<Gasolinera> {
    private static final long serialVersionUID = 1L;
    private Random myRandom;
    private static final double[] DPet = new double[]{0.05, 0.65, (double)0.25F, 0.05};
    private static final double[] DDias = new double[]{0.6, 0.2, 0.15, 0.05};

    public Gasolineras(int var1, int var2) {
        this.myRandom = new Random((long)var2);

        for(int var4 = 0; var4 < var1; ++var4) {
            Gasolinera var3 = new Gasolinera(this.myRandom.nextInt(100), this.myRandom.nextInt(100), this.generaPeticiones());
            this.add(var3);
        }

    }

    private ArrayList generaPeticiones() {
        ArrayList var1 = new ArrayList();
        double var2 = this.myRandom.nextDouble();
        byte var4;
        if (var2 < DPet[0]) {
            var4 = 0;
        } else if (var2 < DPet[0] + DPet[1]) {
            var4 = 1;
        } else {
            var4 = 2;
        }

        for(int var6 = 0; var6 < var4; ++var6) {
            var2 = this.myRandom.nextDouble();
            byte var5;
            if (var2 < DDias[0]) {
                var5 = 0;
            } else if (var2 < DDias[0] + DDias[1]) {
                var5 = 1;
            } else if (var2 < DDias[0] + DDias[1] + DDias[2]) {
                var5 = 2;
            } else {
                var5 = 3;
            }

            var1.add(new Integer(var5));
        }

        return var1;
    }
}
