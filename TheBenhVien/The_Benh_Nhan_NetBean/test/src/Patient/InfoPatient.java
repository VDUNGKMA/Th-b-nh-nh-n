
package Patient;


public class InfoPatient {
      private static InfoPatient instance;
    private String id;
    private String name;
    private String address;
    private String phone;
    private String pin;
    private byte[] image;
    private String dob; // Ngày sinh
    private String insuranceNumber; // Số bảo hiểm y tế
    private int tk; // Tài khoản
    private int score; // Điểm (nếu cần)
    private long pay; // Thanh toán (nếu cần)

    
    public static InfoPatient getInstance() {
        if (instance == null) {
            instance = new InfoPatient();
        }
        return instance;
    }
    // Getter và Setter cho id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter và Setter cho phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter và Setter cho pin
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    // Getter và Setter cho image
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    // Getter và Setter cho dob (Ngày sinh)
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    // Getter và Setter cho insuranceNumber (Số bảo hiểm y tế)
    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    // Getter và Setter cho tk (Tài khoản)
    public int getTk() {
        return tk;
    }

    public void setTk(int tk) {
        this.tk = tk;
    }

    // Getter và Setter cho score (Điểm)
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Getter và Setter cho pay (Thanh toán)
    public long getPay() {
        return pay;
    }

    public void setPay(long pay) {
        this.pay = pay;
    }
}
