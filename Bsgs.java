import java.math.BigInteger;

// Jeff Vinke
// CSC 440
/*
2. (25 points) Baby step, giant step attack on discrete log Implement the attack described in section 7.2.2 of the textbook. 

You will need these values:

p = 595117
alpha = 1002
alpha^x = 437083

Your program will output x, which will be an integer where each pair of digits repressents a letter in the encoding 
    scheme A = 01, B = 02, ..., Z = 26. Please translate the integer to an acronym. 
*/
public class Bsgs {
    public static void main(String[] args) {
        BigInteger p = BigInteger.valueOf(595117);
        BigInteger alpha = BigInteger.valueOf(1002);
        BigInteger alphaX = BigInteger.valueOf(437083);
        int n = ((int) Math.ceil(Math.sqrt((p.intValue()-1))));
        int x = 0;

        System.out.println("N - " + n);
        System.out.println("alpha - " + alpha.toString());
        System.out.println("alphaX - " + alphaX.toString());
        

        int[] listOne = new int[n];
        int[] listTwo = new int[n];

        for (int j = 0; j < n; j++) {
            BigInteger exponent = BigInteger.valueOf(j);
            int result = alpha.modPow(exponent, p).intValue();
            listOne[j] = result;
        }


        for (int k = 0; k < n; k++) {
            listTwo[k] = (alphaX.multiply(alpha.modPow((BigInteger.valueOf(k).multiply(BigInteger.valueOf(n)).negate()), p))).mod(p).intValue();

            for (int j = 0; j < n; j++) {                
                if (listOne[j] == listTwo[k]) {
                    System.out.println(">> found " + listOne[j]);

                    x = j + (k * n);
                    System.out.println("x:" + x);
                    break;
                }
            }
        }
        
        String result = String.valueOf(x);
        int offsetChar = ((int)'A') - 1;
        for (int i = 0; i < result.length(); i+=2) {
            int chunk = Integer.parseInt(result.substring(i, i+2));
            System.out.println(" >> " + (char)(offsetChar + chunk));
        }
    }
}