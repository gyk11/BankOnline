<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>

    <style>
        li{
            list-style: none;
        }
        .main div{
            float: left;
        }
        .main iframe{
            margin-left: 20px;
            width: 500px;
            height: 450px;
        }
    </style>
    <div class="contrain">
        <p>${account.cardno},<a href="base.do?action=logout">退出登录</a></p>
        <div class="main">
            <div>
                <ul>
                    <li><a href="balance.html" target="myframe">查询余额</a></li>
                    <li><a href="tranfer.html" target="myframe">转账</a></li>
                    <li><a href="record.html" target="myframe">查询交易记录</a></li>
                    <li><a href="updatepass.html" target="myframe">修改密码</a></li>
                </ul>

            </div>
            <iframe src="welcome.html" name="myframe" id="myframe"></iframe>
        </div>
    </div>
</body>
</html>
