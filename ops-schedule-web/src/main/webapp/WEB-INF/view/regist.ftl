<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册</title>
</head>
<body>
账号:<input id="username" type="text" maxlength="12"><br>
密码:<input id="password" type="password" maxlength="20"><br>
确认密码:<input id="password_p" type="password" maxlength="20"><br>
<button id="btn" onclick="fun()">注册</button><br><br>
<a href="/ops-schedule-web/page/login">登录</a>

<script type="text/javascript" src="http://s1.xmcdn.com/lib/common/last/jquery/2.1.4/jquery.js?v=1"></script>
<script>
    function fun() {
        $('#btn').prop('disabled',true);
        var username = $('#username').val().trim();
        var password = $('#password').val().trim();
        var password_p = $('#password_p').val().trim();
        if(username==''){
            alert("请输入账号");
            $('#btn').prop('disabled',false);
            return;
        }
        if(username.length < 4 || username.length > 12){
            alert("账号长度要求4-12");
            $('#btn').prop('disabled',false);
            return;
        }
        if(password != password_p){
            alert("两次输入密码不一致")
            $('#btn').prop('disabled',false);
            return;
        }
        if(password.length < 6){
            alert("密码长度最小6位")
            $('#btn').prop('disabled',false);
            return;
        }
        $.ajax({
            url: '/ops-schedule-web/user/regist',
            type: 'post',
            dataType: 'json',
            data: {
                username: username,
                password: password
            },
            success: function (data) {
                if(data.code == 200){
                    alert(data.message);
                    self.location.href="/ops-schedule-web/page/login";
                }
                else{
                    alert(data.message);
                }
                $('#btn').prop('disabled',false);
            }
        })
    }
</script>
</body>
</html>