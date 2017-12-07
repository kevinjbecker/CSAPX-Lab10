import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author James Heliotis
 */
public class Tests {

    public static final int MIN_LEN = 8;
    public static final int MIN_WID = 5;
    public static final String RANDOM_ARG_OPTION = "--random";
    private static String LS = System.lineSeparator();
    private static int MSEC_PER_SEC = 1000;

    /**
     * Choose which kind of test to run based on command line arguments.
     * @param args <br>
     *             <i><small>none</small></i> &rArr; Run 3 fixed tests.<br>
     *             <code>--random n</code> &rArr; Generate a random test of size
     *             at most <code>n</code>&times;<code>n</code>.<br>
     *             <i>fileName</i> &rArr; Run the problem instance stored in
     *             <i>fileName</i>.
     */
    public static void main( String[] args ) {
        Backtracker solver = new Backtracker( false );
        try {
            if ( args.length == 2 && args[ 0 ].equals( RANDOM_ARG_OPTION ) ) {
                final int dim = Integer.parseInt( args[ 1 ] );
                if ( dim >= MIN_LEN && dim >= MIN_WID ) {
                    Tests.random( solver, dim );
                }
                else {
                    throw new Exception( "Dim. for random test too small." );
                }
            }
            else if ( args.length == 1 ) {
                Tests.fromFile( solver, args[ 0 ] );
            }
            else if ( args.length > 0 ) {
                throw new Exception( "Illegal command line arguments." );
            }
            else {
                System.out.println( "Fixed Tests" + LS );
                fixedTests( solver );
            }
        }
        catch( Exception e ) {
            System.err.println( "Problem: " + e );
            e.printStackTrace();
        }
    }

    /**
     * Before running a test, print out details of the problem instance.
     * This includes the trunk size and a graphical list of the suitcases
     * to be put in the trunk.
     * @param cases a collection of suitcases
     * @param length the trunk's length
     * @param width the trunk's width
     * @rit.pre Problem instance must have already been formulated and
     *          stored in the parameters described here.
     */
    public static void testPreamble(
            Iterable< Suitcase > cases, int length, int width ) {
        System.out.println( "TEST COMMENCING" + LS );
        System.out.println( "Suitcases:" + LS );
        for ( Suitcase s : cases ) {
            s.display();
            System.out.println();
        }
        System.out.println(
                "...into a " +  length +  " x " +  width +  " trunk" +  LS
        );
    }

    /**
     * Run a single test of the Trunks backtracking solver
     * @param solver an initialized Backtracker instance
     * @param starting the initial, empty Trunk configuration
     */
    private static void oneTest( Backtracker solver, Trunk starting ) {
        System.out.println( "SOLVING..." + LS );
        Instant begin = Instant.now();
        Optional< Configuration > maybeSol = solver.solve( starting );
        long execTime = Duration.between( begin, Instant.now() ).toMillis();
        if ( maybeSol.isPresent() ) {
            System.out.println( "YES!" );
            maybeSol.get().display();
        }
        else {
            System.out.println( "NO :-(" );
        }

        System.out.println(
                "Execution time " +
                ( execTime / MSEC_PER_SEC ) +
                '.' + ( execTime % MSEC_PER_SEC ) +
                " seconds"
        );

        System.out.println();
    }

    /**
     * Run a Trunks problem based on data from a file. The first line
     * in the file contains the length, then a space, then the width,
     * of the trunk. Each successive line contains the name (one character),
     * length, and width of a suitcase.
     * @param solver an initialized Backtracker instance
     * @param fileName the name of the file containing the problem instance
     *                 description
     * @throws FileNotFoundException if the file cannot be opened for reading
     */
    private static void fromFile( Backtracker solver, String fileName )
            throws FileNotFoundException {
        int trunkL;
        int trunkW;
        List< Suitcase > cases = new LinkedList<>();
        try ( Scanner file = new Scanner( new File( fileName ) ) ) {
            String[] dims = file.nextLine().split( "\\s+" );
            trunkL = Integer.parseInt( dims[ 0 ] );
            trunkW = Integer.parseInt( dims[ 1 ] );
            while ( file.hasNextLine() ) {
                String[] scParams = file.nextLine().split( "\\s+" );
                cases.add( new Suitcase(
                        scParams[ 0 ].charAt( 0 ),
                        Integer.parseInt( scParams[ 1 ] ),
                        Integer.parseInt( scParams[ 2 ] )
                    )
                );
            }
        }
        testPreamble( cases, trunkL, trunkW );
        oneTest( solver, new Trunk( trunkL, trunkW, cases ) );
    }

