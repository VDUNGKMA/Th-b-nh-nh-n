package Bai_TH1;

import javacard.framework.*;
/*
	/select A000000151000001
	/send 00000000


*/
public class Bai1 extends Applet {

    // Byte array containing the string "hello"
    byte[] hello = { 'h', 'e', 'l', 'l', 'o' };
    short len;

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Bai1().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }

    public void process(APDU apdu) throws ISOException {
    	if(selectingApplet()){
	    	return;
    	}
        byte[] buf = apdu.getBuffer();
		apdu.setIncomingAndReceive();
 // Handle APDU instruction
        switch (buf[ISO7816.OFFSET_INS]) {
            case (byte) 0x00:
                // cach 1_1
                // apdu.setOutgoing();
                // len = (short) hello.length;
                // apdu.setOutgoingLength(len);
                // Util.arrayCopy(hello, (short) 0, buf, (short) 0, len);
                // apdu.sendBytes((short) 0, len);
				// break;
                // cach 1_2
                apdu.setOutgoing();
                len = (short) hello.length;
                apdu.setOutgoingLength(len);
                apdu.sendBytesLong(hello, (short) 0, len);
                break;

            // // cach 2
            // case (byte) 0x01:
                // len = (short) hello.length;
                // Util.arrayCopy(hello, (short) 0, buf, (short) 0, len);
                // apdu.setOutgoingAndSend((short) 0, len);
                // break;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}