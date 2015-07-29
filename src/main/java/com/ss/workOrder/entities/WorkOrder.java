package com.ss.workOrder.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ss.workOrder.util.CommonUtil;

public class WorkOrder {
	
	long workOrderID;
	long timeStampMs;
	byte idClass;
 
 public WorkOrder( @JsonProperty("workOrderID") long id, @JsonProperty("timeStampMs")long timeValue){
	 this.workOrderID = id;
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
