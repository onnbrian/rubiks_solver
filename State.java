import java.util.*;

/**
 * This is the class for representing a single state of the rush hour
 * puzzle.  Methods are provided for constructing a state, for
 * accessing information about a state, for printing a state, and for
 * expanding a state (i.e., obtaining a list of all states immediately
 * reachable from it).
 * <p>
 * Every car is constrained to only move horizontally or vertically.
 * Therefore, each car has one dimension along which it is fixed, and
 * another dimension along which it can be moved.  This variable
 * dimension is stored here as part of the state.  A link to the
 * puzzle with which this state is associated is also stored.
 * Note that the goal car is always assigned index 0.  
 * <p>
 * To make it easier to use <tt>State</tt> objects with some of the
 * data structures provided as part of the Standard Java Platform, we
 * also have provided <tt>hashCode</tt> and <tt>equals</tt> methods.
 * You probably will not need to access these methods directly, but
 * they are likely to be used implicitly if you take advantage of the
 * Java Platform.  These methods define two <tt>State</tt> objects to
 * be equal if they refer to the same <tt>Puzzle</tt> object, and if
 * they indicate that the cars have the identical variable positions
 * in both states.  The hashcode is designed to satisfy the general
 * contract of the <tt>Object.hashCode</tt> method that it overrides,
 * with regard to the redefinition of <tt>equals</tt>.
 */

public class State {
    
    private RubiksCube rb;
    private int[][] faces;
    
    /**
     * The main constructor for constructing a state.
     *   @param rb the cube that this state is associated with
     *   @param faces the state of each face on the cube
     */
    public State(RubiksCube rb,
                 int[][] faces) 
    {
        this.rb = rb;
        this.faces = faces;
        //computeHashCode();
    }
    
    /** Returns true if and only if this state is a goal state. */
    public boolean isGoal() 
    {
        boolean[] allColors = new boolean[RubiksCube.NUM_FACES];
        // for each face...   
        for (int i = 0; i < RubiksCube.NUM_FACES; i++)
        {
            int[] face = faces[i];

            // get a color on face
            int color = face[0];

            // is the entire face same color?
            // return false is no
            for (int j = 0; j < RubiksCube.FACE_SIZE; j++)
            {
                if (color != face[j])
                {
                    return false;
                }
            }
            allColors[color] = true;
        }

        // make sure each face has a different color
        for (int i = 0; i < RubiksCube.NUM_FACES; i++)
        {
            if (!allColors[i])
            {
                return false;
            }
        }

        return true;
    }
    
    // returns the face on side <side>
    public int[] getFace(String side) 
    {
        for (int i = 0; i < RubiksCube.NUM_FACES; i++)
        {
            if (side == RubiksCube.FACE_INDEX[i])
            {
                return faces[i];
            }
        }
        assert (false);
        return null;
    }
    
    /** Returns the puzzle associated with this state. */
    public RubiksCube getRubiksCube() {
        return rb;
    }
    
    /** Prints to standard output a primitive text representation of the state. */
    public void print() 
    {
        for (int i = 0; i < RubiksCube.NUM_FACES; i++)
        {
            System.out.println(RubiksCube.FACE_INDEX[i]);
            int k = 0;
            for (int j = 0; j < RubiksCube.FACE_SIZE; j++)
            {
                System.out.print(faces[i][j] + " ");
                k++;
                if (k > 2)
                {
                    System.out.println();
                    k = 0;
                }
            }
        }
    }
    
    // return a new copy of <faces>
    private int[][] newCopyOfFaces()
    {
        int[][] copy = new int[RubiksCube.NUM_FACES][RubiksCube.FACE_SIZE];
        for (int i = 0; i < RubiksCube.NUM_FACES; i++)
        {
            for (int j = 0; j < RubiksCube.FACE_SIZE; j++)
            {
                copy[i][j] = faces[i][j];
            }
        }
        return copy;
    }

    private void rotateFace(int[] face, boolean clockwise)
    {
        int[] face_orig = face.clone();
        if (clockwise)
        {
            for (int i = 0; i < RubiksCube.CW_ROTATION.length; i++)
            {
                face[RubiksCube.CW_ROTATION[i]] = face_orig[i];
            }
        }
        else
        {
            for (int i = 0; i < RubiksCube.CW_ROTATION.length; i++)
            {
                face[i] = face_orig[RubiksCube.CW_ROTATION[i]];
            }   
        }
        return;
    }

    /**
     * Computes all of the states immediately reachable from this
     * state and returns them as an array of states.  You probably
     * will not need to use this method directly, since ordinarily you
     * will be expanding <tt>Node</tt>s, not <tt>State</tt>s.
     */
    public State[] expand()
    {
        ArrayList<State> new_states = new ArrayList<State>();

        for (int i = 0; i < RubiksCube.turns.length; i++)
        {
            int[][] turn = RubiksCube.turns[i];
            // two directions to turn side
            for (int j = 0; j < 2; j++)
            {
                int[][] newFaces = newCopyOfFaces();
                // clockwise
                if (j == 0)
                {
                    // rotate face @ turn[0]
                    rotateFace(newFaces[i], true);

                    for (int to = 0; to < turn.length; to++)
                    {
                        int from = to == 0 ? turn.length - 1 : to - 1;
                        int[] from_side = turn[from];
                        int[] to_side = turn[to];

                        newFaces[to_side[0]][to_side[1]] = faces[from_side[0]][from_side[1]];
                        newFaces[to_side[0]][to_side[2]] = faces[from_side[0]][from_side[2]];
                        newFaces[to_side[0]][to_side[3]] = faces[from_side[0]][from_side[3]];
                    }
                }
                // counterclockwise
                else
                {
                    rotateFace(newFaces[i], false);
                    for (int to = 0; to < turn.length; to++)
                    {
                        int from = to == turn.length - 1 ? 0 : to + 1;
                        int[] from_side = turn[from];
                        int[] to_side = turn[to];

                        newFaces[to_side[0]][to_side[1]] = faces[from_side[0]][from_side[1]];
                        newFaces[to_side[0]][to_side[2]] = faces[from_side[0]][from_side[2]];
                        newFaces[to_side[0]][to_side[3]] = faces[from_side[0]][from_side[3]];
                    }
                }
                new_states.add(new State(rb, newFaces));
            }
        }
    
        rb.incrementSearchCount(new_states.size());    
        return (State[]) new_states.toArray(new State[0]);
    }
}
