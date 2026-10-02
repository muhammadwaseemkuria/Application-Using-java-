package org.example;

public class CircularDynamicQueue {

    HeapNode[] heapNode = new HeapNode[10];
    int front;
    int rear;

    public CircularDynamicQueue(){
        front = -1;
        rear = -1;
    }


    public void enqueue(HeapNode newNode){
      if((rear+1)%heapNode.length == front){
          increaseCapacity();
      }
        rear = (rear+1)% heapNode.length;
        heapNode[rear] = newNode;

      if(front == -1 ){
          front++;
      }


    }

    public HeapNode dequeue(){

        if(front == -1 && rear == -1){

          return null;

        }
        else{
            HeapNode toReturn =  heapNode[front];

            if(front == rear){
                front = rear = -1;
            }
            else{
                front = (front+1)%heapNode.length;
            }

            return toReturn;

        }


    }





    public void display(){

        if(front == -1)
            return;

        int i = front;
        while(true){
            System.out.println(heapNode[i]);
            if(i == rear)
                break;
            i = (i+1)% heapNode.length;

        }


//        for(int i = front; i <= rear; i++){
//            //System.out.println(patients[i].toString());
//        }

    }

    public void increaseCapacity(){
        HeapNode[] temp = new HeapNode[heapNode.length+5];
        int size = getSize();

        for(int i = 0; i < size; i++){
            int previousIndex = (front+i)% heapNode.length;
            temp[i] = heapNode[previousIndex];
        }


        heapNode = temp;
        front = 0;
        rear = size - 1;


    }

    public int getSize(){
        if(front == -1)
            return 0;
        if(front <= rear)
            return rear-front+1;
        return (heapNode.length - front) +(rear +1);

    }

    public boolean isEmpty(){
        if(front == -1 & rear == -1){
            return true;
        }
        return false;

    }


}
