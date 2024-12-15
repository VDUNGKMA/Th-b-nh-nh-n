create database javacard;
use javacard;


-- Xóa các bảng theo thứ tự phụ thuộc
DROP TABLE IF EXISTS avatars;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS cards;
DROP TABLE IF EXISTS users;

-- Tạo lại các bảng

-- Bảng `users`
CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,            -- Mã định danh người dùng (thẻ)
    name VARCHAR(100) NOT NULL,            -- Họ và tên
    dob DATE,                              -- Ngày sinh
    phone VARCHAR(20),                     -- Số điện thoại
    insurance_number VARCHAR(50),          -- Số bảo hiểm y tế
    address VARCHAR(255),                  -- Địa chỉ
    pin_hash VARCHAR(255) NOT NULL,        -- Băm của mã PIN để xác thực
    public_key TEXT NOT NULL,              -- Khóa công khai dùng để xác thực chữ ký số
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Ngày tạo
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Ngày cập nhật
);

-- Bảng `cards`
CREATE TABLE cards (
    card_id VARCHAR(50) PRIMARY KEY,       -- Mã định danh thẻ
    user_id VARCHAR(50),                   -- ID người dùng (liên kết với bảng `users`)
    balance DECIMAL(10, 2) DEFAULT 0.00,   -- Số dư tài khoản trên thẻ
    failed_attempts INT DEFAULT 0,         -- Số lần nhập sai mã PIN
    is_locked BOOLEAN DEFAULT FALSE,       -- Trạng thái thẻ (bị khóa hay không)
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Bảng `transactions`
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY, -- ID giao dịch
    card_id VARCHAR(50),                           -- Mã thẻ liên quan
    type ENUM('deposit', 'payment') NOT NULL,      -- Loại giao dịch (nạp tiền hoặc thanh toán)
    amount DECIMAL(10, 2) NOT NULL,                -- Số tiền giao dịch
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời điểm thực hiện
    FOREIGN KEY (card_id) REFERENCES cards(card_id) ON DELETE CASCADE
);

-- Bảng `avatars`
CREATE TABLE avatars (
    user_id VARCHAR(50) PRIMARY KEY,              -- ID người dùng (liên kết với bảng `users`)
    image BLOB NOT NULL,                          -- Ảnh đại diện
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời điểm tải lên
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
select * from users where id ='b8c8de4a-5b4c-4e75-8ab9-79255c5c6887';


