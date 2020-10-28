package com.colm.blog.service;

import com.colm.blog.dao.CommentRepository;
import com.colm.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Colm on 2020/10/28
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    /**
     * 根据博客id获取评论列表
     * @param blogId
     * @return
     */
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime"); // 默认是正序：DEFAULT_DIRECTION = Sort.Direction.ASC;
        // 当前blog id下的parentComment为空的所有评论。换言之，所有的顶级评论
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return iterateComment(comments);
    }

    /**
     * 保存评论
     * @param comment
     * @return
     */
    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.getById(parentCommentId));
        } else {
            comment.setParentComment(null); // -1的时候没有实体化的评论对象
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> iterateComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>(); // 存放的是
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        // 合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * 将子节点组合起来
     * @param comments root根节点，blog不为空的对象集合
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replies = comment.getReplayComments();
            for (Comment reply : replies) {
                // 循环迭代，找出子代，存放在 tempReplies
                recursively(reply);
            }
            // 修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplayComments(tempReplies);
            // 清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }

    // 存放迭代找出的所有子代的集合
    private List<Comment> tempReplies = new ArrayList<>();

    /**
     * 递归迭代
     * @param comment
     */
    private void recursively(Comment comment) {
        tempReplies.add(comment); // 顶节点添加到临时存放集合
        if (comment.getReplayComments().size() > 0) {
            List<Comment> replies = comment.getReplayComments();
            for (Comment reply : replies) {
                tempReplies.add(reply);
                if (reply.getReplayComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }

}
