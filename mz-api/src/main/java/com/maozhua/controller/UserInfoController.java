package com.maozhua.controller;

import com.maozhua.bo.UpdatedUserBO;
import com.maozhua.config.MinIoConfig;
import com.maozhua.enums.FileTypeEnum;
import com.maozhua.enums.UserInfoModifyType;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.grace.result.ResponseStatusEnum;
import com.maozhua.pojo.Users;
import com.maozhua.service.UserService;
import com.maozhua.utils.MinIoUtil;
import com.maozhua.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/6/3 23:43
 * @description 用户信息接口
 */
@Slf4j
@Api(tags = "UserInfoController => 用户信息接口")
@RequestMapping("userInfo")
@RestController
public class UserInfoController extends BaseInfoProperties {

    @Resource
    private UserService userService;

    @Resource
    private MinIoConfig minIoConfig;

    @ApiOperation(value = "获取用户信息")
    @GetMapping("query")
    public GraceJsonResult getUserInfo(@RequestParam String userId) {
        Users user = userService.getUserById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        // 我的关注博主总数量
        String myFollowsCountsStr = redisOperator.get(REDIS_MY_FOLLOWS_COUNTS + ":" + userId);

        // 我的粉丝总数
        String myFansCountsStr = redisOperator.get(REDIS_MY_FANS_COUNTS + ":" + userId);

        // 用户获赞总数，视频+评论（点赞/喜欢）总和
        String likedVlogCountsStr = redisOperator.get(REDIS_VLOG_BE_LIKED_COUNTS + ":" + userId);
        String likedVlogerCountsStr = redisOperator.get(REDIS_VLOGER_BE_LIKED_COUNTS + ":" + userId);

        Integer myFollowsCounts = 0;
        Integer myFansCounts = 0;
        Integer likedVlogCounts = 0;
        Integer likedVlogerCounts = 0;
        Integer totalLikeMeCounts = 0;

        if (StringUtils.isNotBlank(myFollowsCountsStr)) {
            myFollowsCounts = Integer.parseInt(myFollowsCountsStr);
        }
        if (StringUtils.isNotBlank(myFansCountsStr)) {
            myFansCounts = Integer.parseInt(myFansCountsStr);
        }
        if (StringUtils.isNotBlank(likedVlogCountsStr)) {
            likedVlogCounts = Integer.parseInt(likedVlogCountsStr);
        }
        if (StringUtils.isNotBlank(likedVlogerCountsStr)) {
            likedVlogerCounts = Integer.parseInt(likedVlogerCountsStr);
        }
        totalLikeMeCounts = likedVlogCounts + likedVlogerCounts;

        userVO.setMyFollowsCounts(myFollowsCounts);
        userVO.setMyFansCounts(myFansCounts);
        userVO.setTotalLikeMeCounts(totalLikeMeCounts);

        return GraceJsonResult.ok(userVO);
    }

    @ApiOperation(value = "修改用户信息")
    @PostMapping("modifyUserInfo")
    public GraceJsonResult modifyUserInfo(@RequestBody UpdatedUserBO updatedUserBO,
                                          @RequestParam Integer type) {

        UserInfoModifyType.checkUserInfoTypeIsRight(type);

        Users newUserInfo = userService.updateUserInfo(updatedUserBO, type);

        return GraceJsonResult.ok(newUserInfo);
    }

    @ApiOperation(value = "修改用户背景图或头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "上传图片", required = true, dataType = "__file"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "上传类型 0:背景图 1:用户头像", required = true, dataType = "Integer"),
    })
    @PostMapping("modifyImage")
    public GraceJsonResult modifyImage(MultipartFile file,
                                       @RequestParam String userId,
                                       @RequestParam Integer type) throws Exception {

        if (!type.equals(FileTypeEnum.BGIMG.type) && !type.equals(FileTypeEnum.FACE.type)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();

        MinIoUtil.uploadFile(minIoConfig.getBucketName(),
                fileName,
                file.getInputStream(),
                contentType);

        String imgUrl = minIoConfig.getFileHost()
                + "/"
                + minIoConfig.getBucketName()
                + "/"
                + fileName;

        // 修改图片地址到数据库
        UpdatedUserBO updatedUserBO = new UpdatedUserBO();
        updatedUserBO.setId(userId);
        if (type.equals(FileTypeEnum.BGIMG.type)) {
            updatedUserBO.setBgImg(imgUrl);
        } else {
            updatedUserBO.setFace(imgUrl);
        }
        Users user = userService.updateUserInfo(updatedUserBO);

        return GraceJsonResult.ok(user);
    }


}
