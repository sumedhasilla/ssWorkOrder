package com.ss.workOrder.service;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.ss.workOrder.SsWorkOrderApplicationTests;
import com.ss.workOrder.entities.WorkOrder;

public class WorkOrderServiceImplTest extends SsWorkOrderApplicationTests{

	private WorkOrderServiceImpl workOrderServiceImpl;
	
	WorkOrder workOrder;
	
	@Before
	public void setUp() throws Exception {
		workOrderServiceImpl = new WorkOrderServiceImpl();
	}
	
	public boolean removeAllWorkOrders(){
		boolean  flag = false;
		try{
			TreeSet<WorkOrder> workOrders = workOrderServiceImpl.getSortedWorkOrder();
			if(workOrders.size() == 0){
				flag = true;
				return flag;
			}
			Iterator<WorkOrder> itr = workOrders.iterator();
			 while(itr.hasNext()) {
				 workOrderServiceImpl.dequeueWorkOrder();
			 }
			 flag=true;
		}catch(Exception e){
			 flag = false;
		}
		return flag;
	}
	
	@Test
	public void testEnqueueWorkOrder() {
		long currentTime = System.currentTimeMillis();
		assertEquals(0, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(0, workOrderServiceImpl.getWorkOrderPriorityQueueSize());
		
		// Test1 - Add multiple WorkOrders containing equal Ranks
		
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
				
		try {
			workOrderServiceImpl.enqueueWorkOrder(1005, currentTime + 1000);
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("The work Order ID (1005) already Exists!"));
		}
		
		try {
			workOrderServiceImpl.enqueueWorkOrder(0, currentTime);
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("Work Order ID cannot have value less than or equal to 0"));
		}
		
		try {
			workOrderServiceImpl.enqueueWorkOrder(-30, currentTime);
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("Work Order ID cannot have value less than or equal to 0"));
		}

		try {
			workOrderServiceImpl.enqueueWorkOrder(10006, -20);
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("Time cannot have value less than 0"));
		}
		
		// To test the Total number of elements and making sure TreeSet/map are in Sync
		assertEquals(13, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(workOrderServiceImpl.getWorkOrderPriorityQueueSize(), workOrderServiceImpl.getWorkOrderMapSize());
		
		removeAllWorkOrders();
		
		assertEquals(0, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(0, workOrderServiceImpl.getWorkOrderPriorityQueueSize());
		
	}
	
	@Test
	public void testDequeueWorkOrder() {
		assertEquals(0, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(0, workOrderServiceImpl.getWorkOrderPriorityQueueSize());

		workOrderServiceImpl.enqueueWorkOrder(4, 39);
		
		workOrder = workOrderServiceImpl.dequeueWorkOrder();
		assertNotNull("Error with WorkOrder.", workOrder);		
		assertEquals(4, workOrder.getWorkOrderID());
		assertEquals(39, workOrder.getTimeStampMs());
		
		assertEquals(0, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(0, workOrderServiceImpl.getWorkOrderPriorityQueueSize());
		
		
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
		
		removeAllWorkOrders();
		
		assertEquals(0, workOrderServiceImpl.getWorkOrderMapSize());
		assertEquals(0, workOrderServiceImpl.getWorkOrderPriorityQueueSize());
		
	}
	
	@Test
	public void testGetSortedWorkOrder() {
		
		TreeSet<WorkOrder> workOrders = workOrderServiceImpl.getSortedWorkOrder();
	
	}

}
