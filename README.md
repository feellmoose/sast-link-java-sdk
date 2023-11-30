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

~~~java
class Example {
    
    static private SastLinkService service = HttpClientSastLinkService.Builder()
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

