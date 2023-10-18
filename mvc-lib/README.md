# 说明
这个框架是一个教学用的框架，主要目标是只利用Servlet技术与jdk 8基础知识来实现一个功能上类似spring mvc框架的小框架，
以便初学者能更好的理解知识的运用，特别是OOP编程的思维逻辑与代码实现

# 框架功能

# 各个技术点

这里列出的一些技术点，是我认为初学者能学会，但不知道如何应用在实际的项目中的一些技术点，排名不分先后

列在这里是在看懂框架源码之后进行知识点的梳理使用，以便更好的掌握知识的运用

## 4种修饰符
- MvcContext的默认修饰符
- DispatcherServlet的protected修饰符
- HandlerContext的set相关方法

## 方法
- VoidViewResult:0行代码
- ExceptionHandlersExceptionResolver的resolveExceptionHandlerMethods方法：1行代码
- 多行代码 

### 静态方法

- ServletApiEnum#of
- com.nf.mvc.ViewResult#adaptHandlerResult
- com.nf.mvc.argument.MethodArgumentResolverComposite#defaultInstance

## 接口
- 典型接口:HandlerAdapter
- 都是默认方法的接口:HandlerInterceptor
- 部分默认方法：HandlerMapping
- 有静态方法的接口:ScanUtils
## 注解

- 修饰在方法参数上的PathVariable
- 修饰在类和方法上的RequestMapping
- 只修饰在类上的Intercepts
- 只修饰在方法上的ExceptionHandler
- 修饰在方法参数上的RequestParam

## 抽象类
- ViewResult
- AbstractCommonTypeMethodArgumentResolver ：实现了部分功能，主要是子类去实现
- 各种工具类，比如CollectionUtils
- HttpHeaders，主要用来定义常量，也可以用接口来定义常量

## 枚举的使用
- HttpMethod：典型枚举写法
- CommonResultCode ：这里枚举实现了接口
- Delimiters：这里在枚举里写了方法
- com.nf.mvc.argument.ServletApiMethodArgumentResolver.ServletApiEnum

## 正则表达式
- AntPathMatcher
- Delimiters

## 静态方法导入的运用
- HandlerHelper

## 异常处理

### 方法签名抛检查异常

因为参数解析是可能出现异常的，出了异常就直接往调用链抛出即可，而解析形式多样，比如反序列化流，简单的读取请求参数等，流的相关异常通常是检查异常，所以这个方法抛出检查异常，又由于mvc框架的异常解析器处理的顶级异常是Exception，所以签名设计为抛出Exception

```java
public interface MethodArgumentResolver {
    Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception;
}
```
### catch里写有业务意义的代码

见DispatcherServlet类的doDispatch方法

### catch后又抛出

- 见DispatcherServlet类的rejectRequest
- WebTypeConverterUtils的toSimpleTypeValue方法

### catch里什么都没写

- FileCopyUtils的close方法

### 主动抛异常是有业务意义的

- 见DispatcherServlet类的resolveException
- 见DispatcherServlet类的getHandlerAdapter

### finally代码块

- 见DispatcherServlet的doService方法，用来清理ThreadLocal
- FileCopyUtils的copy相关方法，这些方法没有catch

## null

- 异常解析器返回null表示自己不能处理异常，要交给下一个异常解析器去处理，见LogHandlerExceptionResolver

## 多层继承

- ParameterizedExceptionHandlersExceptionResolver：每一个类都是具体类，都是能实际使用的功能类

## 上下文类

- MvcContext
- HandlerContext

## ThreadLocal

HandlerContext

## 拦截器链的实现

HandlerExecutionChain

## 自定义比较器(Comparator)实现

- AntPatternComparator
- OrderComparator

## 缓存

三个地方使用了缓存

- WebTypeConverterUtils
- RequestMappingHandlerMapping
- MethodArgumentResolverComposite

## builder写法

- AntPathMatcher类

## 组合设计模式

MethodArgumentResolverComposite

## 模板方法

- DispatcherServlet类
- ExceptionHandlerExceptionResolver，特别留意resolveExceptionHandlerMethods方法

## 适配器

- HandlerAdapter
- ViewResult#adaptHandlerResult

## 责任链

- ExceptionResolver链 :通过返回null找下一个
- MethodArgumentResolver链：通过supports返回false找下一个

## 单例模式

- MvcContext
- HandlerContext:threadlocal下的单例

## 流

- MethodArgumentResolverComposite类的insertResolvers方法
- com.nf.mvc.argument.ServletApiMethodArgumentResolver#supports

## 链式方法实现

MethodArgumentResolverComposite类的添加解析器的相关方法

## 任意值但用户不会提供的值

- ValueConstants的DEFAULT_NONE

## if else优化

- AbstractCommonTypeMethodArgumentResolver的resolveArgument（三目运算符)
- ServletApiMethodArgumentResolver

## Ioc

- @Inject

## yml解析与配置属性类

- ConfigurationProperties
- YmlParser

## 嵌入式tomcat

- MvcApplication

## 可执行的jar包

在maven的pom文件中配置下面的内容

```xml
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
        <archive>
            <manifest>
                <mainClass>mvcdemo.MvcLibDemoApplication</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```



## 泛型

- ReflectionUtils.getActualArgument
- com.nf.mvc.argument.MethodParameter#isParameterizedType

## equals与hashcode重写

- MethodParameter:这里对equals的重写有注释说明
- AntPathMatcher

## 递归

- BeanPropertyMethodArgumentResolver

## 线程安全

- HandlerContext的ThreadLocal
- BeanPropertyMethodArgumentResolver的getResolvers方法的synchronized语句块

## 自定义ascii的log

- MvcApplication#printBanner

## shutdown钩子

- MvcApplication#registerShutdownHook

## 命令行参数解析

- MvcApplication#parseArgs

## 函数接口

- DispatcherServlet#executeMvcComponentsConfig
- ExceptionHandlersExceptionResolver#scanExceptionHandlerMethods

## 可变长度的参数

- MethodArgumentResolverComposite#addResolvers

## 断言（防御性代码）

- Assert

## 反射的应用

- ReflectionUtils

## 跨域处理

- CorsUtils
- com.nf.mvc.DispatcherServlet#service
- com.nf.mvc.DispatcherServlet#processCors

## 默认servlet

- com.nf.mvc.DispatcherServlet#noHandlerFound
- com.nf.mvc.MvcApplication#start
