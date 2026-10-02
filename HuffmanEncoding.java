package org.example;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HuffmanEncoding {

    private String inputText;
    char[] charInput;
    MinHeap minHeap = new MinHeap(5);
    LinkedHashMap<Character,Integer> frequencyTable = new LinkedHashMap<>();
    HashMap<Character,String> charToCodeTable = new HashMap<>();
    HashMap<String,Character> codeToCharTable = new HashMap<>();
    HuffmanTree huffmanTree = new HuffmanTree();
    StringBuilder compressedText = new StringBuilder();
    StringBuilder decompressedText  =new StringBuilder();

    public HuffmanEncoding(String input){
        inputText = input;


    }


    public void toFrequency(){

        charInput = inputText.toCharArray();


        for(int i =0; i < charInput.length; i++){
            if(frequencyTable.containsKey(charInput[i])){
                int currentValue = frequencyTable.get(charInput[i]);
                frequencyTable.put(charInput[i], currentValue+1);
            }
            else{
                frequencyTable.put(charInput[i],1);
            }

        }



    }

    public void makeMinHeap(){
        for(Map.Entry<Character,Integer> entry: frequencyTable.entrySet()){
            char tempChar = entry.getKey();
            int tempVal = entry.getValue();

            minHeap.insert(new HeapNode(tempChar,tempVal));

        }


    }

    public void makeHuffmanTree(){


        if(!minHeap.isEmpty()){
            huffmanTree.buildTree(minHeap);

        }


    }


    public void deleteRoot(){
        minHeap.removeRoot();
    }



    public void displayHeap(){
        minHeap.display();
    }


    public void displayFrequencyTable(){

        for(Map.Entry<Character,Integer> entry: frequencyTable.entrySet()){

            char character = entry.getKey();
            int value = entry.getValue();

            System.out.println(character+": "+value);

        }
    }

    public void getCodes(){
        String code  = "";
        HeapNode root = huffmanTree.root;
        calculateCodes(root,code);
    }

    private boolean isLeaf(HeapNode n) {
        return (n.left == null) && (n.right == null);
    }


    private void calculateCodes(HeapNode node, String code) {

        if(node!=null){
            if(!isLeaf(node)){
                calculateCodes(node.left, code+"0");
                calculateCodes(node.right, code+"1");
            }
            else{
                charToCodeTable.put(node.getCharacter(),code);
                codeToCharTable.put(code,node.getCharacter());
            }

        }
    }


    public StringBuilder displayCodes(){
        StringBuilder returnTemp = new StringBuilder();

        for(Map.Entry<Character,String> entry: charToCodeTable.entrySet()){
            char tempChar = entry.getKey();
            String temp = entry.getValue();


            String temp2 = tempChar+":"+temp;

            returnTemp.append(temp2).append(System.lineSeparator());


        }

        return returnTemp;
    }

    public void compressText(){
        for (int i = 0; i < inputText.length(); i++){
            char temp = charInput[i];
            String code = charToCodeTable.get(temp);
            compressedText.append(code);
        }
    }

    public StringBuilder displayCompressedText(){
        return compressedText;
    }



    public void decode(){
        String code ="";

        for(int i = 0; i < compressedText.length(); i++){
            code += compressedText.charAt(i);
            if(codeToCharTable.containsKey(code)){
                decompressedText.append(codeToCharTable.get(code));
                code = "";
            }

        }

    }

    public void displayDecodedText(){
        System.out.println(decompressedText);
    }


    public byte[] getBitArray(){

        String bits = compressedText.toString();

        int length = (bits.length()+7) / 8;
        byte[] out = new byte[length];

        for(int i = 0; i < bits.length(); i++){
            if(bits.charAt(i) == '1'){
                out[i/8] |= (1 << (7-(i%8)));
            }
        }
        return out;
    }

    public HashMap<Character, String> getCharToCodeTable() {
        return charToCodeTable;
    }


}
