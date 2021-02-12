# Web Crawler

An exercise on web crawling with Java language, which load request urls from a url list file, and then
crawling the content and detecting the search term exists or not.

## Direction

```text
├─config        The configuration class for the crawling job.
├─download      A http downloader based on OKHttp client.
├─entity        Entities used for this project
├─io            The code for loading and parsing urls from a text file.
├─pipeline      A pipeline to combine the filtering and storing job.
│  └─handler
└─worker        The job dispatcher.
Main.java       The main entry of this program.
```

# How to run
To build an executable jar with dependencies, use the follow instruction
```shell
$ mvn clean assembly:assembly
```

Use the `--help` parameter or without any argument, to list the help message.
```shell
$ java -jar target/crawler-1.0-SNAPSHOT-jar-with-dependencies.jar
web crawler.
<term> [maxRequest] [input] [output]
term		the search term.
maxRequest	the max number of concurrent requests, default is 20.
input		the urls to be search, default is web_crawler_url_list.txt.
output		the output filename, default is result.txt.
```

Run with the follow command,
```shell
$java -jar target/crawler-1.0-SNAPSHOT-jar-with-dependencies.jar link
```