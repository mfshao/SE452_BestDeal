import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderPayment implements Serializable {

	private static final long serialVersionUID = 1L;

	private int orderId;
	private String userName;
	private String orderName;
	private double orderPrice;
	private String userAddress;
	private String creditCardNo;
	private Date deliveryDate;

	public OrderPayment(int orderId, String userName, String orderName, double orderPrice, String userAddress,
			String creditCardNo, Date deliveryDate) {
		this.orderId = orderId;
		this.userName = userName;
		this.orderName = orderName;
		this.orderPrice = orderPrice;
		this.userAddress = userAddress;
		this.creditCardNo = creditCardNo;
		this.deliveryDate = deliveryDate;
	}
	
	public String getDeliveryDateString() {
		if (this.deliveryDate == null) {
			return "";
		} else {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			return dateFormat.format(this.getDeliveryDate());
		}
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getCreditCardNo() {
		return creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

}
