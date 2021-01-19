# autoCoder-maven-plugin
代码生成器，maven插件形式，代码可直接生成到项目代码里，无需额外部署，简单快速；已使用三年了

##安装
###1.拉下源码后，使用maven install到仓库

###2.pom.xml引入插件

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
---
###进行配置
见使用说明

##使用说明
https://my.oschina.net/u/2526698/blog/1556347