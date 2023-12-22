import java.util.Scanner;
import java.util.Stack;

import javax.swing.text.rtf.RTFEditorKit;

class Job{
	int num;	// 第幾個Job
	int arrive;	// 抵達時間arrive time 
	int burst;	// 剩餘runtime
	int runtime;	// 預設runtime (reset用)
	int priority;	// 現在priority
	int order;	// 預設priority (reset用)
	
	Job(int num, int arr, int bur, int pri){	// class Job 建構子
		this.num = num;
		this.arrive = arr;	// set num, arrive
		
		this.burst = bur;
		this.runtime = bur;	// set burst, runtime
		
		this.priority = pri;
		this.order = pri;	// set priority, order
		
		System.out.print(">>Process " + num + ": (" + arr + ", " + bur + ", " + pri + ") was created.\n\n");
	}
	public void run(int rt, int Timer) {	// 在時間Timer時，執行此 Job，持續rt時間單位 
		System.out.print("Time: " + Timer + "~" + +(rt+Timer) + "\n");
		System.out.print(">>Process " + num + " is running.\n");
		this.burst -= rt;
		
		if (burst > 0)
			System.out.print(">>Process " + num + " was interrupted.\n\n");
		else
			System.out.print(">>Process " + num + " was terminated.\n\n");
	}
}

class Scheduling {
	static int Timer;	// 計時器
	static int TWT;		// 總等待時間 Total waiting time
	static int Count;	// 工作數量 Job counter
	static Stack<Job> JOBS;		// 儲存Jobs
	private Scanner scanner;
	
	Scheduling(){	// class Scheduling 建構子
		Timer = 0;
		TWT = 0;
		Count = 0;
		JOBS = new Stack<Job>();
		scanner = new Scanner(System.in);	// reset properties
		
		System.out.print("Input processes:\t(End with 0 0 0)\n{Arrival Time} {Burst Time} {Priority num}\n");
		while(true){	// input Jobs
			int a = 0, b = 0, c = 0;
			
			a = scanner.nextInt();
			b = scanner.nextInt();
			c = scanner.nextInt();
			
			if (b == 0)
				break;
			Count++;
			JOBS.add(new Job(Count, a, b, c));	// construct and add Job into stack JOBS	
		}
	}
	public float AWT(){	// computing AWT by divide TWT by Count(# of Jobs)
		return (float)TWT/Count;
	}
	
	public static void reset() {	// reset Timer & TWT, reset all jobs by recovering burst and priority
		Timer = 0;
		TWT = 0;
		for (Job job : JOBS) {
			job.burst = job.runtime;
			job.priority = job.order;
		}
	}
	public void FCFS() {	// Frist Come First Serve scheduling
		reset();
		Stack<Job> jobs = (Stack<Job>) JOBS.clone();
		
		System.out.print("\n\n\n<FCFS>\n\n");
		for (Job job : jobs) {
			if (Timer < job.arrive)
				Timer = job.arrive;
			
			int rt = job.burst;
			job.run(rt, Timer);
			Timer += rt;
			
			if (job.burst == 0){
				TWT += Timer - job.arrive - job.runtime; 
				//jobs.remove(job);
			}
		}
		
		System.out.print("AWT = " + this.AWT());
	}
	
	public void SJFnp() {	// non-preemptive Shortest Job First scheduling
		reset();
		Stack<Job> jobs = (Stack<Job>) JOBS.clone();
		
		System.out.print("\n\n\n<SJF (non-preemptive)>\n\n");
		
		while(!jobs.empty()){
			Job job = null;
			for (Job j : jobs){
				if (Timer < j.arrive)
					break;
				if (job == null || job.burst > j.burst)
					job = j;
			}
			if (job == null) {
				Timer = jobs.firstElement().arrive;
				continue;
			}
			
			int rt = job.burst;
			job.run(rt, Timer);
			Timer += rt;
			
			if (job.burst == 0){
				TWT += Timer - job.arrive - job.runtime; 
				jobs.remove(job);
			}
		}
		
		System.out.print("AWT = " + this.AWT());
	}
	
	
	public void SJFp() {	// preemptive Shortest Job First scheduling
		reset();
		Stack<Job> jobs = (Stack<Job>) JOBS.clone();
		
		System.out.print("\n\n\n<SJF (preemptive)>\n\n");
		
		while(!jobs.empty()){
			Job job = null;
			for (Job j : jobs){
				if (Timer < j.arrive)
					break;
				if (job == null || job.burst > j.burst)
					job = j;
			}
			if (job == null) {
				Timer = jobs.firstElement().arrive;
				continue;
			}
			
			int rt = job.burst;
			
			
			for (Job j : jobs){
				if (Timer < j.arrive){
					if (rt >  j.arrive - Timer)
						rt = j.arrive - Timer;
					break;
				}
			}
			
			job.run(rt, Timer);
			Timer += rt;
			
			
			if (job.burst == 0){
				TWT += Timer - job.arrive - job.runtime; 
				jobs.remove(job);
			}
		}
		
		System.out.print("AWT = " + this.AWT());
	}
	public void Priority() {	// Priority scheduling
		reset();
		Stack<Job> jobs = (Stack<Job>) JOBS.clone();
		
		System.out.print("\n\n\n<Priority>\n\n");
		
		while(!jobs.empty()){
			Job job = null;
			for (Job j : jobs){
				if (Timer < j.arrive)
					break;
				if (job == null || job.priority > j.priority)
					job = j;
			}
			if (job == null) {
				Timer = jobs.firstElement().arrive;
				continue;
			}
			
			int rt = job.burst;
			
			job.run(rt, Timer);
			Timer += rt;
			
			
			if (job.burst == 0){
				TWT += Timer - job.arrive - job.runtime; 
				jobs.remove(job);
			}
		}
		
		System.out.print("AWT = " + this.AWT());
	}
	
	public void RR() {	// Round-robin scheduling
		reset();
		Stack<Job> jobs = (Stack<Job>) JOBS.clone();
		
		System.out.print("\n\n\n<Round-Robin>\n\n");
		System.out.print("Input RR's T = ");
		int T = scanner.nextInt();	// input time quantum
		
		while(!jobs.empty()){
			for (int i = 0; i < jobs.size(); i++){
				Job job = jobs.elementAt(i); 
				
				
				int rt = Math.min(job.burst, T);
				job.run(rt, Timer);
				Timer += rt;
				
				if (job.burst == 0){
					TWT += Timer - job.arrive - job.runtime; 
					jobs.remove(job);
					i--;
				}
			}
		}
		
		System.out.print("AWT = " + this.AWT());
	}
}



public class Schedule {

	public static void main(String[] args) {
		Scheduling scheduling = new Scheduling();
		scheduling.FCFS();
		scheduling.SJFnp();
		scheduling.SJFp();
		scheduling.Priority();
		scheduling.RR();
		
	}

}
