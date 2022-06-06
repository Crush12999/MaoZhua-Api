package com.maozhua.mapper;

import com.maozhua.my.mapper.MyMapper;
import com.maozhua.pojo.Fans;
import com.maozhua.vo.FansVO;
import com.maozhua.vo.VlogerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @author sryzzz
 * @create 2022/6/2 12:00
 * @description FansMapperCustom
 */
@Repository
public interface FansMapperCustom extends MyMapper<Fans> {

    /**
     * 获取我关注的博主列表
     *
     * @param map 查询条件
     * @return 我关注的博主列表
     */
    List<VlogerVO> listMyFollows(@Param("paramMap") Map<String, Object> map);

    /**
     * 获取我的粉丝列表
     *
     * @param map 查询条件
     * @return 我的粉丝列表
     */
    List<FansVO> listMyFans(@Param("paramMap") Map<String, Object> map);
}