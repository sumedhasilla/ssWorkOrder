package com.ss.workOrder.service;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.TreeSet;

import javax.ws.rs.BadRequestException;

import org.junit.Before;
import org.junit.Test;
import com.ss.workOrder.SsWorkOrderApplicationTests;
import com.ss.workOrder.entities.WorkOrder;
import com.ss.workOrder.exception.IdAlreadyExistsException;
import com.ss.workOrder.exception.NoContentException;
import com.ss.workOrder.exception.NotFoundException;
import com.ss.workOrder.util.CommonUtil;

public class WorkOrderServiceImplTest extends SsWorkOrderApplicationTests{

	private WorkOrderServiceImpl workOrderServiceImpl;
	long currentTime;
	WorkOrder workOrder;
	
	@Before
	public void setUp() throws Exception {
		workOrderServiceImpl = new WorkOrderServiceImpl();
		currentTime = System.currentTimeMillis();
	}
	
	public void removeAllWorkOrders() {
		while(null != workOrderServiceImpl.dequeueWorkOrder()) {
			 
		 }
		
		// Making sure that the Map/queue are empty.
		assertEquals(0, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(0, workOrderServiceImpl.getWorkOrderPriorityQueueSize());		
	}
	
	@Test
	public void testEnqueueWorkOrder() {
		
		assertEquals(0, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(0, workOrderServiceImpl.getWorkOrderPriorityQueueSize());
		
		// Test1 - Add multiple WorkOrders containing where some of them have equal Ranks
		
		for (int i = 1; i <= 10; ++i) {
			workOrder = workOrderServiceImpl.enqueueWorkOrder(i, currentTime - (i * 1000));
			assertNotNull("Error with WorkOrder.", workOrder);	
		}
		// End Test1
		// Test2 - Add multiple WorkOrders containing same time and future Time Value
		workOrderServiceImpl.enqueueWorkOrder(1000, currentTime);
		workOrderServiceImpl.enqueueWorkOrder(1003, currentTime);
		workOrderServiceImpl.enqueueWorkOrder(1005, currentTime + 1000);
		
		// To test the Total number of elements and making sure TreeSet/map are in Sync
		assertEquals(13, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(workOrderServiceImpl.getWorkOrderPriorityQueueSize(), workOrderServiceImpl.getWorkOrderMapSize());
		
		//  Test to check enqueue process for duplicate WorkOrder ID's.
		try {
			workOrderServiceImpl.enqueueWorkOrder(1005, currentTime + 1000);
		} catch (IdAlreadyExistsException e) {
			assertTrue(e.getMessage().contains("The work Order ID (1005) already Exists!"));
		}
		
		// Test to check if WorkOrder ID = 0. 
		try {
			workOrderServiceImpl.enqueueWorkOrder(0, currentTime);
		} catch (BadRequestException e) {
			assertTrue(e.getMessage().contains("Work Order ID cannot have value less than or equal to 0"));
		}
		
		// Test to check if WorkOrder ID is a negative value. 
		try {
			workOrderServiceImpl.enqueueWorkOrder(-30, currentTime);
		} catch (BadRequestException e) {
			assertTrue(e.getMessage().contains("Work Order ID cannot have value less than or equal to 0"));
		}
		
		// To test the Total number of elements and making sure TreeSet/map are in Sync
		assertEquals(13, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(workOrderServiceImpl.getWorkOrderPriorityQueueSize(), workOrderServiceImpl.getWorkOrderMapSize());
		
		removeAllWorkOrders();
	}
	
	@Test
	public void testDequeueWorkOrder() {
		//Making sure the priority queue is empty before the test.
		removeAllWorkOrders();

		//Start Test 1. Add single Work Order and then dequeue (Display the Work Order and remove it from the Queue.)
		workOrderServiceImpl.enqueueWorkOrder(4, 39);
		workOrder = null;
		workOrder = workOrderServiceImpl.dequeueWorkOrder(); // dequeue.
		
		assertNotNull("Error with WorkOrder.", workOrder);		
		assertEquals(4, workOrder.getWorkOrderID());
		assertEquals(39, workOrder.getTimeStampMs());		
		assertEquals(0, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(0, workOrderServiceImpl.getWorkOrderPriorityQueueSize());		
		// End Test 1
		
		//Test2 - Test Dequeue operation when the queue is empty.
		assertNull(workOrderServiceImpl.dequeueWorkOrder());
		//End Test 2
		
		// Start Test 3. Add multiple Work Orders and then test the dequeue results.
		workOrderServiceImpl.enqueueWorkOrder(15, 69);
		workOrderServiceImpl.enqueueWorkOrder(30, 37);
		workOrderServiceImpl.enqueueWorkOrder(11, 33);
		workOrderServiceImpl.enqueueWorkOrder(7, 33);
		workOrderServiceImpl.enqueueWorkOrder(1, 33);
		
		workOrder = workOrderServiceImpl.dequeueWorkOrder();
		assertEquals(30, workOrder.getWorkOrderID());
		
		workOrder = workOrderServiceImpl.dequeueWorkOrder();
		assertEquals(15, workOrder.getWorkOrderID());
		
		// To test the Total number of elements and making sure TreeSet/map are in Sync
		assertEquals(3, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(workOrderServiceImpl.getWorkOrderPriorityQueueSize(), workOrderServiceImpl.getWorkOrderMapSize());
		// End Test3
		removeAllWorkOrders();			
	}
	
	@Test
	public void testGetSortedWorkOrder() {	
		removeAllWorkOrders();	// making sure we start the test with empty queue.
		workOrderServiceImpl.enqueueWorkOrder(7, currentTime);
		workOrderServiceImpl.enqueueWorkOrder(30, currentTime - 1000);
		workOrderServiceImpl.enqueueWorkOrder(11, currentTime - 2828);
		workOrderServiceImpl.enqueueWorkOrder(15, currentTime - 2822);
		workOrderServiceImpl.enqueueWorkOrder(1, currentTime - 2000);
		workOrderServiceImpl.enqueueWorkOrder(3, currentTime - 1000);
		workOrderServiceImpl.enqueueWorkOrder(10, currentTime - 4000);
		workOrderServiceImpl.enqueueWorkOrder(13, currentTime - 3002);
		
		long [] expectedOrderArray = {15, 30, 10, 3, 13,11,1,7};		
		
		// Test the order of the workOrders based on its rank (highest to lowest)
		TreeSet<WorkOrder> workOrders = workOrderServiceImpl.getSortedWorkOrder();
		//System.out.println("1. Printing All workOrders along with its Rank... ");
		
		Iterator<WorkOrder> itr = workOrders.iterator();
		int i = 0;
		while(itr.hasNext()) {
			 workOrder = itr.next();
			 assertEquals(expectedOrderArray[i], workOrder.getWorkOrderID());
			// System.out.println("WorkOrder having ID ("+workOrder.getWorkOrderID()+", "+workOrder.getTimeStampMs() +" )Rank = "+ getWorkOrderRank(workOrder) );
			++i;
			 }	
		 
		 //Test2
		removeAllWorkOrders();
		workOrders = null;
		workOrderServiceImpl.enqueueWorkOrder(7, currentTime - 2000);
		workOrderServiceImpl.enqueueWorkOrder(30, currentTime - 1000);
		workOrderServiceImpl.enqueueWorkOrder(11, currentTime - 2828);
		workOrderServiceImpl.enqueueWorkOrder(15, currentTime - 2822);
		workOrderServiceImpl.enqueueWorkOrder(1, currentTime - 3000);
		workOrderServiceImpl.enqueueWorkOrder(3, currentTime - 1000);
		workOrderServiceImpl.enqueueWorkOrder(10, currentTime );
		workOrderServiceImpl.enqueueWorkOrder(13, currentTime - 20002);
		
		long[] expectedOrderArray2 = {15, 30, 13, 3, 1,11,7,10};		
		
		// Test the order of the workOrders based on its rank (highest to lowest)
		workOrders = workOrderServiceImpl.getSortedWorkOrder();
		//System.out.println("2. Printing All workOrders along with its Rank... ");
		
		itr = workOrders.iterator();
		i = 0;
		while(itr.hasNext()) {
			 workOrder = itr.next();
			 assertEquals(expectedOrderArray2[i], workOrder.getWorkOrderID());
			// System.out.println("WorkOrder having ID ("+workOrder.getWorkOrderID()+" , "+workOrder.getTimeStampMs() +")Rank = "+ getWorkOrderRank(workOrder) );
			 ++i;
		 }
	}

	@Test
	public void testDeleteWorkOrderById(){
		removeAllWorkOrders();	// making sure we start the test with empty queue.
		workOrderServiceImpl.enqueueWorkOrder(7, currentTime);
		workOrderServiceImpl.enqueueWorkOrder(30, currentTime - 1000);
		workOrderServiceImpl.enqueueWorkOrder(11, currentTime - 2828);
		workOrderServiceImpl.enqueueWorkOrder(15, currentTime - 2822);
		workOrderServiceImpl.enqueueWorkOrder(1, currentTime - 2000);
		
		//Test 1 - Delete an existing/Valid work Order ID
		assertEquals("WorkOrder (11) Deleted", workOrderServiceImpl.deleteWorkOrderById(11));
		//End Test 1
		
		//Test 2 - Delete a non-existing/InValid Work Order ID
		assertEquals("Work Order (11) Not Found!", workOrderServiceImpl.deleteWorkOrderById(11));
		//End Test 2
		removeAllWorkOrders();
	}
	
	@Test
	public void testGetWorkOrderPosition(){
		removeAllWorkOrders();	// making sure we start the test with empty queue.
		workOrderServiceImpl.enqueueWorkOrder(60, currentTime - 2000);
		workOrderServiceImpl.enqueueWorkOrder(30, currentTime - 1000);
		workOrderServiceImpl.enqueueWorkOrder(15, currentTime - 822);
		workOrderServiceImpl.enqueueWorkOrder(45, currentTime - 1822);
		
		//Test 1 - Find an existing/Valid work Order ID
		//System.out.println(" found - "+ workOrderServiceImpl.getWorkOrderPosition(11));
		assertEquals(0, workOrderServiceImpl.getWorkOrderPosition(60));
		assertEquals(1, workOrderServiceImpl.getWorkOrderPosition(45));
		assertEquals(2, workOrderServiceImpl.getWorkOrderPosition(30));
		assertEquals(3, workOrderServiceImpl.getWorkOrderPosition(15));
		//End Test 1
		
		//Test 2 - Find a non-existing/InValid Work Order ID
		try {
			 workOrderServiceImpl.getWorkOrderPosition(10);
		} catch (NotFoundException e) {
			assertTrue(e.getMessage().contains("The work Order ID (10) not Found!"));
		}
		//End Test 2
		removeAllWorkOrders();
	}
	
	@Test 
	public void testGetAverageWaitTime(){
		removeAllWorkOrders();	// making sure we start the test with empty queue.
		workOrderServiceImpl.enqueueWorkOrder(7, currentTime);
		workOrderServiceImpl.enqueueWorkOrder(30, currentTime-1000);
		//Test 1 - Find average wait time for queue containing work Orders.
		//System.out.println(" Avg Time - "+ workOrderServiceImpl.getAverageWaitTime(currentTime+2000));
		assertEquals(2500, workOrderServiceImpl.getAverageWaitTime(currentTime+2000));
		//End Test 1
		removeAllWorkOrders();
		
		//Test 2. Find Average Wait time in an empty queue.
		try{
			workOrderServiceImpl.getAverageWaitTime(currentTime);
		}catch(NoContentException e){
			assertTrue(e.getMessage().contains("No Work Orders Present in the Queue!"));
		}
		//End Test2
		
	}
	
	private double getWorkOrderRank(WorkOrder workOrder1){		
		return CommonUtil.calculateRank(workOrder1);
	}
}
