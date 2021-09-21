<%--
  Created by IntelliJ IDEA.
  User: happyelements
  Date: 2020/11/17
  Time: 3:01 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>发生错误啦  - <a href="javascript:history.go(-1)">返回</a></h1>
<pre>
<%
    Throwable t = (java.lang.Throwable)request.getAttribute("javax.servlet.error.exception");
    if(t != null) {
        org.apache.log4j.Logger.getLogger("com.happyelements.animal.antiaddiction").error("500 error：" + t.getMessage(), t);
    }
%>
</pre>
