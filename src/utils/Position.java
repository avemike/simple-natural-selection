package utils;

public class Position {
    public double x;
    public double y;

    public Position(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public static boolean isInRange(final Position src, final Position obj, final double range) {
        double horizontal_diff = Math.abs(src.x - obj.x);
        double vertical_diff = Math.abs(src.y - obj.y);

        return Math.sqrt(Math.pow(horizontal_diff, 2) + Math.pow(vertical_diff, 2)) <= range;
    }
}
