package org.example;

public class LinkList {

    Node head;
   // Random random = new Random();

    public void insertAtStart(CapturedPacket capturedPacket){
        //int newData = random.nextInt(10);
        Node newNode = new Node(capturedPacket);
        newNode.next = head;
        head = newNode;


    }

    public synchronized void insertAtEnd(CapturedPacket capturedPacket) {
        Node newNode = new Node(capturedPacket);

        if (head == null) {
            head = newNode;
            return;
        }

        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    public int getCount() {
        int count = 0;
        Node temp = head;
        while (temp != null) { // Fixed: check temp, not temp.next
            count++;
            temp = temp.next; // Fixed: must move to next node
        }
        return count;
    }

/*
    public void deleteByValue(byte[] value){
        Node temp = head;
        Node pre = null;


        if (head == null)
            return;


        if (head.rawData == value) {
            head = head.next;
            return;
        }
        while (temp.rawData != value ){
            pre = temp;
            temp = temp.next;
        }
        pre.next = temp.next;

    }

*/

    public void deleteByIndex(int index){
        int counter = 0;
        Node temp = head;
        Node pre = null;
        //temp = temp.next;

        if(head == null){
            System.out.println("LinkList is empty");
            return;
        }


        if(index == 0){
            head = head.next;
            //System.out.println("Can not delete Head");
            return;

        }

        while(temp != null && counter != index){

            pre = temp;
            temp = temp.next;
            counter++;


        }

        pre.next = temp.next;


    }
//
//    public int deleteLastNode(){
//        int value = 0;
//        Node temp = head;
//        Node pre = null;
//
//        if(head == null){
//            return -1;
//        }
//
//        if (head.next == null) {
//            value = head.data;
//            head = null; // The list is now empty
//            return value;
//        }
//
//
//        while(temp.next != null){
//            pre = temp;
//            temp = temp.next;
//        }
//
//        value = temp.data;
//        pre.next = null;
//
//
//        return value;
//    }
//




//    public int deleteFirstNode(){
//        int topValue = 0;
//        if(head == null){
//            System.out.println("Link List is empty");
//            return -1;
//        }
//        topValue = head.data;
//        head = head.next;
//
//
//
//        return topValue;
//    }


    public void display(){
        Node temp = head;
        while (temp!= null){
            System.out.println(temp.capturedPacket);
            temp = temp.next;
        }

    }




    //public void

}
