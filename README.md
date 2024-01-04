# SAST-Link-SDK 接入指南

## Java-SDK

### 简介

- 欢迎使用SAST-Link 开发者工具套件(SDK)，SAST-Link 是一个全面的成员信息管理系统和 OAuth，旨在提供安全高效的方式来管理和授权对应用程序和服务的访问。
- SAST-Link-SDK 是SAST-Link OAuth 及 用户API 的配套工具。为方便 JAVA 开发者调试和接入SAST-Link API，这里向您介绍适用于 Java 的SAST-Link 开发工具包，并提供首次使用开发工具包的简单示例。让您快速获取SAST-Link Java SDK 并开始调用。

### 依赖环境

- JDK 17 版本及以上

- 获取安全凭证。安全凭证包含 client_id 及 client_secret 两部分。client_id 用于标识 API 调用者的身份，client_secret 用于服务器端验证。前往SAST-Link管理页面获取。验证密钥 client_secret 必须严格保管，避免泄露。

- 调用地址

### 安装SDK

#### Maven

1. 使用Maven配合进行依赖管理，为pom文件添加依赖

~~~xml
<dependency>
     <groupId>fun.feellmoose</groupId>
     <artifactId>sast-link-sdk</artifactId>
     <version>0.1.2</version>
</dependency>
~~~

#### 常见问题

### 使用示例

~~~java
class Example {
    
    static private SastLinkService service = new HttpClientSastLinkService.Builder()
            .setRedirectUri("uri")
            .setClientId("id")
            .setClientSecret("secret")
            .setCodeVerifier("verifier")
            .setHostName("path")
            .build();
    
    public static void main(String[] args) {
        AccessToken accessToken = service.accessToken("code");
        User user = service.user(accessToken.getAccessToken());
        RefreshToken refreshToken = service.refreshToken(accessToken.getRefreshToken());
    }

}
~~~

#### 常见问题

---

感谢您使用 SAST-Link-SDK，使用中如遇相关问题请先阅读"常见问题"，若仍无法解决请尝试联系 SAST

