import java.io.*;
import java.text.*;

/**
 * This class contains only a simple main for testing your algorithm
 * on data with one or more heuristics.  The main begins by reading in
 * all of the puzzles described in a file named in <tt>argv[0]</tt>.
 * It then proceeds to run A* using each heuristic listed below on
 * each of the puzzles (simply comment out any heuristics you don't
 * want to be testing on).  In each case, it prints out the solution
 * path that was computed.  Finally, it prints out a summary table of
 * the results.  You may wish to modify or replace this <tt>main</tt>
 * in any way that you wish.  (However, other classes that we have
 * provided should not be modified directly.)
 */
public class RushHourTest {
    
    /**
     * The main for running all puzzles with all heuristics, and
     * printing out all solution paths as well as a final summary
     * table.
     */
    public static void main(String argv[]) throws FileNotFoundException, IOException 
    {
        // read all the puzzles in file named by first argument
        Puzzle[] puzzles = Puzzle.readPuzzlesFromFile(argv[0]);
        
        String[] heuristic_names = null;
        int num_puzzles = puzzles.length;
        int num_heuristics = 0;
        
        int[][] num_generated = null;
        int[][] soln_depth = null;
        
        // run each heuristic on each puzzle
        for(int i = 0; i < num_puzzles; i++) 
        //for (int i = 0; i < 1; i++)
        {
            System.out.println("=================================================");
            System.out.println("puzzle = " + puzzles[i].getName());
            
            Heuristic[] heuristics = {   // these are the heuristics to be used
                new ZeroHeuristic(puzzles[i]),
                new BlockingHeuristic(puzzles[i]),
                new AdvancedHeuristic(puzzles[i])
            };
            
            if (i == 0) 
            {
                num_heuristics = heuristics.length;
                heuristic_names = new String[num_heuristics];
                for (int h = 0; h < num_heuristics; h++)
                    heuristic_names[h] = heuristics[h].getClass().getName();
            }
            
            // get initial state
            Node startNode = puzzles[i].getInitNode();
            State initState = startNode.getState();
            
            // use heuristic and print out value
            for (int h = 0; h < num_heuristics; h++)
            {
                System.out.println(heuristic_names[h]);
                System.out.println(heuristics[h].getValue(initState));
            }
        }
        
    }
}