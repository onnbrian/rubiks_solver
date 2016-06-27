// RubiksCube.java to store the initial state 
// State.java to store a specific state of a Rubiks cube puzzle
// import java.io.*;
import java.util.*;

/**
 * This is the class for representing a particular rubiks cube.
 * Methods are provided for accessing information about the cube.  In
 * addition, this class maintains a counter of the number of search nodes
 * that have been generated for this cube.  Methods for accessing,
 * incrementing or resetting this counter are also provided.
 * <p>
 * Store here is the name of the puzzle and the initial (root) search node of the
 * puzzle.
 * <p>
 * The goal car is always assigned index 0.
 */
public class RubiksCube 
{
	final static int NUM_FACES = 6;
	final static int FACE_SIZE = 9;
	//final static int FACE_LENGTH = 3;
	final static String[] FACE_INDEX = new String[]{"f", "l", "r", "u", "d", "b"};
	// {face, cubbie, cubbie, cubbie}
	final static int[][][] turns = new int[][][]
	{
		// turning face: left, up, right, down
		{{1, 8, 5, 2}, {3, 6, 7, 8}, {2, 0, 3, 6}, {4, 2, 1, 0}},
		// turning left: face, down, back, up
		{{0, 0, 3, 6}, {4, 0, 3, 6}, {5, 8, 5, 2}, {3, 0, 3, 6}},
		// turning right: face, up, back, down
		{{0, 8, 5, 2}, {3, 8, 5, 2}, {5, 0, 3, 6}, {4, 8, 5, 2}},
		// turning up: face, left, back, right
		{{0, 2, 1, 0}, {1, 2, 1, 0}, {5, 2, 1, 0}, {2, 2, 1, 0}},
		// turning down: face, right, back, left
		{{0, 6, 7, 8}, {2, 6, 7, 8}, {5, 6, 7, 8}, {1, 6, 7, 8}},
		// turning back: left, down, right, up
		{{1, 0, 3, 6}, {4, 6, 7, 8}, {2, 8, 5, 2}, {3, 2, 1, 0}}
	};
    final static int[] CW_ROTATION = new int[]{2, 5, 8, 1, 4, 7, 0, 3, 6};

    private String name;
    // 0 = face
    // 1 = left
    // 2 = right
    // 3 = up
    // 4 = down
    // 5 = back
    private int[][] faces = new int[NUM_FACES][FACE_SIZE];

    private Node initNode;
    private int searchCount;

    
    /** Increments the search counter by <tt>d</tt>. */
    public void incrementSearchCount(int d) {
        searchCount += d;
    }
    
    /**
     * Returns the current value of the search counter, which keeps a
     * count of the number of nodes generated on the current
     * search.
     */
    public int getSearchCount() {
        return searchCount;
    }
    
    /** Resets the search counter to 1 (for the initial node). */
    public void resetSearchCount() {
        searchCount = 1;
    }
    
    /** Returns the name of this puzzle. */
    public String getName() {
        return name;
    }
    
    /** Returns the name of this puzzle. */
    public int[][] getAllFaces() 
    {
        return faces;
    }

    public int[] getFace(String side)
    {
    	for (int i = 0; i < NUM_FACES; i++)
    	{
    		if (side == FACE_INDEX[i])
    		{
    			return faces[i];
    		}
    	}
    	assert (false);
    	return null;
    }

    /** Returns the initial (root) node of this puzzle. */
    public Node getInitNode() {
        return initNode;
    }

    /**
     * The main constructor for constructing a rubiks cube.
     * <faces> should be a 2D array of 6 1D arrays of length 9
     * Each 1D array corresponds to a face of the rubiks cube
     * in the following order: f, l, r, u, d, b
 	**/
    public RubiksCube(String name, int[][] input_faces) 
    {
        this.name = name;
        
        assert (input_faces.length == NUM_FACES);

        for (int i = 0; i < NUM_FACES; i++)
        {
        	assert (input_faces.length == FACE_SIZE);
        	System.arraycopy(input_faces[i], 0, this.faces[i], 0, FACE_SIZE);
        }
        
        //initNode = new Node(new State(this, null), 0, null);
        
        resetSearchCount();
    }
}
