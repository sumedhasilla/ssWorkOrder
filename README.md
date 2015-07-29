# ssWorkOrder
SpringBoot WorkOrder Project
This is a sample Java/Maven/SpringBootREST application to add, view and delete work orders.

# Requirements to Run the Project:
Make sure you are using JDK 1.8 and Maven 3.x

This application is packaged as a JAR which has Tomcat 8 embedded. Other application server such as Tomcat or JBoss installation is not necessary to run this application. 

# Steps to Set up and Run
1. Clone this repository : git clone https://github.com/sumedhasilla/ssWorkOrder.git

# Command Line:
2. To build the project and run the tests, go to the project directory in command line. Type: mvn clean install. 
3. To run the service: java -jar target/ssWorkOrder-1.0.jar

You should be able to see similar format after running the above command:
2015-07-28 10:44:26.689  INFO 7780 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8090 (http)
2015-07-28 10:44:26.692  INFO 7780 --- [           main] com.ss.workOrder.SsWorkOrderApplication  : Started SsWorkOrderApplication in 7.835 seconds (JVM running for 8.843)

# STS:

2. To Import Project in STS:
 2.1. After you clone the project in Step 1,  Open STS > In the 'Project Explorer' pane > Right Click > Click 'Import' > Select Option 'Maven / Existing Maven Projects' Click Next.
 2.2. In this Window, click 'Browse' button next to 'Root Directory' Input and browse and select the 'ssWorkOrder' folder. Keep rest of the default options and Click Next for rest of the windows. Click Finish in the end.
2. To Build and Run Tests: 
	2.1 Right Click Project ssWorkOrder > Click 'Run As' >  Select 'Run Configurations..' > In the Run Window, Select 'Maven Build' and click the 'new' button.
	2.2 In the new Configuration Window, Enter the following values:
	 Name: ssWorkOrder_mvn
	 Base Directory: Click 'Browse Workspace' button to select this project. 
	 Goals: clean install
	 JRE TAB: make sure the Runtime is pointing to the JDK 8 Location.
	 
	 Click 'Apply' to save the settings and then 'Run' to build project. The Unit Tests will also execute. 
	
3. To Run the application: After the project is build successfully, you may right click the project > click  'Run As' > Spring Boot App. 
	If you may see similar log lines:
	2015-07-28 23:18:12.202  INFO 9972 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8090 (http)
2015-07-28 23:18:12.205  INFO 9972 --- [           main] com.ss.workOrder.SsWorkOrderApplication  : Started SsWorkOrderApplication in 5.128 seconds (JVM running for 6.129)

The project is running successfully in localhost port 8090.


# Assumptions for the Project:
1. Work Order ID and Time value are based on 'long' data Type Format. All time based calculations are based on Unix TimeStamp in MilliSeconds.
2. The 4 classes of Work Order (normal, priority, VIP, and management override) are calculated based on input WorkOrderID and then assigned as a property to the WorkOrder Object with values mapped as :
NORMAL_ID = 0;
PRIORITY_ID = 1;
VIP_ID = 2;
MANAGEMENT_OVERRIDE_ID = 3;

# REST API Endpoints to Test the Application workflow:

1. To Add a new Work Order to the list:
http://localhost:8090/enqueue/?id=60&time=678889

Expected Result for valid input: JSON response containing workOrderID, timeStampMs and its corresponding class
 
2. To remove and view the top ID from the queue:
http://localhost:8090/dequeue

Expected Result for valid input: JSON response containing workOrderID, timeStampMs and its corresponding class

3. To view the queue in sorted order (highest rank to Lowest)
http://localhost:8090/getWorkOrderList

4. to remove a specific ID from the queue.
http://localhost:8090/deleteWorkOrderById?id=6

5. To find the position of specific ID from the queue:
http://localhost:8090/getWorkOrderPosition?id=60

6. to get the average wait time.
http://localhost:8090/getAverageWaitTime?currentTime=4433232
