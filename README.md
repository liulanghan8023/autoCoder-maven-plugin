# autoCoder-maven-plugin
在使用Mybatis-Generator代码生成器时发现很复杂，功能也不多；所以就开发了自己的代码生成器，maven插件形式，
代码可直接生成到项目代码里，无需额外部署，简单快速；已使用三年，

## 功能
1.快速代码生成(支持自定义目标,分组等)  
2.快速代码删除  
3.数据库导出到word(其实不止word)  
4.数据库差异对比（比较出字段的类型/长度等差异,保持各个环境一致）   
5.待增加...  

## 安装
1.拉下源码后，使用maven install到仓库  
2.pom.xml引入插件  

---
```
    <build>
        <plugins>
            <plugin>
                <groupId>com.tramp</groupId>
                <artifactId>autoCoder-maven-plugin</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <configPath>${basedir}/mbg/generatorConfig.xml</configPath>
                </configuration>
            </plugin>
        </plugins>
    </build>

```
<configPath>${basedir}/mbg/generatorConfig.xml</configPath>中的路径就是你实际配置文件的路径，
后续主要就是对配置文件进行相关配置
---
### 进行配置
见使用说明

## 使用说明
https://my.oschina.net/u/2526698/blog/1556347
有任何问题可咨询我:i123ming