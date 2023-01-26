# KJar deployment on  Spring Boot's Kie Server
A sample Spring Boot project to host a custom BPMN workflow which is being created by using JBPM or Red Hat Process Automation Manager (RHPAM). 

## Configuration
We need to include KJar which is being created by JBpm on our `pom.xml`
```xml
        <!-- kjar here -->
        <dependency>
            <groupId>com.edw</groupId>
            <artifactId>Project01</artifactId>
            <version>1.6.0</version>
        </dependency>
```

And start it from our `kie-server-project01.xml`
```xml
  <containers>
    <container>
      <containerId>project01</containerId>
      <releaseId>
        <groupId>com.edw</groupId>
        <artifactId>Project01</artifactId>
        <version>1.6.0</version>
      </releaseId>
      <status>STARTED</status>
      <configItems/>
      <messages/>
    </container>
  </containers>
```

## BPMN
Im taking sample KJar from below repository
```
https://github.com/edwin/rhpam-hello-world-example
```

Do a `git clone` and `mvn clean install` to generate a jar file which we can import on our Spring Boot.

## How to Test
Check Process deployment status
```
$ curl -kv http://kieserver:password@localhost:8080/rest/server/containers
```

Do a transaction
```
$ curl -kv http://kieserver:password@localhost:8080/rest/server/containers/Project01/processes/Project01.Business01/instances -H 'Content-Type: application/json' --data-raw '{
    "application": {
        "com.edw.project01.User": {
            "age": 37,
            "name":"edwin"
        }
    }
}'
```

It will generate an integer as ID, we can later on use the same ID for transaction. On this example, the generated ID is `152`.
```
$ curl -kv http://kieserver:password@localhost:8080/rest/server/containers/project01/processes/instances/152/variables/instances/
```

the result would be like this, we can check `variable-instance` with the name `status` and can see its value as `true`
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<variable-instance-list>
    <variable-instance>
        <name>application</name>
        <old-value></old-value>
        <value>com.edw.project01.User@7871dcea</value>
        <process-instance-id>152</process-instance-id>
        <modification-date>2023-01-26T13:37:41.479+07:00</modification-date>
    </variable-instance>
    <variable-instance>
        <name>initiator</name>
        <old-value></old-value>
        <value>kieserver</value>
        <process-instance-id>152</process-instance-id>
        <modification-date>2023-01-26T13:37:41.542+07:00</modification-date>
    </variable-instance>
    <variable-instance>
        <name>user</name>
        <old-value>com.edw.project01.User@7871dcea</old-value>
        <value>com.edw.project01.User@7871dcea</value>
        <process-instance-id>152</process-instance-id>
        <modification-date>2023-01-26T13:37:41.629+07:00</modification-date>
    </variable-instance>
    <variable-instance>
        <name>status</name>
        <old-value></old-value>
        <value>true</value>
        <process-instance-id>152</process-instance-id>
        <modification-date>2023-01-26T13:37:41.653+07:00</modification-date>
    </variable-instance>
</variable-instance-list>
```

## Blog Post
for more detail regarding this workflow, we can check my previous blogpost
```
https://edwin.baculsoft.com/2020/05/how-to-create-and-test-workflow-on-red-hat-process-automation-manager-with-rest-api/
```