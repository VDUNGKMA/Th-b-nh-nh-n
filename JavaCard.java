package TheThongMinhMP;

import javacard.framework.*;
import javacard.security.*;
import javacardx.crypto.*;

public class TheThongMinhMP extends Applet {
    final static byte CLA = (byte) 0x00;
    final static byte INS_INIT = (byte) 0x01;
    final static byte INS_CHECK_PIN = (byte) 0x02;
    final static byte INS_UNBLOCK_CARD = (byte) 0x03;
    final static byte INS_THAY_DOI_PIN = (byte) 0x04;
    final static byte INS_SET_TT_KH = (byte) 0x05;
    final static byte INS_SHOW_TT_KH = (byte) 0x06;
    final static byte INS_CHECK_CARD = (byte) 0x07;
    final static byte INS_UPLOAD_IMG = (byte) 0x08;
    final static byte INS_GET_IMG = (byte) 0x09;
    final static byte INS_GET_PUBKEY = (byte) 0x10;
    final static byte INS_SIGN = (byte) 0x11;
    final static byte INS_CHECK_SCORE = (byte) 0x12;
    final static byte INS_RECHARGE = (byte) 0x13;
    final static byte INS_PAY = (byte) 0x14;
    final static byte INS_GET_ID = (byte) 0x15;
    final static byte INS_LOG_PIN_HASH = (byte) 0x16;

    private static byte[] id, name, phone, address, pin, image;
    private static short id_len, name_len, phone_len, address_len, pin_len, image_len, counter, initcard, money_Len;
	private static short dob_len, insurance_len; 
	private static byte[] dob, insurance;      

    private static byte[] tempBuffer, subBuffer, changeBuffer, sigBuffer;
    private static byte[] add = { 0x03 }; 

    // Tài khon, thanh toán
    private static byte[] tempAccount;
    private static byte[] accountBalance;
    private static byte[] scores;

    // Avatar
    private static byte[] avatar;
    private static byte[] avatarBuffer;
    private static short len_avatar;
    private static final short MAX_AVATAR_SIZE = (short) (4096);

    /* Khai báo tham s liên quan n bm */
    private static MessageDigest sha;

    // Khai báo tham s liên quan n AES
    private static final short aesBlock = (short) 16;
    private static AESKey aesKey;
    private static Cipher cipher;
    private static short keyLen;
    private static byte[] keyData;

    // Khai báo tham s liên quan n RSA
    private static RSAPrivateKey rsaPri;
    private static RSAPublicKey rsaPub;
    private static byte x;
    private static byte[] rsaPubKey, rsaPriKey, tempPriKey;
    private static Signature rsaSig;
    private static short sigLen, rsaPubKeyLen, rsaPriKeyLen;
    // random
    private byte[] seed;
    private RandomData ranData;

    private static boolean block_card = false;
    private static byte[] default_pin;
    private byte[] logic;

