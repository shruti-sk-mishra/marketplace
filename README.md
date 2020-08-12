## Self Employment Marketplace  
  
A platform connecting the self-employed people (buyers) with project sellers.  
 - The platform will host several projects of 3 types (software, real-estate & aviation)  
 - Any seller can register on the platform and host his/her projects up for bidding  
 - Any buyer can register on the platform and place his/ her bid for the project  
 - The buyer with the lowest bid amount will win the project  
  
For detailed functional requirements, please refer the attached functional requirement document  
  
## Getting Started  
  
The application is built on Java Spring Boot 2.x, MongoDB 3.6  for persistence & Redis for caching  
  
### Prerequisites  
  
 - Java 8+: application is built on Java  
 - Gradle: dependency management  
 - MongoDB 3.2: data persistence  
 - Redis. Here, the Redis is used to schedule the expiration of project. When a project is created, the project ID as key is created with expiry same as expiration date of the project.

### Installation  

Start Mongo server  
```  
brew services start mongodb  
```
Start Redis server
```  
redis-server  
```
Setup Redis. Enable Redis Keyspace notification
```  
config set notify-keyspace-events KEA  
```    
Clone the project.  
```  
https://github.com/shruti-sk-mishra/marketplace.git  
```
Go into the project folder, install project dependencies  
```  
./gradlew clean build  
```  
 Run the application
```  
./gradlew bootRun  
```    
The above command will start the server on 3501 and host the application. In case required, change the server port or other application properties in application.properties