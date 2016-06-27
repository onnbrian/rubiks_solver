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
public class RubiksCubeTest 
{    
    /**
     * The main for running all puzzles with all heuristics, and
     * printing out all solution paths as well as a final summary
     * table.
     */
    public static void main(String argv[]) throws FileNotFoundException, IOException 
    {   
        String[] face_order = new String[]{"f", "u", "d", "r", "l", "b"};

        int[][] faces = new int[][]
        {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {2, 2, 2, 2, 2, 2, 2, 2, 2},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {4, 4, 4, 4, 4, 4, 4, 4, 4},
            {5, 5, 5, 5, 5, 5, 5, 5, 5}
        };

        RubiksCube rb = new RubiksCube("test", faces);
        /*
        for (int i = 0; i < faces.length; i++)
        {
            System.out.println(face_order[i]);
            int[] face = rb.getFace(face_order[i]);
            for (int j = 0; j < face.length; j++)
            {
                System.out.print(face[j] + " ");
            }
            System.out.println();
        }
        */

        State state = new State(rb, rb.getAllFaces());
        State[] newStates = state.expand();
        for (int i = 0; i < newStates.length; i++)
        {
            System.out.println("State " + i);
            System.out.println("--------------------");
            newStates[i].print();
            System.out.println();
        }
        //state.print();
        //System.out.println(state.isGoal());
    }
}