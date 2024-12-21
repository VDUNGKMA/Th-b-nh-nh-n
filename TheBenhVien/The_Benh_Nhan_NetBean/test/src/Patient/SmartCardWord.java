/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Patient;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;


public class SmartCardWord {

    public static final byte[] AID_APPLET = {(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x00, (byte) 0x01};//aid
    private Card card;
    private TerminalFactory factory;
    private CardChannel channel;
    private CardTerminal terminal;
    private List<CardTerminal> terminals;
    private ResponseAPDU response, response1;
    CommandAPDU cmndAPDU;
    ResponseAPDU resAPDU;
    ImageIcon icon = new ImageIcon("src/img/meo-khoc.png");

    public SmartCardWord() {
    }

    public static void main(String[] args) {

    }

//    public boolean connectCard() {
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            System.out.println("Danh sách terminal:");
//            for (CardTerminal t : terminals) {
//                System.out.println(t.getName());
//            }
//            terminal = terminals.get(0);
//             System.out.println("Đang sử dụng terminal: " + terminal.getName());
//             // Kiểm tra thẻ
//    if (!terminal.isCardPresent()) {
//        System.out.println("Thẻ chưa được chèn.");
//    }
////            card = terminal.connect("T=1");
//            card = terminal.connect("T=1"); // Kết nối với bất kỳ giao thức nào khả dụng
//
//            channel = card.getBasicChannel();
//              System.out.println("Kết nối với thẻ thành công.");
//            if (channel == null) {
//                return false;
//            }
//            response = channel.transmit(new CommandAPDU(0x00, (byte) 0xA4, 0x04, 0x00, AID_APPLET));
//            String check = Integer.toHexString(response.getSW());
////            if (check.equals("9000")) {
////                return true;
////            } else if (check.equals("6400")) {
////                JOptionPane.showMessageDialog(null, "Thẻ đã bị khóa");
////                return true;
////            } else {
////                return false;
////            }
////        } catch (Exception e) {
////        }
////        return false;
//            if ("9000".equals(check)) {
//                return true;
//            } else if ("6400".equals(check)) {
//                JOptionPane.showMessageDialog(null, "Thẻ đã bị khóa");
//                return false;
//            }
//            return false;
//        } catch (HeadlessException | CardException e) {
//            JOptionPane.showMessageDialog(null, e.getMessage());
//        }
//        return false;
//    }
    public boolean connectCard() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();

            if (terminals.isEmpty()) {
                System.out.println("Khong tim thay thiet bi dau doc nao.");
                return false;
            }

            System.out.println("Danh sach thiet bi dau doc:");
            for (CardTerminal t : terminals) {
                System.out.println("- " + t.getName());
            }

            // Chọn terminal đầu tiên
            terminal = terminals.get(0);
            System.out.println("Su dung dau doc: " + terminal.getName());

            // Kiểm tra thẻ đã được chèn chưa
            if (!terminal.isCardPresent()) {
                System.out.println("The chua duoc chen.");
                return false;
            }

            // Kiểm tra giao thức hiện tại trước khi kết nối
            String protocol = "T=1"; // Ưu tiên giao thức T=1
            if (card != null) {
                protocol = card.getProtocol();
                System.out.println("The da duoc ket noi voi giao thuc: " + protocol);
            } else {
                card = terminal.connect(protocol);
                System.out.println("Ket noi thanh cong voi giao thuc: " + card.getProtocol());
            }

            // Kết nối kênh cơ bản
            channel = card.getBasicChannel();
            if (channel == null) {
                System.out.println("Khong the mo kenh co ban.");
                return false;
            }

            // Gửi APDU chọn applet
            response = channel.transmit(new CommandAPDU(0x00, (byte) 0xA4, 0x04, 0x00, AID_APPLET));
            String check = Integer.toHexString(response.getSW());
            System.out.println("Ma trang thai APDU: " + check);

            if ("9000".equals(check)) {
                System.out.println("Applet da duoc chon thanh cong.");
                return true;
            } else if ("6400".equals(check)) {
                System.out.println("The da bi khoa.");
                return false;
            } else if ("6a82".equals(check)) {
                System.out.println("Applet khong ton tai tren the.");
                return false;
            } else {
                System.out.println("Khong the chon applet. Ma trang thai: " + check);
                return false;
            }
        } catch (CardException e) {
            System.err.println("Loi khi ket noi voi the: " + e.getMessage());
        }

