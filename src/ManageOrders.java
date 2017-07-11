import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ManageOrders")

public class ManageOrders extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<form name ='ManageOrders' action='ManageOrders' method='post'>");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Order</a>");
		pw.print("</h2><div class='entry'>");

		HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();

		try {
			FileInputStream fileInputStream = new FileInputStream(
					new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			orderPayments = (HashMap<Integer, ArrayList<OrderPayment>>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {
		}

		if (request.getParameter("Order") != null && request.getParameter("Order").equals("ManageOrder")) {
			try {
				FileInputStream fileInputStream = new FileInputStream(
						new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.PAYMENT_DETAILS_PATH));
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				orderPayments = (HashMap<Integer, ArrayList<OrderPayment>>) objectInputStream.readObject();
				objectInputStream.close();
			} catch (Exception e) {

			}
			int size = 0;

			pw.print("<table  class='gridtable'>");
			pw.print("<tr><td></td>");
			pw.print("<td>OrderId:</td>");
			pw.print("<td>UserName:</td>");
			pw.print("<td>productOrdered:</td>");
			pw.print("<td>productPrice:</td></tr>");

			for (Integer key : orderPayments.keySet()) {
				size = orderPayments.get(key).size();
				if (size > 0) {
					for (OrderPayment od : orderPayments.get(key)) {
						pw.print("<tr>");
						pw.print("<td><input type='radio' name='orderId' value='" + od.getOrderId() + "'></td>");
						pw.print("<td>" + od.getOrderId() + ".</td><td>" + od.getUserName() + "</td><td>"
								+ od.getOrderName() + "</td><td>Price: " + od.getOrderPrice() + "</td>");
						pw.print("<td><input type='submit' name='Order' value='CancelOrder' class='btnbuy'></td>");
						pw.print("</tr>");
					}
				} else {
					pw.print("<h4 style='color:red'>No order available</h4>");
				}
			}
			pw.print("</table>");
		}

		if (request.getParameter("Order") != null && request.getParameter("Order").equals("CancelOrder"))

		{
			if (request.getParameter("orderId") == null) {
				pw.print("<h4 style='color:red'>Please select an order by using radio button</h4>");
				pw.print("</form></div></div></div>");
				utility.printHtml("Footer.html");
				return;
			}

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

			for (OrderPayment oi : orderPayments.get(orderId)) {
				ListOrderPayment.add(oi);
				pw.print("<h4 style='color:red'>The Order is Cancelled</h4>");
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
