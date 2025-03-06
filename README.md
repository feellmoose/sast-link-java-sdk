# SAST-Link-SDK

[![License](https://img.shields.io/badge/license-AGPLv3-blue.svg)](https://choosealicense.com/licenses/agpl-3.0/)

English/[中文](https://github.com/feellmoose/sast-link-java-sdk/blob/main/README_CN.md)

## Java-SDK

### Introduction

- Welcome to the SAST-Link Developer Tools SDK. SAST-Link is a comprehensive member information management system and OAuth solution designed for Nanjing University of Posts and Telecommunications (NJUPT) and its Student Association of Science and Technology (SAST). It aims to provide secure and efficient methods for managing and authorizing access to applications and services.
- SAST-Link-SDK is a companion tool for SAST-Link OAuth and User APIs. To facilitate Java developers in debugging and integrating SAST-Link APIs, this document introduces the Java development toolkit for SAST-Link and provides simple examples for initial usage. It helps you quickly obtain the SAST-Link Java SDK and start making API calls.

### Prerequisites

- JDK 17 or higher
- Maven or Gradle
- Compatible with any Spring 2.x RestTemplate or OkHttp client
- **Security Credentials**: Security credentials consist of `client_id` and `client_secret`. The `client_id` identifies the API caller, while `client_secret` is used for server-side verification. To request access to SAST-Link, please email [sast@njupt.edu.cn](mailto:sast@njupt.edu.cn) for related inquiries. The `client_secret` must be securely stored and protected from disclosure.

### Usage Guide

#### Maven

1. Add the following dependency to your `pom.xml` for Maven dependency management:

```xml
<dependency>
    <groupId>fun.feellmoose</groupId>
    <artifactId>sast-link-sdk</artifactId>
    <version>0.1.2</version>
</dependency>
```

### Example Usage

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

Thank you for using SAST-Link-SDK. If you encounter any issues, **please submit an issue** on our GitHub repository. For questions about credentials or permissions, contact [sast@njupt.edu.cn](mailto:sast@njupt.edu.cn).
