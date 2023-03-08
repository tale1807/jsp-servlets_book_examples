import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Content2Servlet extends HttpServlet{

    public String loginUrl = "SessionLoginServlet";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       HttpSession session = req.getSession();
       if(session == null)
        resp.sendRedirect(loginUrl);
        else{
            String loggedIn = (String) session.getAttribute("loggedIn");
            if(!loggedIn.equals("true"))
                resp.sendRedirect(loginUrl);
        }

        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
        out.println("<HTML>");
        out.println("<HEAD>");
        out.println("<TITLE>Welcome</TITLE>");
        out.println("</HEAD>");
        out.println("<BODY>");
        out.println("Welcome");
        out.println("</BODY>");
        out.println("</HTML>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
    
}
