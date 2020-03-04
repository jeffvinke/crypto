# Jeff Vinke
# CSC 440

#In this problem you are to get your hands dirty doing some programming.
# Write some code that creates a new alphabet {A, C, G, T }. For example,
# this alphabet could correspond to the four nucleotides adenine, cytosine,
# guanine, and thymine, which are the basic building blocks of DNA and
# RNA codes. Associate the letters A, C, G, T with the numbers 0, 1, 2, 3,
# respectively.

# (a) Using the shift cipher with a shift of 1, encrypt the following sequence
#     of nucleotides which is taken from the beginning of the thirteenth
#       human chromosome:
#       GAATTCGCGGCCGCAATTAACCCTCACTAAAGGGATCT
#       CTAGAACT.

# (b) Write a program that performs affine ciphers on the nucleotide alphabet. What restrictions are there on the affine cipher?

# answer: a and m must be relatively prime


def main():


    alphabet = "ACGT"
    plainText = "GAATTCGCGGCCGCAATTAACCCTCACTAAAGGGATCTCTAGAACT"
    a = 5
    b = 7

    print("PlainText  - " + plainText)

    plainTextCharacters = list(plainText)
    alphabetCharacters = list(alphabet)

    for index in range (0, len(plainText)):
        plainTextIndex = alphabetCharacters.index(plainTextCharacters[index])
        
        cipherIndex = ( (plainTextIndex * a) + b) % len(alphabet)
        plainTextCharacters[index] = alphabetCharacters[cipherIndex]
        

    print("Ciphertext - " + "".join(plainTextCharacters))

main()