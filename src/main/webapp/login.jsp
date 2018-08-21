<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/8/21
  Time: 14:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="js/jquery-1.7.2.min.js"></script>
</head>
<body>

    <form>
        卡号： <input name="cardno"/>
        密码：<input name="password" type="password">
        <input type="button" onclick="login()" value="提交"/>
    </form>
    <div class="msg" style="color: red"></div>
    <script>
        function login(){
            $.ajax({
                url:"base.do",
                data:{
                    action:"login",
                    cardno:$("input[name=cardno]").val(),
                    password:$("input[name=password]").val()
                },
                dataType:"json",
                type:"post",
                success:function (data) {
                    $(".msg").html(data);
                    if (data.cardno){
                        location.href="index.jsp";
                    }
                },
                error:function(){
                    alert("数据异常");
                }
            })
        }
    </script>
</body>
</html>