        return false;
    }

    public void sendAPDUtoApplet(byte[] cmnds, byte[] data) {
        try {
            resAPDU = channel.transmit(new CommandAPDU(cmnds[0], cmnds[1], cmnds[2], cmnds[3], data));
        } catch (CardException e) {
            e.printStackTrace();
        }
    }

    public void sendAPDUtoApplet(byte[] cmnds) {
        try {
            resAPDU = channel.transmit(new CommandAPDU(cmnds[0], cmnds[1], cmnds[2], cmnds[3]));
        } catch (CardException e) {
            e.printStackTrace();
        }
    }

    public byte[] sendAPDU(byte[] apduCommand) throws Exception {
        CommandAPDU commandAPDU = new CommandAPDU(apduCommand);
        ResponseAPDU responseAPDU = channel.transmit(commandAPDU);
        return responseAPDU.getBytes();
    }
//    public String login(byte[] data) {
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//            if (channel == null) {
//                return "0";
//            }
//            System.err.println("ok" + data.length);
//            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x01, data));
//            String check = Integer.toHexString(response.getSW());
//            String res = String.format("%x", new BigInteger(1, response.getData()));
//            System.out.println(String.format("%x", new BigInteger(1, response.getData())));
//            return res;
//
//        } catch (CardException e) {
//        }
//        return "0";
//    }
    // Phương thức hỗ trợ chuyển đổi byte sang Hex

    public static String toHex(byte[] data, short offset, short length) {
        StringBuilder hexString = new StringBuilder();
        for (short i = offset; i < offset + length; i++) {
            String hex = Integer.toHexString(0xFF & data[i]);
            if (hex.length() == 1) {
                hexString.append('0'); // Thêm số 0 nếu ký tự chỉ có 1 chữ số
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String login(byte[] pinHash) {
        try {
            // Kết nối với terminal
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();

            if (terminals.isEmpty()) {
                System.err.println("Không tìm thấy đầu đọc thẻ.");
                return "0";
            }

            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();

            if (channel == null) {
                System.err.println("Không thể lấy kênh cơ bản từ thẻ.");
                return "0";
            }

            // Gửi mã băm lên thẻ
            CommandAPDU apdu = new CommandAPDU((byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x01, pinHash);
            ResponseAPDU response = channel.transmit(apdu);

            // Kiểm tra mã trạng thái trả về
            String statusWord = Integer.toHexString(response.getSW());
            System.out.println("SW trả về từ thẻ: " + statusWord);

            if ("9000".equals(statusWord)) {
                byte[] responseData = response.getData();
                if (responseData != null && responseData.length > 0) {
                    return String.format("%x", new BigInteger(1, responseData));
                } else {
                    System.err.println("Không nhận được dữ liệu từ thẻ.");
                    return "0";
                }
            } else {
                System.err.println("Thẻ trả về mã trạng thái lỗi: " + statusWord);
                return "0";
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình kết nối với thẻ.");
            e.printStackTrace();
            return "0";
        }
    }

    public String unblockcard() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            if (channel == null) {
                return "0";
            }
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x03, (byte) 0x01, (byte) 0x01));
            String check = Integer.toHexString(response.getSW());
            System.out.println("check " + check);
            String res = String.format("%x", new BigInteger(1, response.getData()));
            return res;
        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return "0";
    }

//    public boolean uploadAvatar(byte[] data) {
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//            if (channel == null) {
//                return false;
//            }
//            String dataLc = String.valueOf(data.length);
//
//            System.out.println("ok " + String.format("%x", new BigInteger(1, data)));
//            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x08, (byte) 0x01, (byte) 0x01, data));
//            System.out.println("response " + response);
//            String check = Integer.toHexString(response.getSW());
//            switch (check) {
//                case "9000":
//                    return true;
//                case "6400":
//                    JOptionPane.showMessageDialog(null, "Upload that bai");
//                    return true;
//                default:
//                    return false;
//            }
//
//        } catch (HeadlessException | CardException e) {
//        }
//        return false;
//    }
//public boolean uploadAvatar(byte[] data) {
//    try {
//        // Kết nối với thiết bị đầu đọc thẻ
//        TerminalFactory factory = TerminalFactory.getDefault();
//        CardTerminal terminal = factory.terminals().list().get(0);
//        card = terminal.connect("T=1");
//        channel = card.getBasicChannel();
//
//        if (channel == null) {
//            System.out.println("Không thể kết nối với thẻ.");
//            return false;
//        }
//
//        // Kích thước tối đa mỗi lần gửi (255 bytes)
//        int chunkSize = 255; 
//        int offset = 0;
//
//        // Gửi dữ liệu theo từng chunk
//        while (offset < data.length) {
//            int len = Math.min(chunkSize, data.length - offset); // Tính kích thước dữ liệu cần gửi
//
//            // Tạo APDU lệnh
//            CommandAPDU command = new CommandAPDU(
//                0x00,      // CLA
//                0x08,      // INS (Instruction)
//                (byte) (offset >> 8),  // P1 (Offset high byte)
//                (byte) (offset & 0xFF), // P2 (Offset low byte)
//                data, offset, len      // Dữ liệu
//            );
//
//            // Gửi APDU
//            ResponseAPDU response = channel.transmit(command);
//
//            // Kiểm tra mã trạng thái (SW)
//            String check = Integer.toHexString(response.getSW());
//            switch (check) {
//                case "9000":
//                    System.out.println("Chunk gửi thành công: " + len + " bytes.");
//                    break;
//                case "6400":
//                    JOptionPane.showMessageDialog(null, "Upload thất bại tại offset: " + offset);
//                    return false;
//                default:
//                    System.out.println("Lỗi không xác định: SW = " + check);
//                    return false;
//            }
//
//            // Tăng offset
//            offset += len;
//        }
//
//        System.out.println("Upload avatar thành công!");
//        return true;
//
//    } catch (CardException e) {
//        System.out.println("Lỗi kết nối thẻ: " + e.getMessage());
//        return false;
//    }
//}
//    public String getAvatar() {
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x09, (byte) 0x01, (byte) 0x01));
//            System.out.println("response " + response);
//            String res = String.format("%x", new BigInteger(1, response.getData()));
//            String check = Integer.toHexString(response.getSW());
//            switch (check) {
//                case "9000":
//                    return res;
//                case "6A83":
//                    return "";
//                default:
//                    return "";
//            }
//
//        } catch (CardException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//        return "";
//    }
    public String getAvatar() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x09, (byte) 0x01, (byte) 0x01));
            System.out.println("response getavatar " + response);
            String res = String.format("%x", new BigInteger(1, response.getData()));
            String check = Integer.toHexString(response.getSW());
            if (check.equals("9000")) {
                return res;
            } else if (check.equals("6A83")) {
                return "";
            } else {
                return "";
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return "";
    }

