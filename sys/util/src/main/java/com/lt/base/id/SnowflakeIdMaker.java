package com.lt.base.id;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Calendar;

public class SnowflakeIdMaker {


    //开始时间截 (2018-09-20)
    private final long anchor = 1537372800000L;

    //机器id所占的位数
    private final long serviceIdBits = 8L;

    //类型id所占的位数
    private final long typeBits = 5L;
    //支持的最大类型id，结果是63
    private final long maxtype = -1L ^ (-1L << typeBits);
    //时间戳中序号在id中占的位数
    private final long sequenceBits = 12L;

    /********** 位移时的偏移量计算*********/
    //机器ID向左移0位
    private final long serviceIdShift = 0;
    //数据标识id向左移5位(5)
    private final long typeShift = serviceIdBits;
    //时间戳中序号向左移13位(5+8)
    private final long sequenceShift = typeBits + serviceIdBits;
    //时间截向左移25位(5+8+12)
    private final long timestampLeftShift = sequenceBits + serviceIdBits + typeBits;

    //生成序号的掩码，这里为4095 (0b-111111111111=0xfff=4095)
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    //服务器ID(0~31)
    private long serviceId;
    //类型ID(0~255)
    private long type;
    //毫秒内序号(0~4095)
    private long sequence = 0L;
    //上次生成ID的时间截（相对于anchor来计算）
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     * @param type 类型ID (0~31)
     */
    public SnowflakeIdMaker(long type) {
    	InetAddress addr;
    	this.serviceId = 0;
		try {
			addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress();
	    	if (ip != null) {
				this.serviceId = Long.valueOf(ip.substring(ip.lastIndexOf(".")+1));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	
        if (type > maxtype || type < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxtype));
        }
        this.type = type;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序号
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序号溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序号重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;
        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - anchor) << timestampLeftShift) //加入时间戳
                | (sequence << sequenceShift) //加入时间戳中的序号
                | (type << typeShift) //加入类型ID
                | (serviceId << serviceIdShift) //加入机器ID
                ;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /********** 信息提取方法*********/
     //信息提取偏移量，时间（偏移25）+序号（偏移13）+中心（偏移8）+机器号（0）
    public static int getWorkId(long id) {
        return (int) (id & 255);
    }

    public static int gettype(long id) {
        return (int) (id >> 8 & 31);
    }

    public static int getSequence(long id) {
        return (int) (id >> 13 & 4095);
    }

    public static long getTimestamp(long id) {
        return id >> 25 & Long.MAX_VALUE;
    }

    public IdInfo getIdInfo(long id) {
        return new IdInfo(id, anchor);
    }

    public void spiltId(long id) {
        long time = getTimestamp(id);//时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time + this.anchor);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println("时间:" + sdf.format(calendar.getTime()) + ",序号:" + getSequence(id) + ",类型:" + gettype(id) + ",机器号:" + getWorkId(id));

    }
    
    public static byte[] long2byte(long res) {
		byte[] buffer = new byte[8];
		for (int i = 0; i < 8; i++) {
			int offset = 64 - (i + 1) * 8;
			buffer[i] = (byte) ((res >> offset) & 0xff);
		}
		return buffer;
	}
    
    public static void main(String[] args) {
		System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli());
	}
}