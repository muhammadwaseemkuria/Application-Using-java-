package org.example;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSteganography {

    String secretText;
    String encryptedSecretText;
    int key;
    StringBuilder binaryInput = new StringBuilder();
    File image;
    BufferedImage bufferedImage ;
    StringBuilder extractedText = new StringBuilder();
    CaesarCipher cipher;
    File saveFilePath;

    public ImageSteganography (String imagePath, String savePath, String secretText, int key){
        this.image = new File(imagePath);
        this.key = key;
        encryptSecretMessage(secretText,key);
        this.saveFilePath = new File(savePath);
    }

    public ImageSteganography (String imagePath, int key){
        this.saveFilePath = new File(imagePath);
        this.key = key;


    }


    public void encryptSecretMessage(String text, int key){
        cipher = new CaesarCipher(text, key);
        String encrypted = cipher.encrypt()+"\0";
        this.encryptedSecretText = encrypted;

    }


    public void hideText(){

        //first we convert input to binary

        for(char ch: encryptedSecretText.toCharArray()){
            String binaryChar = Integer.toBinaryString(ch);

            binaryInput.append(String.format("%8s",binaryChar).replace(' ','0'));
        }

        // Calculating size of image

        try {
            bufferedImage = ImageIO.read(image);
        }catch (IOException ioException){
            System.out.println(ioException);
            return;
        }

        // calculating bytes per pixels
        int bitsPerPixel = bufferedImage.getColorModel().getPixelSize();
        int bytesPerPixel = bitsPerPixel / 8;

        int imageWidth = bufferedImage.getWidth();
        int imageHeight = bufferedImage.getHeight();

        long sizeOfImage = (long) imageHeight * imageWidth * 4;

        if(binaryInput.length()> sizeOfImage){
            System.out.println("Insufficient Capacity");
            return;
        }


        int index = 0;

        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){

                int currentPixel = bufferedImage.getRGB(i,j);

                //masking and shifting the rdb values

                int a = (currentPixel >> 24) & 0xff;
                int r = (currentPixel >> 16) & 0xFF;
                int g = (currentPixel >> 8) & 0xFF;
                int b = currentPixel & 0xFF;

                if(index<binaryInput.length()){
                    int bit = Character.getNumericValue(binaryInput.charAt(index));
                    a = a & 0XFE|bit;
                    index++;

                }

                if(index<binaryInput.length()){
                    int bit = Character.getNumericValue(binaryInput.charAt(index));
                    r = r & 0xFE |bit;
                    index++;
                }
                if(index<binaryInput.length()){
                    int bit = Character.getNumericValue(binaryInput.charAt(index));
                    g = g & 0xFE |bit;
                    index++;
                }
                if(index<binaryInput.length()){
                    int bit = Character.getNumericValue(binaryInput.charAt(index));
                    b = b & 0xFE |bit;
                    index++;
                }


                //int newPixel = (r,g,b);
                int newPixel = ((a << 24)|(r << 16)|(g << 8)|b);

                bufferedImage.setRGB(i,j,newPixel);


            }


        }

        String newName = "Encoded-"+image.getName();
        File outputFile = new File(saveFilePath,newName);

        try {
            ImageIO.write(bufferedImage,"png",outputFile);
        }
        catch (IOException e){
            e.printStackTrace();
        }



        System.out.println(binaryInput);
        System.out.println("Bytes per pixels"+bytesPerPixel);
        System.out.println("Size of image: "+ sizeOfImage);
        System.out.println("Length of secret text: " + binaryInput.length());

    }

    public String decodeImage(){

        BufferedImage bufferedImage1;
        StringBuilder binaryExtractedText = new StringBuilder();
        int imageWidth = 0;
        int imageHeight = 0;
        try{
            bufferedImage1 = ImageIO.read(saveFilePath);
            imageWidth = bufferedImage1.getWidth();
            imageHeight = bufferedImage1.getHeight();

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

        for(int i = 0; i < imageWidth; i++){
            for(int j = 0; j < imageHeight; j++){


                int currentPixel = bufferedImage1.getRGB(i,j);

                //masking and shifting the rdb values

                int a = (currentPixel >> 24) & 0xff;
                int r = (currentPixel >> 16) & 0xFF;
                int g = (currentPixel >> 8) & 0xFF;
                int b = currentPixel & 0xFF;

                int extractedAlpha = a&1;
                binaryExtractedText.append(extractedAlpha);
                int extractedRed = r&1;
                binaryExtractedText.append(extractedRed);
                int extractedGreen = g&1;
                binaryExtractedText.append(extractedGreen);
                int extractedBlue = b&1;
                binaryExtractedText.append(extractedBlue);


            }




        }

        String binaryText = binaryExtractedText.toString();

        for(int i = 0; i <= binaryExtractedText.length() -8; i+=8){
            String subString =   binaryExtractedText.substring(i,i+8);

            int temp = Integer.parseInt(subString,2);

            if(temp == 0){
                break;
            }

            extractedText.append((char) temp);
        }


        System.out.println("Extracted Text: "+ extractedText.toString());

        CaesarCipher decryptor = new CaesarCipher(extractedText.toString(), key);
        return decryptor.decrypt();

    }



}