//public boolean uploadImage(byte[] imageBytes) {
//    try {
//               factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//            if (channel == null) {
//                return false;
//            }
//        final short CHUNK_SIZE = 240; // Kích thước tối đa mỗi gói APDU
//        short offset = 0; // Điểm bắt đầu của gói hiện tại
//        short remaining = (short) imageBytes.length; // Số byte còn lại để gửi
//
//        // Gửi dữ liệu ảnh theo từng gói
//        while (remaining > 0) {
//            short length = (short) Math.min(CHUNK_SIZE, remaining); // Kích thước gói hiện tại
//
//            // Tạo APDU command
//            byte[] apdu = new byte[5 + length]; // 5 byte header + dữ liệu
//            apdu[0] = (byte) 0x00; // CLA: Class byte (giả định là 0x80)
//            apdu[1] = (byte) 0x08; // INS: Instruction byte (giả định là 0x50)
//            apdu[2] = (byte) (offset >> 8); // P1: Byte cao của offset
//            apdu[3] = (byte) (offset & 0xFF); // P2: Byte thấp của offset
//            apdu[4] = (byte) length; // Lc: Số byte dữ liệu trong gói
//            System.arraycopy(imageBytes, offset, apdu, 5, length); // Copy dữ liệu từ mảng imageBytes
//
//            // Gửi APDU tới thẻ và nhận phản hồi
//            ResponseAPDU response = channel.transmit(new CommandAPDU(apdu));
//
//            // Kiểm tra phản hồi từ thẻ
//            int sw = response.getSW();
//            if (sw != 0x9000) { // 0x9000: Success
//                JOptionPane.showMessageDialog(null, "Upload ảnh thất bại tại offset " + offset, "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return false;
//            }
//
//            // Cập nhật offset và remaining
//            offset += length;
//            remaining -= length;
//        }
//
//        JOptionPane.showMessageDialog(null, "Upload ảnh thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//        return true; // Upload thành công
//    } catch (CardException e) {
//        JOptionPane.showMessageDialog(null, "Lỗi giao tiếp với thẻ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//        e.printStackTrace();
//        return false;
//    } catch (Exception e) {
//        JOptionPane.showMessageDialog(null, "Lỗi không xác định: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//        e.printStackTrace();
//        return false;
//    }
//}
//    public boolean uploadImage(byte[] data) {
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//            if (channel == null) {
//                return false;
//            }
////            String dataLc = String.valueOf(data.length);
//
////            System.out.println("ok " + String.format("%x", new BigInteger(1, data)));
//            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x08, (byte) 0x01, (byte) 0x00, data));
//            System.out.println("response " + response);
//            String check = Integer.toHexString(response.getSW());
//            if (check.equals("9000")) {
//                return true;
//            } else if (check.equals("6400")) {
//                JOptionPane.showMessageDialog(null, "Upload that bai");
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (Exception e) {
//        }
//        return false;
//    }
////    
    public BufferedImage getImage(byte[] img) {
//    if (img == null) {
//        System.out.println("Ảnh đầu vào là null.");
//        return null;
//    }
        try {
            byte[] cmd = {(byte) 0x00, (byte) 0x09, (byte) 0x01, (byte) 0x00};
            sendAPDUtoApplet(cmd);
            int sendlen = img.length;
            byte[] cmnd = {(byte) 0x00, (byte) 0x09, (byte) 0x02, (byte) 0x00};
            byte[] resimg = new byte[sendlen];
            int pointer = 0;
            int datalen = 255;

            while (sendlen > 0) {
                sendAPDUtoApplet(cmnd);
                byte[] temp = resAPDU.getData();
                System.arraycopy(temp, 0, resimg, pointer, datalen);
                pointer += 255;
                sendlen -= 255;
                if (sendlen < 255) {
                    datalen = sendlen;
                }
            }

            System.out.println("Ảnh được xử lý: " + resimg.length + " bytes");

            // Tạo BufferedImage từ mảng byte
            ByteArrayInputStream bais = new ByteArrayInputStream(img);
            BufferedImage b = ImageIO.read(bais);

            return b; // Trả về đối tượng BufferedImage

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Đã xảy ra lỗi khi xử lý ảnh.");
            return null;
        }
    }

//    private void getImage(byte[] img) {
//        if (img == null) {
//            return;
//        }
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//             channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x09, (byte) 0x01, (byte) 0x00));
//            int sendlen = img.length;
//            byte[] cmnd = {(byte) 0xA0, (byte) 0x13, (byte) 0x02, (byte) 0x00};
//            byte[] resimg = new byte[sendlen];
//            int pointer = 0;
//            int datalen = 255;
//            while (sendlen > 0) {
////                connect.sendAPDUtoApplet(cmnd);
//
//                response= channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x09, (byte) 0x02, (byte) 0x00));
////                byte[] temp = connect.rAPDU.getData(esAPDU.getData();
//                   byte[] responseData = response.getData();
//            System.arraycopy(responseData, 0, resimg, pointer, datalen);
//                System.arraycopy(responseData, 0, resimg, pointer, datalen);
//                pointer += 255;
//                sendlen -= 255;
//                if (sendlen < 255) {
//                    datalen = sendlen;
//                }
//            }
////            System.out.println("ảnh res:" + resimg);
////            ByteArrayInputStream bais = new ByteArrayInputStream(img);
////            BufferedImage b;
////            b = ImageIO.read(bais);
////            ImageIcon icon = new ImageIcon(b.getScaledInstance(anhthe.getWidth(), anhthe.getHeight(), Image.SCALE_SMOOTH));
////            icon.getImage();
////            anhthe.setIcon(icon);
//        } catch (IOException ex) {
//            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public String checkCard() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            if (channel == null) {
                return "0";
            }
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x07, (byte) 0x01, (byte) 0x01));
            System.out.println("response " + response.getData());
            String res = String.format("%x", new BigInteger(1, response.getData()));
            return res;

        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return "0";
    }

    public boolean initCard(byte[] data) {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            if (channel == null) {
                return false;
            }

            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x01, data));
            System.out.println("response " + response);
            String check = Integer.toHexString(response.getSW());
            switch (check) {
                case "9000":
                    return true;
                case "6400":
                    JOptionPane.showMessageDialog(null, "Upload that bai");
                    return true;
                default:
                    return false;
            }

        } catch (HeadlessException | CardException e) {
        }
        return false;
    }

    public String getInfo() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x06, (byte) 0x01, (byte) 0x01));

            String res = String.format("%x", new BigInteger(1, response.getData()));
            System.out.println("response " + res);
            return res;

        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return "";
    }

