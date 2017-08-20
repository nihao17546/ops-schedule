<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>编辑任务</title>
</head>
<body>
<input type="hidden" id="id" value="${task.id}">
任务组:<input id="group" type="text" value="${task.group}" maxlength="30"><br>
密钥:<input id="key" type="text" placeholder="格式要求: xxx:xxx" value="${task.key}" maxlength="20"><br>
描述:<input id="description" value="${task.description}" type="text" maxlength="100" style="width: 500px;"><br>
类型:<select id="type">
    <#if task.type == 1>
        <option selected value="1">周期任务</option>
        <option value="2">固定时间任务</option>
        <#elseif task.type == 2>
            <option value="1">周期任务</option>
            <option selected value="2">固定时间任务</option>
    </#if>
</select><br>
<div id="period-div"
<#if task.type == 2>style="display: none;"</#if>
>任务周期:<input id="period" class="type-value" type="text" <#if task.period??>value="${task.period}"</#if> >毫秒</div>
<div id="time-div"
     <#if task.type == 1>style="display: none;"</#if>
>每日执行任务时间:<input id="time" class="type-value" type="text" <#if task.time??>value="${task.time}"</#if> ></div><br>
<button id="btn" onclick="fun()">编辑</button><br><br><br>
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
        $('#btn').prop('disabled',true);
        var group = $('#group').val().trim();
        var id = $('#id').val().trim();
        var key = $('#key').val().trim();
        var description = $('#description').val().trim();
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
                url: '/ops-schedule-web/task/edit',
                type: 'post',
                dataType: 'json',
                data: {
                    id: id,
                    group: group,
                    key: key,
                    description: description,
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