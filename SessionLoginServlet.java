import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionLoginServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendLoginForm(resp,true);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        if(login(userName, password)){
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedIn", new String("true"));
            resp.sendRedirect("Content2Servlet");
        }
        else{
            sendLoginForm(resp, true);
        }
    }
    private void sendLoginForm(HttpServletResponse resp, boolean b) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Login</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<CENTER>");

        if(b){
            out.println("Login failed. Please try again.<BR>");
            out.println("If you think you have enetered the correct user name"+
             " and password, the cookie setting in your browser might be off." +
            "<BR>Click <A HREF=InfoPage.html>here</A> for information" +
            " on how to turn it on.<BR>"
            );
        }
        out.println("<BR>");
        out.println("<BR><H2>Login Page</H2>");
        out.println("<BR>");
        out.println("<BR>Please enter your user name and password.");
        out.println("<BR>");
        out.println("<BR><FORM METHOD=POST>");
        out.println("<TABLE>");
        out.println("<TR>");
        out.println("<TD>User Name:</TD>");
        out.println("<TD><INPUT TYPE=TEXT NAME=userName></TD>");
        out.println("</TR>");
        out.println("<TR>");
        out.println("<TD>Password:</TD>");
        out.println("<TD><INPUT TYPE=PASSWORD NAME=password></TD>");
        out.println("</TR>");
        out.println("<TR>");
        out.println("<td align=right colspan=2>");
        out.println("<input type=submit value=Login></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");
        out.println("</center>");
        out.println("</body>");
        out.println("<html>");
    }

    private static boolean login(String userName, String password) {

        String passwordDb = "reallyStrongPwd123";
        String userNameDb = "SA";
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=master;integratedSecurity=false;" +
                "encrypt=true;trustServerCertificate=true;" +
                "authentication=SqlPassword";
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = DriverManager.getConnection(connectionUrl,userNameDb,passwordDb);
            Statement s = con.createStatement();
            String queryString = "SELECT UserName FROM Users" + 
            " WHERE UserName='" + fixSqlFieldValue(userName) + "'"+
            " AND Password='" + fixSqlFieldValue(password) + "'";
            ResultSet rs = s.executeQuery(queryString);

            if(rs.next()){
                rs.close();
                s.close();
                con.close();
                return true;
            }
            rs.close();
            s.close();
            con.close();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }

        return false;
    }
    private static String fixSqlFieldValue(String value) {
        if(value == null)
            return null;
        StringBuffer fixedValue = new StringBuffer((int) (value.length() * 1.1));
        for(int i = 0; i < value.length(); i++){
            if(value.charAt(i) == '\'')
                fixedValue.append("''");
            else
                fixedValue.append(value.charAt(i));
        }
        return fixedValue.toString();
    }
}
