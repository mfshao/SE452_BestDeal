import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet("/Utilities")

public class Utilities extends HttpServlet {

	private static final long serialVersionUID = 1L;

	HttpServletRequest req;
	PrintWriter pw;
	String url;
	HttpSession session;

	public Utilities(HttpServletRequest req, PrintWriter pw) {
		this.req = req;
		this.pw = pw;
		this.url = this.getFullURL();
		this.session = req.getSession(true);
	}

	public void printHtml(String file) {
		String result = HtmlToString(file);
		if (file == "Header.html") {
			result = result + "<div id='menu' style='float: right;'><ul>";
			if (session.getAttribute("username") != null) {
				String username = session.getAttribute("username").toString();
				username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
				result = result + "<li><a href='ViewOrder'>ViewOrder</a></li>" + "<li><a>Hello," + username
						+ "</a></li>" + "<li><a href='Account'>Account</a></li>"
						+ "<li><a href='Logout'>Logout</a></li>";
			} else
				result = result + "<li><a href='ViewOrder'>View Order</a></li>" + "<li><a href='Login'>Login</a></li>";
			result = result + "<li><a href='Cart'>Cart(" + CartCount() + ")</a></li></ul></div></div><div id='page'>";
			pw.print(result);
		} else
			pw.print(result);
	}

