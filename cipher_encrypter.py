import string
import random

class CipherEncrypter:
    def __init__(self):
        self.available_methods = {
            1: ("Caesar Cipher", self.caesar_encrypt),
            #2: ("Substitution Cipher", self.substitution_encrypt),
            2: ("Permutation Cipher", self.permutation_encrypt), #Transposition Cipher
            #3: ("Vigenere Cipher", self.vigenere_encrypt),
            #4: ("Mixed Substitution Cipher", self.mixed_substitution_encrypt)
        }

    def caesar_encrypt(self, plaintext, key):
        """Caesar cipher encryption"""
        """Encrypt text using Caesar cipher with given key."""
        result = ""
        for char in plaintext.lower():
            if char.isalpha():
                shifted = (ord(char) - ord('a') + key) % 26
                result += chr(shifted + ord('a'))
            else:
                result += char
        return result, "Caesar Cipher", key

    def permutation_encrypt(self, plaintext, key=None):
        """
        Permutation cipher encryption
        Uses a random permutation of positions as the key
        """
        # Remove spaces and convert to uppercase for classic implementation
        text = ''.join(char.upper() for char in plaintext if char.isalnum())
        
        # If text length is not a perfect square, pad with 'X'
        size = len(text)
        square_size = int(size ** 0.5) + (1 if size ** 0.5 % 1 != 0 else 0)
        padded_size = square_size * square_size
        
        # Pad the text if necessary
        text = text.ljust(padded_size, 'X')
        
        # Create the matrix
        matrix = [list(text[i:i + square_size]) for i in range(0, padded_size, square_size)]
        
        # Generate random permutation for rows and columns if no key is provided
        if key is None:
            row_perm = list(range(square_size))
            col_perm = list(range(square_size))
            random.shuffle(row_perm)
            random.shuffle(col_perm)
            key = (row_perm, col_perm)
        else:
            row_perm, col_perm = key
            
        """
        For a text "HELLO WORLD", the process might look like:
        Original: HELLO WORLD
        Prepared: HELLOWORLD
        Padded:   HELLOWORLDX (to make a perfect square 4x4)

        Matrix:
        H E L L
        O W O R
        L D X X
        X X X X

        """
        # Apply row permutation
        matrix = [matrix[i] for i in row_perm]
        """
        After row permutation [2,0,3,1]:
        >L >D  X  X
        >H >E >L >L
        X   X  X  X
        >O  W >O  R
        """
        
        # Apply column permutation
        for i in range(square_size):
            temp_col = [matrix[j][i] for j in range(square_size)]
            for j in range(square_size):
                matrix[j][col_perm[i]] = temp_col[j]
        
        """
        After column permutation [1,3,0,2]:
        >D  X >L  X
        >E >L >H >L
        X X  X  X
        W R >O >O
        """

        # Convert back to string
        ciphertext = ''
        for row in matrix:
            ciphertext += ''.join(row)        
        """ 
        Final: DXLXELHLXXXXWROO
        """

        # Format key for display
        key_str = f"Rows: {row_perm}, Cols: {col_perm}"
        
        return ciphertext, "Permutation", key_str
        
    def random_encrypt(self, plaintext):
        """Randomly choose an encryption method"""
        method_id = random.choice(list(self.available_methods.keys()))
        method_name, encrypt_func = self.available_methods[method_id]
        return encrypt_func(plaintext)

    def get_available_methods(self):
        """Return list of available encryption methods"""
        return [(id, name) for id, (name, _) in self.available_methods.items()]