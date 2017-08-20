<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>主页</title>
</head>
<body>
<div style="">用户:${user.username}</div>
<button onclick="exit()">退出</button>
<br>
<br>
<br>
<button onclick="add()">添加任务</button>

<table id="table" border="1" style="margin-top: 30px;width: 90%;">

</table>

<script type="text/javascript" src="http://s1.xmcdn.com/lib/common/last/jquery/2.1.4/jquery.js?v=1"></script>
<script>
    $(function () {
        var table = $('#table');
        $.ajax({
            url: '/ops-schedule-web/task/list',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                if(data.code == 200){
                    table.html('');
                    var ths = '<tr> ' +
                            '<th>id</th> ' +
                            '<th>任务组</th> ' +
                            '<th>密钥</th> ' +
                            '<th>描述</th> ' +
                            '<th>类型</th> ' +
                            '<th>执行周期/执行时间</th> ' +
                            '<th>创建人</th> ' +
                            '<th>创建时间</th> ' +
                            '<th>最后更新人</th> ' +
                            '<th>最后更新时间</th> ' +
                            '<th>当前任务绑定数量</th> ' +
                            '<th>操作</th> ' +
                            '</tr>';
                    table.append(ths);
                    for(var i=0;i<data.data.length;i++){
                        var show = "none";
                        if(data.data[i].own){
                            show = "block";
                        }
                        var tr = '<tr> ' +
                                '<td>'+data.data[i].id+'</td> ' +
                                '<td>'+data.data[i].group+'</td> ' +
                                '<td>'+data.data[i].key+'</td> ' +
                                '<td>'+data.data[i].description+'</td> ' +
                                '<td>'+data.data[i].type+'</td> ' +
                                '<td>'+data.data[i].typeValue+'</td> ' +
                                '<td>'+data.data[i].createUser+'</td> ' +
                                '<td>'+data.data[i].createAt+'</td> ' +
                                '<td>'+data.data[i].updateUser+'</td> ' +
                                '<td>'+(data.data[i].updateAt == null?"":data.data[i].updateAt)+'</td> ' +
                                '<td>'+data.data[i].count+'</td> ' +
                                '<td>' +
                                '<button onclick="edit(' + data.data[i].id+ ')">编辑</button>' +
                                '<button style="display: ' + show + ';" onclick="auth(' + data.data[i].id+ ')">授权</button>' +
                                '<button style="display: ' + show + ';" class="del-btn" onclick="del(' + data.data[i].id+ ')">删除</button>' +
                                '</td> ' +
                                '</tr>';
                        table.append(tr);
                    }
                }
                else{
                    alert(data.message);
                }
            }
        })
    })

    function add() {
        self.location.href="/ops-schedule-web/page/addTask";
    }

    function edit(obj) {
        self.location.href="/ops-schedule-web/page/editTask?id=" + obj;
    }
    
    function exit() {
        var r = confirm("确认要退出?")
        if (r == true)
        {
            self.location.href="/ops-schedule-web/user/exit";
        }
    }

    function auth(obj) {
        self.location.href="/ops-schedule-web/page/authTask?id=" + obj;
    }

    function del(obj) {
        var r = confirm("确认要删除?")
        if (r == true)
        {
            $('.del-btn').prop('disabled',true);
            $.ajax({
                url: '/ops-schedule-web/task/delete',
                type: 'post',
                dataType: 'json',
                data: {
                    id: obj
                },
                success: function (data) {
                    if(data.code == 200){
                        alert(data.message);
                        self.location.href="/ops-schedule-web/page/index";
                    }
                    else{
                        alert(data.message);
                    }
                    $('.del-btn').prop('disabled',false);
                }
            })
        }
    }
</script>
</body>
</html>