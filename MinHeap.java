package org.example;

public class MinHeap {

    HeapNode[] heap;
    int size;
    int capacity;
    public MinHeap(int capacity){

        this.capacity = capacity;
        heap = new HeapNode[capacity];
        size = 0;

    }


    private void increaseCapacity(){

        HeapNode[] newHeap = new HeapNode[heap.length + 5];

        for(int i = 0; i < heap.length; i++){
            newHeap[i] = heap[i];
        }
        heap = newHeap;
        this.capacity = heap.length;

    }

    public void insert(HeapNode node){
        if(size == capacity){
            increaseCapacity();
        }

        heap[size] = node;
        int i = size;
        size++;


        while(i > 0 && heap[(i-1)/2].getPriority()>heap[i].getPriority()){
            int parentIndex = (i-1)/2;
            HeapNode temp = heap[parentIndex];
            heap[parentIndex] = heap[i];
            heap[i] = temp;

            i = parentIndex;
        }



    }


    public HeapNode removeRoot(){

        if(size == 0){
            return null;
        }

        HeapNode root = heap[0];
        heap[0] = heap[--size];

        heapifyDown(0);

        return root;

    }

    public boolean isEmpty(){
        if(size == 0)
            return true;
        return false;
    }


    public void heapifyDown(int i){

        int smallest = i;


        int leftChildIndex = 2*i + 1;
        int rightChildIndex =2*i + 2;


        if(leftChildIndex < size && heap[leftChildIndex].getPriority() < heap[smallest].getPriority()){
            smallest = leftChildIndex;
        }

        if(rightChildIndex< size && heap[rightChildIndex].getPriority() < heap[smallest].getPriority()){
            smallest = rightChildIndex;
        }

        if(smallest != i){

            HeapNode temp = heap[i];
            heap[i] = heap[smallest];
            heap[smallest] = temp;

            heapifyDown(smallest);

        }

    }

    public void display() {
        if (size == 0) {
            System.out.println("Heap is empty");
            return;
        }

        System.out.println("Heap (Array Representation):");

        for (int i = 0; i < size; i++) {
            System.out.print("[" + heap[i].getCharacter() + " ("
                    + heap[i].getPriority() + ")] ");
        }

        System.out.println();
    }


}
