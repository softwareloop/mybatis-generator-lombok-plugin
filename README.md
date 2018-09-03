# MyBatis Generator Lombok plugin

A plugin for [MyBatis Generator](http://mybatis.github.io/generator/)
to use [Lombok](http://projectlombok.org/) annotations
instead of getters and setters. Helps to reduce the amount of
generated boilerplate code.

Code __before__ applying the Lombok plugin:

```java
package example.dto;

public class Contact {
    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

Code __after__ applying the Lombok plugin (much shorter):

```java
package example.dto;

import lombok.Data;

@Data
public class Contact {
    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;
}
```

## Using the plugin

In your Maven pom.xml file, set up the MyBatis Generator plugin, and add
mybatis-generator-lombok-plugin as a dependency:

```xml
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>${mybatis.generator.version}</version>
    <configuration>
        <overwrite>true</overwrite>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.softwareloop</groupId>
            <artifactId>mybatis-generator-lombok-plugin</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
</plugin>
```

Then, in your MyBatis Generator configuration, include the plugin:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="example"
             targetRuntime="MyBatis3Simple"
             defaultModelType="flat">
        <!-- include the plugin -->
        <plugin type="com.softwareloop.mybatis.generator.plugins.LombokPlugin">
             
             <!-- enable annotations -->
             <property name="builder" value="true"/>
             <!-- annotation's option(boolean) -->
             <property name="builder.fluent" value="true"/>
             <!-- annotation's option(String) -->
             <property name="builder.builderMethodName" value="myBuilder"/>
             
             <property name="accessors" value="true"/>
             <!-- annotation's option(array of String) -->
             <property name="accessors.prefix" value="m_, _"/>
             
             <!-- disable annotations -->
             <property name="allArgsConstructor" value="false"/>
             
             <!-- disable @Mapper annotations -->
             <property name="mapper" value="false"/>
        </plugin>

        <!-- other configurations -->

    </context>
</generatorConfiguration>
```

## Authors

Maintainer:

* [softwareloop](https://github.com/softwareloop)

Contributors:

* [izebit](https://github.com/izebit)
* [kimmking](https://github.com/kimmking)
* [tomoki1207](https://github.com/tomoki1207)
