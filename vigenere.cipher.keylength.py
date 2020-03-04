# Jeff Vinke
# CSC 440

#(10 points) 7. Write a program based on the following pseudocode to determine the likely length of the key used to create the above ciphertext. This is the technique described in section 2.3.1.

import sys

def main():
  cipherText = """QHDLXNQLYNGAIGWBCERJFEARNIBKXUSVGZXKYNPXXTKGAATZRQCRFYIDCCLYXHUQXEIXFAFGEAMMAL
YRGAYXQMTGACDJSYRTLEXUVRVIYFFEGXFKOYSPHGBBYTRESOXUNTXXAKLUAWYDINAAWCZWIFVMCROI
UCEIFJYDJAYZJBEOTMUSGAGAYYQNIPTFPYMCBOYDYYSVGWDOJTBZLMFBYJXLQCUDRRIGMIUYWMQUUF
RPCZQHTVJOUJSMNRVQQZEJYLACNHRFCPTFENZYEJCLYMBQUCGUMYQDBUAWLQTMOAXCZJBEABHQJYEA
MQQDNIRLNTUINRMCYUJAQTZQMGOEXUDEONQPIDBXWNKNIEUNQMBQDUFGXLFXYBVKNTEZCBFJGJUTVH
HMBWOZIFQNCTLMBQELYVGNTUHIAXNQUHSROYZJCEFUIACVOBFVAEGBBHGNEIMOHIYRIOZQ"""

  print("Calculating key length for ")
  print(cipherText)

  keyspace = [0,0]
  
  cipherTextLength = len(cipherText)
  for shift in range(1, 14): 
        coincidenceCount = 0
        for index in range(0, cipherTextLength):
            shiftedIndex = (index + shift) % cipherTextLength
            
            if cipherText[index] == cipherText[shiftedIndex]:
                coincidenceCount = coincidenceCount + 1
        print(str(shift) + " " +  str(coincidenceCount))

        if (keyspace[1] < coincidenceCount):
            keyspace[0] = shift
            keyspace[1] = coincidenceCount



  print("Key length is probably " + str(keyspace[0]) + " with coincidence count " + str(keyspace[1]))

main()
print("Done")