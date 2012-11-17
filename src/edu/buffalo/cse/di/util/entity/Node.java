/**
 * 
 */
package edu.buffalo.cse.di.util.entity;

/**
 * Node for the KNN Algorithm
 * @author sravanku@buffalo.edu
 */
public class Node {
    // TODO This very basic representation has only a single attribute.
    private final String string;

    public Node(String string) {
        this.string = string;
    }
    
    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return "Node [string=" + string + "]";
    }
}
