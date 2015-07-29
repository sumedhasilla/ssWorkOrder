package com.ss.workOrder.service;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import javax.ws.rs.BadRequestException;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import com.ss.workOrder.entities.WorkOrder;
import com.ss.workOrder.exception.IdAlreadyExistsException;
import com.ss.workOrder.exception.NoContentException;
import com.ss.workOrder.exception.NotFoundException;
import com.ss.workOrder.util.CommonUtil;
import com.ss.workOrder.util.Constants;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkOrderServiceImpl.class);

	public synchronized int getWorkOrderPriorityQueueSize() {
		return workOrderPriorityQueue.size();
	}

	private TreeSet<WorkOrder> workOrderPriorityQueue = new TreeSet<WorkOrder>(workOrderComparator);
	
	 @Override
	public  synchronized WorkOrder enqueueWorkOrder(long workOrderId, long timeStampMs) {
		WorkOrder workOrder = null;
		if(workOrderId <= 0){
			 throw new BadRequestException("Work Order ID cannot have value less than or equal to 0. Please Enter valid ID. ");
		}

		workOrder = new WorkOrder(workOrderId, timeStampMs);
	    if (workOrderPriorityQueue.contains(workOrder)) {
			 throw new IdAlreadyExistsException("The work Order ID ("+workOrderId+") already Exists!");
		}
		
	    workOrderPriorityQueue.add(workOrder);
		
	    return workOrder;
	}
	 	 
	 @Override
	public synchronized  WorkOrder dequeueWorkOrder() {
		return workOrderPriorityQueue.pollFirst();
	}
	 
	 @Override
	public synchronized  TreeSet<WorkOrder> getSortedWorkOrder() {			
		return workOrderPriorityQueue;
	}

	@Override
	public synchronized String deleteWorkOrderById(long workOrderId) {
		WorkOrder dummyWorkOrder = new WorkOrder(workOrderId, 0);		//Dummy WorkOrder to match WorkOrder in Queue
        if (workOrderPriorityQueue.remove(dummyWorkOrder)) {
            return "WorkOrder ("+workOrderId+") Deleted";
        } 
        return "Work Order ("+workOrderId+") Not Found!";
	}

	@Override
	public synchronized  int getWorkOrderPosition(long workOrderId) {
		WorkOrder workOrder = null;
		int position= 0;
		Iterator<WorkOrder> itr = workOrderPriorityQueue.iterator();
		while(itr.hasNext()) {
			workOrder = itr.next();
			if(workOrderId == workOrder.getWorkOrderID()){
				 return position;
			}
			++position;
		}	
		
		throw new NotFoundException("The work Order ID ("+workOrderId+") not Found!");
	}
	
	@Override
	public  synchronized long getAverageWaitTime(long currentTime) {
		WorkOrder workOrder = null;
		Iterator<WorkOrder> itr = workOrderPriorityQueue.iterator();
		long totalMilliSeconds = 0;
		int size = workOrderPriorityQueue.size();
		if(0 == size){
			throw new NoContentException("No Work Orders Present in the Queue!");
		}
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
			 if(workOrder1.getWorkOrderID() == workOrder2.getWorkOrderID()){
				 return 0;
			 }
			 if (workOrder1.getIdClass() == Constants.MANAGEMENT_OVERRIDE_ID) {
		            if (workOrder1.getIdClass() == workOrder2.getIdClass()) {
		                if (workOrder1.getTimeStampMs() < workOrder2.getTimeStampMs()) {
		                    return -1;
		                }
		                if (workOrder1.getTimeStampMs() > workOrder2.getTimeStampMs()) {
		                    return 1;
		                }
		                return 1;
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
		        
		        return 1;
		}
    };
}
