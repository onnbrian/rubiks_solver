/**
 * This is the template for a class that performs A* search on a given
 * rush hour puzzle with a given heuristic.  The main search
 * computation is carried out by the constructor for this class, which
 * must be filled in.  The solution (a path from the initial state to
 * a goal state) is returned as an array of <tt>State</tt>s called
 * <tt>path</tt> (where the first element <tt>path[0]</tt> is the
 * initial state).  If no solution is found, the <tt>path</tt> field
 * should be set to <tt>null</tt>.  You may also wish to return other
 * information by adding additional fields to the class.
 */

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Queue;

public class AStar {

    // static wrapper class for a node and its function value
    public static class NodeWrapper 
    {
        private Node node;
        private int cost;
        private int hVal;
        private int funcVal;
        
        public NodeWrapper(Node node, int cost, int hVal)
        {
            this.node = node;
            this.cost = cost;
            this.hVal = hVal;
            this.funcVal = this.cost + this.hVal;
        }
        
        // return the node
        public Node getNode()
        {
            return node;
        }
        
        // get the cost of the node
        public int getCost()
        {
            return cost;
        }
        
        // get its heuristic value
        public int getHVal()
        {
            return hVal;
        }
        
        // get its function value
        public int getFuncVal()
        {
            return funcVal;
        }
    }
    
    // comparator for node wrapper class
    public class NodeWrapperComp implements Comparator<NodeWrapper>
    {
        public int compare(NodeWrapper x, NodeWrapper y)
        {
            if (x.getFuncVal() < y.getFuncVal())
                return -1;
            else if (x.getFuncVal() > y.getFuncVal())
                return 1;
            
            return 0;
        }
    }
    
    /** The solution path is stored here */
    public State[] path;

    /**
     * This is the constructor that performs A* search to compute a
     * solution for the given puzzle using the given heuristic.
     */
    public AStar(Puzzle puzzle, Heuristic heuristic) 
    {   
        path = null;
        // frontier container is priority queue
        Comparator<NodeWrapper> comparator = new NodeWrapperComp();
        PriorityQueue<NodeWrapper> frontier = new PriorityQueue<NodeWrapper>(1, comparator);
        
        // expanded set container is a HashSet
        HashSet<State> expanded = new HashSet<State>();
        
        // add start node to frontier
        Node initial = puzzle.getInitNode();
        int hVal = heuristic.getValue(initial.getState());
        NodeWrapper initialNW = new NodeWrapper(initial, initial.getDepth(), hVal);
        frontier.add(initialNW);
        
        while (frontier.size() > 0)
        {
            // get node n with minimun f(n)
            NodeWrapper minNW = frontier.poll();
            Node minNode = minNW.getNode();
            State minState = minNode.getState();
            
            if (expanded.contains(minState))
            {
                continue;
            }
            
            expanded.add(minState);
            
            // if goal, set <path> to solution
            if (minState.isGoal())
            {
                Node currentNode = minNode;
                path = new State[minNode.getDepth() + 1];
                for (int i = minNW.getCost(); i >= 0; i--)
                {
                    path[i] = currentNode.getState();

                    // asserts to make sure no off-by-one error
                    if (currentNode.getParent() == null)
                    {
                        assert (i == 0);
                    }
                    if (i == 0)
                    {
                        assert (currentNode.getParent() == null);
                    }
                    currentNode = currentNode.getParent();
                }
                break;
            }
            
            // add children of n to frontier
            Node[] childrenNodes = minNode.expand();
            
            for (int i = 0; i < childrenNodes.length; i++)
            {
                hVal = heuristic.getValue(childrenNodes[i].getState());
                NodeWrapper childNW = new NodeWrapper(childrenNodes[i], childrenNodes[i].getDepth(), hVal);
                frontier.add(childNW);
            } 
        }
    }

}
