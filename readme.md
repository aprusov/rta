## RTA
expose '/transfer' and '/get' http servlets 

## Structure
1) 'application' compiles to a single executable jar
2) 'rta-lib' has all the code for the components
3) 'application-test' simulates integration black-box test. It just launches the jar and calls the API

## Idea
0) the app should
    * support different kinds of request listeners, not just the http servlets. Give options to plug-in others
    * not block the new request even if the old one is not processed 

1) transfer servlet has multiple handlers: 
    * store the request. So we keep everything that user sends, despite we couldn't process all the previous ones
    * do actual business-logic
    
2) async
    * servlets. They just schedule the request an listen to status update
    * transfer business-handler does not block disruptor
    * the DB requests

## Libs used
* Disruptor
* Jetty server
* SQLite 
* Jooq  

