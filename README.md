# Java Spring Boot task: feeds reader application

This is a demo application/job apply task which I did to refresh my memory with Java and to learn new Java features that came after version 8. 

Task is to create a Java application that is a feeds reader. The app can read feed from multiple sources and store them to database. Sample feeds http://www.feedforall.com/sample-feeds.htm. 

## Requirements

- As a developer, I want to run a command which help me to setup database easily with one run. 
- As a developer, I want to run a command which accepts the feed urls (separated by comma) as argument to grab items from given urls. Duplicate items are accepted. 
- As a developer, I want to see output of the command not only in shell but also in pre-defined log file. The log file should be defined as a parameter of the application. 
- As a user, I want to see the list of items which were grabbed by running the command line above, via web-based. I also should see the pagination if there are more than one page. The page size is a configurable value. 
- As a user, I want to filter items by category name on list of items. 
- As a user, I want to create new item manually via a form. 
- As a user, I want to update/delete an item
- The implementation should covered by Unit Test or Functional Test

## To note

- There are still some open todos
- More unit tests should be written to cover the implementation
- There is also few classes which were written only to test user authentication and rights management
