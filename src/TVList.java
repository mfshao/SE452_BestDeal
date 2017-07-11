import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TVList")

public class TVList extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String name = null;
		String CategoryName = request.getParameter("maker");

		HashMap<String, TV> hm = new HashMap<String, TV>();
		if (CategoryName == null) {
			hm.putAll(SaxParserDataStore.tvs);
			name = "";
		} else {
			if (CategoryName.equals("sony")) {
				for (Map.Entry<String, TV> entry : SaxParserDataStore.tvs.entrySet()) {
					if (entry.getValue().getRetailer().equals("Sony")) {
						hm.put(entry.getValue().getId(), entry.getValue());
					}
				}
				name = "Sony";
			} else if (CategoryName.equals("samsung")) {
				for (Map.Entry<String, TV> entry : SaxParserDataStore.tvs.entrySet()) {
					if (entry.getValue().getRetailer().equals("Samsung")) {
						hm.put(entry.getValue().getId(), entry.getValue());
					}
				}
				name = "Samsung";
			}
		}

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>" + name + " TVs</a>");
		pw.print("</h2><div class='entry'><table id='tvtable'>");
		int i = 1;
		int size = hm.size();
		for (Map.Entry<String, TV> entry : hm.entrySet()) {
			TV tv = entry.getValue();
			if (i % 3 == 1)
				pw.print("<tr>");
			pw.print("<td><div id='shop_item'>");
			pw.print("<h3>" + tv.getName() + "</h3>");
			pw.print("<strong><del>$" + tv.getPrice() + "</del> <font color=\"red\">$"
					+ Math.round(tv.getPrice() * (100 - tv.getDiscount())) / 100.0 + "</font></strong><ul>");
			pw.print("<li id='item'><img src='images/tvs/" + tv.getImage() + "' alt='' /></li>");
			pw.print("<li><form method='post' action='Cart'>" + "<input type='hidden' name='name' value='"
					+ entry.getKey() + "'>" + "<input type='hidden' name='type' value='tv'>"
					+ "<input type='hidden' name='maker' value='" + CategoryName + "'>"
					+ "<input type='hidden' name='Cart' value='DisplayCart'>"
					+ "<input type='submit' class='btnbuy' name='BuyBtn' value='Buy Now'>"
					+ "<input type='submit' class='btnbuy' name='BuyBtn' value='Buy with 1yr Care $49.99'>"
					+ "<input type='submit' class='btnbuy' name='BuyBtn' value='Buy with 2yr Care $99.99'>"
					+ "<input type='submit' class='btnbuy' name='BuyBtn' value='Buy with 3yr Care $149.99'></form></li>");
			pw.print("</ul></div></td>");
			if (i % 3 == 0 || i == size)
				pw.print("</tr>");
			i++;
		}
		pw.print("</table></div></div></div>");
		utility.printHtml("Footer.html");

	}
}
