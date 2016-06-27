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

public class RushHour 
{
    
    public static double mean(int[] values)
    {
        double total = 0.0;
        for (int i = 0; i < values.length; i++)
        {
            total = total + values[i];
        }
        return (total / values.length);
    }
    
    public static double sd(int[] values)
    {
        double avg = mean(values);
        double total = 0.0;
        for (int i = 0; i < values.length; i++)
        {
            double diff = values[i] - avg;
            total = total + (diff * diff);
        }
        total = (total / values.length);
        return Math.sqrt(total);
        //return 0.0;
    }

    private static String left_pad(String s, int n) 
    {
        for (int i = n - s.length(); i > 0; i--)
            s = " " + s;
        return s;
    }
    
    private static String right_pad(String s, int n) 
    {
        for (int i = n - s.length(); i > 0; i--)
            s = s + " ";
        return s.substring(0,n);
    }
    
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
                new AdvancedHeuristic(puzzles[i]),
            };
            
            if (i == 0) 
            {
                num_heuristics = heuristics.length;
                num_generated = new int[num_puzzles][num_heuristics];
                soln_depth = new int[num_puzzles][num_heuristics];
                
                heuristic_names = new String[num_heuristics];
                for (int h = 0; h < num_heuristics; h++)
                    heuristic_names[h] = heuristics[h].getClass().getName();
            }
            
            for(int h = 0; h < num_heuristics; h++) 
            {
                System.out.println();
                System.out.println("------------------------------------");
                System.out.println();
                System.out.println("heuristic = " + heuristic_names[h]);
                
                puzzles[i].resetSearchCount();
                AStar search = new AStar(puzzles[i], heuristics[h]);
                
                if (search.path == null) 
                {
                    System.out.println("NO SOLUTION FOUND.");
                    soln_depth[i][h] = -1;
                } 
                else 
                {
                    for(int j = 0; j < search.path.length; j++) 
                    {
                        search.path[j].print();
                        System.out.println();
                    }
                    
                    num_generated[i][h] = puzzles[i].getSearchCount();
                    soln_depth[i][h] = search.path.length - 1;
                    
                    System.out.println("nodes generated: " + num_generated[i][h]
                                           + ", soln depth: " + soln_depth[i][h]);
                }
            }
        }
        
        // print the results in a table
        System.out.println();
        System.out.println();
        System.out.println();
        
        System.out.print("          ");
        for (int h = 0; h < num_heuristics; h++)
            System.out.print(" |    "
                                 + right_pad(heuristic_names[h],18));
        System.out.println();
        
        System.out.print("name      ");
        for (int h = 0; h < num_heuristics; h++)
            System.out.print(" |    nodes dpth  br.fac");
        System.out.println();
        
        System.out.print("----------");
        for (int h = 0; h < num_heuristics; h++)
            System.out.print("-+----------------------");
        System.out.println();
        
        NumberFormat brfac_nf = new DecimalFormat("##0.000");
        
        for (int i = 0; i < num_puzzles; i++) 
        {
            System.out.print(right_pad(puzzles[i].getName(),10));
            
            for (int h = 0; h < num_heuristics; h++) 
            {
                if (soln_depth[i][h] < 0) 
                {
                    System.out.print(" |  ** search failed ** ");
                } 
                else 
                {
                    System.out.print(" | "
                                         + left_pad(Integer.toString(num_generated[i][h]),8)
                                         + " "
                                         + left_pad(Integer.toString(soln_depth[i][h]),4)
                                         + " "
                                         + left_pad(brfac_nf.format(BranchingFactor.compute(num_generated[i][h],
                                                                                            soln_depth[i][h])),7));
                }
            }
            System.out.println();
        }
        
        int[] diff12 = new int[num_puzzles];
        int[] diff23 = new int[num_puzzles];
        int[] diff13 = new int[num_puzzles];
        
        for (int i = 0; i < num_puzzles; i++)
        {
            diff12[i] = num_generated[i][0] - num_generated[i][1];
            diff23[i] = num_generated[i][1] - num_generated[i][2];
            diff13[i] = num_generated[i][0] - num_generated[i][2];
            
            System.out.println(right_pad(puzzles[i].getName(),10));
            for (int h = 0; h < num_heuristics; h++) 
            {
                System.out.print(Integer.toString(num_generated[i][h]) + " --- ");
                
            }
            System.out.println();
            System.out.println("diff between 1 and 2: " + Integer.toString(diff12[i]));
            System.out.println("diff between 2 and 3: " + Integer.toString(diff23[i]));
            //System.out.println();
        }
        
        int[] test = {9, 2, 5, 4, 12, 7, 8, 11, 9, 3, 7, 4, 12, 5, 4, 10, 9, 6, 9, 4 };
        System.out.println("test mean: " + mean(test));
        System.out.println("mean diff 1->2: " + mean(diff12));
        System.out.println("mean diff 2->3: " + mean(diff23));
        System.out.println("mean diff 1->3: " + mean(diff13));
        System.out.println("test sd: " + sd(test));
        System.out.println("sd diff 1->2: " + sd(diff12));
        System.out.println("sd diff 2->3: " + sd(diff23));
        System.out.println("sd diff 1->3: " + sd(diff13));
    }
}
