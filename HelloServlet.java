
import java.io.*;
import java.sql.*;
import javax.servlet.http.*;

public class HelloServlet extends HttpServlet{

    public void init() {
       try{
           Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
       }
       catch(Exception e)
       {

       }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendSqlForm(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendSqlForm(request, response);
    }

    private void sendSqlForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>SQL Tool Servlet</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("<BR><H2>SQL Tool</H2>");
        out.println("<BR>Please type your SQL statement in the following box.");
        out.println("<BR>");
        out.println("<BR><FORM METHOD=POST>");
        out.println("<TEXTAREA NAME=sql COLS=80 ROWS=8>");
        String sql = request.getParameter("sql");

        if(sql != null){
            out.println(sql);
        }
        out.println("</TEXTAREA>");
        out.println("<BR>");
        out.println("<INPUT TYPE=SUBMIT VALUE=Execute>");
        out.println("</FORM>");
        out.println("<BR>");
        out.println("<HR>");
        out.println("<BR>");

        if(sql!=null){
            executeSql(sql.trim(), response);
        }
        out.println("</BODY>");
        out.println("</HTML>");
    }

    public void destroy() {
    }

    public void executeSql(String sql, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String password = "reallyStrongPwd123";
        String userName = "SA";
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=master;integratedSecurity=false;" +
                "encrypt=true;trustServerCertificate=true;" +
                "authentication=SqlPassword";
        try{
            Connection con = DriverManager.getConnection(connectionUrl, userName, password);
            Statement st = con.createStatement();

            if(sql.toUpperCase().startsWith("SELECT")){
                out.println("<TABLE BORDER=1>");
                ResultSet rs = st.executeQuery(sql);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                out.println("<TR>");
                for(int i=1; i<=columnCount;i++){
                    out.println("<TD><B>" + rsmd.getColumnName(i) + "</B></TD>\n");
                }
                out.println("</TR>");
                while(rs.next()){
                    out.println("<TR>");
                    for(int i =1; i<=columnCount; i++){
                        out.println("<TD>" + encodeHtmlTag(rs.getString(i)) + "</TD>");
                    }
                    out.println("</TR>");
                }
                rs.close();

                out.println("</TABLE>");
            }
            else{
                int i = st.executeUpdate(sql);
                out.println("Record(s) affected: " + i);
            }
            st.close();
            con.close();
            out.println("</TABLE>");
        }
        catch(Exception e){
            out.println("<B>Error</B>");
            out.println("<BR>");
            out.println(e.toString());
        }
    }

    public String encodeHtmlTag(String tag){
        if(tag == null)
            return null;
        StringBuffer encodedTag = new StringBuffer(2 * tag.length());
        for(int i = 0; i < tag.length(); i++){
            char c = tag.charAt(i);
            switch(c){
                case '<':
                    encodedTag.append("&lt;");
                    break;
                case '>':
                    encodedTag.append("&gt;");
                    break;
                case '&':
                    encodedTag.append("&amp;");
                    break;
                case '"':
                    encodedTag.append("&quot;");
                    break;
                case ' ':
                    encodedTag.append("&nbsp;");
                    break;
                default:
                    encodedTag.append(c);
            }
        }
        return encodedTag.toString();
    }
}