/**
 * Represents a 2-D coordinate pair.  This class's objects are immutable.
 *
 * @author James Heliotis
 */
public class Coordinate {

    /** the x coordinate */
    private final int x;

    /** the y coordinate */
    private final int y;

    /**
     * Create a new coordinate object.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate.
     *
     * @return this coordinate's x value
     */
    public int getX() { return this.x; }

    /**`
     * Get the y coordinate.
     *
     * @return this coordinate's y value
     */
    public int getY() { return this.y; }

    /**
     * Upper left corner convenience constant
     */
    public static final Coordinate ORIGIN = new Coordinate( 0, 0 );

    /**
     * Returns a string in the format "(x, y)".
     *
     * @return string representation of coordinate
     */
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    /**
     * Simple hash code
     * @return x plus y's binary representation shifted left 16 bits
     */
    @Override
    public int hashCode() {
        return this.x + 0x10000 * this.y;
    }

    /**
     * Classic equality test
     * @param other the object to which this one is to be compared
     * @return true iff the other object is another Coordinate with same
     *         x and y values
     */
    @Override
    public boolean equals( Object other ) {
        if ( this == other ) return true;
        try {
            Coordinate otherC = (Coordinate)other;
            return this.x == otherC.x && this.y == otherC.y;
        }
        catch( ClassCastException cce ) {
            return false;
        }
    }
}
