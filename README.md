# blog
blog 的 springboot 后端服务

## Thymeleaf
spring-boot-stater-thymeleaf 默认导入的版本是2.3
3更好用，性能更好



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