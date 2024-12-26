package Bai_TH1;

import javacard.framework.*;
/*
	/select A000000151000002
	/send 00010000
	/send 00020000
	/send 00030000

*/
public class Bai2 extends Applet {
    // Declare the static final byte arrays for hoTen and ngaySinh
    private static final byte[] hoTen = { 'N', 'g', 'u', 'y', 'e', 'n', ' ', 'V', 'a', 'n', ' ', 'A' };
    private static final byte[] ngaySinh = { '0', '1', '/', '0', '1', '/', '1', '9', '9', '0' };

    // Installation method
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Bai2().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }

    // Method to process APDU commands
    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buf = apdu.getBuffer();
        apdu.setIncomingAndReceive();

        switch (buf[ISO7816.OFFSET_INS]) {
            case (byte) 0x01: // Function 1: Print hoTen
                sendResponse(apdu, hoTen);
                break;

            case (byte) 0x02: // Function 2: Print ngaySinh
                sendResponse(apdu, ngaySinh);
                break;

            case (byte) 0x03: // Function 3: Print hoTen and ngaySinh
                short lenHoTen = (short) hoTen.length;
                short lenNgaySinh = (short) ngaySinh.length;
                short totalLength = (short) (lenHoTen + lenNgaySinh + 2); // Add 2 bytes for comma and space

                byte[] fullResponse = new byte[totalLength];
                Util.arrayCopy(hoTen, (short) 0, fullResponse, (short) 0, lenHoTen);
                fullResponse[lenHoTen] = (byte) ','; // Comma separator
                fullResponse[(short) (lenHoTen + 1)] = (byte) ' '; // Space separator
                Util.arrayCopy(ngaySinh, (short) 0, fullResponse, (short) (lenHoTen + 2), lenNgaySinh);

                sendResponse(apdu, fullResponse);
                break;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    // Utility method to send response data
    private void sendResponse(APDU apdu, byte[] data) {
        short len = (short) data.length;
        byte[] buf = apdu.getBuffer();
        Util.arrayCopy(data, (short) 0, buf, (short) 0, len);
        apdu.setOutgoingAndSend((short) 0, len);
    }
}
