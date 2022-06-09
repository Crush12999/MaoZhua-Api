package com.maozhua.mapper;

import com.maozhua.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/2 12:00
 * @description CommentMapperCustom
 */
@Repository
public interface CommentMapperCustom {

    /**
     * 获取视频评论列表
     *
     * @param map 查询条件
     * @return 评论列表
     */
    List<CommentVO> listVlogComments(@Param("paramMap") Map<String, Object> map);
}