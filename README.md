# MyBatis Generator Lombok plugin

A plugin for MyBatis Generator to use Lombok's @Data annotation
instead of getters and setters. Helps to reduce the amount of boilerplate
generated code.

Code __before__ applying the Lombok plugin:

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

Code __after__ applying the Lombok plugin:

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

## Using the plugin

First things first, clone this repository locally and run:

    mvn clean install

Then, in your MyBatis Generator configuration, include the plugin:

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE generatorConfiguration
            PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
            "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

    <generatorConfiguration>
        <classPathEntry location="/Users/predo/.m2/repository/com/h2database/h2/1.4.184/h2-1.4.184.jar" />

        <context id="example"
                 targetRuntime="MyBatis3Simple"
                 defaultModelType="flat">
            <!-- include the plugin -->
            <plugin type="com.softwareloop.mybatis.generator.plugins.LombokPlugin"/>

            <!-- other configurations -->

        </context>
    </generatorConfiguration>

If you run MyBatis Generator from Maven, you can add the plugin as a dependency
for mybatis-generator-maven-plugin:

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
                <version>1.0-SNAPSHOT</version>
            </dependency>
        </dependencies>
    </plugin>


