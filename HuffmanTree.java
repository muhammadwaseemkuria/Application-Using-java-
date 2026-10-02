package org.example;

public class HuffmanTree {

    HeapNode root;

    public HuffmanTree(){
        root = null;
    }


    public void buildTree(MinHeap minHeap) {

        while(true){
            HeapNode smallerNode = minHeap.removeRoot();
            HeapNode largerNode = minHeap.removeRoot();

            if(largerNode == null){
                root = smallerNode;
                break;
            }

            int sum = smallerNode.getPriority() + largerNode.getPriority();
            HeapNode newNode = new HeapNode('*',sum);

            newNode.left = smallerNode;
            newNode.right = largerNode;

            smallerNode.parent = newNode;
            largerNode.parent = newNode;


            minHeap.insert(newNode);

            //minHeap.display();
        }


    }




//    public void insert(MinHeap minHeap){
//
//        while(true){
//            Node smallerNode = minHeap.deleteRoot();
//            Node largerNode = minHeap.deleteRoot();
//
//            if(largerNode == null){
//                root = smallerNode;
//                break;
//            }
//
//            int sum = smallerNode.getPriority() + largerNode.getPriority();
//            Node newNode = new Node('*',sum);
//
//            newNode.left = smallerNode;
//            newNode.right = largerNode;
//
//            smallerNode.parent = newNode;
//            largerNode.parent = newNode;
//
//
//            minHeap.insert(newNode);
//
//        }
//
//    }

//    public void insert(Node node, MinHeap minHeap){
//
//    }




    public void printTree() {
        if (root == null) {
            System.out.println("The heap is empty.");
            return;
        }

        CircularDynamicQueue  newQueue = new CircularDynamicQueue();
        newQueue.enqueue(root);

        System.out.println("Structure: [Node (Priority) | Parent: (Priority)]");

        while (!newQueue.isEmpty()) {
            int levelSize = newQueue.getSize(); // Track size to separate levels visually

            for (int i = 0; i < levelSize; i++) {
                HeapNode current = newQueue.dequeue();
                String parentVal = (current.parent != null) ?
                        String.valueOf(current.parent.getPriority()) : "None";

                System.out.print("[" + current.getCharacter() + " (" + current.getPriority() + ") " +
                        "| P: " + parentVal + "]  ");

                if (current.left != null) newQueue.enqueue(current.left);
                if (current.right != null) newQueue.enqueue(current.right);
            }
            System.out.println(); // New line for each level
        }
    }


}
