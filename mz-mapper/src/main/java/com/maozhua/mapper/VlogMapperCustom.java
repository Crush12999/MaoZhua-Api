package com.maozhua.mapper;

import com.maozhua.vo.IndexVlogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/2 12:00
 * @description VlogMapper
 */
@Repository
public interface VlogMapperCustom {

    /**
     * 获取首页/搜索的视频列表
     *
     * @param map 查询参数
     * @return 视频列表
     */
    List<IndexVlogVO> listIndexVlogs(@Param("paramMap") Map<String, Object> map);

}