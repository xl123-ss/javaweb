package top.mqiu.webstart2;

import top.mqiu.webstart2.util.VerifyCodeUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Properties;

@WebServlet(value = "/json")
public class JsonServlet extends HttpServlet {
    private String message;


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.printf("测试！！！");
        Properties properties = new Properties();
//使用类对象加载配置文件，生成对应的输入流,/就代表class的根目录，否则就是当前目录
        InputStream inputStream = getClass().getResourceAsStream("/1.json");
        ////字节流变为字符流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader br = new BufferedReader(inputStreamReader);
        String ans = null;
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        while ((ans = br.readLine()) != null) {
            if (!"".equals(ans))
                out.write(ans);
        }
    }


}