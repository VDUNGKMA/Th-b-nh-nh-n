package TheThongMinhMP;

import javacard.framework.*;
import javacard.security.*;
import javacardx.crypto.*;

public class TheThongMinhMP extends Applet {
    final static byte CLA = (byte) 0x00;
    final static byte INS_INIT = (byte) 0x01;
    final static byte INS_CHECK_PIN = (byte) 0x02;
    final static byte INS_UNBLOCK_CARD = (byte) 0x03;
    final static byte INS_CHANGE_PIN = (byte) 0x04;
    final static byte INS_CHANGE_INFO = (byte) 0x05;
    final static byte INS_SHOW_INFO = (byte) 0x06;
    final static byte INS_CHECK_CARD = (byte) 0x07;
    final static byte INS_UPLOAD_IMG = (byte) 0x08;
    final static byte INS_GET_IMG = (byte) 0x09;
    final static byte INS_GET_PUBKEY = (byte) 0x10;
    final static byte INS_SIGN = (byte) 0x11;
    final static byte INS_CHECK_BALANCE = (byte) 0x12;
    final static byte INS_RECHARGE = (byte) 0x13;
    final static byte INS_PAY = (byte) 0x14;
    final static byte INS_GET_ID = (byte) 0x15;
    final static byte INS_LOG_PIN_HASH = (byte) 0x16;

    private static byte[] id, name, phone, address, pin, image;
    private static short id_len, name_len, phone_len, address_len, pin_len, image_len, counter, initcard;
	private static short dob_len, insurance_len; 
	private static byte[] dob, insurance;      
	
    private static byte[] tempBuffer, subBuffer, changeBuffer, sigBuffer;
    private static byte[] add = { 0x03 }; 

    // Tai khoan, thanh toan
    private static byte[] tempAccount;
    private static byte[] accountBalance;



	// anh 
	public static final short MAX_LENGTH = (short) (0X61A8);
	private  byte[] image1, image2, image3, image4;
    private short imagelen1, imagelen2, imagelen3, imagelen4, lenback1, lenback2, lenback3, lenback4, pointer1, pointer2, pointer3, pointer4;
    

    
    /* Khai báo tham so liên quan den ham bam */
    private static MessageDigest sha;

    // Khai báo tham so liên quan thuat toan AES
    private static final short aesBlock = (short) 16;
    private static AESKey aesKey;
    private static Cipher cipher;
    private static short keyLen;
    private static byte[] keyData;

    // Khai báo tham so liên quan RSA
    private static RSAPrivateKey rsaPri;
    private static RSAPublicKey rsaPub;
    private static byte x;
    private static byte[] rsaPubKey, rsaPriKey, tempPriKey;
    private static Signature rsaSig;
    private static short sigLen, rsaPubKeyLen, rsaPriKeyLen;
   
	// khoa the
    private static boolean block_card = false;
    private byte[] logic;

    private TheThongMinhMP() {
        logic = new byte[] { 0x05, 0x00, 0x01, 0x02 };
        image1 = new byte[MAX_LENGTH];
        image2 = new byte[MAX_LENGTH];
        image3 = new byte[MAX_LENGTH];
        image4 = new byte[MAX_LENGTH];
        
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        TheThongMinhMP applet = new TheThongMinhMP();
        applet.register(bArray, (short) (bOffset + 1), bArray[bOffset]);
        id = new byte[aesBlock*2]; // 128 bit
        name = new byte[(short) aesBlock * 3];
        phone = new byte[aesBlock];
        address = new byte[(short) aesBlock * 5];
        dob = new byte[aesBlock];         // Ngày sinh (16 bytes)
		insurance = new byte[aesBlock*2];  // So bao hiem (16 bytes)

        pin = new byte[32];
	
        tempBuffer = JCSystem.makeTransientByteArray((short) 128, JCSystem.CLEAR_ON_DESELECT);
        subBuffer = JCSystem.makeTransientByteArray((short) 128, JCSystem.CLEAR_ON_DESELECT);
        changeBuffer = JCSystem.makeTransientByteArray((short) 128, JCSystem.CLEAR_ON_DESELECT);

        tempAccount = new byte[8];
        accountBalance = new byte[8];
        Util.arrayFillNonAtomic(accountBalance, (short) 0, (short) 8, (byte) 0x00);
        Util.arrayFillNonAtomic(tempAccount, (short) 0, (short) 8, (byte) 0x00);

        counter = 0;
        initcard = 0;

       
		sha = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);

