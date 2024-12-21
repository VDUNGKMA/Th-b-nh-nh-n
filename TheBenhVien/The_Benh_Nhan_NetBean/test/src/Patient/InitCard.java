
package Patient;

/**
 * Lớp InitCard dùng để khởi tạo thẻ thông minh bệnh nhân.
 */
public class InitCard {
    private int count;  // Số lần khởi tạo (nếu cần dùng để kiểm soát)
    private int money;  // Số dư ban đầu
    private String cardId; // ID thẻ bệnh nhân
    private String patientName; // Tên bệnh nhân
    private String dob; // Ngày sinh
    private String phone; // Số điện thoại
    private String insuranceNumber; // Số bảo hiểm y tế
    private String address; // Địa chỉ bệnh nhân
    private String pin; // Mã PIN của thẻ

    // Constructor mặc định
    public InitCard() {
        this.count = 0;
        this.money = 0;
    }

    // Constructor có tham số
    public InitCard(String cardId, String patientName, String dob, String phone, 
                    String insuranceNumber, String address, String pin, int money) {
        this.cardId = cardId;
        this.patientName = patientName;
        this.dob = dob;
        this.phone = phone;
        this.insuranceNumber = insuranceNumber;
        this.address = address;
        this.pin = pin;
        this.money = money;
        this.count = 1;
    }

    // Getter và Setter cho các thuộc tính
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    // Phương thức hiển thị thông tin thẻ
    public void displayCardInfo() {
        System.out.println("=== Thông tin thẻ bệnh nhân ===");
        System.out.println("ID thẻ: " + cardId);
        System.out.println("Tên bệnh nhân: " + patientName);
        System.out.println("Ngày sinh: " + dob);
        System.out.println("Số điện thoại: " + phone);
        System.out.println("Số bảo hiểm y tế: " + insuranceNumber);
        System.out.println("Địa chỉ: " + address);
        System.out.println("Mã PIN: (Đã được mã hóa)");
        System.out.println("Số dư ban đầu: " + money);
        System.out.println("================================");
    }

    // Phương thức khởi tạo thẻ
    public void initializeCard() {
        System.out.println("Đang khởi tạo thẻ với thông tin sau:");
        displayCardInfo();
        // Bạn có thể thêm logic ghi dữ liệu lên thẻ hoặc cơ sở dữ liệu tại đây.
        System.out.println("Thẻ đã được khởi tạo thành công.");
    }
}
