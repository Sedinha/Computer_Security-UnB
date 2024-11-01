import os
import textwrap
from cipher_breaker import CipherBreaker
from cipher_encrypter import CipherEncrypter

class CryptoCLI:
    def __init__(self):
        self.terminal_width = os.get_terminal_size().columns
        self.breaker = CipherBreaker()
        self.encrypter = CipherEncrypter()

    def clear_screen(self):
        os.system('cls' if os.name == 'nt' else 'clear')

    def center_text(self, text):
        return text.center(self.terminal_width)

    def print_welcome_message(self):
        welcome_message = "WELCOME TO MY ETHICAL HACKER COURSE"
        print("\n" * 3)  # Add some vertical space
        print(self.center_text(welcome_message))
        print("\n" * 2)  # Add some vertical space

    def encrypt_text(self):
        print("\nText Encryption")
        print("==============")

        plaintext = input("\nEnter the text to encrypt: ").strip()
        if not plaintext:
            print("Error: Text cannot be empty!")
            return
        
        print("\nAvailable Encryption Methods:")
        print("1. Random (System will choose randomly)")
        for method_id, method_name in self.encrypter.get_available_methods():
            print(f"{method_id + 1}. {method_name}")
        
        choice = input("\nChoose an encryption method (1 for random): ")

        try:
            if choice == '1':
                encrypted, method, key = self.encrypter.random_encrypt(plaintext)
                print(f"\nRandomly chosen method. Method: {method}")
            elif choice == '2':
                key = input("Enter the key (shift amount) or press Enter to define key=3: ")
                if key == '':
                    key = '3'
                encrypted, method, key = self.encrypter.caesar_encrypt(plaintext, int(key))
                print(f"\nChosen method: {method}")
            elif choice == '3':
                key = input("Enter the key (row permutation, column permutation) or press Enter to define key=(2,1,0,3) (1,3,0,2): ")
                if key == '':
                    key = ([2,1,0,3], [1,3,0,2])
                else:
                    key = key.split(',')
                    key = ([int(x) for x in key[0]], [int(x) for x in key[1]])
                encrypted, method, key = self.encrypter.permutation_encrypt(plaintext, key)
                print(f"\nChosen method: {method}")
            else:
                print("Invalid choice! ")
                return None, None
            
            print(f"Encryption sucessful!")
            print(f"Encryption method: {method}")
            print(f"Key: {key}")
            print(f"Encrypted text: {encrypted}")
            return encrypted, method
        
        except Exception as e:
            print(f"An error occurred: {e}")
            return None, None

    def try_decrypt(self, ciphertext):
        while True:
            print("\nCipher Breaking Attempts:")
            print("1. Try Frequency Analysis")
            print("2. Try Brute Force")
            print("3. Enter new text")
            print("4. Exit")
            
            choice = input("\nChoose a method (1-4): ")
            
            if choice == '1':
                result = self.breaker.break_by_frequency(ciphertext)
                print("\n=== Frequency Analysis Results ===")
                print(f"Detected key: {result['key']}")
                print(f"Decrypted text: {result['plaintext']}")
                print(f"Time taken: {result['time']:.4f} seconds")
                
            elif choice == '2':
                result = self.breaker.break_by_brute_force(ciphertext)
                print("\n=== Brute Force Results ===")
                print(f"Time taken: {result['time']:.4f} seconds")
                print("\nAll possible combinations:")
                for key, plaintext in result['all_results']:
                    print(f"\nKey {key:2d}: {plaintext}")
                    
            elif choice == '3':
                return True  # Return to main input loop
                
            elif choice == '4':
                return False  # Exit program
                
            else:
                print("Invalid choice. Please try again.")
            
            input("\nPress Enter to continue...")

    def run(self):
        while True:
            self.clear_screen()
            self.print_welcome_message()
            
            encrypted_text, method = self.encrypt_text()
            if encrypted_text is None:
                input("\nPress Enter to try again...")
                continue

            print("\nNow let's try to break the cipher!")
            input("\nPress Enter to continue...")
                
            if not self.try_decrypt(encrypted_text):
                break

def main():
    cli = CryptoCLI()
    cli.run()

if __name__ == "__main__":
    main()