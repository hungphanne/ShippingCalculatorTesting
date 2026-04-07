import static org.junit.jupiter.api.Assertions.*;

import org.example.ShippingCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ShippingCalculatorTest {

    private final ShippingCalculator calculator = new ShippingCalculator();

    // Giá trị danh định (Nominal values) để giữ cố định khi test biến còn lại
    private static final double NOM_DIST = 25.0;
    private static final double NOM_WGHT = 15.0;

    @Test
    void testBaseFee() {
        // Kiểm thử biên dưới: 0.1km và 0.1kg -> Phải là phí cơ bản
        assertEquals(15000, calculator.calculateFee(0.1, 0.1));
    }

    @Test
    void testBoundaryDistance() {
        // Ngay biên 2km: Vẫn là 15k
        assertEquals(15000, calculator.calculateFee(2.0, 3.0));
        // Ngay trên biên 2.1km: 15000 + (0.1 * 5000) = 15500
        assertEquals(15500, calculator.calculateFee(2.1, 3.0));
    }

    @Test
    void testMaxBoundary() {
        // Kiểm thử biên trên tối đa: 50km và 30kg
        // Phí = 15k + (48km * 5k) + (27kg * 4k) = 15k + 240k + 108k = 363k
        assertEquals(363000, calculator.calculateFee(50.0, 30.0));
    }

    @Test
    void testInvalidInput() {
        // Kiểm thử giá trị ngoài biên (ngoài 50km) -> Phải ném ra ngoại lệ
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFee(50.1, 10.0);
        });
    }

    // --- NHÓM 1: KIỂM THỬ BIÊN CHO QUÃNG ĐƯỜNG (DISTANCE) ---
    // Giữ nguyên Khối lượng = 15.0kg

    @Test
    @DisplayName("Test Distance: Min (0.1km)")
    void testDistMin() {
        // Phí = 15k (base) + 0 (extra dist) + (12kg * 4k) = 63000
        assertEquals(63000, calculator.calculateFee(0.1, NOM_WGHT));
    }

    @Test
    @DisplayName("Test Distance: Min+ (0.2km)")
    void testDistMinPlus() {
        assertEquals(63000, calculator.calculateFee(0.2, NOM_WGHT));
    }

    @Test
    @DisplayName("Test Distance: Nominal (25.0km)")
    void testDistNominal() {
        // Phí = 15k + (23km * 5k) + (12kg * 4k) = 15k + 115k + 48k = 178000
        assertEquals(178000, calculator.calculateFee(NOM_DIST, NOM_WGHT));
    }

    @Test
    @DisplayName("Test Distance: Max- (49.9km)")
    void testDistMaxMinus() {
        // Phí = 15k + (47.9 * 5k) + 48k = 15k + 239.5k + 48k = 302500
        assertEquals(302500, calculator.calculateFee(49.9, NOM_WGHT));
    }

    @Test
    @DisplayName("Test Distance: Max (50.0km)")
    void testDistMax() {
        // Phí = 15k + (48 * 5k) + 48k = 303000
        assertEquals(303000, calculator.calculateFee(50.0, NOM_WGHT));
    }

    // --- NHÓM 2: KIỂM THỬ BIÊN CHO KHỐI LƯỢNG (WEIGHT) ---
    // Giữ nguyên Quãng đường = 25.0km

    @Test
    @DisplayName("Test Weight: Min (0.1kg)")
    void testWeightMin() {
        // Phí = 15k + (23km * 5k) + 0 (extra weight) = 130000
        assertEquals(130000, calculator.calculateFee(NOM_DIST, 0.1));
    }

    @Test
    @DisplayName("Test Weight: Min+ (0.2kg)")
    void testWeightMinPlus() {
        assertEquals(130000, calculator.calculateFee(NOM_DIST, 0.2));
    }

    @Test
    @DisplayName("Test Weight: Max- (29.9kg)")
    void testWeightMaxMinus() {
        // Phí = 130k + (26.9kg * 4k) = 130k + 107.6k = 237600
        assertEquals(237600, calculator.calculateFee(NOM_DIST, 29.9));
    }

    @Test
    @DisplayName("Test Weight: Max (30.0kg)")
    void testWeightMax() {
        // Phí = 130k + (27kg * 4k) = 130k + 108k = 238000
        assertEquals(238000, calculator.calculateFee(NOM_DIST, 30.0));
    }

    // --- NHÓM 3: KIỂM THỬ GIÁ TRỊ NGOÀI BIÊN (ROBUSTNESS) ---

    @Test
    @DisplayName("Test Invalid Distance: 50.1km")
    void testInvalidDistance() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFee(50.1, NOM_WGHT));
    }

    @Test
    @DisplayName("Test Invalid Weight: 30.1kg")
    void testInvalidWeight() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFee(NOM_DIST, 30.1));
    }

    // --- NHÓM 4: KIỂM THỬ DÒNG ĐIỀU KHIỂN

    @Test
    @DisplayName("ID 1: Không phụ phí (Distance <= 2, Weight <= 3)")
    void testCase1() {
        assertEquals(15000, calculator.calculateFee(1.5, 2.4));
    }

    @Test
    @DisplayName("ID 2: Cả hai phụ phí (Distance > 2, Weight > 3)")
    void testCase2() {
        // Phí: 15000 + (4.3-2.0)*5000 + (10.5-3.0)*4000 = 56500
        assertEquals(56500, calculator.calculateFee(4.3, 10.5));
    }

    @Test
    @DisplayName("ID 3: Chỉ phụ phí quãng đường (Distance > 2, Weight <= 3)")
    void testCase3() {
        // Phí: 15000 + (3.0-2.0)*5000 = 20000
        assertEquals(20000, calculator.calculateFee(3.0, 2.0));
    }

    @Test
    @DisplayName("ID 4: Chỉ phụ phí khối lượng (Distance <= 2, Weight > 3)")
    void testCase4() {
        // Phí: 15000 + (5.0-3.0)*4000 = 23000
        assertEquals(23000, calculator.calculateFee(1.0, 5.0));
    }

    // --- NHÓM 5: KIỂM THỬ DÒNG DỮ LIỆU

    @Test
    void testCalculateFee_AllUses_Case1() {
        // Path: 1->2->3->4T->5->6T->7->8->EXIT (Cả d và w đều vượt limit)
        long expected = 15900;
        assertEquals(expected, calculator.calculateFee(2.1, 3.1));
    }

    @Test
    void testCalculateFee_AllUses_Case2() {
        // Path: 1->2->3->4F->6F->8->EXIT (Cả d và w đều trong limit)
        assertEquals(15000, calculator.calculateFee(0.1, 2.9));
    }

    @Test
    void testCalculateFee_AllUses_Case3() {
        // Path: 1->2->3->4F->6T->7->8->EXIT (Chỉ w vượt limit)
        assertEquals(15400, calculator.calculateFee(1.9, 3.1));
    }

    @Test
    void testCalculateFee_AllUses_Case4() {
        // Path: 1->2->3->4T->5->6F->8->EXIT (Chỉ d vượt limit)
        assertEquals(15500, calculator.calculateFee(2.1, 0.1));
    }

}