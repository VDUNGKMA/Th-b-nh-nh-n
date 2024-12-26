package Bai_TH1;

import javacard.framework.*;
/*
	- Select: /select A000000151000004
	// name Nguyen Van A
    - Set Name: 00 10 00 00 0C 4E 67 75 79 65 6E 20 56 61 6E 20 41
    - Set DOB: /send 001100000A30312F30312F31393930
    - Get Name: /send 0001000000
    - Get DOB: /send 0002000000
    - Get Both: /send 0003000000


*/
public class Bai4 extends Applet
{
// Các mng lu tr h tên và ngày sinh ca sinh viên
    private byte[] hoTen;
    private byte[] ngaySinh;
     //  dài ti a ca h tên và ngày sinh
    private static final short MAX_NAME_LENGTH = 50;
    private static final short MAX_DOB_LENGTH = 10;
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new Bai4().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	// Phng thc x lý APDU
    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        byte instruction = buffer[ISO7816.OFFSET_INS]; // Ly mã lnh INS
        short dataLength = apdu.setIncomingAndReceive();

        switch (instruction) {
            case 0x10: // Lnh nhp h tên
                if (dataLength > MAX_NAME_LENGTH) {
                    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                }
                hoTen = new byte[dataLength];
                Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, hoTen, (short) 0, dataLength);
                break;

            case 0x11: // Lnh nhp ngày sinh
                if (dataLength > MAX_DOB_LENGTH) {
                    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                }
                ngaySinh = new byte[dataLength];
                Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, ngaySinh, (short) 0, dataLength);
                break;

            case 0x01: // Lnh in h tên
                sendResponse(apdu, hoTen);
                break;

            case 0x02: // Lnh in ngày sinh
                sendResponse(apdu, ngaySinh);
                break;

            case 0x03: // Lnh in c h tên và ngày sinh
                short totalLength = (short) (hoTen.length + ngaySinh.length + 2); // Cng thêm 2 byte cho du cách và du phy
                byte[] fullResponse = new byte[totalLength];
                Util.arrayCopy(hoTen, (short) 0, fullResponse, (short) 0, (short) hoTen.length);
                fullResponse[hoTen.length] = (byte) ','; // Du phy ngn cách
                fullResponse[(short) (hoTen.length + 1)] = (byte) ' '; // Du cách ngn cách
                Util.arrayCopy(ngaySinh, (short) 0, fullResponse, (short) (hoTen.length + 2), (short) ngaySinh.length);

                sendResponse(apdu, fullResponse);
                break;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    // Phng thc tin ích  gi d liu phn hi
    private void sendResponse(APDU apdu, byte[] data) {
        if (data == null) {
            ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND); // Tr li nu d liu cha c nhp
        }
        short len = (short) data.length;
        byte[] buf = apdu.getBuffer();
        Util.arrayCopy(data, (short) 0, buf, (short) 0, len);
        apdu.setOutgoingAndSend((short) 0, len);
    }
}
