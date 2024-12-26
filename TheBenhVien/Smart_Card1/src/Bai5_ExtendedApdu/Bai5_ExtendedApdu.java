package Bai5_ExtendedApdu;


import javacard.framework.*;
import javacardx.apdu.ExtendedLength;

public class Bai5_ExtendedApdu extends Applet implements ExtendedLength
{
    private static byte temp[];
    private final static short MAX_SIZE = (short)4096;
    private final static byte INS_NHAP = (byte)0x01;
    private final static byte INS_XUAT = (byte)0x02;
    private static short dataLen;

    public static void install(byte[] bArray, short bOffset, byte bLength)
    {
        new Bai5_ExtendedApdu().register(bArray, (short)(bOffset + 1), bArray[bOffset]);
        temp = new byte[MAX_SIZE];
    }

    public void process(APDU apdu)
    {
        if (selectingApplet())
        {
            return;
        }

        byte[] buf = apdu.getBuffer();

        short recvLen = apdu.setIncomingAndReceive();

        short pointer = 0; // vi tri con tro trong temp

        switch (buf[ISO7816.OFFSET_INS])
        {
            case INS_NHAP: // nhan du lieu gui tu may tinh
                // lay ra do dai du lieu gui xuong
                dataLen = apdu.getIncomingLength();
                if (dataLen > MAX_SIZE)
                {
                    ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                }
                // lay ra vi tri bat dau data
                short dataOffset = apdu.getOffsetCdata();
                pointer = 0;
                while (recvLen > 0)
                {
                    // copy du lieu nhan duoc tu apdu buffer vao mang temp
                    Util.arrayCopy(buf, dataOffset, temp, pointer, recvLen);
                    pointer += recvLen;

                    // tiep tuc nhan du lieu va ghi vao apdu buffer tai vi tri dataOffset
                    recvLen = apdu.receiveBytes(dataOffset);
                }
                break;

            case INS_XUAT: // gui du lieu len may tinh
                short toSend = dataLen;
                short le = apdu.setOutgoing(); // do dai du lieu toi da gui len may tinh
                apdu.setOutgoingLength(toSend);
                short sendLen = 0;
                pointer = 0;
                while (toSend > 0)
                {
                    sendLen = (toSend > le) ? le : toSend;

                    apdu.sendBytesLong(temp, pointer, sendLen);
                    toSend -= sendLen;
                    pointer += sendLen;
                }
                break;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
}
