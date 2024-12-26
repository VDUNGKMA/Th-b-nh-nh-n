package Conect_Card;

import javacard.framework.*;


public class Conect_Card  extends Applet {

    public final static byte IDCARD_CLA = (byte) 0xB0;
    public final static byte INS_PRINT = (byte) 0x11;

    private static byte[] text;

    protected Conect_Card (byte[] bArray, short bOffset, byte bLength) {
        byte aIDLen = bArray[bOffset];
        if (aIDLen == 0) {
            register();
        } else {
            register(bArray, (short) (bOffset + 1), aIDLen);
        }

        bOffset = (short) (bOffset + aIDLen + 1);
        byte cLen = bArray[bOffset];
        bOffset = (short) (bOffset + cLen + 1);
        byte aLen = bArray[bOffset];
        bOffset = (short) (bOffset + 1);

        text = new byte[]{(byte) 0x48, (byte) 0x45, (byte) 0x4C, (byte) 0x4C, (byte) 0x4F};
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Conect_Card (bArray, bOffset, bLength);
    }

    public void process(APDU apdu) {
        byte[] buf = apdu.getBuffer();

        if ((buf[ISO7816.OFFSET_CLA] == 0x00) && (buf[ISO7816.OFFSET_INS] == (byte) 0xA4)) {
            return;
        }

        if (buf[ISO7816.OFFSET_CLA] != IDCARD_CLA) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }

        switch (buf[ISO7816.OFFSET_INS]) {
            case INS_PRINT:
                PrintText(apdu);
                return;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    private void PrintText(APDU apdu) {
        byte[] buf = apdu.getBuffer();
        short len = (short) text.length;

        Util.arrayCopyNonAtomic(text, (short) 0, buf, (short) 0, len);
        apdu.setOutgoingAndSend((short) 0, len);
    }
}

