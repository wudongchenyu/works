package base;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

public class Test {
	
	 public static void main(String[] args) throws Exception {
	        // System.out.println(getPidFromWindows("javaw"));
	        // 获取监控主机
	        /*MonitoredHost local = MonitoredHost.getMonitoredHost("localhost");
	        // 取得所有在活动的虚拟机集合
	        Set<Object> vmlist = new HashSet<Object>(local.activeVms());
	        // 遍历集合，输出PID和进程名
	        for (Object process : vmlist) {
	            MonitoredVm vm = local.getMonitoredVm(new VmIdentifier("//" + process));
	            // 获取类名
	            String processname = MonitoredVmUtil.mainClass(vm, true);
	            System.out.println(process + " ------> " + processname);
	        }
	        
	        List<String> list = new ArrayList<String>();
	        System.out.println(list.size());*/
	        List<String> list = getPid();
	        System.out.println(list);

	    }
	    
	    public static List<String> getPid(){
	        List<String> list = new ArrayList<String>();
	        try {
	            // 获取监控主机
	            MonitoredHost local;
	            local = MonitoredHost.getMonitoredHost("localhost");
	            // 取得所有在活动的虚拟机集合
	            Set<Object> vmlist = new HashSet<Object>(local.activeVms());
	            // 遍历集合，输出PID和进程名
	            for (Object process : vmlist) {
	                MonitoredVm vm = local.getMonitoredVm(new VmIdentifier("//" + process));
	                // 获取类名
	                String processname = MonitoredVmUtil.mainClass(vm, true);
	                System.out.println(process + " ------> " + processname);
	                if (processname.endsWith("stable.jar")) {
	                    list.add(((Integer)process).toString());
	                }
	            }
	        } catch (MonitorException e) {
	            e.printStackTrace();
	        } catch (URISyntaxException e) {
	            e.printStackTrace();
	        }
	        return list;
	        
	    }

}
