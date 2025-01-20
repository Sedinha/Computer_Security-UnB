import java.security.SecureRandom;
import java.util.Scanner;

public class RSAExample {
    private int n, d, e;
    private int bitLength = 1024;
    private SecureRandom random;

    //Constructor to generate the public and private keys
    public RSAExample(int p, int q, int e){
        //Initialize a secure random number generator
        //random = new SecureRandom();

        //Generate two large prime numbers
        ///int p = int.probablePrime(bitLength, random);
        //int q = int.probablePrime(bitLength, random);

        //Calculate n = p * q
        n = p*q;

        //Calculate φ(n) = (p-1) * (q-1)
        int phi = (p-1)*(q-1);

        //Choose e (public exponent)
        //e = int.valueOf(65537);
        // Set e(public exponent)
        this.e = e;

        // Check if e is coprime with φ(n)
        if (gcd(e, phi) != 1) {
            throw new IllegalArgumentException("e must be coprime with φ(n)");
        }
        //Calculate d (private exponent) = e^(-1) mod  φ(n)
        d = modInverse(e, phi);
    }
    // Constructor for intercepting messages
    public RSAExample(int n, int e) {
        this.n = n;
        this.e = e;
        
        // For n = 35, we know p = 5 and q = 7
        int p = 5;
        int q = 7;
        
        // Calculate φ(n) = (p-1) * (q-1)
        int phi = (p - 1) * (q - 1);  // phi = 24
        
        // Calculate private key d
        this.d = modInverse(e, phi);
    }

    // Method to calculate the greatest common divisor (GCD) using Euclidean Algorithm
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    // Method to calculate modular inverse using Extended Euclidean Algorithm
    private int modInverse(int a, int m) {
        int m0 = m, t, q;
        int x0 = 0, x1 = 1;

        if (m == 1)
            return 0;

        // Apply extended Euclidean Algorithm
        while (a > 1) {
            // q is quotient
            q = a / m;
            t = m;

            // m is remainder now, process same as Euclid's algo
            m = a % m;
            a = t;
            t = x0;

            x0 = x1 - q * x0;
            x1 = t;
        }

        // Make x1 positive
        if (x1 < 0)
            x1 += m0;

        return x1;
    }
    // Method to perform modular exponentiation
    private int modPow(int base, int exp, int mod) {
        int result = 1;
        for (int i = 0; i < exp; i++) {
            result = (result * base) % mod;
        }
        return result;
    }

    //Encrypt a M (message)
    public int encrypt(int message) {
        // c = M^e mod n
        return modPow(message, e, n);
    }

    // Decrypt a message
    public int decrypt(int cipherText) {
        // M = c^d mod n
        return modPow(cipherText, d, n);
    }

