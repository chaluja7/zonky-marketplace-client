# Zonky - marketplace client

### Simple app to process new loans on the marketplace

  - After the startup, the application will process all marketplace loans sequentially (print to standard output).
  - Then every 5 minutes a synchronization job is fired. It will process all new marketplace loans (that were not processed before). 

### Build and run

#### Preconditions
  - ``maven``
  - ``JDK 1.8``
  
#### Build  
  - ``mvn clean install``
  
#### Run  
  - ``mvn spring-boot:run``