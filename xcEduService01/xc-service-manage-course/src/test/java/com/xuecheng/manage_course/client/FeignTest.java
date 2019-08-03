package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Tourist
 * @version 1.0
 * @package com.xuecheng.manage_cms.client
 * @date 2019/7/16 20:28
 * @Description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {

    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void findCmgPageById() {
        CmsPageResult cmsPage = cmsPageClient.findById("5a795ac7dd573c04508f3a56");
        System.out.println(cmsPage);
    }

    @Test
    /**
     * 解码jwt
     */
    public void testToken() {
        //公钥
        String pub_key = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
        String jwt_token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiLmtYvor5XotKbmiLciLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1NjQ2OTQxMTQsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsImNvdXJzZV9waWNfbGlzdCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6IjI0N2I3YzZkLTA5YmYtNDNmYi05MzRhLWFjNzgyYjFkMWFlZiIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.kJoRO0z69CrApswM6H_WldUpxGS0M44JOvpzq_aEjgL-Iw6DC27aTVYYHy6T2fkj2zTYPDWmWXMdXDGNMfZDCAn6DPrC_z2f1TFnq19OYMPZSDgQlfzXZVWsoraBfugg71iaPY_MxYljCbc0nzJNiuo2pEW-EC-KyuFGsaxz_Ursa8jwtEsSYGSjd4eZLDm11Zav-Cur88-QSQQ_zIuHe3UPcn9Nkm61phJpC_9zIhWQNOf10hX3JOPRYzt6eXATJPISb0yAA5w9rY1uO6CxtY55a6JgxoL0Syd7tlKVP5e9SUD2tJGHKTjZQ3tpzaqHP0gqq1s8uHwyF6M3Q_rB2w"; //对jwt解码
        Jwt jwt = JwtHelper.decodeAndVerify(jwt_token, new RsaVerifier(pub_key));
        //拿到令牌的自定义内容
        String claims = jwt.getClaims();
        //打印
        System.out.println(claims);
    }
}