    /**
     * Run the three &quot;fixed&quot; tests stored in this class.
     * @param solver an initialized Backtracker instance
     */
    private static void fixedTests( Backtracker solver ) {
	// The line below generates an unchecked warning that can't be fixed.
        for ( Supplier< Trunk > test: new Supplier[]{
                Tests::test1, Tests::test2, Tests::test3 } ) {

            Trunk starting = test.get();
            oneTest( solver, starting );
        }
    }

    /**
     * A test with 5 suitcases that, if arranged properly,
     * will exactly fit in a 3&times;3 trunk. No rotations are needed.
     * @return the correctly initialized Trunk configuration
     */
    private static Trunk test1() {
        List< Suitcase > cases = Arrays.asList(
                new Suitcase( 'A', 1, 3 ),
                new Suitcase( 'B', 2, 1 ),
                new Suitcase( 'C', 1, 2 ),
                new Suitcase( 'D', 1, 1 ),
                new Suitcase( 'E', 1, 1 )
        );
        testPreamble( cases, 3, 3 );
        return new Trunk( 3, 3, cases );
    }

    /**
     * A test with 2 suitcases that, if arranged and rotated properly,
     * will exactly fit in a 4&times;2 trunk.
     * @return the correctly initialized Trunk configuration
     */
    private static Trunk test2() {
        List< Suitcase > cases = Arrays.asList(
                new Suitcase( 'A', 1, 4 ),
                new Suitcase( 'B', 1, 4 )
        );
        testPreamble( cases, 4, 2 );
        return new Trunk( 4, 2, cases );
    }

    /**
     * A test with 6 suitcases that cannot
     * fit into an 8&times;4 trunk no matter how the are arranged and rotated.
     * @return the correctly initialized Trunk configuration
     */
    private static Trunk test3() {
        List< Suitcase > cases = Arrays.asList(
                new Suitcase( 'A', 1, 1 ),
                new Suitcase( 'B', 2, 2 ),
                new Suitcase( 'C', 1, 4 ),
                new Suitcase( 'D', 2, 1 ),
                new Suitcase( 'E', MIN_LEN, 4 ),
                new Suitcase( 'F', 1, 1 )
        );
        testPreamble( cases, 8, 4 );
        return new Trunk( 8, 4, cases );
    }

    /**
     * Generate and run a random instance of the Trunk problem.
     * The method is designed to always create a failing instance
     * because the combined footprints of the suitcases are more
     * than the area in the trunk/
     * @param solver an initialized Backtracker instance
     * @param dim the maximum length and width
     *            of the Trunk instance to be created
     */
    private static void random( Backtracker solver, int dim ) {
        Random rnd = new Random();
        // Compute dimensions no less than 2. OW Messes up scL/scW computations.
        int trunkL = rnd.nextInt( dim - 2 ) + 2;
        int trunkW = rnd.nextInt( dim - 2 ) + 2;
        int trunkArea = trunkL * trunkW;
        int scArea = 0;
        List< Suitcase > cases = new LinkedList<>();
        char name = 'A';
        while ( scArea <= trunkArea && name <= 'Z' ) {
            int scL = rnd.nextInt( trunkL - 1 ) + 1;
            int scW = rnd.nextInt( trunkW - 1 ) + 1;
            int area = scL * scW;
            cases.add( new Suitcase( name, scL, scW ) );
            scArea += area;
            name += 1;
        }
        System.out.println( "Randomly chosen suitcases total area: " + scArea );
        testPreamble( cases, trunkL, trunkW );
        oneTest( solver, new Trunk( trunkL, trunkW, cases ) );
    }
}
