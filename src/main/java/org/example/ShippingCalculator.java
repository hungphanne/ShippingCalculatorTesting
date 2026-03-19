package org.example;

/**
 * Lớp tính toán phí vận chuyển dựa trên khoảng cách và khối lượng.
 *
 */
public class ShippingCalculator {

    // Định nghĩa các hằng số để code sạch và dễ bảo trì
    private static final long BASE_FEE = 15000;
    private static final double DISTANCE_LIMIT_KM = 2.0;
    private static final double WEIGHT_LIMIT_KG = 3.0;
    private static final long DISTANCE_SURCHARGE_PER_KM = 5000;
    private static final long WEIGHT_SURCHARGE_PER_KG = 4000;

    private static final double MAX_DISTANCE = 50.0;
    private static final double MAX_WEIGHT = 30.0;
    private static final double MIN_VALUE = 0.1;

    /**
     * Hàm tính tổng phí vận chuyển.
     * @param distance Quãng đường (km)
     * @param weight Khối lượng (kg)
     * @return Tổng phí vận chuyển (VNĐ)
     * @throws IllegalArgumentException nếu đầu vào ngoài phạm vi cho phép
     */
    public long calculateFee(double distance, double weight) {
        // Kiểm tra điều kiện biên đầu vào
        validateInput(distance, weight);

        long totalFee = BASE_FEE;

        // Tính phụ phí quãng đường (nếu > 2km)
        if (distance > DISTANCE_LIMIT_KM) {
            double extraDistance = distance - DISTANCE_LIMIT_KM;
            totalFee += (long) (extraDistance * DISTANCE_SURCHARGE_PER_KM);
        }

        // Tính phụ phí khối lượng (nếu > 3kg)
        if (weight > WEIGHT_LIMIT_KG) {
            double extraWeight = weight - WEIGHT_LIMIT_KG;
            totalFee += (long) (extraWeight * WEIGHT_SURCHARGE_PER_KG);
        }

        return totalFee;
    }

    private void validateInput(double distance, double weight) {
        if (distance < MIN_VALUE || distance > MAX_DISTANCE) {
            throw new IllegalArgumentException("Quãng đường không hợp lệ (0.1 - 50km)");
        }
        if (weight < MIN_VALUE || weight > MAX_WEIGHT) {
            throw new IllegalArgumentException("Khối lượng không hợp lệ (0.1 - 30kg)");
        }
    }
}