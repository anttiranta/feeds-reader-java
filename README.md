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
- Cover the implementation with unit tests

## Running the project

In Netbeans (or other IDE) simply click "Run project". Now the website should be visible in the browser at http://http://127.0.0.1:8080/

## Using the console command

To import two sample feeds, simply run: shell:>import-item https://www.feedforall.com/sample.xml,https://www.feedforall.com/sample-feed.xml

## To note

UI (especially edit form) is not the greatest, but since this project's point is to demonstrate my ability to build Java Spring applications, I'm not going to put any extra effort on fine tuning the UI. 

![Screenshot](https://raw.githubusercontent.com/anttiranta/anttiranta.github.io/master/images/feeds-reader-java/java-feeds-reader.jpg)