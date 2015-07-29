package com.ss.workOrder.controller;

import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ss.workOrder.entities.WorkOrder;
import com.ss.workOrder.exception.NotFoundException;
import com.ss.workOrder.service.WorkOrderService;
import org.springframework.http.HttpStatus;

@RestController
public class WorkOrderController {
	
	@Autowired
	private WorkOrderService workOrderService;
	
	@RequestMapping(value="/enqueue", 
			method=RequestMethod.POST,
			consumes={"application/json"},
			produces={"application/json"})	
	@ResponseStatus(HttpStatus.CREATED)
	public WorkOrder workOrderPQ(@RequestBody WorkOrder workOrder) {
		workOrder = this.workOrderService.enqueueWorkOrder(workOrder);
       	if(null == workOrder){
			 throw new NullPointerException("Error with Enqueue process. ");
		}
		return workOrder;
    }
	
	 @RequestMapping("/dequeue")
	    public WorkOrder deQueuePQ() {		 
		 WorkOrder workOrder = this.workOrderService.dequeueWorkOrder();
			 if(null != workOrder){
				 return workOrder;
			 }else{
				 throw new NotFoundException("Work Order Queue is Empty!");
			 }	       	
	    }
	
	 @RequestMapping("/workOrdersSorted")
	    public TreeSet<WorkOrder> getAllWorkOrder() {
	       	return this.workOrderService.getSortedWorkOrder();
	    }
	 
	@RequestMapping(value = "/deleteWorkOrderById",
			method=RequestMethod.DELETE)
	public String deleteWorkOrderById(@RequestParam(value="id") long id){
		return this.workOrderService.deleteWorkOrderById(id);
	}
	
	 @RequestMapping("/workOrderPosition")
	    public int getWorkOrderPosition(@RequestParam(value="id") long id) {
		    return this.workOrderService.getWorkOrderPosition(id);
	    }
	 
	 @RequestMapping("/averageWaitTime")
	   public long getAverageWaitTme(@RequestParam(value="currentTime") long currentTime){
		 return this.workOrderService.getAverageWaitTime(currentTime);
	   }
	 
}
