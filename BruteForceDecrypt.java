import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BruteForceDecrypt {

    // Define the alphabet
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    // Load dictionary from file into a set
    public static Set<String> loadDictionary(String dictionaryFile) {
        Set<String> dictionary = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFile))) {
            String word;
            while ((word = br.readLine()) != null) {
                dictionary.add(word.toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading the dictionary file: " + e.getMessage());
        }
        return dictionary;
    }

    // Count valid words in a decrypted text
    public static int countValidWords(String text, Set<String> dictionary) {
        String[] words = text.split("\\W+"); // Split by non-word characters
        int validWordCount = 0;

        for (String word : words) {
            if (dictionary.contains(word.toLowerCase())) {
                validWordCount++;
            }
        }
        return validWordCount;
    }

    public static void main(String[] args) {
        String cipherFile = "ciphertext.txt"; // File containing the ciphertext
        String dictionaryFile = "dictionary.txt"; // File containing the dictionary
        String ciphertext = "";

        // Load the dictionary
        Set<String> dictionary = loadDictionary(dictionaryFile);

        // Read the ciphertext from the file
        try (BufferedReader br = new BufferedReader(new FileReader(cipherFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            ciphertext = sb.toString();
        } catch (IOException e) {
            System.err.println("Error reading the ciphertext file: " + e.getMessage());
            return;
        }

        // Maximum key length to test
        int maxKeyLength = 1; // Adjust as needed
        System.out.println("Brute-forcing with keys up to length " + maxKeyLength + "...");

        // Perform brute force
        for (int length = 1; length <= maxKeyLength; length++) {
            bruteForce("", length, ciphertext);
        }

    }

    // Recursive method to generate keys and test decryption
    private static void bruteForce(String currentKey, int maxLength, String ciphertext) {
        if (currentKey.length() == maxLength) {
            // Decrypt and print the result for this key
            String decryptedText = decrypt(ciphertext, currentKey);
            System.out.println("Key: " + currentKey + " -> Decrypted Text: " + decryptedText);
            return;
        }

        for (char c : ALPHABET) {
            bruteForce(currentKey + c, maxLength, ciphertext);
        }
    }

    // Decrypt the ciphertext using the given key
    private static String decrypt(String ciphertext, String key) {
        StringBuilder decrypted = new StringBuilder();
        int keyIndex = 0;

        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int shifted = (c - base - (key.charAt(keyIndex % key.length()) - 'a') + 26) % 26;
                decrypted.append((char) (shifted + base));
                keyIndex++;
            } else {
                decrypted.append(c); // Keep non-alphabetic characters unchanged
            }
        }

        return decrypted.toString();
    }
}

