import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/Startup")

public class Startup extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	private void createInitUser(String username, String password, String usertype) {
		HashMap<String, User> hm = new HashMap<String, User>();
		try {
			FileInputStream fileInputStream = new FileInputStream(
					new File(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.USER_DETAILS_PATH));
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			hm = (HashMap<String, User>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (Exception e) {

		}
		
		User user = new User(username, password, usertype);
		hm.put(username, user);
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(Properties.TOMCAT_HOME + Properties.WEBAPP_PATH + Properties.PROJECT_PATH + Properties.USER_DETAILS_PATH);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(hm);
			objectOutputStream.flush();
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	public void init() throws ServletException {
		SaxParserDataStore.addHashmap();
		
		createInitUser("aa", "aa", "customer");
		createInitUser("as", "as", "retailer");
		createInitUser("ad", "ad", "manager");
	}
}
