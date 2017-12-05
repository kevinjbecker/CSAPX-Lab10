import java.util.*;

public class Trunk implements Configuration
{
    private int length;

    private int width;

    private List<Suitcase> toAdd;

    private char [][] trunkState;

    /**
     * Constructs a new Trunk based on the length and width given as well as the list of Suitcase objects that we want
     * to put in.
     *
     * @param length The length of the Trunk.
     * @param width The width of the Trunk.
     * @param toAdd The List of Suitcases to add to the Trunk.
     */
    public Trunk(int length, int width, List<Suitcase> toAdd)
    {
        // length = x / column
        this.length = length;

        // width = y / row
        this.width = width;

        // the ones we want to add are in toAdd
        this.toAdd = new ArrayList<>(  new HashSet<>(toAdd) );

        // trunkState char array of y by x
        this.trunkState = new char[width][length];
    }

    public Trunk(Trunk toCopy)
    {
        // runs the constructor above using the given trunk
        this( toCopy.getLength(), toCopy.getWidth(), new ArrayList<>( toCopy.getNotYetAdded()) );

        // sets our new state (it will be changed later)
        for(int i = 0; i < this.width; i++)
            System.arraycopy(toCopy.trunkState[i], 0, this.trunkState[i],0, toCopy.trunkState[i].length);
    }

    @Override
    public Collection<Configuration> getSuccessors()
    {
        // gathers the next case to check (removes it because once we've attempt to add it, we can consider it placed)
        Suitcase nextCase = toAdd.remove(0);

        // turns it if we need to test a turned version
        Suitcase nextCaseTurned = (!nextCase.isSquare()) ? nextCase.turned() : null;

        // a iterable of Configurations
        List<Configuration> successors = new ArrayList<>();

        // loops through every possible position for it to be in and adds the valid ones
        // THE ROW IS WIDTH
        for(int row = 0; row < trunkState.length; ++row)
        {
            // THE COL IS LENGTH
            for(int col = 0; col < trunkState[row].length; ++col)
            {
                // tests if the next case (regular) can fit and adds a new copy of trunk to successors if it can
                if(open(row, col, nextCase))
                    successors.add(generateCopyTrunk(row, col, nextCase));

                // tests if the turned case a) exists and b) can fit and adds a new copy of trunk to successors if it can
                if(nextCaseTurned != null && open(row, col, nextCaseTurned))
                    successors.add(generateCopyTrunk(row, col, nextCaseTurned));
            }
        }
        // returns all of our successors
        return successors;
    }

    /**
     * Determines if a case can be fit in starting at startRow and startCol.
     *
     * @param startRow The starting row to check for fit.
     * @param startCol The starting col to check for fit.
     * @param suitcase The suitcase we are checking for fit.
     *
     * @return True if the case can fit; false otherwise.
     */
    private boolean open(int startRow, int startCol, Suitcase suitcase)
    {
        // loops from startRow for width many times
        for(int row = startRow; row < startRow + suitcase.getWidth(); ++row)
        {
            // loops from startCol for length many times
            for(int col = startCol; col < startCol + suitcase.getLength(); ++col)
            {
                /*
                if we aren't over the length of the rows, or the width of the rows, and it is a blank char
                we're okay
                however if anything isn't true, we can't consider space open
                 */
                if (row >= trunkState.length || col >= trunkState[row].length || trunkState[row][col] != '\u0000')
                    return false;
            }
        }
        // if we get here the space is open
        return true;
    }

    private Trunk generateCopyTrunk(int row, int col, Suitcase suitcase)
    {
        // runs the copy constructor giving us a new Trunk with the same state as current version
        Trunk copyTrunk = new Trunk(this);
        // fills the spaces with the new suitcase
        copyTrunk.fillSpaces(row, col, suitcase);
        // returns our new Trunk
        return copyTrunk;
    }

    private void fillSpaces(int startRow, int startCol, Suitcase which)
    {
        // fills the space of the newly copied Trunk with the nextCase
        for(int row = startRow; row < startRow + which.getWidth(); ++row)
        {
            for(int col = startCol; col < startCol + which.getLength(); ++col)
            {
                this.trunkState[row][col] = which.getName();
            }
        }
    }

    /**
     * Returns a set of the cases which are not yet added.
     *
     * @return The Suitcases which haven't been added yet.
     */
    public Set<Suitcase> getNotYetAdded()
    {
        return new HashSet<>(toAdd);
    }

    /**
     * Getter for the length of the Trunk.
     *
     * @return The length of the Trunk.
     */
    public int getLength()
    {
        return this.length;
    }

    /**
     * Getter for the width of the Trunk.
     *
     * @return The width of the Trunk.
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * This always returns true since we are only generating valid configurations.
     *
     * @return True always.
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Returns if we are at goal or not.
     *
     * @return True if we are at goal, false otherwise.
     */
    @Override
    public boolean isGoal() {
        return toAdd.size() == 0;
    }

    /**
     * Displays the current state of the Trunk to standard output.
     */
    @Override
    public void display() {
        for(int row = 0; row < trunkState.length; ++row)
        {
            for(int col = 0; col < trunkState[row].length; ++col)
            {
                System.out.print(((this.trunkState[row][col] == '\u0000') ? '-' : this.trunkState[row][col]) + " ");
            }
            System.out.println();
        }

        for(Suitcase suitcase : toAdd)
        {
            suitcase.display();
            System.out.println();
        }
    }
}
