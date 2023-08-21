# 什么是JWT

JWT是JSON Web Token的缩写，是目前最流行的跨域认证解决方案，一种基于JSON的、用于在网络上声明某种主张的令牌token。



# JWT的数据结构

实际的 JWT 大概就像下面这样：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

> 它是一个很长的字符串，中间用点（`.`）分隔成三个部分。
>
> 注意，JWT 内部是没有换行的，这里只是为了便于展示，将它写成了几行。



JWT 的三个部分依次如下

- Header（头部）
- Payload（负载）
- Signature（签名）

写成一行，就是下面的样子

```
Header.Payload.Signature
```

## Header

Header 部分是一个 JSON 对象，描述 JWT 的元数据，通常是下面的样子。

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

上面代码中，`alg`属性表示签名的算法（algorithm），默认是 HMAC SHA256（写成 HS256）；

`typ`属性表示这个令牌（token）的类型（type），JWT 令牌统一写为`JWT`。

最后，将上面的 JSON 对象使用 Base64URL 算法转成字符串。

## Payload

Payload 部分也是一个 JSON 对象，用来存放实际需要传递的数据。JWT 规定了7个官方字段，供选用。

- iss (issuer)：签发人
- exp (expiration time)：过期时间
- sub (subject)：主题
- aud (audience)：受众
- nbf (Not Before)：不生效的时间
- iat (Issued At)：签发时间
- jti (JWT ID)：编号



除了官方字段，你还可以在这个部分定义私有字段，下面就是一个例子。

```json

{
  "iss": "lxj",
  "username": "root",
}
```

注意，JWT 默认是不加密的，任何人都可以读到，所以不要把秘密信息放在这个部分。



这个 JSON 对象也要使用 Base64URL 算法转成字符串。



## Signature

Signature 部分是对前两部分的签名，防止数据篡改。

首先，需要指定一个密钥（secret）。这个密钥只有服务器才知道，不能泄露给用户。然后，使用 Header 里面指定的签名算法（默认是 HMAC SHA256），按照下面的公式产生签名。

> ```javascript
> HMACSHA256(
>   base64UrlEncode(header) + "." +
>   base64UrlEncode(payload),
>   secret)
> ```

算出签名以后，把 Header、Payload、Signature 三个部分拼成一个字符串，每个部分之间用"点"（`.`）分隔，就可以返回给用户。





# 使用 Auth0 java-jwt 管理 JWT

## 依赖关系

首先，我们将 Auth0 Java JWT 库的 [Maven 依赖项](https://search.maven.org/search?q=g:com.auth0 AND a:java-jwt)添加到项目的 *pom.xml* 文件中：

```
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.3.0</version>
</dependency>
```

## 配置算法

我们首先创建`Algorithm`类的实例。在本教程中，我们将使用 HMAC256 算法对 JWT 进行签名，其中HMAC256(secret)方法的参数是指定加密算法所需的密钥。



HMAC（Hash-based Message Authentication Code）是一种基于哈希函数的消息认证码算法，用于对消息进行完整性校验和认证。HMAC算法需要一个密钥作为输入，该密钥将用于生成和验证消息的摘要。

```java
 Algorithm algorithm = Algorithm.HMAC256("nf");
```



> 在上述示例中，变量是一个包含密钥的字符串，用于创建HMAC-SHA256算法的实例。随后，可以使用该算法实例对JWT进行签名和验证操作，以确保其完整性和真实性。
>
> 
>
> 请注意，实际应用中应该选择一个更安全的、足够复杂和难以猜测的密钥值`secret`！！！
>
> 
>
> 如果密钥被别人知道了，可能会对系统的安全性产生严重的风险。因为密钥是用于生成和验证JWT的关键信息，掌握密钥的人可能会伪造有效的JWT、篡改数据或者进行其他恶意行为。
>
> 
>
> 所以请妥善保密好自己的密钥！！！！！！！！



##  创建JWT

为了创建一个JWT，我们使用`JWT.create()`方法，该方法返回`JWTCreator.Builder`类的实例，最后我们用`JWTCreator`的`sign()`方法，来创建一个JWT令牌

```java
    @Test
    public void createJwtTest() {
        Algorithm algorithm = Algorithm.HMAC256("nf");
        String jwtToken = JWT.create()
                .withIssuer("nf")//签名
                .withSubject("login")//主题，差不多是用途的意思
                .withClaim("username", "root")//这个方法是设置任何自定义声明
                .withIssuedAt(new Date())//标识创建的时间
                .withJWTId(UUID.randomUUID().toString())//JWT*的唯一标识符
                .withExpiresAt(new Date(System.currentTimeMillis() + 500000L))//标识到期的时间
//.withNotBefore(new Date(System.currentTimeMillis() + 1000L))//标识不应接受 JWT 进行处理的时间
                .sign(algorithm);//创建一个新的 JWT 并使用给定的算法进行签名
        System.out.println(jwtToken);
    }
```

上面的代码片段返回一个 JWT：

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2dpbiIsImlzcyI6ImJsYWNrIiwiZXhwIjoxNjg0MDY3OTczLCJpYXQiOjE2ODQwNjc5NjcsImp0aSI6IjZhNWZlNjk1LTc5NTUtNDg2MC1iMzU2LWY5MTY3Yjg2Mzk3YiIsInVzZXJuYW1lIjoicm9vdCJ9.QjnyQr3ehrx7vulHsxtKA0Ev2Lg7izkmJ3z3Hurr7pA
```

上面用来设置声明的一些 `JWTCreator.Builder`类方法：

- withIssuer（）– 标识创建令牌并对其进行签名的一方
- withSubject（）– 标识 JWT 的主题
- withIssuedAt（）– 标识创建 JWT 的时间;我们可以用它来确定 JWT 的年龄
- withExpiresAt（）– 标识 JWT 的到期时间
- withJWTId（）– JWT的唯一标识符
- withNotBefore（）– 标识不应接受 JWT 进行处理的时间
- withClaim（）– 用于设置任何自定义声明

## 验证JWT

1. 首先我们初始化一个 `JWTVerifier` 实例，我们将使用该实例来验证创建的令牌

2. 使用verifier的verify方法验证我们之前创建的JWT(jwtToken)，如果 JWT 无效，该方法将引发 `JWTVerizationException`

3. 如果JWT有效，我们就可以获取到`DecodedJWT`的实例，然后我们可以使用它的各种getter方法来获取声明

   例如：为了获取自定义声明，我们使用*DecodedJWT.getClaim（String）*方法。此方法返回声明的实例

```java
public static void verifyJwt(String jwt) {
        Algorithm algorithm = Algorithm.HMAC256("nf");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("nf")
                .build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        Claim username = decodedJWT.getClaim("username");
        System.out.println(username.asString());
}
```

