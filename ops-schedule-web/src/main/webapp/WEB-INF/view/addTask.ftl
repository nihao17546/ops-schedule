<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>添加任务</title>
</head>
<body>
任务组:<input id="group" type="text" maxlength="30"><br>
密钥:<input id="key" type="text" placeholder="格式要求: xxx:xxx" maxlength="20"><br>
描述:<input id="description" type="text" maxlength="100" style="width: 500px;"><br>
类型:<select id="type">
    <option value="1">周期任务</option>
    <option value="2">固定时间任务</option>
</select><br>
<div id="period-div">任务周期:<input id="period" class="type-value" type="text">毫秒</div>
<div id="time-div" style="display: none;">每日执行任务时间:<input id="time" class="type-value" type="text"></div>
管理员:<input id="users" type="text" placeholder="输入管理员账号,多个之间用英文逗号分割" style="width: 250px;"><br><br>
<button id="btn" onclick="fun()">添加</button><br><br><br>
<a href="/ops-schedule-web/page/index">返回</a>

<script type="text/javascript" src="http://s1.xmcdn.com/lib/common/last/jquery/2.1.4/jquery.js?v=1"></script>
<script src="/ops-schedule-web/static/My97DatePicker/WdatePicker.js"></script>
<script>
    $(function () {
        $("#time").on("focus", function () {
            WdatePicker({dateFmt: 'HH:mm:ss'});
        });
        $('#type').change(function () {
            var t = $(this).val();
            if(t == '1'){
                $('#time-div').hide();
                $('#period-div').show();
                $('.type-value').val('');
            }
            else if(t == '2'){
                $('#period-div').hide();
                $('#time-div').show();
                $('.type-value').val('');
            }
        })
    })

    function fun() {
        $('#btn').prop('disabled',true);$.trim()
        var group = $.trim($('#group').val());
        var key = $.trim($('#key').val());
        var description = $.trim($('#description').val());
        var users = $.trim($('#users').val());
        var type = $('#type').val();
        var typeValue = null;
        if(type == '1'){
            typeValue = $.trim($('#period').val());
            if(typeValue == null || typeValue == ''){
                alert("请输入任务周期")
                $('#btn').prop('disabled',false);
                return;
            }
        }
        else if(type == '2'){
            typeValue = $.trim($('#time').val());
            if(typeValue == null || typeValue == ''){
                alert("请输入任务执行时间")
                $('#btn').prop('disabled',false);
                return;
            }
        }
        if(group != '' && key != ''){
            $.ajax({
                url: '/ops-schedule-web/task/add',
                type: 'post',
                dataType: 'json',
                data: {
                    group: group,
                    key: key,
                    description: description,
                    users: users,
                    type: type,
                    typeValue: typeValue
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
        else{
            alert("任务组,和密钥为必输项")
            $('#btn').prop('disabled',false);
        }
    }
</script>
</body>
</html>