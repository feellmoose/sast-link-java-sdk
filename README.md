# SAST-Link-SDK 接入指南

## Java-SDK

### 简介

- 欢迎使用SAST-Link 开发者工具套件(SDK)，SAST-Link 是一个全面的成员信息管理系统和 OAuth，旨在提供安全高效的方式来管理和授权对应用程序和服务的访问。
- SAST-Link-SDK 是SAST-Link OAuth 及 用户API 的配套工具。为方便 JAVA 开发者调试和接入SAST-Link API，这里向您介绍适用于 Java 的SAST-Link 开发工具包，并提供首次使用开发工具包的简单示例。让您快速获取SAST-Link Java SDK 并开始调用。

### 依赖环境

- JDK 17 版本及以上

- 获取安全凭证。安全凭证包含 client_id 及 client_secret 两部分。client_id 用于标识 API 调用者的身份，client_secret 用于服务器端验证。前往[SAST-Link管理页面]( "点击前往获取安全凭证")获取。验证密钥 client_secret 必须严格保管，避免泄露。

- 调用地址()

### 安装SDK

#### 通过jar包及Maven进行安装

1. 前往[地址]()下载jar包
2. 项目根目录下新建lib目录，在lib目录下导入jar包
3. 使用Maven配合进行依赖管理，为pom文件添加依赖

~~~xml
<dependency>
     <groupId>link.sast</groupId>
     <artifactId>SastLink-Java-SDK</artifactId>
     <type>jar</type>
     <version>0.0.1</version>
     <scope>system</scope>
     <systemPath>${pom.basedir}/lib/SastLink-Java-SDK-0.0.1.jar</systemPath>
</dependency>
~~~

#### 常见问题

### 使用示例

#### 一，Spring环境

1. 注册Bean

~~~java
package fun.sast.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sast.sastlink.sdk.service.SastLinkService;
import sast.sastlink.sdk.service.impl.RestTemplateSastLinkService;

@Configuration
public class SastLinkServiceConfig {

    @Bean
    public SastLinkService sastLinkService(RestTemplate restTemplate) {
        return RestTemplateSastLinkService.Builder()
                .setRedirectUri("redirectUri")
                .setClientId("clientId")
                .setClientSecret("clientSecret")
                .setCodeVerifier("codeVerifier")
                .setHostName("linkhostName")
                .setRestTemplate(restTemplate)//若项目无restTemplate,可省略直接使用默认配置
                .build();
    }
    
}
~~~

2. 注入service直接使用，API使用示例及情景如下

~~~java
package fun.sast.example;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import sast.sastlink.sdk.model.UserInfo;
import sast.sastlink.sdk.model.response.AccessTokenResponse;
import sast.sastlink.sdk.model.response.RefreshResponse;
import sast.sastlink.sdk.service.impl.RestTemplateSastLinkService;

@Component
public class Example {
    @Resource
    private RestTemplateSastLinkService sastLinkService;

    public void USER() {
        //注册sast-link账号
        //获取邮箱发送验证码,验证邮箱有效性,返回registerTicket,由前端获取
        String registerTicket = sastLinkService.sendCaptcha("email@example.com");
        //获取验证码和用户设置好的密码,配合registerTicket注册,成功则返回true
        sastLinkService.checkCaptchaAndRegister("captcha_from_email", registerTicket, "password");
        //登录sast-link账号
        //用户使用邮箱密码登录,返回token
        String token = sastLinkService.login("email@example.com", "password");
        //用户使用token登出,登出后token失效,成功返回ture
        sastLinkService.logout(token);
    }

    public void OAUTH() {
        //使用code获取accessToken和refreshToken
        AccessTokenResponse accessTokenResponse = sastLinkService.accessToken("code");
        String accessToken = accessTokenResponse.getAccess_token();
        String refreshToken = accessTokenResponse.getRefresh_token();
        //accessToken过期后若refreshToken未过期,可使用refreshToken获取新的accessToken和refreshToken
        RefreshResponse refreshResponse = sastLinkService.refresh(refreshToken);
        accessToken = refreshResponse.getAccessToken();
        refreshToken = refreshResponse.getRefreshToken();
        //使用accessToken获取userInfo
        UserInfo userInfo = sastLinkService.userInfo(accessToken);
    }