    // Method to run a single test
    public static void runTest(int p, int q, int e, int message) {
        try {
            RSAExample rsa = new RSAExample(p, q, e);
            System.out.println("Testing with p = " + p + ", q = " + q + ", e = " + e + ", M = " + message);
            System.out.println("Original Message : " + message);

            int encryptedMessage = rsa.encrypt(message);
            System.out.println("Encrypted Message : " + encryptedMessage);

            int decryptedMessage = rsa.decrypt(encryptedMessage);
            System.out.println("Decrypted Message : " + decryptedMessage);
            System.out.println();
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
       
    // Method to handle message interception (Case 2)
    private static void interceptMessage() {
        System.out.println("\nIntercepting encrypted message...");
        System.out.println("Given information:");
        System.out.println("- Encrypted message (C) = 10");
        System.out.println("- Public key (e) = 25");
        System.out.println("- Modulus (n) = 35");
        
        // Create RSA instance with the known public key
        RSAExample rsa = new RSAExample(35, 25);
        
        // Decrypt the intercepted message
        int C = 10;
        int M = rsa.decrypt(C);
        
        System.out.println("\nDecryption steps:");
        System.out.println("1. Factor n = 35 into p = 5 and q = 7");
        System.out.println("2. Calculate φ(n) = (p-1)(q-1) = (4)(6) = 24");
        System.out.println("3. Calculate private key d where d * e ≡ 1 (mod φ(n))");
        System.out.println("4. Use private key to decrypt: M = C^d mod n");
        System.out.println("\nResult:");
        System.out.println("Decrypted Message (M) = " + M);
    }

    private static void binaryRSAExample(){
        System.err.println("\nBinary RSA Encryption and Decryption");
        System.out.println("Given information:");
        System.out.println("- Binary message (m) = 0111001");
        System.out.println("- p = 23");
        System.out.println("- q = 23");
        System.out.println("- e = 23");

        // Convert binary message to decimal
        int decimalMsg = Integer.parseInt("0111001", 2);

        System.out.println("\nConverting binary message to decimal:");
        System.out.println("Binary message (m) = 0111001 (base 2) = " + decimalMsg + " (base 10)");

        // Create RSA instance with the given values
        RSAExample rsa = new RSAExample(11, 23, 3);

        // Encrypt the message
        int encryptedMsg = rsa.encrypt(decimalMsg);

        // Decrypt the message
        int decryptedMsg = rsa.decrypt(encryptedMsg);

        //Convert decrypted decimal back to binary
        String binaryMsg = Integer.toBinaryString(decryptedMsg);
        while (binaryMsg.length() < 7){ // Pad with zeros to get 7 bits
            binaryMsg = "0" + binaryMsg;
        }
        System.out.println("\nStep 2: RSA Parameters Verification");
        System.out.println("n = p * q = 11 * 23 = " + (11 * 23));
        System.out.println("φ(n) = (p-1)(q-1) = 10 * 22 = " + (10 * 22));
        
        System.out.println("\nResults:");
        System.out.println("Original Binary Message: " + binaryMsg);
        System.out.println("Decimal Value: " + decimalMsg);
        System.out.println("Encrypted Message (decimal): " + encryptedMsg);
        System.out.println("Decrypted Message (decimal): " + decryptedMsg);
        System.out.println("Decrypted Binary Message: " + binaryMsg);

    }

    // ASCII RSA Exemple
    private static void asciiRSAExemple(){
        System.out.println("\nASCII RSA Encryption and Decryption for 'HELLO'");
        System.out.println("Given information:");
        System.out.println("- ASCII message (m) = 72 69 76 76 79");
        System.out.println("- p = 11, q = 17, e = 7");

        // Create RSA instance with the given values
        RSAExample rsa = new RSAExample(11, 17, 7);
        String message = "HELLO";

        // Convert each character to ASCII
        System.out.println("\nStep 1: Convert each character to ASCII");
        System.out.println("ASCII Values:");
        for (char c: message.toCharArray()){
            System.out.println(c + " = " + (int)c);
        }
        
        // Arrays to store encrypted and decrypted values
        int[] asciiValues = new int[message.length()];
        int[] encryptedValues = new int[message.length()];
        int[] decryptedValues = new int[message.length()];

        // Encrypt ASCII value
        System.out.println("\nStep 2: Encrypt ASCII values");
        for (int i = 0; i < message.length(); i++){
            asciiValues[i] = (int)message.charAt(i);
            encryptedValues[i] = rsa.encrypt(asciiValues[i]);
            System.out.println("Encrypting" + message.charAt(i) + "ASCII: " + asciiValues[i] + " -> " + encryptedValues[i]);
        }

        // Decrypt ASCII value
        System.out.println("\n Decrypt ASCII Values");
        StringBuilder decryptedMessage = new StringBuilder();
        for (int i = 0; i < encryptedValues.length; i++){
            decryptedValues[i] = rsa.decrypt(encryptedValues[i]);
            char decryptedChar = (char)decryptedValues[i];
            decryptedMessage.append(decryptedChar);
            System.out.println("Decrypting " + encryptedValues[i] + " -> " + decryptedValues[i] + " -> " + decryptedChar);
        }

        System.out.println("\nRSA Results:");
        System.out.println("Original Message: " + message);
        System.out.println("Encrypted Values: ");
        for (int val:encryptedValues){
            System.out.print(val + " ");
        }
        System.out.println("\nDecrypted Message: " + decryptedMessage.toString());

        //Verification
        if (message.equals(decryptedMessage.toString())){
            System.out.println("\nVerification: Successful! Original and decrypted messages match.");
        } else {
            System.out.println("\nVerification: Failed! Messages don't match.");
        }

    }

    // Method to display exercises and run tests
    public static void displayExercises() {
        System.out.println("Select an exercise to run:");
        System.out.println("1) Encrypt and Decrypt exercises");
        System.out.println("2) Intercept the message");
        System.out.println("3) Encrypt and decrypt binary numbers");
        System.out.println("4) ASCII encrypt and Decrypt using 'HELLO'");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the choice: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("a) p = 3, q = 11, e = 7, M = 5");
                System.out.println("b) p = 5, q = 7, e = 3, M = 4");
                System.out.println("c) p = 11, q = 13, e = 17, M = 9");
                System.out.println("d) p = 17, q = 19, e = 5, M = 12");
                System.out.println("e) p = 23, q = 29, e = 11, M = 15");
                System.out.println();

                System.out.println("Select an option (a-e):");
                char subChoice = scanner.next().charAt(0);

                switch (subChoice) {
                    case 'a':
                        runTest(3, 11, 7, 5);
                        break;
                    case 'b':
                        runTest(5, 7, 3, 4);
                        break;
                    case 'c':
                        runTest(11, 13, 17, 9);
                        break;
                    case 'd':
                        runTest(17, 19, 5, 12);
                        break;
                    case 'e':
                        runTest(23, 29, 11, 15);
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                        break;
                }
                break;
            case 2:
                // Implement the logic for intercepting the message
                interceptMessage();
                break;
            case 3:
                // Implement the logic for encrypting and decrypting binary numbers
                binaryRSAExample();
                break;
            case 4:
                asciiRSAExemple();
                break;
            default:
                System.out.println("Invalid choice. Please select a valid exercise number.");
                break;
        }

        scanner.close();
    }

    public static void main(String[] args) {
        System.out.println("-----------Exercise List RSA Algorithm---------");
        displayExercises();
    }
}