public class SDES {
    // Permutation Tables
    private static final int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private static final int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
    private static final int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
    private static final int[] IP_INVERSE = {4, 1, 3, 5, 7, 2, 8, 6};
    private static final int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
    private static final int[] P4 = {2, 4, 3, 1};

    // S-Boxes
    private static final int[][] S0 = {
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 2}
    };

    private static final int[][] S1 = {
            {0, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3}
    };

    private String key1;
    private String key2;

    // Constructor (Key)
    public SDES(String key) {
        if (key.length() != 10) {
            throw new IllegalArgumentException("Key must be 10 bits long");
        }
        System.out.println("Initializing SDES with key: " + key);
        generateKeys(key);
    }

    // Key Generation
    private void generateKeys(String key) {
        System.out.println("***** Starting Generate Keys Process *****");
        // Step 1: Apply P10 permutation
        String permutedKey = permute(key, P10);
        System.out.println("After P10 permutation: " + permutedKey);
        
        // Step 2: Split the 10-bit key into two halves
        String leftHalf = permutedKey.substring(0, 5);
        String rightHalf = permutedKey.substring(5, 10);
        System.out.println("Left half " + leftHalf + ", Right half: " + rightHalf);
        
        // First round - LS-1
        leftHalf = leftShift(leftHalf, 1);
        rightHalf = leftShift(rightHalf, 1);
        System.out.println("After LS-1: Left half: " + leftHalf + ", Right half: " + rightHalf);

        // Generate first key (K1)
        key1 = permute(leftHalf + rightHalf, P8);
        System.out.println("Generated key1 (K1): " + key1);

        // Second round - LS -2
        leftHalf = leftShift(leftHalf, 2);
        rightHalf = leftShift(rightHalf, 2);
        System.out.println("After LS-2: Left half: " + leftHalf + ", Right half: " + rightHalf);
    
        // Generate second key (K2)
        key2 = permute(leftHalf + rightHalf, P8);
        System.out.println("Generated key2 (K2): " + key2);
        System.out.println("***** Finishing Generate Keys Process *****");
    }

    // Encryption method
    public String encrypt(String plaintext) {
        if (plaintext.length() != 8) {
            throw new IllegalArgumentException("Plaintext must be 8 bits long");
        }
        System.out.println("***** Starting Encryption Process *****");
        System.out.println("Encrypting plaintext: " + plaintext);
        
        // Initial Permutation
        String current = permute(plaintext, IP);
        System.out.println("After initial permutation (IP): " + current);
        
        // Split into two halves
        String left = current.substring(0, 4);
        String right = current.substring(4, 8);
        System.out.println("Initial split: Left: " + left + ", Right: " + right);
        
        // First round
        String temp = right;
        right = xor(left, functionF(right, key1));
        left = temp;
        System.out.println("After first round with K1: Left: " + left + ", Right: " + right);
        
        // Second round
        temp = right;
        right = xor(left, functionF(right, key2));
        left = temp;
        System.out.println("After second round with K2: Left: " + left + ", Right: " + right);
        
        // Final permutation
        String encrypted = permute(right + left, IP_INVERSE);
        System.out.println("After final permutation (IP-1): " + encrypted);

        System.out.println("***** Ending Encryption Process *****");
        
        return encrypted;
    }

    // Decryption method
    public String decrypt(String ciphertext) {
        if (ciphertext.length() != 8) {
            throw new IllegalArgumentException("Ciphertext must be 8 bits long");
        }

        System.out.println("***** Starting Decryption Process *****");

        System.out.println("Decrypting ciphertext: " + ciphertext);
        
        // Initial Permutation
        String current = permute(ciphertext, IP);
        System.out.println("After initial permutation (IP): " + current);
        
        // Split into two halves
        String left = current.substring(0, 4);
        String right = current.substring(4, 8);
        System.out.println("Initial split: Left: " + left + ", Right: " + right);
        
        // First round (Using K2 instead of K1 first) 
        String temp = right;
        right = xor(left, functionF(right, key2));
        left = temp;
        System.out.println("After first round with K2: Left: " + left + ", Right: " + right);
        
        // Second round (Now using K1)
        temp = right;
        right = xor(left, functionF(right, key1));
        left = temp;
        System.out.println("After second round with K1: Left: " + left + ", Right: " + right);
        
        // Final permutation
        String decrypted = permute(right + left, IP_INVERSE);
        System.out.println("After final permutation (IP-1): " + decrypted);

        System.out.println("***** Ending Decryption Process *****");
        
        return decrypted;
    }


    // F-function
    private String functionF(String input, String key) {
        // Expansion
        String expanded = permute(input, EP);
        
        // XOR with key
        String xored = xor(expanded, key);
        
        // Split for S-box substitution, into two 4-bits parts
        String left = xored.substring(0, 4);
        String right = xored.substring(4, 8);
        
        // S-box substitution
        String sbox1 = sBox(left, S0);
        String sbox2 = sBox(right, S1);
        
        // P4 permutation
        return permute(sbox1 + sbox2, P4);
    }

    // Helper Methods
    private String leftShift(String bits, int n) {
        return bits.substring(n) + bits.substring(0, n);
    }

    private String permute(String input, int[] permutation) {
        StringBuilder output = new StringBuilder();
        for (int p : permutation) {
            output.append(input.charAt(p - 1));
        }
        return output.toString();
    }

    private String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }

    private String sBox(String input, int[][] sbox) {
        int row = Integer.parseInt(input.charAt(0) + "" + input.charAt(3), 2);
        int col = Integer.parseInt(input.charAt(1) + "" + input.charAt(2), 2);
        String result = Integer.toBinaryString(sbox[row][col]);
        return result.length() == 1 ? "0" + result : result;
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            // Test the implementation
            SDES sdes = new SDES("1010000010");
            String plaintext = "11010111";
            String ciphertext = sdes.encrypt(plaintext);
            SDES sdesDecrypt = new SDES("1010000010");
            String decrypted = sdesDecrypt.decrypt(ciphertext);
            
            System.out.println("Plaintext:  " + plaintext);
            System.out.println("Key:        1010000010");
            System.out.println("Ciphertext: " + ciphertext);
            System.out.println("Decrypted:  " + decrypted);
            System.out.println(ciphertext.equals(sdes.encrypt(plaintext)) ? "Encryption successful" : "Encryption failed");
            
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}