import java.security.SecureRandom;
import java.util.Scanner;

public class RSAExemple {
    private int n,d,e;
    private int bitLength = 1024;
    private SecureRandom random;

    //Constructor to generate the public and private keys
    public RSAExemple(int p, int q, int e){
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
            RSAExemple rsa = new RSAExemple(p, q, e);
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
                System.out.println("Intercept the message exercise is not implemented yet.");
                break;
            case 3:
                // Implement the logic for encrypting and decrypting binary numbers
                System.out.println("Encrypt and decrypt binary numbers exercise is not implemented yet.");
                break;
            case 4:
                // Implement the logic for ASCII encrypt and decrypt using "HELLO"
                System.out.println("ASCII encrypt and decrypt using 'HELLO' exercise is not implemented yet.");
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