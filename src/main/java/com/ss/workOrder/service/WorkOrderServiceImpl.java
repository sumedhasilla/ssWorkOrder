package com.ss.workOrder.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import com.ss.workOrder.entities.WorkOrder;
import com.ss.workOrder.util.CommonUtil;
import com.ss.workOrder.util.Constants;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkOrderServiceImpl.class);
	private HashMap<Long, WorkOrder> workOrderMap = new HashMap<Long, WorkOrder>();
	private TreeSet<WorkOrder> workOrderPriorityQueue = new TreeSet<WorkOrder>(workOrderComparator);
	
	 @Override
	public WorkOrder enqueueWorkOrder(long workOrderId, long timeStampMs) {
		 WorkOrder workOrder = null;
		 if ( workOrderMap.containsKey(workOrderId)) {
			 throw new IllegalArgumentException("The work Order ID ("+workOrderId+") already Exists!");
		 }
	     workOrder = new WorkOrder(workOrderId, timeStampMs);
		 workOrderPriorityQueue.add(workOrder);
		 workOrderMap.put(workOrderId, workOrder);
		 LOGGER.debug("Work order added");
		 return workOrder;
	}
	 	 
	 @Override
	public WorkOrder dequeueWorkOrder() {		 
		 WorkOrder workOrder = workOrderPriorityQueue.pollFirst();
		 workOrderMap.remove(workOrder.getWorkOrderID());
		 return workOrder;
	}
	 
	 @Override
	public TreeSet<WorkOrder> getSortedWorkOrder() {			
		return workOrderPriorityQueue;
	}

	@Override
	public String deleteWorkOrderById(long workOrderId) {
		WorkOrder workOrder = workOrderMap.get(workOrderId);
        if (workOrder != null) {
        	workOrderPriorityQueue.remove(workOrder);
            workOrderMap.remove(workOrderId);
            return "WorkOrder ("+workOrderId+") Deleted";
        } 
        return "Work Order ("+workOrderId+") Not Found!";
	}

	@Override
	public int getWorkOrderPosition(long workOrderId) {
		WorkOrder workOrder = workOrderMap.get(workOrderId);
		if (workOrder != null) {
			return workOrderPriorityQueue.headSet(workOrder).size();
		}
		 throw new NullPointerException("The work Order ID ("+workOrderId+") not Found!");
	}
	
	@Override
	public long getAverageWaitTime(long currentTime) {
		WorkOrder workOrder = null;
		Iterator<WorkOrder> itr = workOrderPriorityQueue.iterator();
		int totalMilliSeconds = 0;
		int size = workOrderPriorityQueue.size();
        while(itr.hasNext()) {
        	workOrder = itr.next();	 
        	totalMilliSeconds += (currentTime - workOrder.getTimeStampMs());	        	
        }	 
        return totalMilliSeconds/size;
	}
	
	 //Comparator implementation
    public static Comparator<WorkOrder> workOrderComparator = new Comparator<WorkOrder>(){
         
		@Override
		public int compare(WorkOrder workOrder1, WorkOrder workOrder2) {
			 if (workOrder1.getIdClass() == Constants.MANAGEMENT_OVERRIDE_ID) {
		            if (workOrder1.getIdClass() == workOrder2.getIdClass()) {
		                if (workOrder1.getTimeStampMs() < workOrder2.getTimeStampMs()) {
		                    return -1;
		                }
		                if (workOrder1.getTimeStampMs() > workOrder2.getTimeStampMs()) {
		                    return 1;
		                }
		                return 0;
		            } else {
		                return -1;
		            }
		        } else if (workOrder2.getIdClass() == Constants.MANAGEMENT_OVERRIDE_ID) {
		            return 1;
		        } 
		        
		        double dworkOrder1Rank = CommonUtil.calculateRank(workOrder1);
		        double dworkOrder2Rank = CommonUtil.calculateRank(workOrder2);
		        
		        if (dworkOrder1Rank < dworkOrder2Rank) {
		            return 1;
		        }
		        if (dworkOrder1Rank > dworkOrder2Rank) {
		            return -1;
		        }
		        
		        return 0;
		}
    };


}
