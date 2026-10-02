package org.example;


public class HeapNode {
    private int priority;
    private char character;
    HeapNode left;
    HeapNode right;
    HeapNode parent;
    public HeapNode(char character , int priority){
        this.priority = priority;
        this.character = character;
        left = null;
        right = null;
        parent = null;

    }

    public int getPriority() {
        return priority;
    }

    public char getCharacter() {
        return character;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setCharacter(char character) {
        this.character = character;
    }


}
