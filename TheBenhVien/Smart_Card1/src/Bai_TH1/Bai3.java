package Bai_TH1;

import javacard.framework.*;
/*
	/select A000000151000003
	/send 0001000004000A0014 (10+20)
	/send 0002000004000A0002 (10-2)
	/send 0003000004000A0002 (10*2)
	/send 0004000004000A0002 (10/2)
	


*/
public class Bai3 extends Applet
{

	public static void install(byte[] bArray, short bOffset, byte bLength) 
	{
		new Bai3().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
	}
// Phuongng thuc xu lý APDU
    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        apdu.setIncomingAndReceive();

        // Lay các toán hang tu APDU
        short operand1 = (short) ((buffer[ISO7816.OFFSET_CDATA] << 8) | (buffer[ISO7816.OFFSET_CDATA + 1] & 0xFF));
        short operand2 = (short) ((buffer[ISO7816.OFFSET_CDATA + 2] << 8) | (buffer[ISO7816.OFFSET_CDATA + 3] & 0xFF));
        byte operation = buffer[ISO7816.OFFSET_INS]; // Ly mã lnh INS  xác nh phép toán

        short result = 0;
        switch (operation) {
            case 0x01: // Phép cong
                result = (short) (operand1 + operand2);
                break;
            case 0x02: // Phép tru
                result = (short) (operand1 - operand2);
                break;
            case 0x03: // Phép nhân
                result = (short) (operand1 * operand2);
                break;
            case 0x04: // Phép chia
                if (operand2 == 0) {
                    ISOException.throwIt(ISO7816.SW_WRONG_DATA); // Ném ngoi l nu chia cho 0
                }
                result = (short) (operand1 / operand2);
                break;
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED); // Lnh không c h tr
        }

        // Chuan bi du lieu phan hoi
        buffer[0] = (byte) (result >> 8); // Byte cao cua ket qua
        buffer[1] = (byte) (result & 0xFF); // Byte thap cua ket qua

        apdu.setOutgoingAndSend((short) 0, (short) 2); // Gui ket qua (2 byte)
    }

}
