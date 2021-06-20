package utils;

public class Position {
    public double x;
    public double y;

    public Position(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public static double getRange(final Position a, final Position b) {
        final var horizontal_diff = Math.abs(a.x - b.x);
        final var vertical_diff = Math.abs(a.y - b.y);

        return Math.sqrt(Math.pow(horizontal_diff, 2) + Math.pow(vertical_diff, 2));
    }

    public static boolean isInRange(final Position src, final Position obj, final double range) {
        return getRange(src, obj) <= range;
    }

    public static Position oppositePosition(final Position middle, final Position src) {
        final var horizontal_diff = middle.x - src.x;
        final var vertical_diff = middle.y - src.y;

        return new Position(middle.x + horizontal_diff, middle.y + vertical_diff);
    }

    /**
     * @apiNote returned degree is written in radians
     */
    public static double getAngle(final Position src, final Position target) {
        double angle = Math.toDegrees(Math.atan2(target.y - src.y, target.x - src.x));

        if (angle < 0) angle += 360;

        return angle * Math.PI / 180;
    }
}
