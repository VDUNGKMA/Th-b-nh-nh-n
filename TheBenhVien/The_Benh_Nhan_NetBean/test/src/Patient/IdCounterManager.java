/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Patient;

import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IdCounterManager {

    // Bản đồ để lưu số đếm (counter) dựa trên timestamp
    private static final ConcurrentHashMap<String, AtomicInteger> counterMap = new ConcurrentHashMap<>();

    /**
     * Lấy số đếm hiện tại và tăng lên 1 dựa trên timestamp.
     *
     * @param timestamp Chuỗi timestamp (yyyyMMddHHmm).
     * @return Số đếm sau khi tăng.
     */
    public static int getAndUpdateCounter(String timestamp) {
        // Nếu chưa có counter cho timestamp này, tạo mới với giá trị ban đầu là 1
        counterMap.putIfAbsent(timestamp, new AtomicInteger(1));

        // Tăng giá trị counter lên 1 và trả về giá trị trước khi tăng
        return counterMap.get(timestamp).getAndIncrement();
    }

    /**
     * Xóa các counter đã cũ để tiết kiệm bộ nhớ (nếu cần).
     * Có thể gọi định kỳ nếu lưu trữ nhiều timestamp.
     */
    public static void cleanupOldCounters() {
        // Thời điểm hiện tại
        long currentTime = System.currentTimeMillis();

        // Xóa các counter đã cũ hơn một khoảng thời gian nhất định (ví dụ: 1 giờ)
        long cleanupThreshold = currentTime - 3600 * 1000;

        counterMap.keySet().removeIf(timestamp -> {
            try {
                long timeInMillis = new SimpleDateFormat("yyyyMMddHHmm").parse(timestamp).getTime();
                return timeInMillis < cleanupThreshold;
            } catch (Exception e) {
                // Nếu parse thất bại (không hợp lệ), xóa luôn
                return true;
            }
        });
    }
}