	public String getFullURL() {
		String scheme = req.getScheme();
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		String contextPath = req.getContextPath();
		StringBuffer url = new StringBuffer();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath);
		url.append("/");
		return url.toString();
	}

	public String HtmlToString(String file) {
		String result = null;
		try {
			String webPage = url + file;
			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		} catch (Exception e) {
		}
		return result;
	}

	public void logout() {
		session.removeAttribute("username");
		session.removeAttribute("usertype");
	}

	public boolean isLoggedin() {
		if (session.getAttribute("username") == null)
			return false;
		return true;
	}

	public String username() {
		if (session.getAttribute("username") != null)
			return session.getAttribute("username").toString();
		return null;
	}

	public String usertype() {
		if (session.getAttribute("usertype") != null)
			return session.getAttribute("usertype").toString();
		return null;
	}

	@SuppressWarnings("unchecked")
	public User getUser() {
		HashMap<String, User> hm = new HashMap<String, User>();
		try {
			FileInputStream fileInputStream = new FileInputStream(
					new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.USER_DETAILS_PATH));
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			hm = (HashMap<String, User>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {
		}
		User user = hm.get(username());
		return user;
	}

	public ArrayList<OrderItem> getCustomerOrders() {
		ArrayList<OrderItem> order = new ArrayList<OrderItem>();
		if (OrdersHashMap.orders.containsKey(username()))
			order = OrdersHashMap.orders.get(username());
		return order;
	}

	@SuppressWarnings("unchecked")
	public int getOrderPaymentSize() {
		HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
		try {
			FileInputStream fileInputStream = new FileInputStream(
					new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			orderPayments = (HashMap<Integer, ArrayList<OrderPayment>>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {

		}
		int size = 0;
		for (Map.Entry<Integer, ArrayList<OrderPayment>> entry : orderPayments.entrySet()) {
			size = entry.getKey();
		}
		return size;
	}

	/* CartCount Function gets the size of User Orders */
	public int CartCount() {
		if (isLoggedin())
			return getCustomerOrders().size();
		return 0;
	}

	public void storeProduct(String name, String type, String maker, String acc, double extraCost) {
		if (!OrdersHashMap.orders.containsKey(username())) {
			ArrayList<OrderItem> arr = new ArrayList<OrderItem>();
			OrdersHashMap.orders.put(username(), arr);
		}
		ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
		if (type.equals("smartphone")) {
			SmartPhone smartphone;
			smartphone = SaxParserDataStore.smartphones.get(name);
			for (OrderItem oi : orderItems) {
				if (oi.getId().equalsIgnoreCase(name)) {
					oi.setAmount(oi.getAmount() + 1);
					return;
				}
			}
			OrderItem orderitem = new OrderItem(name, type, smartphone.getName(), smartphone.getPrice(),
					smartphone.getImage(), smartphone.getRetailer(), smartphone.getDiscount(), extraCost);
			orderItems.add(orderitem);
		}
		if (type.equals("laptop")) {
			Laptop laptop = null;
			laptop = SaxParserDataStore.laptops.get(name);
			OrderItem orderitem = new OrderItem(name, type, laptop.getName(), laptop.getPrice(), laptop.getImage(),
					laptop.getRetailer(), laptop.getDiscount(), extraCost);
			orderItems.add(orderitem);
		}
		if (type.equals("tablet")) {
			Tablet tablet = null;
			tablet = SaxParserDataStore.tablets.get(name);
			OrderItem orderitem = new OrderItem(name, type, tablet.getName(), tablet.getPrice(), tablet.getImage(),
					tablet.getRetailer(), tablet.getDiscount(), extraCost);
			orderItems.add(orderitem);
		}
		if (type.equals("tv")) {
			TV tv = null;
			tv = SaxParserDataStore.tvs.get(name);
			OrderItem orderitem = new OrderItem(name, type, tv.getName(), tv.getPrice(), tv.getImage(),
					tv.getRetailer(), tv.getDiscount(), extraCost);
			orderItems.add(orderitem);
		}
		 if (type.equals("accessory")) {
		 Accessory accessory = SaxParserDataStore.accessories.get(name);
		 OrderItem orderitem = new OrderItem(name, type, accessory.getName(),
		 accessory.getPrice(), accessory.getImage(),
		 accessory.getRetailer(), accessory.getDiscount(), extraCost);
		 orderItems.add(orderitem);
		 }

	}

	@SuppressWarnings("unchecked")
	public void storePayment(int orderId, String orderName, double orderPrice, String userAddress,
			String creditCardNo) {
		HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();

		try {
			FileInputStream fileInputStream = new FileInputStream(
					new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			orderPayments = (HashMap<Integer, ArrayList<OrderPayment>>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {

		}
		if (orderPayments == null) {
			orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
		}

		if (!orderPayments.containsKey(orderId)) {
			ArrayList<OrderPayment> arr = new ArrayList<OrderPayment>();
			orderPayments.put(orderId, arr);
		}
		
		int noOfDays = 14; 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());            
		calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
		
		ArrayList<OrderPayment> listOrderPayment = orderPayments.get(orderId);
		OrderPayment orderpayment = new OrderPayment(orderId, username(), orderName, orderPrice, userAddress,
				creditCardNo, calendar.getTime());
		listOrderPayment.add(orderpayment);

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(
					new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(orderPayments);
			objectOutputStream.flush();
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			System.out.println("inside exception file not written properly");
		}
	}

	public HashMap<String, SmartPhone> getSmartPhones() {
		HashMap<String, SmartPhone> hm = new HashMap<String, SmartPhone>();
		hm.putAll(SaxParserDataStore.smartphones);
		return hm;
	}

	public HashMap<String, Laptop> getLaptops() {
		HashMap<String, Laptop> hm = new HashMap<String, Laptop>();
		hm.putAll(SaxParserDataStore.laptops);
		return hm;
	}

	public HashMap<String, Tablet> getTablets() {
		HashMap<String, Tablet> hm = new HashMap<String, Tablet>();
		hm.putAll(SaxParserDataStore.tablets);
		return hm;
	}

	public HashMap<String, TV> getTVs() {
		HashMap<String, TV> hm = new HashMap<String, TV>();
		hm.putAll(SaxParserDataStore.tvs);
		return hm;
	}
	//
	// /* getProducts Functions returns the Arraylist of smartphones in the
	// store. */
	//
	// public ArrayList<String> getProductsSmartPhone() {
	// ArrayList<String> ar = new ArrayList<String>();
	// for (Map.Entry<String, SmartPhone> entry : getSmartPhones().entrySet()) {
	// ar.add(entry.getValue().getName());
	// }
	// return ar;
	// }
	//
	// /* getProducts Functions returns the Arraylist of laptops in the store.
	// */
	//
	// public ArrayList<String> getProductsLaptop() {
	// ArrayList<String> ar = new ArrayList<String>();
	// for (Map.Entry<String, Laptop> entry : getLaptops().entrySet()) {
	// ar.add(entry.getValue().getName());
	// }
	// return ar;
	// }
	//
	// /* getProducts Functions returns the Arraylist of Tablets in the store.
	// */
	//
	// public ArrayList<String> getProductsTablets() {
	// ArrayList<String> ar = new ArrayList<String>();
	// for (Map.Entry<String, Tablet> entry : getTablets().entrySet()) {
	// ar.add(entry.getValue().getName());
	// }
	// return ar;
	// }

	public void deleteOrderItem(String name) {
		ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
		ListIterator<OrderItem> iter = orderItems.listIterator();
		while (iter.hasNext()) {
			if (iter.next().getId().equalsIgnoreCase(name)) {
				iter.remove();
			}
		}
	}

	public void updateProductAmount(String name, int amount) {
		ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
		for (OrderItem oi : orderItems) {
			if (oi.getId().equalsIgnoreCase(name)) {
				oi.setAmount(amount);
				return;
			}
		}
	}

}
