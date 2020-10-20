# blog
blog 的 springboot 后端服务

## Thymeleaf
spring-boot-stater-thymeleaf 默认导入的版本是2.3

建议使用3更好用，性能更好



填坑

我的springboot版本2.3.4.RELEASE，引入thymeleaf3

```xml
<properties>
    <java.version>1.8</java.version>
    <springboot-thymeleaf.version>3.0.2.RELEASE</springboot-thymeleaf.version>
    <thymeleaf-layout-dialect.version>2.1.1</thymeleaf-layout-dialect.version>
</properties>
```



Linux查看MySQL服务起了没

```bash
[root@localhost ~]# service mysqld status
 SUCCESS! MySQL running (1655)
[root@localhost ~]# service mysql status
Redirecting to /bin/systemctl status mysql.service
● mysqld.service - LSB: start and stop MySQL
   Loaded: loaded (/etc/rc.d/init.d/mysqld; bad; vendor preset: disabled)
   Active: active (running) since 五 2020-10-16 13:17:45 CST; 1min 53s ago
     Docs: man:systemd-sysv-generator(8)
  Process: 1260 ExecStart=/etc/rc.d/init.d/mysqld start (code=exited, status=0/SUCCESS)
   CGroup: /system.slice/mysqld.service
           ├─1309 /bin/sh /usr/local/mysql/mysql-8.0.21-linux-glibc2.12-x86_64/bin/m...
           └─1655 /usr/local/mysql/mysql-8.0.21-linux-glibc2.12-x86_64/bin/mysqld --...

10月 16 13:17:15 localhost.colmdomain systemd[1]: Starting LSB: start and stop MyS....
10月 16 13:17:45 localhost.colmdomain mysqld[1260]: Starting MySQL...................!
10月 16 13:17:45 localhost.colmdomain systemd[1]: Started LSB: start and stop MySQL.
Hint: Some lines were ellipsized, use -l to show in full.
```



IDEA 的编码一定要改

settings >> Editor >> File Encodings



IDEA 不重启更新代码

https://blog.csdn.net/JunSIrhl/article/details/103995646



edit run/debug configuration >> tomcat server >> vm options >> 两个都设置为 update classes and resources

然后在 build >> recompile ...



logback 给日志添加颜色，让信息更醒目

https://www.cnblogs.com/sxdcgaq8080/p/7885340.html

```xml
<pattern>%black(控制台-) %red(%d{yyyy-MM-dd HH:mm:ss}) %green([%thread]) %highlight(%-5level) %boldMagenta(%logger{10}) - %cyan(%msg%n)</pattern>
```



aop概念及各种名词复习：https://blog.csdn.net/changudeng1992/article/details/80625134

springmvc中几种获取路径方式对比：https://www.cnblogs.com/wang3680/p/4210830063c46be1b0e1144ae72ea806.html



将页面工程导入到idea，注意本地资源的引用，由于使用了thymeleaf模板引擎需要做出相应的修改

```html
<link rel="stylesheet" href="../static/css/complement.css" th:href="@{/css/complement.css}">
```

这样一个个改太慢了，可以使用fragment



fragment应用于script引用部分

```html
<!--/*/<th:block>/*/-->
	    <script src="https://cdn.jsdelivr.net/npm/jquery@3.2/dist/jquery.min.js"></script>

<!--/*/</th:block>/*/-->
```

这种写法打开静态页面的时候是完全注释掉th的，但使用thymeleaf模板引擎的时候又是可用的



实体关系

Blog n--------1 Type

Blog n--------n Tag

Blog n--------1 User

Blog 1--------n Comment



评论类自关联关系

parentComment 1-------n replayComment



避坑：之前踩过

```java
@Entity
@Table(name = "t_blog")
public class Blog {
    // 这里巨坑无比，注意使用javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;
    ...
}
```

> import org.springframework.data.annotation.Id;
>
> import javax.persistence.Id; 使用这个



配置 format_sql：http://leeyk.cn/?p=168

```yml
spring:
  jpa:
    show-sql: true #显示sql语句
    hibernate:
      ddl-auto: update  # 配置开启自动更新表结构
    database: mysql
    properties:
      hibernate:
        format_sql: true  #格式化sql语句
    open-in-view: true
```



PO、VO：https://www.cnblogs.com/cuiqq/p/11089279.html



SpringBoot2.0（官方推荐）

```java
@Configuration
public class WebMvcConfg implements WebMvcConfigurer {
}
```

SpringBoot2.0以前版本

```java
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
｝
```



IDEA查看继承树 ctrl alt u

查看继承层次结构 CTRL h



```java
@Transactional
@Override
public Type saveType(Type type) {
    return typeRepository.save(type);
}
```

使用的是 import org.springframework.transaction.annotation.Transactional;







统一异常处理

AOP日志处理

导入模板页面修改fragment

实体类生成对应数据库表

管理员登录推出，权限拦截，MD5加密