package models;

public abstract class Representative extends Spatial {
    protected int width;
    protected int height;

    // Constructors
    Representative(final int x, final int y, final int width, final int height) {
        super(x, y);

        setDimensions(width, height);
    }

    Representative() {
        super(0, 0);

        setDimensions(0, 0);
    }


    // Setters
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
}
