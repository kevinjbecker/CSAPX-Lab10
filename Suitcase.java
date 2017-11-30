/**
 * A description of a suitcase for the Trunks lab
 * @author James Heliotis
 * @author YOUR   NAME   HERE
 */
public class Suitcase {

    private char name;

    private int length;
    private int width;

    /**
     * Make a new suitcase.
     * @param name the suitcase's <em>one-letter</em> name,
     *             <em>assumed to be unique</em>
     * @param length the horizontal extent of the suitcase
     * @param width the vertical extent of the suitcase
     */
    public Suitcase( char name, int length, int width ) {
        this.name = name;
        this.length = length;
        this.width = width;
    }

    /**
     * Is there no point in rotating this suitcase to make it fit?
     * @return true iff the length and width are the same
     */
    public boolean isSquare() {
        return (length == width);
    }

    /**
     * What would the suitcase be if it were turned 90 degrees?
     * @return a new suitcase with transposed dimensions
     */
    public Suitcase turned() {
        return new Suitcase(this.name, this.width, this.length);
    }

    /**
     * What is the name of this suitcase?
     * @return this suitcase's name, as provided in the constructor call
     */
    public char getName() {
        return this.name;
    }

    /**
     * What is the length of this suitcase?
     * @return this suitcase's length, as provided in the constructor call
     */
    public int getLength() {
        return this.length;
    }

    /**
     * What is the width of this suitcase?
     * @return this suitcase's width, as provided in the constructor call
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * for hash table data structures
     * @return a reasonable hash code that takes into account the entire state
     */
    @Override
    public int hashCode() {
        return Character.hashCode( this.name ) + this.length + this.width;
    }

    /**
     * for all collections of Suitcase objects
     * @param other the object to which to compare this suitcase
     * @return true iff the other object is a suitcase with the same data
     *         (we assume all Suitcase objects have unique names)
     */
    @Override
    public boolean equals( Object other ) {
        if ( this == other ) return true;
        try {
            Suitcase otherSC = (Suitcase)other;
            return this.name == otherSC.name &&
                   this.length == otherSC.length &&
                   this.width == otherSC.width;
        }
        catch( ClassCastException cce ) {
            return false;
        }
    }

    /**
     * Print, on standard output, a rendering of a suitcase
     * in the following format.
     * <pre>
     *     NNN
     *     NNN
     *     NNN
     *     NNN
     * </pre>
     * <p>
     *     The horizontal dimension is the length,
     *     and the vertical dimension is the suitcase's width.
     * </p>
     */
    public void display() {
        for ( int y = 0 ; y < this.width; ++y ) {
            System.out.print( '\t' );
            for ( int x = 0; x < this.length; ++x ) {
                System.out.print( this.name );
            }
            System.out.println();
        }
    }
}
