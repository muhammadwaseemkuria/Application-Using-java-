package org.example;

public class CaesarCipher {

    private int key;
    private String inputText;

    public CaesarCipher(String inputText, int keyValue) {
        // Normalize key to range 0–25
        this.key = ((keyValue % 26) + 26) % 26;
        this.inputText = inputText;
    }

    // Encryption
    public String encrypt() {

        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < inputText.length(); i++) {

            char ch = inputText.charAt(i);

            if (Character.isUpperCase(ch)) {

                char encryptedChar =
                        (char) ((ch - 'A' + key) % 26 + 'A');

                encryptedText.append(encryptedChar);

            } else if (Character.isLowerCase(ch)) {

                char encryptedChar =
                        (char) ((ch - 'a' + key) % 26 + 'a');

                encryptedText.append(encryptedChar);

            } else {
                // Keep spaces and symbols unchanged
                encryptedText.append(ch);
            }
        }

        return encryptedText.toString();
    }

    // Decryption
    public String decrypt() {

        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < inputText.length(); i++) {

            char ch = inputText.charAt(i);

            if (Character.isUpperCase(ch)) {

                char decryptedChar =
                        (char) ((ch - 'A' - key + 26) % 26 + 'A');

                decryptedText.append(decryptedChar);

            } else if (Character.isLowerCase(ch)) {

                char decryptedChar =
                        (char) ((ch - 'a' - key + 26) % 26 + 'a');

                decryptedText.append(decryptedChar);

            } else {
                decryptedText.append(ch);
            }
        }

        return decryptedText.toString();
    }

}