    private TheThongMinhMP() {
        logic = new byte[] { 0x05, 0x00, 0x01, 0x02 };
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new TheThongMinhMP().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
        id = new byte[aesBlock*2]; // 128 bit
        name = new byte[(short) aesBlock * 3];
        phone = new byte[aesBlock];
        address = new byte[(short) aesBlock * 5];
        dob = new byte[aesBlock];         // Ngày sinh (16 bytes)
		insurance = new byte[aesBlock*2];  // S bo him (16 bytes)

        pin = new byte[32];
        default_pin = new byte[] { 0x31, 0x31, 0x31, 0x31, 0x31, 0x31 };

        avatar = new byte[(short) (256 * aesBlock)];

        tempBuffer = JCSystem.makeTransientByteArray((short) 128, JCSystem.CLEAR_ON_DESELECT);
        subBuffer = JCSystem.makeTransientByteArray((short) 128, JCSystem.CLEAR_ON_DESELECT);
        changeBuffer = JCSystem.makeTransientByteArray((short) 128, JCSystem.CLEAR_ON_DESELECT);
        // avatarBuffer = JCSystem.makeTransientByteArray((short) MAX_AVATAR_SIZE, JCSystem.CLEAR_ON_DESELECT);
        // tk
        tempAccount = new byte[8];
        accountBalance = new byte[8];
        scores = new byte[8];
        Util.arrayFillNonAtomic(accountBalance, (short) 0, (short) 8, (byte) 0x00);
        Util.arrayFillNonAtomic(scores, (short) 0, (short) 8, (byte) 0x00);
        Util.arrayFillNonAtomic(tempAccount, (short) 0, (short) 8, (byte) 0x00);

        money_Len = 3;
        counter = 0;
        initcard = 0;

       
        // sha = MessageDigest.getInstance(MessageDigest.ALG_SHA, false);
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
            case INS_THAY_DOI_PIN:// 0x04;
                change_pin(apdu, len);
                break;
            case INS_SET_TT_KH: // 0x05;
                change_info(apdu, len);
                break;
            case INS_SHOW_TT_KH: // 0x06;
                getInfo(apdu);
                break;
            case INS_CHECK_CARD:// 0x07;
                checkcard(apdu);
                break;
            case INS_UPLOAD_IMG:// 0x08;
                loadImage(apdu,len);
                break;
            case INS_GET_IMG: // 0x09;
                sendImage(apdu,len);
                break;
            case INS_GET_PUBKEY: // 0x10
                getRsaPubKey(apdu, len);
                break;
            case INS_SIGN: // 0x11;
                signHandler(apdu, len);
                break;
            case INS_CHECK_SCORE: // 0x12;
				checkBalance(apdu);
                break;
            case INS_RECHARGE: // 0x13;
                accBalance(apdu, len);
                break;
            case INS_PAY: // 0x14;
                aPay(apdu, len);
                break;
            case INS_GET_ID:// 0x15
                getId(apdu);
                break;
            case INS_LOG_PIN_HASH: // Ví d mã lnh là 0x16
				logPinHash(apdu);
				break;
		    case (byte) 0x17: // Ví d: mã lnh INS 0x17
				getEncryptedId(apdu);
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
// Ly mã PIN trc khi bm
    byte[] pinBeforeHash = new byte[pin_len];  // Mng lu tr mã PIN trc khi bm
    Util.arrayCopy(buffer, (short)(flag6 + 1), pinBeforeHash, (short)0, pin_len);  // Sao chép mã PIN t buffer vào pinBeforeHash

    
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
    apdu.setOutgoingLength((short) 32); //  dài SHA-256 là 32 bytes
    // Debug: kim tra ni dung `pin`
    for (short i = 0; i < 32; i++) {
        buffer[i] = pin[i];
    }
    // Util.arrayCopy(pin, (short) 0, buffer, (short) 0, (short) 32);
    apdu.sendBytes((short) 0, (short) 32);
}

    private void getId(APDU apdu) {
       
	  byte[] buffer = apdu.getBuffer();
		
		// Khôi phc li khóa AES t mã pin ã bm
		// aesKey.setKey(keyData, (short) 0);
		cipher.init(aesKey, Cipher.MODE_DECRYPT);

		// Gii mã ID
		cipher.doFinal(id, (short) 0, (short)(aesBlock * 2), tempBuffer, (short) 0);
	   apdu.setOutgoing();
	   apdu.setOutgoingLength((short)(id_len));
	   apdu.sendBytesLong(tempBuffer,(short) 0, id_len);
    
   
    }


   
private void change_info(APDU apdu, short len) {
    short flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0;
    byte[] buffer = apdu.getBuffer();

    // Xóa d liu tm và sao chép d liu t buffer
    Util.arrayFillNonAtomic(tempBuffer, (short) 0, len, (byte) 0x00);
    Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, tempBuffer, (short) 0, len);

    // Xác nh các v trí phân tách gia các trng
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

    //  dài ca phone
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

