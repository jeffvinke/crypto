// Jeff Vinke
// CSC 440
/*
 1. (50 points) Meet in the middle attack on 2DES Implement the meet-in-the-middle attack on 2DES, where you use the simplified DES from the textbook. 
    Follow the instructions on pp. 143-144. Because you're using the simplifed DES, which has a key containing 9 bits, you will need to generate two 
    lists, each of length 512.

You should have the DES encryption function implemented in assignment 4 and you will now need to implement the decryption function. Write the program 
    so that it outputs a list of possible key pairs. Here are two sets ofinputs on which to test your program:

Plaintext: 101, ciphertext: 199
Plaintext: 110, ciphertext: 2816

Each of those inputs will produce a list of about 50 possible key pairs Write your program so that constructs lists of key pairs for each input and
    then finds and prints the key pair(s) that appear on both lists.
*/

import java.security.InvalidParameterException;
import java.util.Hashtable;

public class Sdes {
    private static int[][] s1 = {
        {5,2,1,6,3,4,7,0},
        {1,4,6,2,0,7,5,3}
    };

    private static int[][] s2 = {
        {4,0,6,5,7,1,3,2},
        {5,3,0,7,6,2,1,4}
    };

    public static void main(final String[] args) {
        int text;
        int initialKey;

        if (args != null && args.length == 2) {
            text = Integer.parseInt(args[0]);
            initialKey = Integer.parseInt(args[1]);
        } else {
            text = Integer.parseInt("100010110101", 2);
            initialKey = Integer.parseInt("111000111", 2);
        }

       if (text < 0 || text > 4095) {
           throw new InvalidParameterException("Parameter plaintext must be between 0 and 4095");
       }
       if (initialKey < 0 || initialKey > 511) {
           throw new InvalidParameterException("Key must be between 0 and 511");
       }

       final int cipherText = encrypt(text,initialKey, true);
       System.out.println("");
       
       decrypt(cipherText, initialKey, true);
       System.out.println("");

       meetInTheMiddle(101, 199);
       System.out.println("");

       meetInTheMiddle(110, 2816);
    }

    public static void meetInTheMiddle(int plainText, int cipherText) {
        System.out.println("> Performing meet in the middle attack..");
        System.out.println("    > Plaintext  -> " + plainText);
        System.out.println("    > Ciphertext -> " + cipherText);

        // eve wants to find key 1, key 2.
            // build list of keys
        Hashtable<Integer, Integer> encryptedTable = new Hashtable<Integer, Integer>();
        Hashtable<Integer, Integer> decryptedTable = new Hashtable<Integer, Integer>();
        
        int[] keys = new int[512];
        for (int i = 0; i < 512; i++) {
            keys[i] = i;
        }

        // compute and store
            // Ek(m) for all possible keys of k
        int[] encrypted = new int[512];
        for (int i = 0; i < 512; i++) {
            encrypted[i] = encrypt(plainText, keys[i], false);
            encryptedTable.put(encrypted[i], keys[i]);
        }

        // compute and store 
            // Dk(c) for all possible keys of k
        int[] decrypted = new int[512];
        for (int i = 0; i < 512; i++) {
            decrypted[i] = decrypt(cipherText, keys[i], false);
            decryptedTable.put(decrypted[i], keys[i]);
        }

        // compare two lists
        for (int i = 0; i < 512; i++) {
            for (int j = 0; j < 512; j++) {
                if (encrypted[i] == decrypted[j]) {
                    System.out.println(">>> Possible match found ->  k1 =" + encryptedTable.get(encrypted[i]) + 
                        " ; k2 = " + decryptedTable.get(decrypted[j]));
                }
            }
        }
    }

    public static int encrypt(int plainText, int key, Boolean showOutput) {
        Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
        Hashtable<Integer, Integer> keys = new Hashtable<Integer, Integer>(); 
        
        int l0 = plainText >> 6;
        int r0 = plainText & 0b111111;

        if (showOutput)
            System.out.println("> Input Plaintext: " + plainText);

        ht.put("l0", l0);
        ht.put("r0", r0);

        keys = getRoundKey(key);
        int left = 0;
        int right = 0;

        for (int round = 1; round < 5; round++) {
            
            int roundKey = keys.get(round);
            int previousRight = ht.get("r"+(round-1));
            int previousLeft = ht.get("l"+(round-1));

            int v = expand(previousRight) ^ roundKey;

            int leftBits = sbox1(v);
            int rightBits = sbox2(v);

            right = ((leftBits << 3) + rightBits) ^ previousLeft;
            left = previousRight;

            ht.put("l"+round, left);
            ht.put("r"+round, right);
            
            if (showOutput)
                System.out.println("   >>> L" + round + ": " + Integer.toHexString(left) + " (" + left + "); R" + round + ":" +
                                 Integer.toHexString(right) + " (" + right +  ");  Key="+ Integer.toHexString(roundKey));
        }
        
        int out = (left << 6) | right;

        if (showOutput)
            System.out.println("> Ciphertext: " + out);

        return out;
    }

    public static int decrypt(int cipherText, int key, Boolean showOutput) {
        Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
        Hashtable<Integer, Integer> keys = new Hashtable<Integer, Integer>(); 

        ht.put("l4", (cipherText >> 6));
        ht.put("r4", (cipherText & 0b111111));

        keys = getRoundKey(key);
        int left = 0;
        int right = 0;

        for (int round = 4; round > 0; round--) {
            
            int roundKey = keys.get(round);
            right = ht.get("l"+(round));

            int v = expand(right) ^ roundKey;

            int leftBits = sbox1(v);
            int rightBits = sbox2(v);

            left = ht.get("r"+(round)) ^ ((leftBits << 3) + rightBits);

            ht.put("l"+(round-1), left);
            ht.put("r"+(round-1), right);
            
            if (showOutput)
                System.out.println("   >>> L" + round + ": " + Integer.toHexString(left) + " (" + left + "); R" + round + ":" +
                                 Integer.toHexString(right) + " (" + right +  ");  Key="+ Integer.toHexString(roundKey));
        }
        
        int out = (left << 6) | right;

        if (showOutput)
            System.out.println("> Decrypted Plaintext: " + out);

        return out;
    }

    private static Hashtable<Integer, Integer> getRoundKey(int key) {
        int roundKey = key;
        Hashtable<Integer, Integer> keys = new Hashtable<Integer, Integer>();

        for (int i = 1; i < 5; i++) {
            int round = roundKey >> 1;

            keys.put(i, round);
            
            int bitOffset = roundKey >> 8;
            
            roundKey = (roundKey << 1) | bitOffset;
            roundKey = roundKey & 0b111111111;
        }

        return keys;
    }

    public static int expand(int value) {
        int bit4 = (value >> 3) & 1;       
        int bit3 = (value >> 2) & 1;
        int middle = ((bit3 << 1) | bit4);
        
        middle = middle << 2 | middle;
        int endBits = value & 3;
        
        return ((((value >> 4) << 4) | middle) << 2) | endBits;
    }

    public static int sbox1(int vector) {
        // left 
        int left = (vector & 0xf0) >> 4;
        return  s1[left >>3][left & 0x07];
    }

    public static int sbox2(int vector) {
        // right
        int right = vector & 0x0f;
        return  s2[right >>3][right & 0x07];
    }

    public static void dump(int value, String message) {
        String binaryString = Integer.toBinaryString(value);
        System.out.println(message + " --> " + binaryString);
    }
}
