import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoggingFilter implements Filter {


    private FilterConfig filterConfig = null;

    @Override
    public void destroy() {
        System.out.println("Filter destroyed");
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        servletContext.log(arg0.getRemoteHost());
        arg2.doFilter(arg0, arg1);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("Filter initialized");
        this.filterConfig = arg0;
    }
    
}
