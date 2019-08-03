package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.ucenter.dao
 * @date 2019/8/1 14:52
 * @Description
 */
@Mapper
public interface XcMenuMapper {

    List<XcMenu> selectPermissionByUserId(String userid);

}
