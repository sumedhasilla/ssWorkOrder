# ssWorkOrder
SpringBoot WorkOrder Project
This is a sample Java/Maven/SpringBootREST application to add, view and delete work orders.

## Requirements to Run the Project:
* JDK 1.8 
* Maven 3.x

This application is packaged as a JAR which has embedded Tomcat 8. Other application server such as Tomcat or JBoss installation is not necessary to run this application. 

## Steps to Set up and Run
1. Clone this repository : git clone https://github.com/sumedhasilla/ssWorkOrder.git

### Command Line:
2. To build the project and run the tests, go to the project directory in command line. 
      * **mvn clean install.** 
3. To run the service: 
      * **java -jar target/ssWorkOrder-1.0.jar**

You should be able to see similar format after running the above command:


2015-07-28 10:44:26.689  INFO 7780 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8090 (http)
2015-07-28 10:44:26.692  INFO 7780 --- [           main] com.ss.workOrder.SsWorkOrderApplication  : Started SsWorkOrderApplication in 7.835 seconds (JVM running for 8.843)

### STS:

1. To Import Project in STS:
 * 1.1. After you clone the project in Step 1,  Open STS > In the 'Project Explorer' pane > Right Click > Click 'Import' > Select Option 'Maven / Existing Maven Projects' Click Next.
 * 1.2. In this Window, click 'Browse' button next to 'Root Directory' Input and browse and select the 'ssWorkOrder' folder. Keep rest of the default options and Click Next for rest of the windows. Click Finish in the end.
2. To Build and Run Tests: 
	* 2.1 Right Click Project ssWorkOrder > Click 'Run As' >  Select 'Run Configurations..' > In the Run Window, Select 'Maven Build' and click the 'new' button.
	* 2.2 In the new Configuration Window, Enter the following values:
	 Name: ssWorkOrder_mvn
	 Base Directory: Click 'Browse Workspace' button to select this project. 
	 Goals: clean install
	 JRE TAB: make sure the Runtime is pointing to the JDK 8 Location.
	 
	 Click 'Apply' to save the settings and then 'Run' to build project. The Unit Tests will also execute. 
	
3. To Run the application: After the project is build successfully, you may right click the project > click  'Run As' > Spring Boot App. 

If you may see similar log lines, the application is running successfully in **Port 8090**:

2015-07-28 23:18:12.202  INFO 9972 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8090 (http)
2015-07-28 23:18:12.205  INFO 9972 --- [           main] com.ss.workOrder.SsWorkOrderApplication  : Started SsWorkOrderApplication in 5.128 seconds (JVM running for 6.129)

### Assumptions for the Project:
1. Work Order ID and Time value are based on 'long' data Type Format. All time based calculations are based on Unix TimeStamp in MilliSeconds.
2. The 4 classes of Work Order (normal, priority, VIP, and management override) are calculated based on input WorkOrderID and then assigned as a property to the WorkOrder Object with values mapped as :

 NORMAL_ID = 0;
  PRIORITY_ID = 1;
  VIP_ID = 2;
  MANAGEMENT_OVERRIDE_ID = 3; 

### REST API Endpoints to Test the Application workflow:

#### To Add a new Work Order to the list:
* curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d "{\"workOrderID\" : 60, \"timeStampMs\":555555}" http://localhost:8090/enqueue

* Response:
HTTP/1.1 201 Created
....
{"workOrderID":60,"timeStampMs":555555,"idClass":3}

* Expected Result for valid input: JSON response containing workOrderID, timeStampMs and its corresponding class
* URL Endpoint:  http://localhost:8090/enqueue
* [] TODO: Validate workOrder ID and Time should not be Empty values

#### To remove and view the top ID from the queue:
* http://localhost:8090/dequeue

* Expected Result for valid input: JSON response containing workOrderID, timeStampMs and its corresponding class

#### To view the queue in sorted order (highest rank to Lowest):
* http://localhost:8090/workOrdersSorted

#### To remove a specific ID from the queue:
* curl -i -X DELETE "http://localhost:8090/deleteWorkOrderById?id=60

* Response:
HTTP/1.1 200 OK
.....
WorkOrder (60) Deleted

* [] TODO: Return 404 Response for Id not Found for Delete By ID

#### To find the position of specific ID from the queue:
* http://localhost:8090/workOrderPosition?id=60

#### To get the average wait time:
* http://localhost:8090/averageWaitTime?currentTime=4433232
