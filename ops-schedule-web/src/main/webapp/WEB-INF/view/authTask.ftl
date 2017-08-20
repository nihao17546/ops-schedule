<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>任务授权</title>
</head>
<body>
<input type="hidden" id="id" value="${task.id}">
任务组:<input id="group" disabled type="text" value="${task.group}" maxlength="30"><br>
密钥:<input id="key" disabled type="text" value="${task.key}" maxlength="20"><br>
描述:<input id="description" disabled value="${task.description}" type="text" maxlength="100" style="width: 500px;"><br>
管理员:<input id="users" type="text" value="${users}" placeholder="输入管理员账号,多个之间用英文逗号分割" style="width: 250px;"><br><br>
<button id="btn" onclick="fun()">授权</button><br><br><br>
<a href="/ops-schedule-web/page/index">返回</a>

<script type="text/javascript" src="http://s1.xmcdn.com/lib/common/last/jquery/2.1.4/jquery.js?v=1"></script>
<script>
    function fun() {
        $('#btn').prop('disabled',true);
        var id = $('#id').val().trim();
        var users = $('#users').val().trim();
        $.ajax({
            url: '/ops-schedule-web/task/auth',
            type: 'post',
            dataType: 'json',
            data: {
                id: id,
                users: users
            },
            success: function (data) {
                if(data.code == 200){
                    alert(data.message);
                    self.location.href="/ops-schedule-web/page/index";
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