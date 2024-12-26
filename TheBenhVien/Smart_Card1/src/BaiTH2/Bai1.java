package BaiTH2;

import javacard.framework.*;

/*
AID: 11 22 33 44 55 01
 APDU:  00 10 00 00 
 data:  06 31 32 33 34 35 36 0C 4E 67 75 79 65 6E 20 56 61 6E 20 41 0A 30 31 2F 30 31 2F 31 39 39 30
 APDU:  00 20 00 00 
 
*/

public class Bai1 extends Applet {
    // Mang luu thong tin sinh vien
    private byte[] maSinhVien; // Luu ma sinh vien
    private byte[] hoTen;      // Luu ho ten sinh vien
    private byte[] ngaySinh;   // Luu ngay sinh

    // Do dai toi da cho tung truong
    private static final short MAX_MA_LENGTH = 10;    // Toi da 10 byte cho ma sinh vien
    private static final short MAX_NAME_LENGTH = 50;  // Toi da 50 byte cho ho ten
    private static final short MAX_DOB_LENGTH = 10;   // Toi da 10 byte cho ngay sinh

    // Ma lenh INS
    private static final byte INS_SET_INFO = (byte) 0x10; // Lenh nap thong tin sinh vien
    private static final byte INS_GET_INFO = (byte) 0x20; // Lenh gui thong tin sinh vien

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        // Khoi tao applet
        Bai1 applet = new Bai1();

        // Khoi tao cac mang trong bo nho lien tuc
        applet.maSinhVien = new byte[MAX_MA_LENGTH];
        applet.hoTen = new byte[MAX_NAME_LENGTH];
        applet.ngaySinh = new byte[MAX_DOB_LENGTH];

        // Dang ky applet
        applet.register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return; // Neu lenh SELECT duoc goi, thoat
        }

        byte[] buffer = apdu.getBuffer();
        byte instruction = buffer[ISO7816.OFFSET_INS]; // Lay ma INS tu APDU
        short dataLength = apdu.setIncomingAndReceive();

        switch (instruction) {
            case INS_SET_INFO:
                setStudentInfo(buffer, dataLength); // Nap thong tin sinh vien xuong the
                break;

            case INS_GET_INFO:
                getStudentInfo(apdu); // Gui thong tin sinh vien len may chu
                break;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // Ma INS khong hop le
        }
    }

    // Phuong thuc nap thong tin sinh vien
    private void setStudentInfo(byte[] buffer, short dataLength) {
        short offset = ISO7816.OFFSET_CDATA; // Vi tri du lieu trong APDU

        // Nap ma sinh vien
        short maLength = buffer[offset];
        if (maLength > MAX_MA_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }
        Util.arrayCopy(buffer, (short) (offset + 1), maSinhVien, (short) 0, maLength);

        // Nap ho ten
        offset += (short) (1 + maLength);
        short nameLength = buffer[offset];
        if (nameLength > MAX_NAME_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }
        Util.arrayCopy(buffer, (short) (offset + 1), hoTen, (short) 0, nameLength);

        // Nap ngay sinh
        offset += (short) (1 + nameLength);
        short dobLength = buffer[offset];
        if (dobLength > MAX_DOB_LENGTH) {
            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
        }
        Util.arrayCopy(buffer, (short) (offset + 1), ngaySinh, (short) 0, dobLength);
    }

    // Phuong thuc gui thong tin sinh vien len may chu
    private void getStudentInfo(APDU apdu) {
        if (maSinhVien == null || hoTen == null || ngaySinh == null) {
            ISOException.throwIt(ISO7816.SW_RECORD_NOT_FOUND);
        }

        // Tinh tong chieu dai thuc te cua du lieu
        short maLength = getActualLength(maSinhVien);
        short nameLength = getActualLength(hoTen);
        short dobLength = getActualLength(ngaySinh);

        short totalLength = (short) (maLength + nameLength + dobLength + 3);
        byte[] fullResponse = new byte[totalLength]; // Bo nho tam thoi de chua du lieu phan hoi

        short offset = 0;

        // Sao chep ma sinh vien
        fullResponse[offset++] = (byte) maLength;
        Util.arrayCopy(maSinhVien, (short) 0, fullResponse, offset, maLength);
        offset += maLength;

        // Sao chep ho ten
        fullResponse[offset++] = (byte) nameLength;
        Util.arrayCopy(hoTen, (short) 0, fullResponse, offset, nameLength);
        offset += nameLength;

        // Sao chep ngay sinh
        fullResponse[offset++] = (byte) dobLength;
        Util.arrayCopy(ngaySinh, (short) 0, fullResponse, offset, dobLength);

        sendResponse(apdu, fullResponse);
    }

    // Phuong thuc gui phan hoi APDU
    private void sendResponse(APDU apdu, byte[] data) {
        short len = (short) data.length;
        byte[] buf = apdu.getBuffer();
        Util.arrayCopy(data, (short) 0, buf, (short) 0, len);
        apdu.setOutgoingAndSend((short) 0, len);
    }

    // Phuong thuc lay chieu dai thuc te cua mang
    private short getActualLength(byte[] data) {
        short length = 0;
        for (short i = 0; i < data.length; i++) {
            if (data[i] == (byte) 0x00) {
                break;
            }
            length++;
        }
        return length;
    }
}
