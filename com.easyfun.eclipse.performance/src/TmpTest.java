import org.apache.commons.lang.math.NumberUtils;


public class TmpTest {
	public static void main(String[] args) {
		String line ="1	4682	RUNNABLE	WorkerThread#1[10.3.3.216:48402]	sun.management.ThreadImpl.getThreadInfo1(ThreadImpl.java:-2)";
		String[] tmps = line.split("\t");
		if(tmps.length >0){
			System.out.println(tmps[2] + NumberUtils.isNumber(tmps[2]));;
		}
		System.out.println(tmps.length);
		System.out.println(tmps[0]);
		System.out.println(tmps[1]);
	}
}