        //Khai bao cac thu tuc can thiet se thiet dat thuat toan ma hoa AES, co padding
        keyLen = (short) (KeyBuilder.LENGTH_AES_256 / 8);
        keyData = new byte[keyLen]; // 256

		

        cipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_ECB_NOPAD, false);
        aesKey = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES, (short) (8 * keyLen), false); // key 256

        sigLen = (short) (KeyBuilder.LENGTH_RSA_1024 / 8); 
        rsaSig = Signature.getInstance(Signature.ALG_RSA_SHA_PKCS1, false);

        rsaPubKey = new byte[(short) (2 * sigLen)]; // 128*2
        rsaPriKey = new byte[(short) (2 * sigLen)];
        tempPriKey = new byte[(short) (2 * sigLen)];
        sigBuffer = new byte[(short) (2 * sigLen)];
        rsaPubKeyLen = 0;
        rsaPriKeyLen = 0;
		
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }
		
        byte[] buf = apdu.getBuffer();
        short len = apdu.setIncomingAndReceive();
        if (buf[ISO7816.OFFSET_CLA] != CLA)
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        switch (buf[ISO7816.OFFSET_INS]) {
            case INS_INIT:// 0x01
                init_card(apdu, len);
                break;
            case INS_CHECK_PIN:// 0x02;
                check_pin(apdu, len);
                break;
            case INS_UNBLOCK_CARD:// 0x03;
                unblockcard(apdu, len);
                break;
            case INS_CHANGE_PIN:// 0x04;
                change_pin(apdu, len);
                break;
            case INS_CHANGE_INFO: // 0x05;
                change_info(apdu, len);
                break;
            case INS_SHOW_INFO: // 0x06;
                getInfo(apdu);
                break;
            case INS_CHECK_CARD:// 0x07;
                checkcard(apdu);
                break;
            case INS_UPLOAD_IMG:// 0x08;
               if (buf[ISO7816.OFFSET_P1] == 0x01) {
                    imagelen1 = 0;
                    imagelen2 = 0;
                    imagelen3 = 0;
                    imagelen4 = 0;
                }
                if (buf[ISO7816.OFFSET_P1] == 0x02) {
                    set_img(apdu, len);
                }
                break;
            case INS_GET_IMG: // 0x09;
            	 if (buf[ISO7816.OFFSET_P1] == 0x01) {
                    lenback1 = imagelen1;
                    lenback2 = imagelen2;
                    lenback3 = imagelen3;
                    lenback4 = imagelen4;
                    pointer1 = 0;
                    pointer2 = 0;
                    pointer3 = 0;
                    pointer4 = 0;
                    if (imagelen2 == 0) {
                        lenback2 = 1;
                    }
                    if (imagelen3 == 0) {
                        lenback3 = 1;
                    }
                    if (imagelen4 == 0) {
                        lenback4 = 1;
                    }
                }
                if (buf[ISO7816.OFFSET_P1] == 0x02) {
                    get_img(apdu);
                }
                break;
            case INS_GET_PUBKEY: // 0x10
                getRsaPubKey(apdu, len);
                break;
            case INS_SIGN: // 0x11;
                signHandler(apdu, len);
                break;
            case INS_CHECK_BALANCE: // 0x12;
				checkBalance(apdu);
                break;
            case INS_RECHARGE: // 0x13;
                addBalance(apdu, len);
                break;
            case INS_PAY: // 0x14;
                aPay(apdu, len);
                break;
            case INS_GET_ID:// 0x15
                getId(apdu);
                break;
            case INS_LOG_PIN_HASH: 
				logPinHash(apdu);
				break;
		    case (byte) 0x17: 
				   getImageLength(apdu);
				break;


            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    private void genRsaKeyPair(APDU apdu) {
      
        byte[] buffer = apdu.getBuffer();
        KeyPair keyPair = new KeyPair(KeyPair.ALG_RSA, (short) (sigLen * 8));
        keyPair.genKeyPair();
        JCSystem.beginTransaction();
        rsaPubKeyLen = 0;// len public
        rsaPriKeyLen = 0;
        JCSystem.commitTransaction();
        // Get a reference to the public key component of this 'keyPair' object.
        RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
        short pubKeyLen = 0;
        // Store the RSA public key value in the global variable 'rsaPubKey', the public key contains modulus N and Exponent E
        pubKeyLen += pubKey.getModulus(rsaPubKey, pubKeyLen);// N

        pubKeyLen += pubKey.getExponent(rsaPubKey, pubKeyLen);// E

        short priKeyLen = 0;
        // Returns a reference to the private key component of this KeyPair object.
        RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();
        // RSA Algorithm, the Private Key contains N and D, and store these parameters value in global variable 'rsaPriKey'.
        priKeyLen += priKey.getModulus(rsaPriKey, priKeyLen);// N
        priKeyLen += priKey.getExponent(rsaPriKey, priKeyLen);// D

        JCSystem.beginTransaction();
        rsaPubKeyLen = pubKeyLen;
        rsaPriKeyLen = priKeyLen;
        JCSystem.commitTransaction();

        JCSystem.requestObjectDeletion();
    }

    private void getRsaPubKey(APDU apdu, short len) {
        byte[] buffer = apdu.getBuffer();
        short offset = (short) 128;
        switch (buffer[ISO7816.OFFSET_P1]) {
            case (byte) 0x01:
                Util.arrayCopy(rsaPubKey, (short) 0, buffer, (short) 0, offset);
                apdu.setOutgoingAndSend((short) 0, offset);
                break;
            case (byte) 0x02:
                short eLen = (short) (rsaPubKeyLen - offset);
                Util.arrayCopy(rsaPubKey, offset, buffer, (short) 0, eLen);
                apdu.setOutgoingAndSend((short) 0, eLen);
                break;
            default:
               ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
        }
    }
private void init_card(APDU apdu, short len) {
    byte[] buffer = apdu.getBuffer();
    short flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, flag5 = 0, flag6 = 0;
    
    for (short i = 5; i < (short)(len + 5); i++) {
        if (buffer[i] == (byte) 0x03) {
            if (flag1 == 0) { // ID
                flag1 = i;
                id_len = (short) (flag1 - 5);
            } else if (flag2 == 0) { // Name
                flag2 = i;
                name_len = (short) (flag2 - (short)(flag1 + 1));
            } else if (flag3 == 0) { // DOB
                flag3 = i;
                dob_len = (short) (flag3 - (short)(flag2 + 1));
            } else if (flag4 == 0) { // Phone
                flag4 = i;
                phone_len = (short) (flag4 - (short)(flag3 + 1));
            } else if (flag5 == 0) { // Insurance
                flag5 = i;
                insurance_len = (short) (flag5 - (short)(flag4 + 1));
            } else { // Address
                flag6 = i;
                address_len = (short) (flag6 - (short)(flag5 + 1));
                // PIN
               pin_len = (short)(5 + (short)(len - (short)(flag6 + 1)));
            }
        }
    }

    // Sinh khóa RSA
    genRsaKeyPair(apdu);
    
   //bam ma pin
	short ret = sha.doFinal(buffer,(short)(flag6+1), pin_len, tempBuffer,(short)0);//dau ra ma hoa ghi ra tempBuffer, do dai 32

   Util.arrayCopy(tempBuffer, (short) 0, pin, (short) 0, (short) 32); // Luu ma PIN (SHA-256)
   Util.arrayCopy(tempBuffer, (short) 0, keyData, (short) 0, (short) 32); //dau vao key aes

    aesKey.setKey(keyData, (short) 0);
    cipher.init(aesKey, Cipher.MODE_ENCRYPT);

   // ma hoa aes id, name, phone, address, dob, insurance

    // ID
    Util.arrayCopy(buffer, (short)(0x05), tempBuffer, (short) 0, id_len);
    cipher.doFinal(tempBuffer, (short) 0, (short)(aesBlock*2), id, (short) 0);

    // Name
    Util.arrayCopy(buffer, (short)(flag1 + 1), tempBuffer, (short) 0, name_len);
    cipher.doFinal(tempBuffer, (short) 0, (short)(aesBlock*3), name, (short) 0);

    // DOB
    Util.arrayCopy(buffer, (short)(flag2 + 1), tempBuffer, (short) 0, dob_len);
    cipher.doFinal(tempBuffer, (short) 0, aesBlock, dob, (short) 0);

    // Phone
    Util.arrayCopy(buffer, (short)(flag3 + 1), tempBuffer, (short) 0, phone_len);
    cipher.doFinal(tempBuffer, (short) 0, aesBlock, phone, (short) 0);

    // Insurance
    Util.arrayCopy(buffer, (short)(flag4 + 1), tempBuffer, (short) 0, insurance_len);
    cipher.doFinal(tempBuffer, (short) 0, (short)(aesBlock*2), insurance, (short) 0);

    // Address
    Util.arrayCopy(buffer, (short)(flag5 + 1), tempBuffer, (short) 0, address_len);
    cipher.doFinal(tempBuffer, (short) 0, (short)(aesBlock*5), address, (short) 0);

    // Mã hóa khóa private RSA
    Util.arrayCopy(rsaPriKey, (short) 0, tempPriKey, (short) 0, rsaPriKeyLen);
    cipher.doFinal(tempPriKey, (short) 0, (short)(2 * sigLen), rsaPriKey, (short) 0);

 
     initcard = 1;
}


     private void getInfo(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        apdu.setOutgoing();
        cipher.init(aesKey, Cipher.MODE_DECRYPT);
      
 
        short totalLength = (short) (id_len + name_len + phone_len + address_len + dob_len + insurance_len+5);
        apdu.setOutgoingLength(totalLength);
        cipher.doFinal(id, (short) 0, (short) (aesBlock * 2), tempBuffer, (short) 0);
        apdu.sendBytesLong(tempBuffer, (short) 0, id_len);
        apdu.sendBytesLong(add, (short) 0, (short) 1);// 0x03

        cipher.doFinal(name, (short) 0, (short) (aesBlock * 3), tempBuffer, (short) 0);
        apdu.sendBytesLong(tempBuffer, (short) 0, name_len);
        apdu.sendBytesLong(add, (short) 0, (short) 1);

        cipher.doFinal(phone, (short) 0, aesBlock, tempBuffer, (short) 0);
        apdu.sendBytesLong(tempBuffer, (short) 0, phone_len);
        apdu.sendBytesLong(add, (short) 0, (short) 1);

        cipher.doFinal(address, (short) 0, (short) (aesBlock * 5), tempBuffer, (short) 0);
        apdu.sendBytesLong(tempBuffer, (short) 0, (short) address_len);
        apdu.sendBytesLong(add, (short) 0, (short) 1);
        
        cipher.doFinal(dob, (short) 0, aesBlock, tempBuffer, (short) 0);
		apdu.sendBytesLong(tempBuffer, (short) 0, dob_len);
		apdu.sendBytesLong(add, (short) 0, (short) 1); // 0x03
		
		cipher.doFinal(insurance, (short) 0, (short) (aesBlock * 2), tempBuffer, (short) 0);
		apdu.sendBytesLong(tempBuffer, (short) 0, insurance_len);
        
    }
private void logPinHash(APDU apdu) {
    byte[] buffer = apdu.getBuffer();
    apdu.setOutgoing();
    apdu.setOutgoingLength((short) 32); // do dài SHA-256 là 32 bytes
    // Debug: kiem tra noi dung `pin`
    for (short i = 0; i < 32; i++) {
        buffer[i] = pin[i];
    }
    // Util.arrayCopy(pin, (short) 0, buffer, (short) 0, (short) 32);
    apdu.sendBytes((short) 0, (short) 32);
}

    private void getId(APDU apdu) {
       
	  byte[] buffer = apdu.getBuffer();
		
		cipher.init(aesKey, Cipher.MODE_DECRYPT);

		// Giai mã ID
		cipher.doFinal(id, (short) 0, (short)(aesBlock * 2), tempBuffer, (short) 0);
	   apdu.setOutgoing();
	   apdu.setOutgoingLength((short)(id_len));
	   apdu.sendBytesLong(tempBuffer,(short) 0, id_len);
    
   
    }


   
private void change_info(APDU apdu, short len) {
    short flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0;
    byte[] buffer = apdu.getBuffer();

    // Xóa du lieu tam và sao chép du lieu vao buffer
    Util.arrayFillNonAtomic(tempBuffer, (short) 0, len, (byte) 0x00);
    Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, tempBuffer, (short) 0, len);

    
    for (short i = 0; i < len; i++) {
        if (tempBuffer[i] == (byte) 0x03) {
            if (flag1 == 0) {
                flag1 = i;
                name_len = flag1;
            } else if (flag2 == 0) {
                flag2 = i;
                address_len = (short) (flag2 - flag1 - 1);
            } else if (flag3 == 0) {
                flag3 = i;
                dob_len = (short) (flag3 - flag2 - 1);
            } else if (flag4 == 0) {
                flag4 = i;
                insurance_len = (short) (flag4 - flag3 - 1);
            }
        }
    }

    phone_len = (short) (len - (flag4 + 1));

    // Mã hóa name
    cipher.init(aesKey, Cipher.MODE_ENCRYPT);
    Util.arrayCopy(tempBuffer, (short) 0, changeBuffer, (short) 0, name_len);
    Util.arrayFillNonAtomic(name, (short) 0, (short) (aesBlock * 3), (byte) 0x00);
    cipher.doFinal(changeBuffer, (short) 0, (short) (aesBlock * 3), name, (short) 0);

    // Mã hóa address
    Util.arrayCopy(tempBuffer, (short) (flag1 + 1), changeBuffer, (short) 0, address_len);
    Util.arrayFillNonAtomic(address, (short) 0, (short) (aesBlock * 5), (byte) 0x00);
    cipher.doFinal(changeBuffer, (short) 0, (short) (aesBlock * 5), address, (short) 0);

    // Mã hóa dob
    Util.arrayCopy(tempBuffer, (short) (flag2 + 1), changeBuffer, (short) 0, dob_len);
    Util.arrayFillNonAtomic(dob, (short) 0, aesBlock , (byte) 0x00);
    cipher.doFinal(changeBuffer, (short) 0, aesBlock, dob, (short) 0);

    // Mã hóa insurance
    Util.arrayCopy(tempBuffer, (short) (flag3 + 1), changeBuffer, (short) 0, insurance_len);
    Util.arrayFillNonAtomic(insurance, (short) 0, (short) (aesBlock * 2), (byte) 0x00);
    cipher.doFinal(changeBuffer, (short) 0, (short) (aesBlock * 2), insurance, (short) 0);

    // Mã hóa phone
    Util.arrayCopy(tempBuffer, (short) (flag4 + 1), changeBuffer, (short) 0, phone_len);
    Util.arrayFillNonAtomic(phone, (short) 0, aesBlock, (byte) 0x00);
    cipher.doFinal(changeBuffer, (short) 0, aesBlock, phone, (short) 0);
}

    
private void check_pin(APDU apdu, short len) {
     byte[] buffer = apdu.getBuffer();
     apdu.setOutgoing();
     apdu.setOutgoingLength((short) 1);

     // So sánh mã pin 
     if (Util.arrayCompare(buffer, ISO7816.OFFSET_CDATA, pin, (short) 0, (short) 32) == 0) {
        // // Mã PIN dung
         logic[0] = 0x01;  // Trang thái "PIN dung"
		 counter = 0;      // Reset bo dem
         apdu.sendBytesLong(logic, (short) 0, (short) 1);
     } else {
        // Mã PIN sai
         counter++;
         if (counter >= 5) {
             block_card = true;
             logic[0] = 0x02;  // Trang thái "The bi khoa"
         } else {
             logic[0] = (byte) (0x08 - counter);  // So lan còn lai (7 -> 4 -> 1)
        }
         apdu.sendBytesLong(logic, (short) 0, (short) 1);
     }
}



    // kiem tra thong tin the
    private void checkcard(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        apdu.setOutgoing();
        apdu.setOutgoingLength((short) 1);
        // card da khoi tao thi tra ve 1 
        if (initcard == 1) {
            apdu.sendBytesLong(logic, (short) 2, (short) 1);// return 1
        } else {
            apdu.sendBytesLong(logic, (short) 1, (short) 1);// return 0
        }

    }

   private void unblockcard(APDU apdu, short len) {
		byte[] buffer = apdu.getBuffer();
		apdu.setOutgoing();
		apdu.setOutgoingLength((short) 1);

		// Kiem tra neu the da bi khóa khong
		if (!block_card) {
			logic[0] = 0x00; // The không bi khóa
			apdu.sendBytesLong(logic, (short) 0, (short) 1);
			return;
		}

		// Mo khóa the
		block_card = false;
		counter = 0;

		// Gui phan hoi thành công
		logic[0] = 0x01; // Thong bao the da mo khóa
		apdu.sendBytesLong(logic, (short) 0, (short) 1);
}

	

    
private void change_pin(APDU apdu, short len) {
    byte[] buffer = apdu.getBuffer();
    short flag1 = 0;
    for (short i = 5; i < (short) (len + 5); i++) {
        if (buffer[i] == (byte)0x03) {
            flag1 = i;
            break;
        }
    }

    if (flag1 == 0) {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID);
    }

    short oldPinLen = (short) (flag1 - 5); 
    short newPinLen = (short)((5 + len) - (short)(flag1 + 1));

    MessageDigest sha256 = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);
    short retOld = sha256.doFinal(buffer, (short)5, oldPinLen, tempBuffer, (short)0);
    if (Util.arrayCompare(tempBuffer, (short)0, pin, (short)0, (short)32) == 0) {
        short retNew = sha256.doFinal(buffer, (short)(flag1 + 1), newPinLen, tempBuffer, (short)0);
          // Cap nhat ma PIN bam moi vao pin[]
        Util.arrayCopy(tempBuffer, (short)0, pin, (short)0, (short)32);
        aesKey.setKey(pin, (short)0); 
    } else {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID);
    }
}
 
