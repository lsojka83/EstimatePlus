package pl.estimateplus;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

public class AppInitializer implements WebApplicationInitializer {
//
//    private String TMP_FOLDER = "/tmp";
//    private int MAX_UPLOAD_SIZE = 5 * 1024 * 1024;

    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext ctx =
                new AnnotationConfigWebApplicationContext();
        ctx.register(AppConfig.class);
        ctx.setServletContext(container);
        ServletRegistration.Dynamic servlet =
                container.addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        FilterRegistration.Dynamic fr = container.addFilter("encodingFilter",
                new CharacterEncodingFilter());
        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/*");

        //MultipartConfigElement
//        ServletRegistration.Dynamic appServlet = container.addServlet("mvc", new DispatcherServlet(
//                new GenericWebApplicationContext()));
//
//        appServlet.setLoadOnStartup(1);
//
//        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(TMP_FOLDER,
//                MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2, MAX_UPLOAD_SIZE / 2);
//
//        appServlet.setMultipartConfig(multipartConfigElement);
    }
}