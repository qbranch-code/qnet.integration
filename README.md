qnet.integration
================

Contains public components such as schemas for use in integrations with Qbranch. The schemas and samples currently available focus on the push-type integration of tickets via Azure Servicebus. 

You currently have to roll your own classes to serialize/deserialize while interacting with our REST API.

### C# samples
The C# samples target .NET 4.5x and Visual Studio 2013. Connection string parameters can be configured in `app.config`

### Java samples

The Java samples requires that you have Java 8 and Maven 3 installed and "properly" setup (in your path etc), this will allow you to compile and package the projects using the command `mvn clean package`.

#### TicketSubscriber

The ticket subscriber sample requires the following configuration parameters to be present...

* ...`namespace`: blah
* ...`user`: bleh
* ...`password`: bloh
* ...`topic`: foo
* ...`subscription`: bar

...they can either be set as os environment variables or as properties passed to the JVM on startup using the `-D<property>=<value>` command line syntax. A fairly sane way of running is by putting the above variables in a `.env` file in the root of the project (in the format `<var>=<value>`), install [foreman](https://github.com/ddollar/foreman#foreman) and then running `mvn clean package && foreman start`.

### Thanks
* [chids](https://github.com/chids) for help with the Java samples
