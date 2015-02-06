qnet.integration
================

Contains public components such as schemas for use in integrations between third parties and Qnet.


### Java samples

The Java samples requires that you have Java 8 and Maven 3 installed and "properly" setup (in your path etc), this will allow you to compile and package the projects using the command `mvn clean package`.

#### TicketSubscriber

The ticket subscriber sample requires the following configuration paramters to be present...

* ...`namespace`: blah
* ...`user`: bleh
* ...`password`: bloh
* ...`topic`: foo
* ...`subscription`: bar

...they can either be set as os environment variables or as properties passed to the JVM on startup using the `-D<property>=<value>` command line syntax.

### Thanks
* [chids](https://github.com/chids) for help with the Java samples
