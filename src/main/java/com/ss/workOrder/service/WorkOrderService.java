package com.ss.workOrder.service;

import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.ss.workOrder.entities.WorkOrder;
@Service
public interface WorkOrderService {

	/** Method to add new WorkOrder to Priority Queue
	 * @param workOrderId
	 * @param timeStampMs
	 * @return WorkOrder */
	public WorkOrder enqueueWorkOrder(long workOrderId,long timeStampMs);
	
	/** Method to return and remove TOP WorkOrder from Priority Queue 
	 * @return WorkOrder */
	public WorkOrder dequeueWorkOrder();
	
	/** Method to get WorkOrder Priority Queue sorted from highest ranked to lowest 
	 * @return TreeSet<WorkOrder> */
	public TreeSet<WorkOrder> getSortedWorkOrder();
	
	/** Method to delete specific WorkOrder based on WorkOrderID 
	 * @param workOrderId 
	 * @return String */
	public String deleteWorkOrderById(long workOrderId);
		
	/** Method to obtain the position of the WorkOrder  
	 * @param workOrderId 
	 * @return int */
	public int getWorkOrderPosition(long workOrderId);
	
	/** Method to obtain average wait time for all WorkOrders in Priority Queue 
	 * @param currentTime
	 * @return averageWaitTime */
	public long getAverageWaitTime(long currentTime);
		
}
