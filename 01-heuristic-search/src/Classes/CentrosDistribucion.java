package Classes;

import java.util.ArrayList;
import java.util.Random;

public class CentrosDistribucion extends ArrayList<Distribucion> {
    private static final long serialVersionUID = 1L;
    private Random myRandom;

    public CentrosDistribucion(int var1, int var2, int var3) {
        this.myRandom = new Random((long)(var3 + 1));

        for(int var5 = 0; var5 < var1; ++var5) {
            Distribucion var4 = new Distribucion(this.myRandom.nextInt(100), this.myRandom.nextInt(100));

            for(int var6 = 0; var6 < var2; ++var6) {
                this.add(var4);
            }
        }

    }
}