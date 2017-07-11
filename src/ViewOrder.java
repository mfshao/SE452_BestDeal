import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.io.*;

@WebServlet("/ViewOrder")

public class ViewOrder extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		Utilities utility = new Utilities(request, pw);
		if (!utility.isLoggedin()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("login_msg", "Please Login to View your Orders");
			response.sendRedirect("Login");
			return;
		}
		String username = utility.username();
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<form name ='ViewOrder' action='ViewOrder' method='get'>");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Order</a>");
		pw.print("</h2><div class='entry'>");

		if (request.getParameter("Order") == null) {
			pw.print("<table align='center'><tr><td>Enter OrderNo &nbsp&nbsp<input name='orderId' type='text'></td>");
			pw.print("<td><input type='submit' name='Order' value='ViewOrder' class='btnbuy'></td></tr></table>");
		}

		HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();

		try {
			FileInputStream fileInputStream = new FileInputStream(
					new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			orderPayments = (HashMap<Integer, ArrayList<OrderPayment>>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {
		}

		if (request.getParameter("Order") != null && request.getParameter("Order").equals("ViewOrder")) {
			int orderId = Integer.parseInt(request.getParameter("orderId"));
			pw.print("<input type='hidden' name='orderId' value='" + orderId + "'>");

			try {
				FileInputStream fileInputStream = new FileInputStream(
						new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				orderPayments = (HashMap<Integer, ArrayList<OrderPayment>>) objectInputStream.readObject();
				objectInputStream.close();
			} catch (Exception e) {

			}
			int size = 0;

			if (orderPayments.get(orderId) != null) {
				for (OrderPayment od : orderPayments.get(orderId))
					if (od.getUserName().equals(username))
						size = orderPayments.get(orderId).size();
			}

			if (size > 0) {
				pw.print("<table  class='gridtable'>");
				pw.print("<tr><td></td>");
				pw.print("<td>OrderId:</td>");
				pw.print("<td>UserName:</td>");
				pw.print("<td>productOrdered:</td>");
				pw.print("<td>productPrice:</td>");
				pw.print("<td>deliveryDate:</td></tr>");
				for (OrderPayment op : orderPayments.get(orderId)) {
					pw.print("<tr>");
					pw.print("<td><input type='radio' name='orderName' value='" + op.getOrderName() + "'></td>");
					pw.print("<td>" + op.getOrderId() + ".</td><td>" + op.getUserName() + "</td><td>"
							+ op.getOrderName() + "</td><td>Price: " + op.getOrderPrice() + "</td><td>Delivery: "
							+ op.getDeliveryDateString() + "</td>");
					pw.print("<td><input type='submit' name='Order' value='CancelOrder' class='btnbuy'></td>");
					pw.print("</tr>");
				}
				pw.print("</table>");
			} else {
				pw.print("<h4 style='color:red'>You have not placed any order with this order id</h4>");
			}
		}

		if (request.getParameter("Order") != null && request.getParameter("Order").equals("CancelOrder")) {
			String orderName = request.getParameter("orderName");
			int orderId = 0;
			orderId = Integer.parseInt(request.getParameter("orderId"));
			ArrayList<OrderPayment> ListOrderPayment = new ArrayList<OrderPayment>();

			try {
				FileInputStream fileInputStream = new FileInputStream(
						new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				orderPayments = (HashMap<Integer, ArrayList<OrderPayment>>) objectInputStream.readObject();
				objectInputStream.close();
			} catch (Exception e) {
			}

			for (OrderPayment op : orderPayments.get(orderId)) {
				if (op.getOrderName().equals(orderName)) {
					ListOrderPayment.add(op);
					pw.print("<h4 style='color:red'>Your Order is Cancelled</h4>");
				}
			}

			orderPayments.get(orderId).removeAll(ListOrderPayment);
			if (orderPayments.get(orderId).size() == 0) {
				orderPayments.remove(orderId);
			}

			try {
				FileOutputStream fileOutputStream = new FileOutputStream(
						new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(orderPayments);
				objectOutputStream.flush();
				objectOutputStream.close();
				fileOutputStream.close();
			} catch (Exception e) {
			}
		}
		pw.print("</form></div></div></div>");
		utility.printHtml("Footer.html");
	}

}
