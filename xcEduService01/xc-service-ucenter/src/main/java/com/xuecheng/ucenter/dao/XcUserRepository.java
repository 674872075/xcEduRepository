package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.ucenter.dao
 * @date 2019/7/31 11:48
 * @Description
 */
public interface XcUserRepository extends JpaRepository<XcUser,String> {

    /**
     * 根据用户名查询用户信息
     * @param username
     * 用户名
     * @return
     */
    XcUser findXcUserByUsername(String username);

}
