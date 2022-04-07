由于该模块导入了security 相关依赖
```xml
<dependencies>
        <!-- oauth2 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.codehaus.jackson</groupId>
                    <artifactId>jackson-mapper-asl</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.security.oauth</groupId>
            <artifactId>spring-security-oauth2</artifactId>
            <version>${spring-security-oauth2.version}</version>
        </dependency>
</dependencies>
```

所以 springboot 依赖该模块时，需要在启动类添加注解，禁用相关功能
```java
@EnableAutoConfiguration(exclude={SecurityAutoConfiguration.class, OAuth2AutoConfiguration.class})
```