//    public String getId() {
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x15, (byte) 0x01, (byte) 0x01));
//
//            String res = String.format("%x", new BigInteger(1, response.getData()));
//            System.out.println("response " + res);
//            return res;
//
//        } catch (CardException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
    //return "";
//    }
// Chuyển đổi byte[] sang chuỗi Hex
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0'); // Thêm số 0 vào đầu nếu cần
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

// Chuyển đổi chuỗi Hex sang byte[]
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        System.out.println("len " + len);
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public String getId() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                System.err.println("Không tìm thấy đầu đọc thẻ nào.");
                return "";
            }

            terminal = terminals.get(0); // Sử dụng terminal đầu tiên
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();

            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x15, (byte) 0x01, (byte) 0x01));

            byte[] rawData = response.getData();
            if (rawData == null || rawData.length == 0) {
                System.err.println("Không có dữ liệu ID thẻ trả về.");
                return "";
            }

            // Sử dụng hàm bytesToHex thay vì javax.xml.bind.DatatypeConverter
            System.out.println("Raw Data (Hex): " + bytesToHex(rawData));

            // Loại bỏ các byte `0x00` dư thừa
            int actualLength = 0;
            for (int i = 0; i < rawData.length; i++) {
                if (rawData[i] != 0x00) {
                    actualLength = i + 1;
                }
            }
            byte[] trimmedData = Arrays.copyOf(rawData, actualLength);

            String cardId = new String(trimmedData, StandardCharsets.UTF_8).trim();
            System.out.println("Card ID (String): " + cardId);

            return cardId;

        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi đọc ID thẻ: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi không xác định: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return "";
    }

    public boolean changeInfo(byte[] data) {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            if (channel == null) {
                return false;
            }
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x05, (byte) 0x01, (byte) 0x01, data));
            System.out.println("response " + response);
            String check = Integer.toHexString(response.getSW());
            switch (check) {
                case "9000":
                    return true;
                case "6400":
                    JOptionPane.showMessageDialog(null, "Update that bai");
                    return true;
                default:
                    return false;
            }

        } catch (HeadlessException | CardException e) {
        }
        return false;
    }

    public boolean changePin(byte[] data) {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            if (channel == null) {
                return false;
            }
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x04, (byte) 0x01, (byte) 0x01, data));
            System.out.println("response " + response);
            String check = Integer.toHexString(response.getSW());
            switch (check) {
                case "9000":
                    return true;
                case "6984":
                    UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Times New Roman", Font.BOLD, 24)));
                    JOptionPane.showMessageDialog(null, "Nhập sai mã pin, mời nhập lại!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                    return false;
                default:
                    return false;
            }

        } catch (HeadlessException | CardException e) {
        }
        return false;
    }

    public BigInteger getModulusPubkey() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x01));

            BigInteger res = new BigInteger(1, response.getData());
            System.out.println("responseM " + res);
            return res;

        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    public BigInteger getExponentPubkey() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x10, (byte) 0x02, (byte) 0x01));

            BigInteger res = new BigInteger(1, response.getData());
            System.out.println("responseE " + res);
            return res;

        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }

    public String getRandomData() {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x12, (byte) 0x01, (byte) 0x01));

            String res = String.format("%x", new BigInteger(1, response.getData()));
            System.out.println("response " + res);
            return res;

        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return "";
    }

    public String getSign(byte[] data) {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x11, (byte) 0x01, (byte) 0x01, data));
            String check = Integer.toHexString(response.getSW());
            if (check.equals("9000")) {
                String res = String.format("%x", new BigInteger(1, response.getData()));
                System.out.println("response " + res);
                return res;
            } else if (check.equals("6984")) {

                return "";
            }

        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return "";
    }

    //check money 
