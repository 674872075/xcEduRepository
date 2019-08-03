package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.auth.client
 * @date 2019/7/31 15:44
 * @Description
 */
@FeignClient(XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient {

    @GetMapping("/ucenter/getuserext")
    XcUserExt getUserExt(@RequestParam("username") String username);
}
