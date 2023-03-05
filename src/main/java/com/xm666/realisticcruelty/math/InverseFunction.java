package com.xm666.realisticcruelty.math;

public class InverseFunction {
    public double offsetX, width, offsetY;

    public InverseFunction(double x, double y, boolean reversed) {
        if (reversed) {
            x = 1 - x;
        }
        this.offsetX = y / (1 - x);
        this.width = (1 - this.offsetX) / x;
        this.offsetY = -1 / (this.offsetX + this.width);
        double shrink = 1 / this.offsetX + this.offsetY;
        this.shrink(shrink);
        if (reversed) {
            offsetX += width;
            width = -width;
        }
    }

    public void shrink(double shrink) {
        this.offsetX *= shrink;
        this.width *= shrink;
        this.offsetY /= shrink;
    }

    public double f(double x) {
        return 1 / (this.offsetX + this.width * x) + this.offsetY;
    }
}
