# snapshot service
Simple server application to create thumbnail from any web page.

## Input parameters
GET http://localhost:8080/thumb?twidth={RESIZED_WIDTH}&theight={RESIZED_HEIGHT}&wwidth={WINDOW_WIDTH}&wheight={WINDOW_HEIGHT}&url=${urlToTestPage}

## Where:
RESIZED_WIDTH - thumbnail width in pixels

RESIZED_HEIGHT - thumbnail height in pixels

WINDOW_WIDTH - widow width in pixels

WINDOW_HEIGHT - widow height in pixels

urlToTestPage - url of page

## Result
The result png encoded in base64

##  Example
http://localhost:8080/thumb?twidth=200&theight=100&wwidth=768&wheight=1024&url=http://google.com




