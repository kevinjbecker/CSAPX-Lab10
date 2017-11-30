import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trunk implements Configuration {

    private int length;
    private int width;
    private List<Suitcase> cases;

    private char [][] trunkState;

    public Trunk(int length, int width, List<Suitcase> cases)
    {
        this.length = length;
        this.width = width;
        this.cases = cases;

        this.trunkState = new char[length][width];
    }

    @Override
    public Iterable<Configuration> getSuccessors() {

        Suitcase nextCase = cases.get(0);
        Suitcase turnedSuitcase = nextCase.turned();

        List<Configuration> validSuccessors = new ArrayList<>();

        for(int row = 0; row < trunkState.length; ++row)
        {
            for(int col = 0; col < trunkState[row].length; ++col)
            {
                for(int smallRow = row; smallRow < nextCase.getLength(); ++smallRow)
                {

                }


                if(row + nextCase.getLength() < trunkState.length && col + nextCase.getWidth() < trunkState[row].length)
                    {
                        char [][] temp = new char[this.length][this.width];
                        System.arraycopy(trunkState, 0, temp,0, width);
                        for(int fill = row; fill < row + nextCase.getLength(); ++fill)
                        {
                            Arrays.fill(temp[row], col, col + nextCase.getWidth(), nextCase.getName());
                        }
                        // add to validSuccessors
                    }
                }
            }
        return validSuccessors;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isGoal() {
        return false;
    }

    @Override
    public void display() {
        System.out.println(this.length + " " + this.width + " " + cases.size());
    }
}
