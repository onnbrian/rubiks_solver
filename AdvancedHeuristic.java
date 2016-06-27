/**
 * This is a template for the class corresponding to your original
 * advanced heuristic.  This class is an implementation of the
 * <tt>Heuristic</tt> interface.  After thinking of an original
 * heuristic, you should implement it here, filling in the constructor
 * and the <tt>getValue</tt> method.
 */
public class AdvancedHeuristic implements Heuristic {
    
    Puzzle puzzle;
    int goalY;
    
    public AdvancedHeuristic(Puzzle puzzle) 
    {
        this.puzzle = puzzle;
        goalY = puzzle.getFixedPosition(0);
    }
 
    private int getRightmostXCarH(int i, State state)
    {
        // get length of horizontal car
        int length = puzzle.getCarSize(i);
   
        // get left x position
        int xLeft = state.getVariablePosition(i);
        
        return (xLeft + length - 1);
    }
       
    private boolean isStuck(State state, int[][] grid, boolean[] horizBlocking, int vCar)
    {
        // x position of <vCar>
        int x = puzzle.getFixedPosition(vCar);
        
        // get length of <VCar>
        int lengthV = puzzle.getCarSize(vCar);
        
        // is <vCar> free to move out of the way on top?
        boolean stuckOnTop = false;
        // is <vCar> free to move out of the way on below?
        boolean stuckOnBottom = false;
        
        //////////////
        // top case
        //////////////
        
        // ideal case where no cars are blocking -- is it just too big?
        if (goalY - (lengthV) < 0)
        {
            stuckOnTop = true;
        }
        // with no cars in way, this <vCar> can get out of the way
        else
        {
            int horizBlockingCar = -1;
            int rightmostX = -1;
            
            // scan up starting at (<x>, <goalY>) to see if any 
            // horizontal or vertical cars are blocking <vCar>
            for (int y = goalY; y >= (goalY - lengthV); y--)
            {
                // car at this position
                int currentCar = grid[x][y];
                
                // skip if the current car is <vCar>
                if (currentCar == vCar)
                    continue;
                
                // empty space, so skip
                if (currentCar < 0)
                    continue;
                
                // vertical car is blocking -- this means <vCar> is stuck on top
                if (puzzle.getCarOrient(currentCar))
                {
                    stuckOnTop = true;
                    break;
                }
                
                // horizontal car is blocking -- tricky case!
                // we only allow a horizontal car to block at most one vertical car
                // to ensure consistency.
                // since we are scanning from the right to the left, choose the car with the rightmost left corner x value if there are
                // multiple horizontals cars available to block <vCar>
                // this will allow us to "maximize" the number of horizontal cars we can use to block vertical cars like <vCar>
                else
                {
                    // keep track of left rightmost horizontal blocking car
                    if (!horizBlocking[currentCar])
                    {
                        if (state.getVariablePosition(currentCar) > rightmostX)
                        {
                            horizBlockingCar = currentCar;
                            rightmostX = state.getVariablePosition(currentCar);
                        }
                    }
                }
            }
            
            // do we have a horizontal blocking car?
            if (horizBlockingCar > 0 && !stuckOnTop)
            {
                // we do
                // make sure no other vertical car uses <horizBlockingCar> as a blocking car
                horizBlocking[horizBlockingCar] = true;
                // this <vCar> is blocked on top
                stuckOnTop = true;
            }
        }
        
        if (!stuckOnTop)
            return false;
        
        ////////////////////
        // bottom case
        ////////////////////
        
        // ideal case where no cars are blocking -- is it just too big?
        if (goalY + (lengthV) >= puzzle.getGridSize())
        {
            stuckOnBottom = true;
        }
        // with no cars in way, this <vCar> can move down out of the way
        else
        {
            int horizBlockingCar = -1;
            int rightmostX = -1;
            
            // scan down starting at (<x>, <goalY>) to see if any 
            // horizontal or vertical cars are blocking <vCar>            
            for (int y = goalY; y <= (goalY + lengthV); y++)
            {
                int currentCar = grid[x][y];
                
                // skip if the current car is <vCar>
                if (currentCar == vCar)
                    continue;
                
                // empty space, so skip
                if (currentCar < 0)
                    continue;
                
                // vertical car is blocking -- this means <vCar> is stuck on bottom
                if (puzzle.getCarOrient(currentCar))
                {
                    stuckOnBottom = true;
                    break;
                }
                
                // horizontal car is blocking -- tricky case!
                // we only allow a horizontal car to block at most one vertical car
                // to ensure consistency.
                // since we are scanning from the right to the left, choose the car with the rightmost left corner x value
                // this will allow us to "maximize" the number of horizontal cars we can use to block vertical cars like <vCar>               
                else
                {
                    // keep track of rightmost horizontal blocking car
                    if (!horizBlocking[currentCar])
                    {
                        if (state.getVariablePosition(currentCar) > rightmostX)
                        {
                            horizBlockingCar = currentCar;
                            rightmostX = state.getVariablePosition(currentCar);
                        }
                    }
                }
            }
            // do we have a horizontal blocking car?
            if (horizBlockingCar > 0 && !stuckOnBottom)
            {
                // we do
                // make sure no other car uses <horizBlockingCar> as a blocking car
                horizBlocking[horizBlockingCar] = true;
                
                // this <vCar> is blocked on bottom
                stuckOnBottom = true;
            }
        }
        
        return stuckOnBottom;
    }
    
    public int getValue(State state)
    {
        // return 0 if state is goal
        if (state.isGoal())
            return 0;
        
        boolean[] horizBlocking = new boolean[puzzle.getNumCars()];
        int[][] grid = state.getGrid();
        
        int count = 0;
        for (int i = (puzzle.getGridSize() - 1); i >= 0; i--)
        {
            int car = grid[i][goalY];

            assert (i != 0);

            if (car == 0)
                break;
            
            // if the car is a blocking car, increment the count
            if (car > 0)
            {
                count++;
                // if the car is also stuck on both sides (up and down) by unique cars/the border, increment the count again
                if (isStuck(state, grid, horizBlocking, car))
                {
                    count++;
                }
            }
        }
        count++;
        return count;
    }
}
