# blog

blog 的 springboot 后端服务，并且将前端模板整合进来

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



```html
<!-- 下面这种方式html原型可以正常显示div ，thymeleaf 渲染后不显示div -->
<!--/*-->
<div class="item">xxxx</div>
<!--*/-->
```



Ctrl + i 实现方法



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



新增type的非空校验

1. 可以这么做

前端

```html
<!--/*/
<div class="ui error message" th:unless="${#strings.isEmpty(message)}">
<i class="close icon"></i>
<div class="header">提示：</div>
<p th:text="${message}"></p>
</div>
/*/-->
```

后端

```java
/**
  * 新增操作
  * @return
  */
@PostMapping("/types")
public String save(Type type, RedirectAttributes attributes, Model model) {
    if (StringUtils.isEmpty(type.getName())) {
        model.addAttribute("message", "名称不能为空");
        return "admin/types-input";
    }
   	...
}
```

2. 也可以这么做

```xml
<!-- 需要添加依赖 -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.1.6.Final</version>
</dependency>
```

前端修改

```html
<form action="#" method="post" class="ui form" th:object="${type}" th:action="@{/admin/types}">
    <div class="required field">
        <div class="ui left labeled input">
            <label class="ui teal basic label">分类名称</label>
            <!-- 输入框name应和封装的对象属性名一致 -->
            <input type="text" name="name" placeholder="分类名称" th:value="*{name}">
        </div>
    </div>

    <!-- validation error message -->
    <div class="ui error message"></div>
    <!--/*/
	<div class="ui negative message" th:if="${#fields.hasErrors('name')}">
	<i class="close icon"></i>
	<div class="header">验证失败：</div>
	<p th:errors="*{name}">提交信息不符合规则</p>
	</div>
	/*/-->
    ......
```

>想要thymeleaf模板正确渲染错误消息需要为form表单添加如下属性
>
>th:object="${type}"
>th:value="*{name}"

后端修改

```java
// first
@Entity
@Table(name = "t_type")
public class Type {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "分类名称不能为空")
    private String name;
    ......
        
// second
/**
 * 跳到新增页
 * @return
 */
@GetMapping("/types/input")
public String input(Model model) {
    model.addAttribute("type", new Type());
    return "admin/types-input";
}
    
// third
/**
 * 新增操作
 * @return
 */
@PostMapping("/types")
public String save(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
    if (result.hasErrors()) {
        return "admin/types-input";
    }
    ......
```

> 新增操作需要做如下修改
>
> @Valid
> BindingResult result
> 判断是否发生错误

> 跳到新增页前需要准备好type
>
> model.addAttribute("type", new Type());

> 实体类需要标注 @NotBlank
>
> @NotBlank @NotEmpty 被移除
> Hibernate 6.x 中， 「org.hibernate.validator」包下的@NotBlank、@NotEmpty被移除了，若是想使用验证注解，可以使用 「javax.validation.constraints」 包下的@NotBlank、@NotEmpty。「org.hibernate.validator」 定义了校验实体类属性的一套规范，「javax.validation.constraints」 实现了这种规范。
>
> 即使使用的是javax.validation.constraints.NotBlank也要导入hibernate validator

另外BindingResult可以手动的加异常信息，这样就可以在发生非校验错误时返回我们自己定义的异常信息

```java
result.rejectValue("name", "nameError", type.getName() + " 分类已存在");
```

我多挖一锹挖出来一个坑，如果少一个参数会发生如下异常

> result.rejectValue("name", " 分类已存在");
>
> Caused by: org.springframework.context.NoSuchMessageException: No message found under code 'aaa 分类已存在.type.name' for locale 'zh_CN'.



更新分类与新增分类复用同一个页面表单，需要对form的请求地址做处理，同时id使用隐藏域

```html
<form action="#" method="post" class="ui form" th:object="${type}" 
      th:action="*{id}==null ? @{/admin/types} : @{/admin/types/{id}(id=${type.id})}">
    <input type="hidden" name="id" th:value="*{id}">
```

URL 对比

