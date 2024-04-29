package rikkei.academy.model.entity;

public enum OrderStatusName {
    WAITING, // Đơn hàng mới, chờ xác nhận
    CONFIRM, // Đã xác nhận
    DELIVERY, // Đang giao hàng
    SUCCESS, // Đã giao hàng
    CANCEL, // Đã huỷ đơn
    DENIED // Bị từ chối
}
