<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <%
        request.setAttribute("path", request.getContextPath());
    %>
</head>
<body>

<h1>图文并茂</h1>
图片:<br/>
<img style="height: 500px;width: 500px" src="${path}/static/1.jpg"/><br/>
视频:<br/>
<video width="320" height="240" controls>
    <source src="${path}/static/2.mp4" type="video/mp4">
    您的浏览器不支持 video 标签。
</video>
<br/>
<a href="${path}/json">json格式</a><br/>
<a href="${path}/code">code图片</a><br/>

</body>
</html>