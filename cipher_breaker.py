import string
from collections import Counter
import time

class CipherBreaker:
    def __init__(self):
        # English language letter frequencies (approximate percentages)
        self.english_frequencies = {
            'e': 12.7, 't': 9.1, 'a': 8.2, 'o': 7.5, 'i': 7.0, 'n': 6.7, 's': 6.3,
            'h': 6.1, 'r': 6.0, 'd': 4.3, 'l': 4.0, 'c': 2.8, 'u': 2.8, 'm': 2.4,
            'w': 2.4, 'f': 2.2, 'g': 2.0, 'y': 2.0, 'p': 1.9, 'b': 1.5, 'v': 1.0,
            'k': 0.8, 'j': 0.15, 'x': 0.15, 'q': 0.10, 'z': 0.07
        }
        
    def caesar_decrypt(self, ciphertext, key):
        """Decrypt text using Caesar cipher with given key."""
        result = ""
        for char in ciphertext.lower(): 
            if char in string.ascii_lowercase: # Only process letters a-z
                shifted = (ord(char) - ord('a') - key) % 26  # Calculate shift by converting to 0-25, subtracting key, then wrapping
                #for exemple ord('d') = 100 so if we want to convert it to 0-25 we do 100 - 97 (that is our first letter 'a') = 3
                result += chr(shifted + ord('a'))
            else:
                result += char
        return result

    def calculate_frequency_score(self, text):
        """Calculate how well the letter frequencies match English."""
        freq = Counter(filter(str.isalpha, text.lower()))
        text_len = sum(freq.values())
        
        if text_len == 0:
            return float('inf')
            
        score = 0
        for char, count in freq.items():
            observed_freq = (count / text_len) * 100
            expected_freq = self.english_frequencies.get(char, 0)
            score += abs(observed_freq - expected_freq)
        return score

    def break_by_frequency(self, ciphertext):
        """Break cipher using frequency analysis."""
        start_time = time.time()
        best_score = float('inf')
        best_key = 0
        best_plaintext = ""

        for key in range(26):
            plaintext = self.caesar_decrypt(ciphertext, key)
            score = self.calculate_frequency_score(plaintext)
            
            if score < best_score:
                best_score = score
                best_key = key
                best_plaintext = plaintext

        end_time = time.time()
        return {
            'key': best_key,
            'plaintext': best_plaintext,
            'time': end_time - start_time
        }

    def break_by_brute_force(self, ciphertext):
        """Break cipher using brute force."""
        start_time = time.time()
        results = []
        
        for key in range(26):
            plaintext = self.caesar_decrypt(ciphertext, key)
            results.append((key, plaintext))
            
        end_time = time.time()
        return {
            'all_results': results,
            'time': end_time - start_time
        }