//    public String checkBalance() {
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x12, (byte) 0x01, (byte) 0x01));
//
//            String res = String.format("%x", new BigInteger(1, response.getData()));
//            System.out.println("response " + res);
//            return res;
//
//        } catch (CardException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//        return "";
//    }
    public String checkBalance() {
        try {
            // Lấy đối tượng TerminalFactory và danh sách các thiết bị đầu đọc thẻ
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();

            // Chọn thiết bị đầu đọc thẻ đầu tiên
            terminal = terminals.get(0);

            // Kết nối với thẻ, chế độ T=1
            card = terminal.connect("T=1");

            // Lấy kênh cơ bản của thẻ
            channel = card.getBasicChannel();

            // Gửi lệnh APDU để lấy số dư
            CommandAPDU command = new CommandAPDU((byte) 0x00, (byte) 0x12, (byte) 0x01, (byte) 0x01);
            ResponseAPDU response = channel.transmit(command);

            // Kiểm tra mã trạng thái (SW1 SW2) từ phản hồi, ví dụ 0x90 0x00 là thành công
            byte[] responseData = response.getData();
            if (response.getSW() == 0x9000) {
                // Chuyển dữ liệu thành chuỗi Hex và in ra để kiểm tra
                String res = bytesToHex(responseData);
                System.out.println("response: " + res);
                // Chuyển dữ liệu byte thành long (số dư tài khoản)
                long soDu = bytesToLong(responseData, 0); // Đảm bảo dữ liệu bắt đầu từ byte đầu tiên
                System.out.println("response so du: " + soDu);
                return String.valueOf(soDu); // Trả về số dư
            } else {
                // Nếu mã trạng thái không phải 0x90 0x00, thông báo lỗi
                System.out.println("Lỗi: " + Integer.toHexString(response.getSW()));
                return "Lỗi khi lấy dữ liệu";
            }
        } catch (CardException e) {
            // Xử lý lỗi CardException
            JOptionPane.showMessageDialog(null, "Lỗi khi giao tiếp với thẻ: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return "";
    }

    public long bytesToLong(byte[] bytes, int offset) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result = (result << 8) | (bytes[offset + i] & 0xFF);
        }
        return result;
    }

    //account balance
    public boolean accountBalance(byte[] data) {
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            channel = card.getBasicChannel();
            if (channel == null) {
                return false;
            }
            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x13, (byte) 0x13, (byte) 0x01, data));
            System.out.println("response " + response);
            String check = Integer.toHexString(response.getSW());
            switch (check) {
                case "9000":
                    return true;
                case "6f00":
                    JOptionPane.showMessageDialog(null, "Lỗi nạp tiền!");
                    return false;
                default:
                    return false;
            }

        } catch (HeadlessException | CardException e) {
        }
        return false;
    }
    //pay 

