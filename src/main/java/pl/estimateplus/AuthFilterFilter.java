package pl.estimateplus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter(urlPatterns = {"/user/*","/admin/*"}, filterName = "AppAccessFilter")
public class AuthFilterFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
        System.out.println("Filter AuthFilter - DESTROY");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        System.out.println("AuthFilter doFilter was called");
        final HttpServletRequest req = (HttpServletRequest) request;


        if (req.getSession().getAttribute("user") == null)
        {
            System.out.println("AuthFilter triggered");
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect(req.getContextPath() + "/");
            System.out.println("AuthFilter finished");
            return;
        }
        System.out.println("AuthFilter doFilter was executed");
        chain.doFilter(request, response);
    }
}
