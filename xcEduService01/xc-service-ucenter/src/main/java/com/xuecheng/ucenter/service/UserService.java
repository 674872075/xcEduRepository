package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.ucenter.service
 * @date 2019/7/31 14:41
 * @Description
 */
@Service
public class UserService {

    @Autowired
    private XcCompanyUserRepository xcCompanyUserRepository;

    @Autowired
    private XcUserRepository xcUserRepository;

    @Autowired
    private XcMenuMapper xcMenuMapper;
    /**
     * 根据用户账号查询用户信息
     * @param username
     * @return
     */
    public XcUser findXcUserByUsername(String username){
        return xcUserRepository.findXcUserByUsername(username);
    }

    public XcUserExt getUserExt(String username) {

        XcUser xcUser = xcUserRepository.findXcUserByUsername(username);
        //查询到的结果为空
        if(xcUser==null){
            return null;
        }
        String userId = xcUser.getId();
        //查询用户权限
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);

        XcUserExt xcUserExt=new XcUserExt();
        //将xcUser属性拷贝到xcUserExt
        BeanUtils.copyProperties(xcUser,xcUserExt);
        //设置用户权限
        xcUserExt.setPermissions(xcMenus);
        //根据用户id查询中间表
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);
        if(xcCompanyUser!=null){
            //将companyId设置进去
            xcUserExt.setCompanyId(xcCompanyUser.getCompanyId());
        }
        return xcUserExt;
    }
}
