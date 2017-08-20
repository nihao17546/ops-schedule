<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
</head>
<body>
账号:<input id="username" type="text"><br>
密码:<input id="password" type="password"><br>
<button id="btn" onclick="fun()">登录</button><br><br>
<a href="/ops-schedule-web/page/regist">注册</a>

<script type="text/javascript" src="http://s1.xmcdn.com/lib/common/last/jquery/2.1.4/jquery.js?v=1"></script>
<script>
    function fun() {
        $('#btn').prop('disabled',true);
        var username = $('#username').val().trim();
        var password = $('#password').val().trim();
        if(username != '' && password != ''){
            $.ajax({
                url: '/ops-schedule-web/user/login',
                type: 'post',
                dataType: 'json',
                data: {
                    username: username,
                    password: password
                },
                success: function (data) {
                    if(data.code == 200){
//                        alert(data.message);
                        self.location.href="/ops-schedule-web/page/index";
                    }
                    else{
                        alert(data.message);
                    }
                    $('#btn').prop('disabled',false);
                }
            })
        }
    }
</script>
</body>
</html>