private void signHandler(APDU apdu, short len){
		//nguoi dung nhap pin de xac thuc dong thoi giai ma khoa bi mat
		//The su dung khoa bi mat ky va gui lai chu ky len cho he thong
		//He thong nhan duoc chu ky, su dung khoa cong khai cua nguoi dung trong he thong de kiem tra
        byte[] buffer = apdu.getBuffer();
        short flag = 0;
        short lenPinReq = 0;
        short lenRandom=0;

		byte[] extractedPin;
        Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, tempBuffer, (short) 0, len);
		for (short i = 0; i < len; i++) { 
            if (tempBuffer[i] == (byte) 0x03) {
                if (flag == 0) {
                    flag = i;
                    lenPinReq = flag;
                    lenRandom= (short)(len-(short)(flag+1));
                }
            }
        }
        // Luu mã PIN da tách ra vào mang `extractedPin`
    extractedPin = new byte[lenPinReq];
    Util.arrayCopy(tempBuffer, (short) 0, extractedPin, (short) 0, lenPinReq);

         // Kiem tra xem da xac minh dc mã PIN hay chua
    if (flag == 0) {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID); // Không tìm thy ký t `0x03`
    }
       // Luu mã PIN da tách ra vào mang `extractedPin`
    extractedPin = new byte[lenPinReq];
    Util.arrayCopy(tempBuffer, (short) 0, extractedPin, (short) 0, lenPinReq);
		short ret = sha.doFinal(tempBuffer,(short)0,lenPinReq, subBuffer,(short)0);//sub 32 byte
		if (Util.arrayCompare(subBuffer, (short) 0, pin, (short) 0, (short)32) == 0){
			//giai aes rsaPriKey
			cipher.init(aesKey, Cipher.MODE_DECRYPT);
			cipher.doFinal(rsaPriKey,(short)0, (short)(2*sigLen),sigBuffer,(short)0);
			
			
			short offset = (short) 128;
			RSAPrivateKey priKey = (RSAPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PRIVATE, (short)(8*sigLen), false);
			priKey.setModulus(sigBuffer, (short) 0, offset);
			priKey.setExponent(sigBuffer, offset, offset);
	
			rsaSig.init(priKey, Signature.MODE_SIGN);
			rsaSig.sign(tempBuffer,(short)0,len ,sigBuffer,(short)0);
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)sigLen);
			apdu.sendBytesLong(sigBuffer,(short)0, (short)sigLen);
			
		}else{
			ISOException.throwIt(ISO7816.SW_DATA_INVALID);
		}
	}

