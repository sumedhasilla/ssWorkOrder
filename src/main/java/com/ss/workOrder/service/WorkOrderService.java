package com.ss.workOrder.service;

import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.ss.workOrder.entities.WorkOrder;

@Service
public interface WorkOrderService {

	/** Method to add new WorkOrder to Priority Queue */
	public WorkOrder enqueueWorkOrder(long workOrderId,long timeStampMs);
	
	/** Method to return and remove TOP WorkOrder from Priority Queue */
	public WorkOrder dequeueWorkOrder();
	
	/** Method to get WorkOrder Priority Queue sorted from highest ranked to lowest */
	public TreeSet<WorkOrder> getSortedWorkOrder();
	
	/** Method to delete specific WorkOrder based on WorkOrderID */
	public String deleteWorkOrderById(long workOrderId);
	
	/** Method to obtain WorkOrder based on its ID *//*
	public WorkOrder getWorkOrderById(int id);*/
	
	/** Method to obtain the position of the WorkOrder */
	public int getWorkOrderPosition(long workOrderId);
	
	/** Method to obtain average wait time for all WorkOrders in Priority Queue */
	public long getAverageWaitTime(long currentTime);
		
}
