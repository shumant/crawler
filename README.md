# crawler
HTTP crawler

Spring Boot app for command line (via implementing CommandLineRunner.class);

Uses ExecutorCompletionService based on ThreadPoolExecutor for task management;
Apache Commons for HTTP requests;
Jsoup for parsing HTMLs;
Google Guava for minor utils and RateLimiter for throttling HTTP requests;

# usage
1. clone repo to local machine
2. mvn clean install 
3. java -jar path-to-jar site-url required-scanning-depth

# settings
Number of threads, HTTP requests per second and simultaneous HTTP request to host might be tuned in application.properties

# limitations
* Url links usage is not perfect: for instance, cyrillic symbols are forbidden
* Should make more tests
* Should experiment with number of threads and HTTP request throttling in order to archive better performance
* Word tokenizing is handled by simple regexp '[^a-zA-Zа-яА-Я]+' - great field for improvements