package com.ss.workOrder.entities;

import com.ss.workOrder.util.CommonUtil;
import com.ss.workOrder.util.Constants;

public class WorkOrder {
	long workOrderID;
	long timeStampMs;
	byte idClass;
 
 public WorkOrder( long ID, long timeValue){
	 this.workOrderID = ID;
	 this.timeStampMs = timeValue;
	 this.idClass=CommonUtil.getIdClassFromId(workOrderID);
 }
 public long getWorkOrderID() {
	return workOrderID;
}

public void setWorkOrderID(long workOrderID) {
	this.workOrderID = workOrderID;
}

public long getTimeStampMs() {
	return timeStampMs;
}

public void setTimeStampMs(long timeStampMs) {
	this.timeStampMs = timeStampMs;
}

public byte getIdClass() {
	return idClass;
}

public void setIdClass(byte idClass) {
	this.idClass = idClass;
}

}