private void checkBalance(APDU apdu) {
    byte[] buffer = apdu.getBuffer();

    Util.arrayCopyNonAtomic(accountBalance, (short) 0, buffer, (short) 0, (short) accountBalance.length);

    apdu.setOutgoingAndSend((short) 0, (short) accountBalance.length);
}
   
private void addBalance(APDU apdu, short len) {
    byte[] buffer = apdu.getBuffer();

    // Sao chep du lieu apdu buffer(gui tu host) vao mang tam
    byte[] incomingData = JCSystem.makeTransientByteArray(len, JCSystem.CLEAR_ON_RESET);
    Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, incomingData, (short) 0, len);

    // cong so tien (big number addition)
    addBigNumbers(accountBalance, (short) accountBalance.length, incomingData, len);
}

private void addBigNumbers(byte[] a, short aLen, byte[] b, short bLen) {
    short carry = 0;

    for (short i = (short) (aLen - 1), j = (short) (bLen - 1); i >= 0; i--, j--) {
        short aValue = (short) (a[i] & 0xFF);
        short bValue = (j >= 0) ? (short) (b[j] & 0xFF) : 0;

        short sum = (short) (aValue + bValue + carry);
        a[i] = (byte) (sum & 0xFF); // Luu ket qua
        carry = (short) (sum >> 8); // Tính carry
    }

    // Neu còn carry mà không the chua thêm thì tra ve loi
    if (carry > 0) {
        ISOException.throwIt((short) 0x6A84); // Không du dung luong
    }
}


