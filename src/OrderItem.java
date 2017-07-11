import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/OrderItem")

public class OrderItem extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private String id;
	private String type;
	private String name;
	private double price;
	private String image;
	private String retailer;
	private double discount;
	private int amount;
	private double extraCost;

	public OrderItem(String id, String type, String name, double price, String image, String retailer, double discount,
			double extraCost) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.price = price;
		this.image = image;
		this.retailer = retailer;
		this.discount = discount;
		this.amount = 1;
		this.extraCost = extraCost;
	}

	public double getExtraCost() {
		return extraCost;
	}

	public void setExtraCost(double extraCost) {
		this.extraCost = extraCost;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRetailer() {
		return retailer;
	}

	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}
}
