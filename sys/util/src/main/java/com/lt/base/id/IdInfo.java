package com.lt.base.id;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IdInfo {
	

    private long anchor = 0;
    private long timestamp = 0;
    private long sequence = 0;
    private int serviceType = 0;
    private int serviceId = 0;

    public IdInfo() {
    }

    public IdInfo(long id, long anchor) {
        this.anchor = anchor;
        this.timestamp = SnowflakeIdMaker.getTimestamp(id);
        this.sequence = SnowflakeIdMaker.getSequence(id);
        this.serviceType = SnowflakeIdMaker.gettype(id);
        this.serviceId = SnowflakeIdMaker.getWorkId(id);
    }


    public long getAnchor() {
        return anchor;
    }

    public void setAnchor(long anchor) {
        this.anchor = anchor;
    }

    public Date getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp + this.anchor);
        return calendar.getTime();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public int getserviceType() {
        return serviceType;
    }

    public void setserviceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "IdInfo{ time=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(getTime()) +
                ", timestamp=" + timestamp +
                ", sequence=" + sequence +
                ", serviceType=" + serviceType +
                ", serviceId=" + serviceId +
                ", anchor=" + anchor +
                '}';
    }


}
