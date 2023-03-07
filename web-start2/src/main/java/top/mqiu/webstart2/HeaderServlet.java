package top.mqiu.webstart2;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/header")
public class HeaderServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }
//通过请求头判断用户是PC还是手机端发出的请求，给用户不同的返回结果，可以直接返回信息，也可以用  response.sendRedirect("重定向页面"); 的方法，转到不同布局的页面。
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setContentType("application/text;charset=UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String userAgent = request.getHeader("user-agent");
//        response.setContentType("text/html");
//        response.setCharacterEncoding("UTF-8");
        System.out.printf(userAgent);
        System.out.printf(userAgent);
        PrintWriter writer = response.getWriter();
        if (userAgent.contains("Windows")) {
            writer.write("Windows");
        }else{
            writer.write("安卓端");
        }

    }

    public void destroy() {
    }
}