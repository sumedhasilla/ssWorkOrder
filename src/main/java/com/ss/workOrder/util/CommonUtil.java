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
	        
	        long lCurentTime = System.currentTimeMillis() / 1000L;
	            
	        long lWaitSeconds = lCurentTime - workOrder.getTimeStampMs();
	        double dRank = lWaitSeconds;
	        
	        if (workOrder.getIdClass() == Constants.VIP_ID || workOrder.getIdClass() == Constants.PRIORITY_ID) {
	            double dLogWaitSeconds = Math.log(lWaitSeconds);
	            if (workOrder.getIdClass() == Constants.VIP_ID) {
	                dRank = Math.max(4, (2 * lWaitSeconds * dLogWaitSeconds));
	            } else {
	                dRank = Math.max(3, (lWaitSeconds * dLogWaitSeconds));
	            }
	        }
	        return dRank;
	    }
}
