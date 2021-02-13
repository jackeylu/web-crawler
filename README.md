# Web Crawler

An exercise on web crawling with Java language, which load request urls from a url list file, and then
crawling the content and detecting the search term exists or not.

## Requirements and Implementation

- Search is case insenstive
- Should be concurrent AND it should not have more than 20 HTTP requests at any given time
- The results should be written out to a file result.txt
- Avoid using any thread pooling libraries. For example, in Java, don't use Executor, ThreadPoolExecutor, Celluloid, or Parallel streams.

Implementation in the [source code directories](src/main/java/com/example/crawler/):

```text
├─config        The configuration class for the crawling job, including the default settings on max concurrent requests, file input and output.
├─download      A http downloader based on OKHttp client, the max concurrent running requests is controlled by the dispaterch of http client instance, it shares a blocking queue with Crawler.java for the crawled result .
├─entity        Entities used for this project
├─io            The code for loading and parsing urls from a text file.
├─pipeline      A pipeline to combine the filtering and storing job.
│  └─handler   TermSearchHandler.java can search the required term with case insenstive, ResultStoreHandler.java will write out results to the result file.
└─worker        The job dispatcher Crawler.java does the working flow control.
Main.java       The main entry of this program, include the arguments parsing and calling the crawling job.
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

For example, you can run with the follow command, to search a term like `link\d.*` in the url list.
```shell
$java -jar target/crawler-1.0-SNAPSHOT-jar-with-dependencies.jar link\d.*
01:18:54.749 [main] INFO  c.e.c.download.HttpGetDownloader - downloading 1  http://facebook.com/
01:18:54.763 [main] INFO  c.e.c.download.HttpGetDownloader - downloading 2  http://twitter.com/
01:18:54.764 [main] INFO  c.e.c.download.HttpGetDownloader - downloading 3  http://google.com/
01:18:54.765 [main] INFO  c.e.c.download.HttpGetDownloader - downloading 4  http://youtube.com/
01:18:54.767 [main] INFO  c.e.c.download.HttpGetDownloader - downloading 5  http://wordpress.org/

...

01:20:51.916 [OkHttp http://bloglovin.com/...] WARN  c.e.c.download.HttpGetDownloader - call job 497 for http://bloglovin.com/ with failure connect timed out
01:20:54.064 [OkHttp http://globo.com/...] WARN  c.e.c.download.HttpGetDownloader - on retrieving body content, the job 387, url http://globo.com/, failed message: timeout
01:21:05.107 [OkHttp http://illinois.edu/...] WARN  c.e.c.download.HttpGetDownloader - call job 496 for http://illinois.edu/ with failure Read timed out
01:21:05.107 [main] INFO  com.example.crawler.worker.Crawler - Jobs are done.
01:21:05.107 [main] INFO  com.example.crawler.Main - Jobs done with 131197 ms.
```
When the program quited,  you can find the result in `result.txt`
```shell
$  cat -n result.txt
     1  "Rank","URL","Term Exists"
     2  10,"http://wordpress.com/",0
     3  9,"http://linkedin.com/",0
     4  22,"http://qq.com/",0
     5  16,"http://w3.org/",0
     
     ... omitted for better readablity
     
    239  245,"http://about.me/",0
    240  257,"http://ifeng.com/",0
    241  252,"http://cbc.ca/",0
    242  255,"http://shinystat.com/",1
    243  261,"http://topsy.com/",0
    244  254,"http://jugem.jp/",0
    ...
    496  409,"http://zimbio.com/",0
    497  460,"http://netlog.com/",0
    498  493,"http://unc.edu/",0
    499  466,"http://nymag.com/",0
    500  484,"http://shutterfly.com/",0
    501  496,"http://illinois.edu/",0
```
