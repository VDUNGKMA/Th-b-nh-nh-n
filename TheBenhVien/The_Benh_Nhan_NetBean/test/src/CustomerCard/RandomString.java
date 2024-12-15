///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package CustomerCard;
//
///**
// *
// * @author Nga
// */
//public class RandomString {
//     public String getAlphaNumericString(int n)
//    {
//  
//        // chose a Character random from this String
//        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//                                    + "0123456789"
//                                    + "abcdefghijklmnopqrstuvxyz";
//  
//        // create StringBuffer size of AlphaNumericString
//        StringBuilder sb = new StringBuilder(n);
//  
//        for (int i = 0; i < n; i++) {
//  
//            // generate a random number between
//            // 0 to AlphaNumericString variable length
//            int index
//                = (int)(AlphaNumericString.length()
//                        * Math.random());
//            System.out.println("random"+Math.random());
//            System.out.println("index "+index);
//                  
//  
//            // add Character one by one in end of sb
//            sb.append(AlphaNumericString
//                          .charAt(index));
//        }
//  
//        return sb.toString();
//    }
//  
//    
//}
package CustomerCard;

import java.security.SecureRandom;

/**
 * Lớp tiện ích dùng để tạo chuỗi ngẫu nhiên gồm ký tự chữ và số.
 */
public class RandomString {

    // Các ký tự được phép sử dụng
    private static final String ALPHA_NUMERIC_STRING = 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            "abcdefghijklmnopqrstuvwxyz";

    // Sử dụng SecureRandom để tạo ngẫu nhiên bảo mật hơn
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Tạo chuỗi ngẫu nhiên chứa chữ và số với độ dài được chỉ định.
     * 
     * @param length độ dài chuỗi cần tạo
     * @return chuỗi ngẫu nhiên
     * @throws IllegalArgumentException nếu độ dài <= 0
     */
    public String getAlphaNumericString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Độ dài phải lớn hơn 0.");
        }

        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHA_NUMERIC_STRING.length());
            result.append(ALPHA_NUMERIC_STRING.charAt(index));
        }

        return result.toString();
    }
}