private void subtractBytes(byte[] source, byte[] value, short offset, short length) {
    short carry = 0;
    for (short i = (short) (length - 1); i >= 0; i--) {
        short temp = (short) ((source[(short) (offset + i)] & 0xFF) - (value[i] & 0xFF) - carry);
        if (temp < 0) {
            temp += 256;
            carry = 1;
        } else {
            carry = 0;
        }
        source[(short) (offset + i)] = (byte) (temp & 0xFF);
    }
}


    // Hàm so sánh mang byte
    private byte compareBytes(byte[] a, byte[] b, short offset, short length) {
        for (short i = 0; i < length; i++) {
            byte byteA = a[i];
            short index = (short)(offset + i);
            byte byteB = b[index];
            if (byteA != byteB) {
                return (byte)((byteA & 0xFF) > (byteB & 0xFF) ? 1 : -1);
            }
        }
        return 0; // Bang nhau
    }
    



private void set_img(APDU apdu, short len) {
    byte[] buf = apdu.getBuffer();
    short offData = apdu.getOffsetCdata();
    
    // Kiem tra dung luong con lai truoc khi luu anh
    if ((short) (MAX_LENGTH - imagelen3) < 255) {
            // Luu du lieu vao image4
        Util.arrayCopy(buf, offData, image4, imagelen4, len);
        imagelen4 += len;
    } else {
        if ((short) (MAX_LENGTH - imagelen2) < 255) {
            // Luu du lieu vao image3
            Util.arrayCopy(buf, offData, image3, imagelen3, len);
            imagelen3 += len;
        } else {
            if ((short) (MAX_LENGTH - imagelen1) < 255) {
                 // Luu du lieu vao image2
                Util.arrayCopy(buf, offData, image2, imagelen2, len);
                imagelen2 += len;
            } else {
                // Luu du lieu vao image1
                Util.arrayCopy(buf, offData, image1, imagelen1, len);
                imagelen1 += len;
            }
        }
    }
}

