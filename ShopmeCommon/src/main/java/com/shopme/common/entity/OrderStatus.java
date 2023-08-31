package com.shopme.common.entity;

public enum OrderStatus {
	NEW {
		@Override
		public String defaultDescription() {
			return "Đơn hàng được đặt bởi khách hàng";
		}
	},CANCELLED{
		@Override
		public String defaultDescription() {
			return "Đơn hàng bị từ chối";
		}
	}, PROCESSING {
		@Override
		public String defaultDescription() {
			return "Yêu cầu đang được xử lý";
		}
	}, PACKAGED{
		@Override
		public String defaultDescription() {
			return "Sản phẩm đã được đóng gói";
		}
	}, PICKED {
		@Override
		public String defaultDescription() {
			return "Người vận chuyển đã lấy gói hàng";
		}
	}, SHIPPING {
		@Override
		public String defaultDescription() {
			return "Người vận chuyển đang giao hàng";
		}
	}, DELIVERED {
		@Override
		public String defaultDescription() {
			return "Khách hàng đã nhận sản phẩm";
		}
	}, RETURNED {
		@Override
		public String defaultDescription() {
			return "Sản phẩm đã được trả lại";
		}
	}, PAID {
		@Override
		public String defaultDescription() {
			return "Khách hàng đã thanh toán đơn hàng này";
		}
	}, REFUNDED {
		@Override
		public String defaultDescription() {
			return "Khách hàng đã được hoàn tiền";
		}
	}, RETURN_REQUESTED {
		@Override
		public String defaultDescription() {
			return "Khách hàng gửi yêu cầu đổi trả hàng đã mua";
		}
	};

	public abstract String defaultDescription();

}
