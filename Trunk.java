import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Trunk is a place to store our Suitcases. It has one major purpose: to determine the ways each suitcase can
 * fit inside of it.
 */
public class Trunk implements Configuration
{
    /**
     * The length of the Trunk.
     */
    private int length;

    /**
     * The width of the Trunk.
     */
    private int width;

    /**
     * The List of suitcases that are to be added to the Trunk.
     */
    private List<Suitcase> toAdd;

    /**
     * The "state" of the Trunk. A.K.A. a 2D char array which has each suitcase in its position.
     */
    private char [][] trunkState;

    /**
     * Constructs a new Trunk based on the length and width given as well as the list of Suitcase objects that we want
     * to put in.
     *
     * @param length The length of the Trunk.
     * @param width The width of the Trunk.
     * @param toAdd The List of Suitcases to add to the Trunk.
     */
    Trunk(int length, int width, List<Suitcase> toAdd)
    {
        // length = x / column
        this.length = length;

        // width = y / row
        this.width = width;

        // sorts the list (only really need this once)
        toAdd.sort((s1, s2) -> (s2.getLength() * s2.getWidth()) -  (s1.getLength() * s1.getWidth()));

        // the ones we want to add are in toAdd
        this.toAdd = new ArrayList<>( toAdd );

        // trunkState char array of y by x
        this.trunkState = new char[width][length];
    }

    private Trunk(Trunk toCopy)
    {

        // performs the constructor other constructor using the given trunk
        this( toCopy.getLength(), toCopy.getWidth(), new ArrayList<>( toCopy.getNotYetAdded()) );

        // sets our new state (it will be changed later)
        for(int i = 0; i < this.width; i++)
            System.arraycopy(toCopy.trunkState[i], 0, this.trunkState[i],0, toCopy.trunkState[i].length);
    }

    /**
     * A method which returns a Collection that contains all of the successors to the current Configuration using the
     * next suitcase in toAdd.
     *
     * @return A Collection of Trunk Configuration that are successors to the current Configuration.
     */
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

    /**
     * Generates a new copy of the Trunk and adds in the Suitcase starting at the specified row and column.
     *
     * @param row The row which the Suitcase should begin being placed at in trunkState.
     * @param col The column which the Suitcase should begin being placed at in trunkState.
     * @param suitcase The Suitcase we want to put into the trunkState.
     *
     * @return A copy of the current Trunk with a new Suitcase put into it at the specified location.
     */
    private Trunk generateCopyTrunk(int row, int col, Suitcase suitcase)
    {
        // runs the copy constructor giving us a new Trunk with the same state as current version
        Trunk copyTrunk = new Trunk(this);
        // fills the spaces with the new suitcase
        copyTrunk.fillSpaces(row, col, suitcase);
        // returns our new Trunk
        return copyTrunk;
    }

    /**
     * Fills the spaces that a suitcase (which) takes up in a Trunk starting at a certain row and column.
     *
     * This is what keeps the "state" updated to the most current status. This is called when we are getting successors.
     *
     * @param startRow The row in the trunkState to begin filling at.
     * @param startCol The column in the trunkState to begin filling at.
     * @param which The suitcase we are attempting to "place" in trunkState.
     */
    private void fillSpaces(int startRow, int startCol, Suitcase which)
    {
        // fills the space of the newly copied Trunk with the nextCase
        for(int row = startRow; row < startRow + which.getWidth(); ++row)
        {
            for(int col = startCol; col < startCol + which.getLength(); ++col)
            {
                // sets the char at [row][col] to the Suitcase's name.
                this.trunkState[row][col] = which.getName();
            }
        }
    }

    /**
     * Returns a set of the cases which are not yet added.
     *
     * @return The Suitcases which haven't been added yet.
     */
    private Set<Suitcase> getNotYetAdded()
    {
        // returns a new HashSet of the suitcases not added yet
        return new HashSet<>(toAdd);
    }

    /**
     * Getter for the length of the Trunk.
     *
     * @return The length of the current Trunk.
     */
    private int getLength()
    {
        return this.length;
    }

    /**
     * Getter for the width of the Trunk.
     *
     * @return The width of the current Trunk.
     */
    private int getWidth()
    {
        return this.width;
    }

    /**
     * This always returns true since we are only generating valid configurations.
     *
     * @return True always because the getSuccessors() method only generates valid configurations.
     */
    @Override
    public boolean isValid()
    {
        return true;
    }

    /**
     * Returns if we are at goal or not.
     *
     * @return True if we are at goal, false otherwise.
     */
    @Override
    public boolean isGoal()
    {
        return toAdd.size() == 0;
    }

    /**
     * Displays the current state of the Trunk to standard output.
     */
    @Override
    public void display()
    {
        // goes through each row of trunkState
        for (char[] row : this.trunkState)
        {
            // goes through each char in row
            for (char nextChar : row)
            {
                // prints out either a '-' if there is no suitcase there, or the char itself
                System.out.print(((nextChar == '\u0000') ? '-' : nextChar) + " ");
            }
            // prints a new line
            System.out.println();
        }

        // goes through each suitcase in toAdd
        for(Suitcase suitcase : toAdd)
        {
            // displays each suitcase
            suitcase.display();
            // prints a new line
            System.out.println();
        }
    }
}