| 操作     | 该工程请求                        | 以前的写法                                                   |
| -------- | --------------------------------- | ------------------------------------------------------------ |
| 获取所有 | @GetMapping("/types")             | @RequestMapping(value = "/emps")                             |
| 查询一个 | @GetMapping("/types/{id}/input")  | @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET) |
| 更新     | @PostMapping("/types/{id}")       | @RequestMapping(value = "/emp/{id}", method = RequestMethod.PUT) |
| 删除     | @GetMapping("/types/{id}/delete") | @RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE) |
| 添加     | @PostMapping("/types")            | @RequestMapping(value = "/emp", method = RequestMethod.POST) |



标签和分类是一样的，按照分类写标签就行



让IDEA更友好 https://www.cnblogs.com/zhou2016/p/5545800.html
Ctrl+Alt+O	自动import和去除没用的import



[使用Thymeleaf时，ajax的url如何设置？](https://www.cnblogs.com/zhangruifeng/p/12347419.html)



jpa提供复杂查询接口： `JpaSpecificationExecutor<T>` 

```java
@Transactional
@Override
public Page<Blog> getList(Pageable pageable, Blog blog) {
    return blogRepository.findAll(new Specification<Blog>() {
        @Override
        public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();
            // 标题
            if (!"".equals(blog.getTitle()) || blog.getTitle() != null) {
                predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
            }
            // 分类
            if (blog.getType().getId() != null) {
                predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getType().getId()));
            }
            // 推荐
            if (blog.isRecommend()) {
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommed"), blog.isRecommend()));
            }
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            return null;
        }
    }, pageable);
}
```



thymeleaf 自定义属性

```html
<a class="item" onclick="page(this)" th:attr="data-page=${page.number} - 1)" th:unless="${page.first}">上一页</a>

取值
$(obj).data("page")
```



semantic-ui支持下拉列表通过api取值，适用于下拉列表中数据多的情况，具体参看dropdown

这个项目没有那么多下拉选项，跳转页面的时候直接带过来最好



```java
import javax.persistence.*; // 注意是这个包下的注解

// 标记注解，不作为表结构
@Transient
private String tagIds;


// 注意这里字段的长度
@Basic(fetch = FetchType.LAZY) // 太大了，使用延迟加载
@Lob
private String content; // 内容，string默认255，应该是长文本类型


// 怎么转的不重要，重要的是在controller中调用此方法完成转换，然后在页面的上可获取该属性值 tagIds
public void initTagIds() {
    this.tagIds = tagsToIds(this.getTags()); 
}
```



springboot-jpa 的EntityNotFoundException

```java
@Query("select t from Tag t where t.id = ?1 ")
Tag getById(Long id);
```

参考：https://blog.csdn.net/u012462033/article/details/79466679/



[十大Intellij IDEA快捷键](https://blog.csdn.net/dc_726/article/details/42784275)



MarkDown转HTML：commonmark-java

https://github.com/atlassian/commonmark-java

```xml
<dependency>
    <groupId>com.atlassian.commonmark</groupId>
    <artifactId>commonmark</artifactId>
    <version>0.15.2</version>
</dependency>
<dependency>
    <groupId>com.atlassian.commonmark</groupId>
    <artifactId>commonmark-ext-gfm-tables</artifactId>
    <version>0.15.2</version>
</dependency>
<dependency>
    <groupId>com.atlassian.commonmark</groupId>
    <artifactId>commonmark-ext-heading-anchor</artifactId>
    <version>0.15.2</version>
</dependency>
```

> 基本：commonmark
>
> table：commonmark-ext-gfm-tables
>
> anchor（锚点）：commonmark-ext-heading-anchor



th:text="" 这种不会转移html标签，即例如 `<p>` 会被直接输出到界面上

th:utext="" 这种会转移html标签，即例如 `<p>` 会被解释为html元素正常进行网页显示



自己加一个img转换，后经测试不加也行，记录下来说不定啥时候单独使用

```xml
<dependency>
    <groupId>com.atlassian.commonmark</groupId>
    <artifactId>commonmark-ext-image-attributes</artifactId>
    <version>0.15.2</version>
</dependency>
```



注意thymeleaf模板中的 thymeleaf语法使用和路径书写方式

```html
<script th:inline="javascript">
    /* 二维码生成 */
    var hostUrl = "127.0.0.1:8080";
    var url = /*[[@{/blog/{id}(id=${blog.id})}]]*/""; // 模板引擎的路径书写方式
    var qrcode = new QRCode("qrcode", {
        // text: "http://jindo.dev.naver.com/collie",
        text: hostUrl + url,
        width: 110,
        height: 110,
        colorDark : "#000000",
        colorLight : "#ffffff",
        correctLevel : QRCode.CorrectLevel.H
    });
</script>
```

未加 inline 时

```js
/* 二维码生成 */
var hostUrl = "127.0.0.1:8080";
var url = /*/blog/85*/""; // 模板引擎的路径书写方式
var qrcode = new QRCode("qrcode", {
    // text: "http://jindo.dev.naver.com/collie",
    text: hostUrl + url,
    width: 110,
    height: 110,
    colorDark : "#000000",
    colorLight : "#ffffff",
    correctLevel : QRCode.CorrectLevel.H
});
```



使用onclick跳转页面

```html
<h2 class="ui teal header item" onclick="javascript:window.location.href='/'">Blog</h2>
```





idea maven import 卡死：https://blog.csdn.net/weixin_44647868/article/details/97148498



**JPA查询关键字** https://blog.csdn.net/lionel_zsj/article/details/98937678

Spring Data JPA使用方法名可解决大部分的查询问题，但是也存在不能解决所有问题，以下是方法名中支持的关键字：

| 关键字               | 简单示例                        | JPQL片段示例                                                 |
| -------------------- | ------------------------------- | ------------------------------------------------------------ |
| AND                  | findByLastNameAndFirstName      | WHERE Entity.lastName = ?1 AND Entity.firstName = ?2         |
| IsNull               | findByAddressIsNull             | WHERE Entity.address is NULL                                 |
| IsNotNull            | readByAddressIsNotNull          | WHERE Entity.address NOT NULL                                |
| NotNull              | readByAddressNotNull            | WHERE Entity.address NOT NULL                                |
| OR                   | readByLastNameOrFirstName       | WHERE Entity.lastName = ?1 OR Entity.firstName = ?2          |
| Between              | getByStartDateBetween           | WHERE Entity.startDate BETWEEN ?1 AND ?2                     |
| LessThan             | findByAgeLessThan               | WHERE Entity.age < ?1                                        |
| GreaterThan          | readByAgeGreaterThan            | WHERE Entity.age > ?1                                        |
| After`只作用在时间`  | findByStartDateAfter            | WHERE Entity.startDate > ?1                                  |
| Before`只作用在时间` | findByStartDateBefore           | WHERE Entity.startDate < ?1                                  |
| LessThanEqual        | findByAgeLessThenEqual          | WHERE Entity.age <= ?1                                       |
| GreaterThanEqual     | readByAgeGreaterThenEqual       | WHERE Entity.age >= ?1                                       |
| Is                   | findByLastNameIs                | WHERE Entity.lastName = ?1                                   |
| Equal                | getByFirstNameEqual             | WHERE Entity.firstName = ?1                                  |
| Like                 | findByNameLike                  | WHERE [Entity.name](http://entity.name/) LIKE ? `不包括'%'符号` |
| Not Like             | findByNameNotLike               | WHERE [Entity.name](http://entity.name/) not like ?1 `不包括'%'符号` |
| StartingWith         | readByNameStartingWith          | WHERE [Entity.name](http://entity.name/) like ‘?1%’`条件以'%'符号结尾` |
| EndingWith           | readByName EndingWith           | WHERE [Entity.name](http://entity.name/) like ‘%?1’`条件以'%'符号开头` |
| Containing           | getByNameContaining             | WHERE [Entity.name](http://entity.name/) like ‘%?1%’ `包含'%'符号` |
| OrderBy              | findByAgeOrderByAddressDesc     | WHERE Entity.age = ? Order By Entity.address DESC            |
| Not                  | readByAgeNot                    | WHERE Entity.age <> ?1 `不等于`                              |
| In                   | findByNameIn(Collection name)   | WHERE [Entity.name](http://entity.name/) IN (?1 ,?2, ?3)     |
| NotIn                | getByNameNotIn(Collection name) | WHERE [Entity.name](http://entity.name/) NOT IN (?1 ,?2, ?3) |
| True                 | readByFloagTrue()               | WHERE Entity.floag = true                                    |
| False                | readByFloagFalse()              | WHERE Entity.floag = false                                   |
| IgnoreCase           | findByNameIgnoreCase            | WHERE UPPER([Entity.Name](http://entity.name/)) = UPPER(?1)  |



Thymeleaf判断集合是否为空 https://blog.csdn.net/ymylsq1/article/details/106646435

```html
<div th:if="${#lists.isEmpty(myCart)}" ></div>
<div th:if="${not #lists.isEmpty(myCart)}"></div>
```



IDEA直接跳转到方法的实现类  Ctrl + Alt + 鼠标左键



jpa手写update

李哥结论：@Modifying、@Query、@Transactional配合使用

```java
// 更新views
@Transactional
@Modifying
@Query("update Blog b set b.views = b.views+1 where b.id = ?1")
int updateViews(Long id);
```

> update需要配合@Modifying，且需要事务@Transactional（service或repository加都可以）

只写了@Query

```
org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.colm.blog.po.Blog b set b.views = b.views+1 where b.id = ?1]; nested exception is java.lang.IllegalStateException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.colm.blog.po.Blog b set b.views = b.views+1 where b.id = ?1]
......
Caused by: java.lang.IllegalStateException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.colm.blog.po.Blog b set b.views = b.views+1 where b.id = ?1]
......
Caused by: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.colm.blog.po.Blog b set b.views = b.views+1 where b.id = ?1]
......
01:45:52.096 [http-nio-8080-exec-5] WARN  o.s.w.s.m.m.a.ExceptionHandlerExceptionResolver:199- Resolved [org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.colm.blog.po.Blog b set b.views = b.views+1 where b.id = ?1]; nested exception is java.lang.IllegalStateException: org.hibernate.hql.internal.QueryExecutionRequestException: Not supported for DML operations [update com.colm.blog.po.Blog b set b.views = b.views+1 where b.id = ?1]]
```

加上@Modifying

```
org.springframework.dao.InvalidDataAccessApiUsageException: Executing an update/delete query; nested exception is javax.persistence.TransactionRequiredException: Executing an update/delete query
......
Caused by: javax.persistence.TransactionRequiredException: Executing an update/delete query
......
01:52:44.095 [http-nio-8080-exec-1] WARN  o.s.w.s.m.m.a.ExceptionHandlerExceptionResolver:199- Resolved [org.springframework.dao.InvalidDataAccessApiUsageException: Executing an update/delete query; nested exception is javax.persistence.TransactionRequiredException: Executing an update/delete query]
```

> 此时加上事务，三者到齐异常解决

我看了以后产生了疑问，查了一下，下面资料这哥们个人博客写了不少

相关知识点：https://www.cnblogs.com/yihuihui/p/11071949.html



清理mysql测试数据

```sql
DELETE FROM t_blog WHERE id IN 
(SELECT tt.id FROM 
	(SELECT b.id FROM t_blog b WHERE b.id != 60 AND b.description IS NULL) AS tt
);
```

查询、开启、禁用外键约束

```sql
-- 查询当前外键约束是否打开
SELECT @@FOREIGN_KEY_CHECKS;
-- 设置为1的时候外键约束是打开的，设置为0的时候外键约束是关闭的;
SET FOREIGN_KEY_CHECKS=1;
```



查询年份

```sql
SELECT DATE_FORMAT(b.create_time, '%Y') AS YEAR FROM t_blog b GROUP BY YEAR ORDER BY YEAR DESC;
```

查询指定年分的博客

```sql
SELECT * FROM t_blog b WHERE DATE_FORMAT(b.create_time, '%Y') = '2020';
```





简单打镜像方式：https://www.cnblogs.com/yslss/p/12985931.html



---



后期搜索可做ES全文检索，排期吧

后期各种静态图片改造成可配置的（使用配置文件或者oss）

后期优化，评论的时候用户头像可选，提供几种默认头像

> 思路参考下来列表，且可参照用户邮箱查找历史头像，多个历史头像按时间倒序排列取最近的一个

后期优化，发表评论定位到评论位置

> 思路点击回复的时候通过js给当前位置添加标记，若是发表评论则不滚动

后期优化，点击评论后输入框状态为回复，此时不想回复了想要直接发表评论，应实现切换

> 思路添加一个取消回复button，回复的时候显示button，点击将表单重置

后期可以对评论消息做后台管理，审核通过可以显示，可以标记仅管理员可见，可删除评论

后期可加友链以及收藏的其他优秀作者的个人博客

> 概念解释，友链即有人链接涉及到互换或友情展示。收藏个博即博客写得好或技术牛
