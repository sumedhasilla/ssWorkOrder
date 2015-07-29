package com.ss.workOrder.controller;

import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.workOrder.entities.WorkOrder;
import com.ss.workOrder.exception.NotFoundException;
import com.ss.workOrder.service.WorkOrderService;


@RestController
public class WorkOrderController {
	
	@Autowired
	private WorkOrderService workOrderService;
	
	@RequestMapping("/enqueue")
    public WorkOrder workOrderPQ(@RequestParam(value="id")  long id, @RequestParam(value="time") long time) {
       	WorkOrder workOrder = this.workOrderService.enqueueWorkOrder(id,time);
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
	
	 @RequestMapping("/getWorkOrderList")
	    public TreeSet<WorkOrder> getAllWorkOrder() {
	       	return this.workOrderService.getSortedWorkOrder();
	    }
	 
	@RequestMapping("/deleteWorkOrderById")
	public String deleteWorkOrderById(@RequestParam(value="id") long id){
		return this.workOrderService.deleteWorkOrderById(id);
	}
	
	 @RequestMapping("/getWorkOrderPosition")
	    public int getWorkOrderPosition(@RequestParam(value="id") long id) {
		    return this.workOrderService.getWorkOrderPosition(id);
	    }
	 
	 @RequestMapping("/getAverageWaitTime")
	   public long getAverageWaitTme(@RequestParam(value="currentTime") long currentTime){
		 return this.workOrderService.getAverageWaitTime(currentTime);
	   }
	 
}
