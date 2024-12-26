package Bai_TH4;

import javacard.framework.*;


public class Bai_TH4 extends Applet {
    // AID: [‘D’, ‘I’, ‘E’, ‘M’, ‘_’, ‘T’, ‘H’, ‘I’]
    private static final byte[] APPLET_AID = {0x44, 0x49, 0x45, 0x4D, 0x5F, 0x54, 0x48, 0x49};

    // ID sinh viên: [‘S’, ‘V’, ‘0’, ‘1’]
    private static final byte[] STUDENT_ID = {0x53, 0x56, 0x30, 0x31};

    // S môn thi ti a
    private static final byte MAX_SUBJECTS = 0x09;

    // Các mã lnh APDU
    private static final byte INS_INPUT_SCORE = 0x01;
    private static final byte INS_PRINT_SCORES = 0x02;
    private static final byte INS_UPDATE_SCORE = 0x03;
    private static final byte INS_DELETE_SCORE = 0x04;

    // Mng lu im thi
    private byte[] diemThi;
    private byte currentSubjectCount;

    // Constructor
    private Bai_TH4() {
        diemThi = new byte[MAX_SUBJECTS * 2]; // Mi môn: 1 byte ID môn, 1 byte im
        currentSubjectCount = 0;
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Bai_TH4().register();
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        byte ins = buffer[ISO7816.OFFSET_INS];

        switch (ins) {
            case INS_INPUT_SCORE:
                inputScore(apdu);
                break;
            case INS_PRINT_SCORES:
                printScores(apdu);
                break;
            case INS_UPDATE_SCORE:
                updateScore(apdu);
                break;
            case INS_DELETE_SCORE:
                deleteScore(apdu);
                break;
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    private void inputScore(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        byte subjectId = buffer[ISO7816.OFFSET_CDATA];
        byte score = buffer[ISO7816.OFFSET_CDATA + 1];

        // Kim tra s môn thi ti a
        if (currentSubjectCount >= MAX_SUBJECTS) {
            ISOException.throwIt(ISO7816.SW_FILE_FULL);
        }

        // Kim tra trùng ID môn hc
        for (short i = 0; i < (short) (currentSubjectCount * 2); i += 2) {
            if (diemThi[i] == subjectId) {
                ISOException.throwIt(ISO7816.SW_DATA_INVALID);
            }
        }

        // Lu môn thi và im
        short index = (short) (currentSubjectCount * 2);
        diemThi[index] = subjectId;
        diemThi[(short) (index + 1)] = score;
        currentSubjectCount++;

        printScores(apdu); // T ng in im sau khi nhp
    }

    private void printScores(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short length = (short) (currentSubjectCount * 2);
        Util.arrayCopyNonAtomic(diemThi, (short) 0, buffer, (short) 0, length);
        apdu.setOutgoingAndSend((short) 0, length);
    }

    private void updateScore(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        byte subjectId = buffer[ISO7816.OFFSET_CDATA];
        byte newScore = buffer[ISO7816.OFFSET_CDATA + 1];

        // Tìm và cp nht im môn hc
        boolean found = false;
        for (short i = 0; i < (short) (currentSubjectCount * 2); i += 2) {
            if (diemThi[i] == subjectId) {
                diemThi[(short) (i + 1)] = newScore;
                found = true;
                break;
            }
        }

        if (!found) {
            ISOException.throwIt(ISO7816.SW_DATA_INVALID);
        }

        printScores(apdu); // T ng in im sau khi cp nht
    }

    private void deleteScore(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        byte subjectId = buffer[ISO7816.OFFSET_CDATA];

        // Tìm và xóa môn hc
        boolean found = false;
        for (short i = 0; i < (short) (currentSubjectCount * 2); i += 2) {
            if (diemThi[i] == subjectId) {
                found = true;
                // Dch chuyn các phn t còn li
                short lengthToMove = (short) ((currentSubjectCount * 2) - i - 2);
                Util.arrayCopyNonAtomic(diemThi, (short) (i + 2), diemThi, i, lengthToMove);
                currentSubjectCount--;
                break;
            }
        }

        if (!found) {
            ISOException.throwIt(ISO7816.SW_DATA_INVALID);
        }

        printScores(apdu); // T ng in im sau khi xóa
    }
}
