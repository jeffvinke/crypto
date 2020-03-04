#Jeff Vinke
#CSC 440

# CSPRNG Write a program that:

# Takes as input a plaintext that is a sequence of bits represented as a string of zeroes and ones
# Using the Blum-Blum-Shub CSPRNG written in the last assignment and initialized as shown in the 
#   textbook, generate a sequence of key bits represented as a string of zeroes and ones with the
#   same length as the input string

# Create a ciphertext string of ones and zeroes where each bit is the exclusive-or of the 
#   corresponding bits in the plaintext and key strings

# Print the ciphertext string as a sequence of bits in groups of 4 

# You can test your program with this plaintext string, which is the word SECURE in ASCII:

# 0101 0011 0100 0101 0100 0011 0101 0101 0101 0010 0100 0101


import sys
import random
import math
import textwrap

def main():

    p = 24672462467892469787
    q = 396736894567834589803
    n = p * q

    # input = "SECURE"
    print("Enter a binary string or press enter to default.")
    data = input(">>>")
    if not data:
        data = "0101 0011 0100 0101 0100 0011 0101 0101 0101 0010 0100 0101"
    
    print("p = " + str(p) + "  q= " + str(q))
    print("n = " + str(n))
    print()

    print("Stream cipher input: ", data)
    data = data.replace(' ', '')


    x = 873245647888478349013 

    x0 = (x*x) % n
    s_x0 = str(x0)

    result = list()
    # xVals = list()
    # xVals.append(x0)
    textwrap.wrap(data, 8)
    index = 0
    for i in textwrap.wrap(data, 8):
        ival = int(i, 2)

        #print("data:", i)
        current_prng_stream = ord(s_x0[index])
        #print("xor against: ", s_x0[index])
        #print("xorer:", str(bin(current_prng_stream)[2:].zfill(8) ))

        xor_results = ival ^ current_prng_stream
        # char(xor_results)
        #print ("      ", str(bin(xor_results)[2:].zfill(8) ))
        result.append(str(bin(xor_results)[2:].zfill(8) ))
        index += 1

    out = "".join(result)
    ciphertext_chunks = [out[i:i+4] for i in range(0, len(out), 4)]
    print(str(ciphertext_chunks))

main()