private void get_img(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    short datalen = 255;
    
    // Truyen anh tu image4, image3, image2 hoac image1 tùy vào trang thái
    if (lenback3 == 0) {
        if (lenback4 < 255) {
            datalen = lenback4;
        }
        apdu.setOutgoing();
        apdu.setOutgoingLength(datalen);
        Util.arrayCopy(image4, pointer4, buf, (short) 0, datalen);
        apdu.sendBytes((short) 0, datalen);
        pointer4 += (short) 255;
        lenback4 -= (short) (255);
    } else {
        if (lenback2 == 0) {
            if (lenback3 < 255) {
                datalen = lenback3;
            }
            apdu.setOutgoing();
            apdu.setOutgoingLength(datalen);
            Util.arrayCopy(image3, pointer3, buf, (short) 0, datalen);
            apdu.sendBytes((short) 0, datalen);
            pointer3 += (short) 255;
            lenback3 -= (short) (255);
        } else {
            if (lenback1 == 0) {
                if (lenback2 < 255) {
                    datalen = lenback2;
                }
                apdu.setOutgoing();
                apdu.setOutgoingLength(datalen);
                Util.arrayCopy(image2, pointer2, buf, (short) 0, datalen);
                apdu.sendBytes((short) 0, datalen);
                pointer2 += (short) 255;
                lenback2 -= (short) (255);
            } else {
                if (lenback1 < 255) {
                    datalen = lenback1;
                }
                apdu.setOutgoing();
                apdu.setOutgoingLength(datalen);
                Util.arrayCopy(image1, pointer1, buf, (short) 0, datalen);
                apdu.sendBytes((short) 0, datalen);
                pointer1 += (short) 255;
                lenback1 -= (short) (255);
            }
        }
    }
}

