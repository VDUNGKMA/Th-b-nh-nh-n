package Bai_TH1;

import javacard.framework.*;
/* - Select: /select A000000151000005
	- Set Id: /send 0010000006313233343536
    - Set Name: /send 001100000C4E677579656E2056616E2041
    - Set DOB: /send 001200000A30312F30312F31393930
    - Set address: /send 00130000064861204E6F69
    - Get Id: /send 0001000000
    - Get Name: /send 0002000000
    - Get DOB: /send 0003000000
    - Get Address : /send 0004000000
    - Get All: /send 0005000000
*/
public class Bai5 extends Applet
{
// C�c mng lu tr th�ng tin sinh vi�n
    private byte[] maSinhVien;
    private byte[] hoTen;
    private byte[] ngaySinh;
    private byte[] queQuan;

    //  d�i ti a ca tng trng
    private static final short MAX_LENGTH = 100;
	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new Bai5().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}

	// Phng thc x l� APDU
    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        byte instruction = buffer[ISO7816.OFFSET_INS];
        short dataLength = apdu.setIncomingAndReceive();

        switch (instruction) {
            case 0x10: // Nhp m� sinh vi�n
                maSinhVien = copyData(buffer, dataLength);
                break;

            case 0x11: // Nhp h t�n sinh vi�n
                hoTen = copyData(buffer, dataLength);
                break;

            case 0x12: // Nhp ng�y sinh
                ngaySinh = copyData(buffer, dataLength);
                break;

            case 0x13: // Nhp qu� qu�n
                queQuan = copyData(buffer, dataLength);
                break;

            case 0x01: // In m� sinh vi�n
                sendResponse(apdu, maSinhVien);
                break;

            case 0x02: // In h t�n
                sendResponse(apdu, hoTen);
                break;

            case 0x03: // In ng�y sinh
                sendResponse(apdu, ngaySinh);
                break;

            case 0x04: // In qu� qu�n
                sendResponse(apdu, queQuan);
                break;

            case 0x05: // In to�n b th�ng tin
                sendFullInformation(apdu);
                break;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    // Phng thc sao ch�p d liu v�o mng byte
    private byte[] copyData(byte[] buffer, short dataLength) {
        if (dataLength > MAX_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }
        byte[] data = new byte[dataLength];
        Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, data, (short) 0, dataLength);
        return data;
    }

    // Phng thc gi phn hi d liu
    private void sendResponse(APDU apdu, byte[] data) {
        if (data == null) {
            ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND);
        }
        short len = (short) data.length;
        byte[] buf = apdu.getBuffer();
        Util.arrayCopy(data, (short) 0, buf, (short) 0, len);
        apdu.setOutgoingAndSend((short) 0, len);
    }

    // Phng thc gi to�n b th�ng tin
    private void sendFullInformation(APDU apdu) {
        if (maSinhVien == null || hoTen == null || ngaySinh == null || queQuan == null) {
            ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND);
        }
        short totalLength = (short) (maSinhVien.length + hoTen.length + ngaySinh.length + queQuan.length + 3);
        byte[] fullResponse = new byte[totalLength];

        short offset = 0;
        Util.arrayCopy(maSinhVien, (short) 0, fullResponse, offset, (short) maSinhVien.length);
        offset += maSinhVien.length;
        fullResponse[offset++] = (byte) ','; // Du phy ngn c�ch
        Util.arrayCopy(hoTen, (short) 0, fullResponse, offset, (short) hoTen.length);
        offset += hoTen.length;
        fullResponse[offset++] = (byte) ','; // Du phy ngn c�ch
        Util.arrayCopy(ngaySinh, (short) 0, fullResponse, offset, (short) ngaySinh.length);
        offset += ngaySinh.length;
        fullResponse[offset++] = (byte) ','; // Du phy ngn c�ch
        Util.arrayCopy(queQuan, (short) 0, fullResponse, offset, (short) queQuan.length);

        sendResponse(apdu, fullResponse);
    }
}