     // So sánh mã bm nhn c vi mã bm ã lu trên th
     if (Util.arrayCompare(buffer, ISO7816.OFFSET_CDATA, pin, (short) 0, (short) 32) == 0) {
        // // Mã PIN úng
         logic[0] = 0x01;  // Trng thái "PIN úng"
		 counter = 0;      // Reset b m
         apdu.sendBytesLong(logic, (short) 0, (short) 1);
     } else {
        // Mã PIN sai
         counter++;
         if (counter >= 5) {
             block_card = true;
             logic[0] = 0x02;  // Trng thái "Th b khóa"
         } else {
             logic[0] = (byte) (0x08 - counter);  // S ln còn li (7 -> 4 -> 1)
        }
         apdu.sendBytesLong(logic, (short) 0, (short) 1);
     }
    // byte[] buffer = apdu.getBuffer();
     // apdu.setOutgoing();
    // apdu.setOutgoingLength((short) 32);  // Mã PIN ã hash có  dài 32 byte (SHA-256)

    // // Gi bn bm mã PIN (32 byte) ã lu trong th
    // apdu.sendBytesLong(pin, (short) 0, (short) 32);  // Gi 32 byte mã PIN ã bm
}

private short padData(byte[] data, short length) {
    short padding = (short)(aesBlock - (length % aesBlock));
    for (short i = length; i < (short)(length + padding); i++) {
        data[i] = (byte)padding; // PKCS#7 padding
    }
    return (short)(length + padding);
}

private short unpadData(byte[] data, short length) {
    byte padValue = data[(short)(length - 1)];
    return (short)(length - padValue);
}


    // 0 cha khi to th, 1 ã khi to th
    private void checkcard(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        apdu.setOutgoing();
        apdu.setOutgoingLength((short) 1);
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
        if (Util.arrayCompare(buffer, (short)5, default_pin, (short)0, (short)len) == 0) {
			// i sang SHA-256 và lu 32 byte
			short r = sha.doFinal(default_pin,(short)0x00, (short)0x06, tempBuffer, (short)0);
			Util.arrayCopy(tempBuffer, (short)0, pin, (short)0, (short)32);
			 counter = 0;
			 block_card = false;
			 apdu.sendBytesLong(logic, (short)2, (short)1);
		} 
		 else {
			 apdu.sendBytesLong(logic, (short)1, (short)1);
	 }	

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
    } else {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID);
    }
}

    // private void loadImage(APDU apdu, short len) {

        // byte[] buffer = apdu.getBuffer();
        // // ly  dài d liu gi xung
        // Util.arrayFillNonAtomic(avatar, (short) 0, MAX_AVATAR_SIZE, (byte) 0x00);
        // short dataOffset = ISO7816.OFFSET_CDATA;
        // short pointer = 0;
        // while (len > 0) {
            // Util.arrayCopy(buffer, dataOffset, avatar, pointer, len);
            // pointer += len;
            // len = apdu.receiveBytes(dataOffset);
        // }
        // len_avatar = (short) pointer;

        // apdu.setOutgoing();
        // Util.setShort(buffer, (short) 0, len_avatar);
        // apdu.setOutgoingLength((short) 5);
        // apdu.sendBytes((short) 0, (short) 5);
    // }
    
    
// private void loadImage(APDU apdu, short len) {
    // byte[] buffer = apdu.getBuffer();

    // // Kim tra kích thc d liu
    // if (len > MAX_AVATAR_SIZE) {
        // ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
    // }

    // // Xóa d liu c trong mng avatar
    // Util.arrayFillNonAtomic(avatar, (short) 0, MAX_AVATAR_SIZE, (byte) 0x00);

    // short dataOffset = ISO7816.OFFSET_CDATA;
    // short pointer = 0;

    // // Nhn d liu qua APDU
    // while (len > 0) {
        // short copyLen = (len > (short) (MAX_AVATAR_SIZE - pointer)) ? 
                        // (short) (MAX_AVATAR_SIZE - pointer) : len;

        // Util.arrayCopy(buffer, dataOffset, avatar, pointer, copyLen);
        // pointer += copyLen;
        // len -= copyLen;

        // // Nhn gói d liu tip theo nu cha xong
        // if (len > 0) {
            // len = apdu.receiveBytes(dataOffset);
        // }
    // }

    // // Lu chiu dài d liu avatar
    // len_avatar = pointer;

    // // Gi phn hi chiu dài nh nhn c
    // apdu.setOutgoing();
    // Util.setShort(buffer, (short) 0, len_avatar);
    // apdu.setOutgoingLength((short) 2); // 2 byte  gi len_avatar
    // apdu.sendBytes((short) 0, (short) 2);