private void getImageLength(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    
    // Tính tong kich thuoc anh
    short imageLength = (short) (imagelen1 + imagelen2 + imagelen3 + imagelen4);
    
    // Chuan bi du lieu tra ve
    buf[0] = (byte) (imageLength >> 8);   // Byte cao
    buf[1] = (byte) (imageLength & 0xFF); // Byte thap
    
    apdu.setOutgoing();
    apdu.setOutgoingLength((short) 2);
    apdu.sendBytes((short) 0, (short) 2);
}
	private void aPay(APDU apdu, short len) {
    byte[] buf = apdu.getBuffer();
    apdu.setOutgoing();
    apdu.setOutgoingLength((short) 1); // Chi tra ve 1 byte ket qua

    byte check = buf[5]; // Kiem tra su dung bao hiem
    Util.arrayCopy(buf, (short) 6, tempAccount, (short) 0, (short) 8); // Sao chép so tien can thanh toán
 
    // Neu su dung bao hiem (check == 0x01), giam 80% tong hóa don
    if (check == 0x01) {
       int totalAmount = 0;
    
    // Chuyen doi 8 byte thành so nguyên lon (tong hóa don)
    for (short i = 0; i < 8; i++) {
        totalAmount = (totalAmount << 8) | (tempAccount[i] & 0xFF); // Lay so nguyên tu các byte
    }

    // Tính 20% cua tong hóa don
    int reducedAmount = totalAmount * 20 / 100; // Lay 20% tong hóa don

    // Chuyen lai reducedAmount thành 8 byte
    for (short i = 7; i >= 0; i--) {
        tempAccount[i] = (byte) (reducedAmount & 0xFF);
        reducedAmount >>= 8;
    }
    }
		
    // Kiem tra so du
    if (compareBytes(tempAccount, accountBalance, (short) 0, (short) 8) > 0) {
        logic[0] = 0x00; // Gán ket qua that bai
        apdu.sendBytesLong(logic, (short) 0, (short) 1);
        return;
    }

   // Tru tien 
    subtractBytes(accountBalance, tempAccount, (short) 0, (short) 8);
    logic[0] = 0x01; // Gán ket qua thanh cong
    apdu.sendBytesLong(logic, (short) 0, (short) 1);
}







   

}
