import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Payment")

public class Payment extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		Utilities utility = new Utilities(request, pw);
		if (!utility.isLoggedin()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("login_msg", "Please Login to Pay");
			response.sendRedirect("Login");
			return;
		}

		String userAddress = request.getParameter("userAddress");
		String creditCardNo = request.getParameter("creditCardNo");
		int orderId = utility.getOrderPaymentSize() + 1;

		for (OrderItem oi : utility.getCustomerOrders()) {
			utility.storePayment(orderId, oi.getName(), oi.getPrice(), userAddress, creditCardNo);
		}
		
		int noOfDays = 14; 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());            
		calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String dDate = dateFormat.format(calendar.getTime());

		OrdersHashMap.orders.remove(utility.username());
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Order</a>");
		pw.print("</h2><div class='entry'>");
		pw.print("<h2>Your Order");
		pw.print("&nbsp&nbsp");
		pw.print("is stored ");
		pw.print("<br>Your Order No is " + (orderId));
		pw.print("<br>Your Order will be deliveried by " + dDate);
		pw.print("</h2></div></div></div>");
		utility.printHtml("Footer.html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		@SuppressWarnings("unused")
		Utilities utility = new Utilities(request, pw);
	}
}