// }

private void loadImage(APDU apdu, short len) {
    byte[] buffer = apdu.getBuffer();
    short dataLength = apdu.getIncomingLength(); // Ly  dài d liu gi xung
    short dataOffset = apdu.getOffsetCdata();
    short pointer = 0;

    // Làm sch mng avatar trc khi sao chép d liu mi
    Util.arrayFillNonAtomic(avatar, (short) 0, MAX_AVATAR_SIZE, (byte) 0x00);

    // Lp qua và sao chép d liu cho n khi hoàn thành
    while (len > 0 && pointer < MAX_AVATAR_SIZE) {
        short bytesToCopy = (len < (MAX_AVATAR_SIZE - pointer)) ? len : (short) (MAX_AVATAR_SIZE - pointer); // Thay th Math.min
        Util.arrayCopy(buffer, dataOffset, avatar, pointer, bytesToCopy);
        pointer += bytesToCopy;
        len -= bytesToCopy;
        
        // Nhn thêm d liu nu cn
        if (len > 0) {
            len = apdu.receiveBytes(dataOffset); // Nhn tip d liu nu cn
        }
    }

    len_avatar = pointer; // Lu li kích thc thc t ca avatar

    // Chun b  gi d liu li
    apdu.setOutgoing();
    Util.setShort(buffer, (short) 0, len_avatar); // Cp nht  dài avatar
    apdu.setOutgoingLength((short) 5);
    apdu.sendBytes((short) 0, (short) 5); // Gi li d liu
}


	 
    // private void sendImage(APDU apdu, short len) {
        // if (len_avatar == (short) 0) {
            // ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND);
        // }
        // byte buf[] = apdu.getBuffer();
        // short toSend = len_avatar;
        // short le = apdu.setOutgoing();
        // apdu.setOutgoingLength(MAX_AVATAR_SIZE);
        // short sendLen = 0;
        // short pointer = 0;
        // while (toSend > 0) {
            // sendLen = (toSend > 0) ? le : toSend;
            // apdu.sendBytesLong(avatar, pointer, sendLen);// gi avatar start= pointer, len= sendLen
            // toSend -= sendLen;
            // pointer += sendLen;
        // }
    // }
// private void sendImage(APDU apdu) {
    // byte[] buffer = apdu.getBuffer();

    // // Kim tra nu d liu nh ã c lu tr
    // if (len_avatar == 0 || avatar == null) {
        // ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND); // Không có d liu nh
    // }

    // // Chun b  gi d liu nh theo tng gói nu d liu ln hn 255 byte
    // short bytesToSend = len_avatar;
    // short offset = 0;

    // apdu.setOutgoing();
    // apdu.setOutgoingLength(len_avatar);

    // while (bytesToSend > 0) {
        // short chunkLength = (bytesToSend > 255) ? 255 : bytesToSend; // Gi ti a 255 byte mi gói
        // apdu.sendBytesLong(avatar, offset, chunkLength);

        // offset += chunkLength;
        // bytesToSend -= chunkLength;
    // }
