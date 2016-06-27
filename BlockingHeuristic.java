/**
 * This is a template for the class corresponding to the blocking
 * heuristic.  This heuristic returns zero for goal states, and
 * otherwise returns one plus the number of cars blocking the path of
 * the goal car to the exit.  This class is an implementation of the
 * <tt>Heuristic</tt> interface, and must be implemented by filling in
 * the constructor and the <tt>getValue</tt> method.
 */
public class BlockingHeuristic implements Heuristic {
    Puzzle puzzle;
    int answerCarY;
    
    public BlockingHeuristic(Puzzle puzzle) 
    {
        this.puzzle = puzzle;
        answerCarY = puzzle.getFixedPosition(0);
    }
    
    /**
     * This method returns the value of the heuristic function at the
     * given state.
     */
    public int getValue(State state) 
    {        
        // goal state case
        if (state.isGoal())
            return 0;
        
        // not goal state
        // first get the right position of goal car
        int cutoff = state.getVariablePosition(0) + 1;
        
        int blockCount = 0;
        for (int i = 0; i < puzzle.getNumCars(); i++)
        {
            // filter for vertical cars
            if (puzzle.getCarOrient(i))
            {
                // filter for vertical cars past cutoff point
                if (puzzle.getFixedPosition(i) > cutoff)
                {
                    // get length of vertical car
                    int lengthV = puzzle.getCarSize(i);
                    
                    // get top y position of vertical car
                    int minVertY = state.getVariablePosition(i);
                    
                    // bottom y position of vertical car
                    int maxVertY = minVertY + (lengthV - 1);
                    
                    // if blocking, increment block count
                    if ((minVertY <= answerCarY) && (answerCarY <= maxVertY))
                        blockCount++;
                    
                }
            }
        }
        return blockCount + 1;
    }
    
}
