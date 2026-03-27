Hệ thống tính phí vận chuyển thông minh cho Shipper.

Cho bài toán: 
Hệ thống nhận vào 2 biến số để tính tiền ship cho một đơn hàng:
- khoangCach (d): Đơn vị km (số thực, giới hạn từ 0.1 đến 50.0 km).
- khoiLuong (w): Đơn vị kg (số thực, giới hạn từ 0.1 đến 30.0 kg).

Quy tắc tính phí:
- Phí cơ bản: 15.000 VNĐ cho 2km đầu tiên và khối lượng dưới 3kg.
- Nếu quãng đường > 2km: Mỗi km tiếp theo cộng thêm 5.000 VNĐ.
- Nếu khối lượng > 3kg: Mỗi kg tiếp theo cộng thêm 4.000 VNĐ phí cồng kềnh.
- Giới hạn: Không nhận đơn hàng xa quá 50km hoặc nặng quá 30kg.
