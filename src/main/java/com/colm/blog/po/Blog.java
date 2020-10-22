package com.colm.blog.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 博客 PO
 *
 * Created by Colm on 2020/10/19
 */
@Entity
@Table(name = "t_blog")
public class Blog {
    // 这里巨坑无比，注意使用javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;
    private String title; // 标题
    private String content; // 内容
    private String firstPicture; // 首图
    private String flag; // 标记
    private String views; // 浏览数量
    private boolean appreciation; // 赞赏开关
    private boolean shareStatement; // 分享开关
    private boolean commentabled; // 评论开关
    private boolean published; // 发布状态
    private boolean recommend; // 评论开关
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime; // 创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; // 更新时间

    @ManyToOne
    private Type type;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments;

    public Blog() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public void setShareStatement(boolean shareStatement) {
        this.shareStatement = shareStatement;
    }

    public void setCommentabled(boolean commentabled) {
        this.commentabled = commentabled;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public String getViews() {
        return views;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public boolean isShareStatement() {
        return shareStatement;
    }

    public boolean isCommentabled() {
        return commentabled;
    }

    public boolean isPublished() {
        return published;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", views='" + views + '\'' +
                ", appreciation=" + appreciation +
                ", shareStatement=" + shareStatement +
                ", commentabled=" + commentabled +
                ", published=" + published +
                ", recommend=" + recommend +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}