//    public String pay(byte[] data) {
//        try {
//            factory = TerminalFactory.getDefault();
//            terminals = factory.terminals().list();
//            terminal = terminals.get(0);
//            card = terminal.connect("T=1");
//            channel = card.getBasicChannel();
//            response = channel.transmit(new CommandAPDU((byte) 0x00, (byte) 0x14, (byte) 0x01, (byte) 0x01, data));
//
//            String res = String.format("%x", new BigInteger(1, response.getData()));
//            System.out.println("response " + res);
//            return res;
//
//        } catch (CardException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//        return "";
//    }
    public void pay(byte[] amount, boolean useInsurance) throws Exception {
        // Lệnh APDU để gọi hàm aPay
        // CLA = 0x00, INS = 0x01 (aPay), P1 = 0x00, P2 = 0x00
        byte[] apduCommand = new byte[6 + amount.length];
        apduCommand[0] = (byte) 0x00; // CLA
        apduCommand[1] = (byte) 0x14; // INS (aPay)
        apduCommand[2] = (byte) 0x00; // P1
        apduCommand[3] = (byte) 0x00; // P2
        apduCommand[4] = (byte) (amount.length + 1); // Lc (độ dài dữ liệu + 1 byte cho trạng thái bảo hiểm)
        apduCommand[5] = (byte) (useInsurance ? 0x01 : 0x00); // Trạng thái sử dụng bảo hiểm
        System.arraycopy(amount, 0, apduCommand, 6, amount.length); // Sao chép số tiền vào APDU

        // Gửi APDU
        byte[] response = sendAPDU(apduCommand);

        // Kiểm tra kết quả trả về
        if (response[response.length - 2] == (byte) 0x90 && response[response.length - 1] == (byte) 0x00) {
            if (response[0] == (byte) 0x01) {
                // Thanh toán thành công
                JOptionPane.showMessageDialog(null, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Thanh toán thất bại: Số dư không đủ
                JOptionPane.showMessageDialog(null, "Thanh toán thất bại: Số dư không đủ.", "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Lỗi trong quá trình thanh toán
            JOptionPane.showMessageDialog(null, "Lỗi trong quá trình thực hiện thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

    }

    private byte[] intToByteArray(int value) {
        byte[] array = new byte[4];
        array[3] = (byte) (value & 0xFF);
        array[2] = (byte) ((value >> 8) & 0xFF);
        array[1] = (byte) ((value >> 16) & 0xFF);
        array[0] = (byte) ((value >> 24) & 0xFF);
        return array;
    }

    //format
    public String stringToHex(String string) {
        StringBuilder buf = new StringBuilder(200);
        for (char ch : string.toCharArray()) {
            if (buf.length() > 0) {
                buf.append(' ');
            }
            buf.append(String.format("%04x", (int) ch));
        }
        return buf.toString();
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        System.out.println("len " + len);
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public boolean disconnect() {
        try {
            card.disconnect(false);
            return true;
        } catch (CardException e) {
            System.err.println("Loi: " + e);
        }
        return false;
    }

    public short getTotalImageLength() {
        try {
            byte[] cmd = {(byte) 0x00, (byte) 0x17, (byte) 0x00, (byte) 0x00}; // INS_GETIMG với P1 = 0x00
            sendAPDUtoApplet(cmd);
            byte[] response = resAPDU.getData();
            short imageLength = (short) ((response[0] << 8) | (response[1] & 0xFF));

            System.out.println("Tổng kích thước ảnh: " + imageLength);
            return imageLength;
        } catch (Exception e) {
        }
        return 0;
    }

    public byte[] getImage(int imageLength) {
        if (imageLength <= 0) {
            System.out.println("Không có dữ liệu ảnh để lấy.");
            return null;
        }
        try {
            // Khởi tạo lệnh để yêu cầu applet chuẩn bị dữ liệu ảnh
            byte[] cmd = {(byte) 0x00, (byte) 0x09, (byte) 0x01, (byte) 0x00};
            sendAPDUtoApplet(cmd);

            // Dữ liệu ảnh sẽ được đọc
            byte[] resimg = new byte[imageLength];
            byte[] cmnd = {(byte) 0x00, (byte) 0x09, (byte) 0x02, (byte) 0x00};
            int pointer = 0;
            int datalen = 255;

            while (imageLength > 0) {
                sendAPDUtoApplet(cmnd); // Gửi lệnh đọc dữ liệu ảnh
                byte[] temp = resAPDU.getData(); // Lấy dữ liệu trả về
                if (temp == null || temp.length == 0) {
                    System.out.println("Không có dữ liệu trả về từ applet.");
                    break;
                }
                int actualLength = Math.min(temp.length, datalen);
                System.arraycopy(temp, 0, resimg, pointer, actualLength); // Sao chép dữ liệu vào mảng resimg
                pointer += actualLength;
                imageLength -= actualLength;

                if (imageLength < datalen) {
                    datalen = imageLength; // Điều chỉnh kích thước cho lần đọc cuối cùng
                }
            }

            return resimg; // Trả về dữ liệu ảnh
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void setImage(byte[] img) {
        if (img == null) {
            return;
        }
        byte[] cmd = {(byte) 0x00, (byte) 0x08, (byte) 0x01, (byte) 0x00};
        sendAPDUtoApplet(cmd);
        int sendlen = img.length;
        System.out.println("ảnh gửi:" + img);
        byte[] cmnd = {(byte) 0x00, (byte) 0x08, (byte) 0x02, (byte) 0x00};
        int pointer = 0;
        byte[] temp = new byte[255];
        int datalen = 255;
        while (sendlen > 0) {
            System.arraycopy(img, pointer, temp, 0, datalen);
            sendAPDUtoApplet(cmnd, temp);
            pointer += 255;
            sendlen -= 255;
            if (sendlen < 255) {
                datalen = sendlen;
            }
        }
    }
}
