/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Patient;

//import static com.sun.corba.se.spi.orbutil.fsm.Guard.Result.convert;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;


public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String x = "1000000";
        String y;
        if (x.length() % 2 == 0) {
            //so chan
            y = x;
        } else {
            y = "0" + x;
        }
        

    }

    static byte[] toByteArray(String hexString) {
        int len = hexString.length();
        byte data[] = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // System.out.println(hexString.substring(i,i+2));
            //String sub=hexString.substring(i,i+2);
            //data[i/2]=sub.getBytes();
            // data[(i/2)+1]=Byte.parseByte(hexString.substring(i,i+1));
            // byte tmp=
            // data[i/2]= convert.ToByte(hexString.substring(i, 2), 16);

        }
        return data;
    }

    private static String convertStringToHex(String str) {
        // StringBuilder stringBuilder = new StringBuilder();
        String string = new String();
        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            String charToHex = Integer.toHexString(c);
            string += charToHex;
        }

        System.out.println("Converted Hex from String: " + string);
        return string;
    }
//    static byte[] toByteArray(String HexString){
//        int NumberChars=HexString.length();
//        byte[] bytes= new byte[NumberChars/2];
//        for(int i=0;i<NumberChars;i+=2){
//            bytes[i/2]=
//        }
//        
//    }
}