// }
private void sendImage(APDU apdu, short len) {
    if (len_avatar == (short) 0) {
        ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND);
    }

    byte[] buf = apdu.getBuffer();
    short toSend = len_avatar;
    short le = apdu.setOutgoing();

    // Kim tra xem chiu dài cn gi có hp l không
    if (toSend > MAX_AVATAR_SIZE) {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID); // Hoc mã li tng ng
    }

    apdu.setOutgoingLength(toSend); // Cp nht chiu dài d liu gi

    short sendLen = 0;
    short pointer = 0;

    while (toSend > 0) {
        sendLen = (toSend > le) ? le : toSend; // S dng giá tr nh hn gia le và toSend
        apdu.sendBytesLong(avatar, pointer, sendLen); // Gi avatar t pointer vi  dài sendLen

        toSend -= sendLen;
        pointer += sendLen;
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
        // Tm thi lu mã PIN sau khi tách
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
        // Lu mã PIN ã tách ra vào mng `extractedPin`
    extractedPin = new byte[lenPinReq];
    Util.arrayCopy(tempBuffer, (short) 0, extractedPin, (short) 0, lenPinReq);

         // Kim tra xem ã xác nh c mã PIN hay cha
    if (flag == 0) {
        ISOException.throwIt(ISO7816.SW_DATA_INVALID); // Không tìm thy ký t `0x03`
    }
       // Lu mã PIN ã tách ra vào mng `extractedPin`
    extractedPin = new byte[lenPinReq];
    Util.arrayCopy(tempBuffer, (short) 0, extractedPin, (short) 0, lenPinReq);
		short ret = sha.doFinal(tempBuffer,(short)0,lenPinReq, subBuffer,(short)0);//sub 32 byte
		if (Util.arrayCompare(subBuffer, (short) 0, pin, (short) 0, (short)32) == 0){
			// //giai aes rsaPriKey
			cipher.init(aesKey, Cipher.MODE_DECRYPT);
			cipher.doFinal(rsaPriKey,(short)0, (short)(2*sigLen),sigBuffer,(short)0);
			
			 //Test
				// Log giá tr sau gii mã
			// apdu.setOutgoing();
			// apdu.setOutgoingLength((short) (2 * sigLen));
			// apdu.sendBytesLong(sigBuffer, (short) 0, (short) (2 * sigLen));
			 //
			  short offset = (short) 128;
			RSAPrivateKey priKey = (RSAPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PRIVATE, (short)(8*sigLen), false);
			priKey.setModulus(sigBuffer, (short) 0, offset);
			 priKey.setExponent(sigBuffer, offset, offset);
	
			rsaSig.init(priKey, Signature.MODE_SIGN);
			 rsaSig.sign(tempBuffer,(short)0,len ,sigBuffer,(short)0);
			apdu.setOutgoing();
			apdu.setOutgoingLength((short)sigLen);
			apdu.sendBytesLong(sigBuffer,(short)0, (short)sigLen);
			
			//buffer[0]=0x01;
			//apdu.sendBytes((short)0, (short)1);
			
		}else{
			ISOException.throwIt(ISO7816.SW_DATA_INVALID);
		}
	}

private void checkBalance(APDU apdu) {
    byte[] buffer = apdu.getBuffer();

    // Copy accountBalance vào buffer tr v APDU
    Util.arrayCopyNonAtomic(accountBalance, (short) 0, buffer, (short) 0, (short) accountBalance.length);

    // Gi li s d trong accountBalance cho host
    apdu.setOutgoingAndSend((short) 0, (short) accountBalance.length);
}
   
private void accBalance(APDU apdu, short len) {
    byte[] buffer = apdu.getBuffer();

    // Copy d liu t APDU buffer (d liu gi t host) vào mt mng tm
    byte[] incomingData = JCSystem.makeTransientByteArray(len, JCSystem.CLEAR_ON_RESET);
    Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, incomingData, (short) 0, len);

    // Cng s tin (big number addition)
    addBigNumbers(accountBalance, (short) accountBalance.length, incomingData, len);
}

