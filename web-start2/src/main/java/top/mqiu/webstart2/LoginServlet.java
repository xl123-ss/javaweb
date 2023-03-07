package top.mqiu.webstart2;



import top.mqiu.webstart2.util.UserEnum;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        response.setContentType("text/html;charset=utf-8");
        String password = request.getParameter("password");

        PrintWriter writer = response.getWriter();
        try {
            UserEnum user = UserEnum.valueOf(username);
            if (user.password.equals(password)){
                writer.write("登录成功,欢迎您:"+user.username);
            }else {
                writer.write("登录失败,错误密码:"+password);
            }
        }catch (Exception e){
            writer.write("登录失败,该用户不存在");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }

    public void destroy() {
    }
}