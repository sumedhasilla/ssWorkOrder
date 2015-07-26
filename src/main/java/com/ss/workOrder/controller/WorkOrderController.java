package com.ss.workOrder.controller;

import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.workOrder.entities.WorkOrder;
import com.ss.workOrder.service.WorkOrderService;


@RestController
public class WorkOrderController {
/*	PriorityQueue<WorkOrder> testque = new PriorityQueue<WorkOrder>();*/
// public WorkOrder workOrderPQ(@RequestParam(value="stringId") String stringId, @RequestParam(value="stringTime", defaultValue="11437761522582") String stringTime) {
    
	
	@Autowired
	private WorkOrderService workOrderService;
	
	@RequestMapping("/enqueue")
    public WorkOrder workOrderPQ(@RequestParam(value="id")  long id, @RequestParam(value="time") long time) {
       	WorkOrder workOrder = this.workOrderService.enqueueWorkOrder(id,time);
       	if(0 == id){
			 throw new IllegalArgumentException("Work Order ID cannot have value '0'. Please Enter valid ID. ");
		}
		if(null == workOrder){
			 throw new NullPointerException("Error with Enqueue process. ");
		}
		return workOrder;
    }
	

	/*@ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
	void handleBadRequests(HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value());
	}*/
	
	 @RequestMapping("/dequeue")
	    public WorkOrder deQueuePQ() {
	       	return this.workOrderService.dequeueWorkOrder();
	    }
	
	 @RequestMapping("/workOrderList")
	    public TreeSet<WorkOrder> getAllWorkOrder() {
	       	return this.workOrderService.getSortedWorkOrder();
	    }
	
	 @RequestMapping("/getWorkOrderPosition")
	    public int getWorkOrderPosition(@RequestParam(value="id") long id) {
		    return this.workOrderService.getWorkOrderPosition(id);
	    }
	 
	 @RequestMapping("/getAverageWaitTme")
	   public long getAverageWaitTme(@RequestParam(value="currentTime") long currentTime){
		 return this.workOrderService.getAverageWaitTime(currentTime);
	   }
	
}
