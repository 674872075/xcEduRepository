package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.ucenter.dao
 * @date 2019/7/31 14:10
 * @Description
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser, String> {
    /**
     * 根据用户id查询所属企业id
     * @param userId
     * 用户id
     * @return
     */
    XcCompanyUser findByUserId(String userId);
}