    public void OAUTH_BY_OTHER() {
        //由于部分业务(小程序)需要,需要模仿sastLink网页端承担部分验证工作,否则只使用accessToken和refreshToken
        //首先用户需要使用邮箱密码登录sast-link账号,返回token
        String token = sastLinkService.login("email@example.com", "password");
        //由前端返回三个参数,获取code,code只可以使用一次
        String code = sastLinkService.authorize(token, "code_challenge", "code_challenge_method");
        //使用code获取accessToken和refreshToken
        AccessTokenResponse accessTokenResponse = sastLinkService.accessToken(code);
        String accessToken = accessTokenResponse.getAccess_token();
        String refreshToken = accessTokenResponse.getRefresh_token();
        //accessToken过期后若refreshToken未过期,可使用refreshToken获取新的accessToken和refreshToken
        RefreshResponse refreshResponse = sastLinkService.refresh(refreshToken);
        accessToken = refreshResponse.getAccessToken();
        refreshToken = refreshResponse.getRefreshToken();
        //使用accessToken获取userInfo
        UserInfo userInfo = sastLinkService.userInfo(accessToken);
        //使用token登出,登出后token失效,成功返回ture
        sastLinkService.logout(token);
    }
    
}
~~~

#### 二，其他环境

- API使用方式相同

~~~java
package sast.evento.service;

import sast.sastlink.sdk.model.UserInfo;
import sast.sastlink.sdk.model.response.AccessTokenResponse;
import sast.sastlink.sdk.model.response.RefreshResponse;
import sast.sastlink.sdk.service.SastLinkService;
import sast.sastlink.sdk.service.impl.RestTemplateSastLinkService;

public class Example {
    private static final SastLinkService sastLinkService = RestTemplateSastLinkService.Builder()
            .setRedirectUri("redirectUri")
            .setClientId("clientId")
            .setClientSecret("clientSecret")
            .setCodeVerifier("codeVerifier")
            .setHostName("linkhostName")
            .build();
    
    public void USER() {
        //注册sast-link账号
        //获取邮箱发送验证码,验证邮箱有效性,返回registerTicket,由前端获取
        String registerTicket = sastLinkService.sendCaptcha("email@example.com");
        //获取验证码和用户设置好的密码,配合registerTicket注册,成功则返回true
        sastLinkService.checkCaptchaAndRegister("captcha_from_email", registerTicket, "password");
        //登录sast-link账号
        //用户使用邮箱密码登录,返回token
        String token = sastLinkService.login("email@example.com", "password");
        //用户使用token登出,登出后token失效,成功返回ture
        sastLinkService.logout(token);
    }

    public void OAUTH() {
        //使用code获取accessToken和refreshToken
        AccessTokenResponse accessTokenResponse = sastLinkService.accessToken("code");
        String accessToken = accessTokenResponse.getAccess_token();
        String refreshToken = accessTokenResponse.getRefresh_token();
        //accessToken过期后若refreshToken未过期,可使用refreshToken获取新的accessToken和refreshToken
        RefreshResponse refreshResponse = sastLinkService.refresh(refreshToken);
        accessToken = refreshResponse.getAccessToken();
        refreshToken = refreshResponse.getRefreshToken();
        //使用accessToken获取userInfo
        UserInfo userInfo = sastLinkService.userInfo(accessToken);
    }

    public void OAUTH_BY_OTHER() {
        //由于部分业务(小程序)需要,需要模仿sastLink网页端承担部分验证工作,否则只使用accessToken和refreshToken
        //首先用户需要使用邮箱密码登录,返回token
        String token = sastLinkService.login("email@example.com", "password");
        //由前端返回三个参数,获取code,code只可以使用一次
        String code = sastLinkService.authorize(token, "code_challenge", "code_challenge_method");
        //使用code获取accessToken和refreshToken
        AccessTokenResponse accessTokenResponse = sastLinkService.accessToken(code);
        String accessToken = accessTokenResponse.getAccess_token();
        String refreshToken = accessTokenResponse.getRefresh_token();
        //accessToken过期后若refreshToken未过期,可使用refreshToken获取新的accessToken和refreshToken
        RefreshResponse refreshResponse = sastLinkService.refresh(refreshToken);
        accessToken = refreshResponse.getAccessToken();
        refreshToken = refreshResponse.getRefreshToken();
        //使用accessToken获取userInfo
        UserInfo userInfo = sastLinkService.userInfo(accessToken);
        //使用token登出,登出后token失效,成功返回ture
        sastLinkService.logout(token);
    }
}

~~~

#### 常见问题

---

感谢您使用 SAST-Link-SDK，使用中如遇相关问题请先阅读"常见问题"，若仍无法解决请尝试联系 SAST

