package com.ss.workOrder.util;

import com.ss.workOrder.entities.WorkOrder;

public class CommonUtil {

	public static byte getIdClassFromId(long idValue){
		
		byte idClass = Constants.NORMAL_ID;
		
		  if ((idValue % 3 == 0) && (idValue % 5 == 0)) {
	            idClass = Constants.MANAGEMENT_OVERRIDE_ID;
	        } else if ((idValue % 5 == 0)) {
	            idClass = Constants.VIP_ID;
	        } else if ((idValue % 3 == 0)) {
	            idClass = Constants.PRIORITY_ID;    
	        } else {
	            idClass = Constants.NORMAL_ID;
	        }
		  
		  return idClass;
	}
	
		public static  double calculateRank(WorkOrder workOrder) {
	        
	        long curentTime = System.currentTimeMillis();
	            
	        long waitTimeMilliSeconds = curentTime - workOrder.getTimeStampMs();
	        double dRank = waitTimeMilliSeconds;
	        
	        if (workOrder.getIdClass() == Constants.VIP_ID || workOrder.getIdClass() == Constants.PRIORITY_ID) {
	            double dLogWaitSeconds = Math.log(waitTimeMilliSeconds);
	            if (workOrder.getIdClass() == Constants.VIP_ID) {
	                dRank = Math.max(4, (2 * waitTimeMilliSeconds * dLogWaitSeconds));
	            } else {
	                dRank = Math.max(3, (waitTimeMilliSeconds * dLogWaitSeconds));
	            }
	        }
	        return dRank;
	    }
}