private void addBigNumbers(byte[] a, short aLen, byte[] b, short bLen) {
    short carry = 0;

    for (short i = (short) (aLen - 1), j = (short) (bLen - 1); i >= 0; i--, j--) {
        short aValue = (short) (a[i] & 0xFF);
        short bValue = (j >= 0) ? (short) (b[j] & 0xFF) : 0;

        short sum = (short) (aValue + bValue + carry);
        a[i] = (byte) (sum & 0xFF); // Lu kt qu
        carry = (short) (sum >> 8); // Tính carry
    }

    // Nu còn carry mà không th cha thêm thì tr v li
    if (carry > 0) {
        ISOException.throwIt((short) 0x6A84); // Không  dung lng
    }
}

    private void aPay(APDU apdu, short len) {
        byte buf[] = apdu.getBuffer();
        apdu.setOutgoing();
        apdu.setOutgoingLength((byte) 16);
        // tin hàng dng decimal (16 ch s)
        byte check = buf[5];// check s dng tích im hay không
        Util.arrayCopy(buf, (short) 6, tempAccount, (short) 0, (short) (len - 1));
        if (check == 0x01) {// dùng tích im
            // while(scores.compareTo(bArray, bOff, bLen, arrayFormat)){}
            while (compareBytes(scores, new byte[] { 0x00, 0x01 }, (short) 0, (short) 2) == 1) {// im > 0
                addBytes(accountBalance, new byte[] { 0x00, 0x02 }, (short) 0, (short) 2);
                subtractBytes(scores, new byte[] { 0x00, 0x01 }, (short) 0, (short) 2);// tr 1
            }
        }
        if (compareBytes(tempAccount, accountBalance, (short) 0, (short) 8) == 1) {// thanh toán > s d
            apdu.sendBytesLong(logic, (short) 1, (short) 1);// 0 1 2 ->return 0
        } else {
            subtractBytes(accountBalance, tempAccount, (short) 0, (short) 8);
            byte[] moneyPoint = new byte[] { 0x00, 0x05, 0x00 }; // tích im sau mi 50000
            while (compareBytes(tempAccount, moneyPoint, (short) 0, money_Len) >= 0) { // >
                subtractBytes(tempAccount, moneyPoint, (short) 0, money_Len);
                addBytes(scores, new byte[] { 0x00, 0x01 }, (short) 0, (short) 2);// cng 1
            }

            apdu.sendBytesLong(logic, (short) 2, (short) 1);// return 1
        }
    }

    // Hàm cng mng byte
    private void addBytes(byte[] result, byte[] operand, short offset, short length) {
         short carry = 0;
        for (short i = (short)(length - 1); i >= 0; i--) {
            short index = (short)(offset + i);
            short sum = (short)((result[i] & 0xFF) + (operand[index] & 0xFF) + carry);
            carry = (short)(sum > 0xFF ? 1 : 0);
            result[i] = (byte)sum;
        }
    }

    // Hàm tr mng byte
    private void subtractBytes(byte[] result, byte[] operand, short offset, short length) {
      short borrow = 0;
        for (short i = (short)(length - 1); i >= 0; i--) {
            short index = (short)(offset + i);
            short diff = (short)((result[i] & 0xFF) - (operand[index] & 0xFF) - borrow);
            borrow = (short)(diff < 0 ? 1 : 0);
            result[i] = (byte)diff;
        }
    }

    // Hàm so sánh mng byte
    private byte compareBytes(byte[] a, byte[] b, short offset, short length) {
        for (short i = 0; i < length; i++) {
            byte byteA = a[i];
            short index = (short)(offset + i);
            byte byteB = b[index];
            if (byteA != byteB) {
                return (byte)((byteA & 0xFF) > (byteB & 0xFF) ? 1 : -1);
            }
        }
        return 0; // Bng nhau
    }
    private void getEncryptedId(APDU apdu) {
    byte[] buffer = apdu.getBuffer();

    // Chun b APDU response
    apdu.setOutgoing();
    apdu.setOutgoingLength(aesBlock); // AES Block length is 16 bytes
    apdu.sendBytesLong(id, (short) 0, aesBlock); // Gi mng `id` ã mã hóa
}

}
