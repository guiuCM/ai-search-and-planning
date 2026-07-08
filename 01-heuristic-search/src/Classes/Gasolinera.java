package Classes;

import java.util.ArrayList;

public class Gasolinera {
    private ArrayList<Integer> Peticiones;
    private int CoordX;
    private int CoordY;

    public Gasolinera(int var1, int var2, ArrayList<Integer> var3) {
        this.Peticiones = var3;
        this.CoordX = var1;
        this.CoordY = var2;
    }

    public int getCoordX() {
        return this.CoordX;
    }

    public void setCoordX(int var1) {
        this.CoordX = var1;
    }

    public int getCoordY() {
        return this.CoordY;
    }

    public void setCoordY(int var1) {
        this.CoordY = var1;
    }

    public ArrayList<Integer> getPeticiones() {
        return this.Peticiones;
    }

    public void setPeticiones(ArrayList<Integer> var1) {
        this.Peticiones = var1;
    }
}
