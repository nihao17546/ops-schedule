# ops-schedule
基于zookeeper的分布式任务调度，多台实例、保证一台任务执行，任务执行策略可在线修改无须重启机器
#### 依赖
<pre>
 &lt;dependency&gt;
   &lt;groupId&gt;com.ximalaya.ops&lt;/groupId&gt;
   &lt;artifactId&gt;ops-schedule-api&lt;/artifactId&gt;
   &lt;version&gt;0.0.5-SNAPSHOT&lt;/version&gt;
 &lt;/dependency&gt;
</pre>
#### 任务配置后台url
http://127.0.0.1/ops-schedule-web/page/index
#### spring整合
<pre>
 &lt;bean class="com.ximalaya.ops.schedule.api.ScheduleHandler"&gt;
   &lt;property name="zk_connection" value="#{zk['zk_connection']}"/&gt;
   &lt;property name="session_timeout" value="#{zk['session_timeout']}"/&gt;
   &lt;property name="connection_timeout" value="#{zk['connection_timeout']}"/&gt;
   &lt;property name="configs"&gt;
     &lt;list&gt;
       &lt;bean class="com.ximalaya.ops.schedule.api.JobConfig"&gt;
         &lt;property name="group" value=""/&gt;
         &lt;property name="key" value=""/&gt;
         &lt;property name="mission" ref=""/&gt;
       &lt;/bean&gt;
     &lt;/list&gt;
   &lt;/property&gt;
 &lt;/bean&gt;
</pre>
#### JobConfig 参数说明：
  group：任务标识，必须唯一<br>
  key：acl认证<br>
  mission：执行任务类，（须继承com.ximalaya.ops.schedule.api.Mission，并实现execute方法，execute函数为任务执行体）<br>
