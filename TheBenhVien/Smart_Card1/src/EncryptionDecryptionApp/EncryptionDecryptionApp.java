package EncryptionDecryptionApp;


import javacard.framework.*;
import javacard.security.*;
import javacardx.crypto.*;

public class EncryptionDecryptionApp extends Applet {

    // AES Key size: 128 bits
    private static final short AES_KEY_LENGTH = 16;

    private AESKey aesKey;
    private Cipher aesCipher;
    private byte[] keyData;
    private byte[] tempBuffer;

    private EncryptionDecryptionApp() {
        // Initialize AES Key and Cipher
        aesKey = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES, KeyBuilder.LENGTH_AES_128, false);
        aesCipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_CBC_NOPAD, false);

        // Allocate buffers
        keyData = new byte[AES_KEY_LENGTH];
        tempBuffer = new byte[256]; // Temporary buffer for operations

        // Generate a default key (for demonstration purposes, this can be modified)
        for (short i = 0; i < AES_KEY_LENGTH; i++) {
            keyData[i] = (byte) i;
        }
        aesKey.setKey(keyData, (short) 0);
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new EncryptionDecryptionApp().register();
    }

    @Override
    public void process(APDU apdu) {
        byte[] buffer = apdu.getBuffer();

        if (selectingApplet()) {
            return;
        }

        byte ins = buffer[ISO7816.OFFSET_INS];
        switch (ins) {
            case (byte) 0x10: // Encrypt data
                encryptData(apdu);
                break;
            case (byte) 0x20: // Decrypt data
                decryptData(apdu);
                break;
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

   private void encryptData(APDU apdu) {
    byte[] buffer = apdu.getBuffer();
    short dataLen = apdu.setIncomingAndReceive();

    // Calculate padded length
    short padding = (short) (16 - (dataLen % 16)); // Calculate padding value
    short paddedLen = (short) (dataLen + padding);

    // Copy original data and add padding
    Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, tempBuffer, (short) 0, dataLen);
    for (short i = dataLen; i < paddedLen; i++) {
        tempBuffer[i] = (byte) padding; // Add padding bytes
    }

    // Initialize cipher for encryption
    aesCipher.init(aesKey, Cipher.MODE_ENCRYPT);

    // Encrypt padded data
    aesCipher.doFinal(tempBuffer, (short) 0, paddedLen, buffer, (short) 0);

    // Send encrypted data back
    apdu.setOutgoing();
    apdu.setOutgoingLength(paddedLen);
    apdu.sendBytesLong(buffer, (short) 0, paddedLen);
}

    private void decryptData(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short dataLen = apdu.setIncomingAndReceive();

        // Ensure input data length is a multiple of 16 (block size for AES)
        if ((dataLen % 16) != 0) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }

        // Initialize cipher for decryption
        aesCipher.init(aesKey, Cipher.MODE_DECRYPT);

        // Decrypt data
        aesCipher.doFinal(buffer, ISO7816.OFFSET_CDATA, dataLen, tempBuffer, (short) 0);
	// Remove padding
		short padding = (short) (tempBuffer[(short) (dataLen - 1)] & 0xFF); // Cast index to short

		if (padding <= 0 || padding > 16) {
			ISOException.throwIt(ISO7816.SW_DATA_INVALID);
		}
		short originalLen = (short) (dataLen - padding);
        // Send decrypted data back
        apdu.setOutgoing();
        apdu.setOutgoingLength(originalLen);
        apdu.sendBytesLong(tempBuffer, (short) 0, originalLen);
    }
}