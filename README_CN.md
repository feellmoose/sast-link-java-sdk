# SAST-Link-SDK

## Java-SDK

中文/[English](https://github.com/feellmoose/sast-link-java-sdk/blob/main/README.md)

### 简介

- 欢迎使用 SAST-Link 开发者工具套件（SDK）。SAST-Link 是面向南京邮电大学及校学生科学技术协会（SAST）的成员信息管理系统与 OAuth 解决方案，旨在为应用服务提供安全高效的访问管理和授权机制。
- SAST-Link-SDK 是 SAST-Link OAuth 及用户 API 的配套开发工具。为帮助 Java 开发者快速调试和接入 SAST-Link API，本文档将介绍 Java 版 SDK 的使用方法，并提供开箱即用的基础示例代码，助您快速上手调用。

### 环境要求

- **JDK 17 及以上版本**
- **Maven 或 Gradle** 构建工具
- 支持 **Spring 2.x RestTemplate** 或 **OkHttp** 客户端
- **安全凭证**：包含 `client_id`（身份标识）和 `client_secret`（验证密钥）。需通过邮件申请接入权限，请发送请求至 [sast@njupt.edu.cn](mailto:sast@njupt.edu.cn)。`client_secret` 属于敏感信息，请严格保密。

### 使用指南

#### Maven 依赖

1. 在项目的 `pom.xml` 中添加以下依赖：

```xml
<dependency>
    <groupId>fun.feellmoose</groupId>
    <artifactId>sast-link-sdk</artifactId>
    <version>0.1.2</version>
</dependency>
```

### 示例代码

```java
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
```

---

感谢使用 SAST-Link-SDK！  
**问题反馈**：如遇技术问题，请在 [GitHub 仓库提交 Issue](https://github.com/your-repo-link)。  
**权限申请**：关于凭证获取或接入权限，请联系 [sast@njupt.edu.cn](mailto:sast@njupt.edu.cn)。
