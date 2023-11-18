import java.util.Scanner;
import java.util.Stack;

import javax.swing.text.rtf.RTFEditorKit;

class Job{
	int num;
	int arrive;
	int burst;
	int runtime;
	int priority;
	int order;
	
	Job(int num, int arr, int bur, int pri){
		this.num = num;
		this.arrive = arr;
		
		this.burst = bur;
		this.runtime = bur;
		
		this.priority = pri;
		this.order = pri;
		
		System.out.print(">>Process " + num + ": (" + arr + ", " + bur + ", " + pri + ") was created.\n\n");
	}
	public void run(int rt, int Timer) {
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
	static int Timer;
	static int TWT;
	static int Count;
	static Stack<Job> JOBS;
	private Scanner scanner;
	
	Scheduling(){
		Timer = 0;
		TWT = 0;
		Count = 0;
		JOBS = new Stack<Job>();
		scanner = new Scanner(System.in);
		
		System.out.print("Input processes:\t(End with 0 0 0)\n{Arrival Time} {Burst Time} {Priority num}\n"); 
		while(true){
			int a = 0, b = 0, c = 0;
			
			a = scanner.nextInt();
			b = scanner.nextInt();
			c = scanner.nextInt();
			
			if (b == 0)
				break;
			Count++;
			JOBS.add(new Job(Count, a, b, c));	
		}
	}
	public float AWT(){
		return (float)TWT/Count;
	}
	
	public static void reset() {
		Timer = 0;
		TWT = 0;
		for (Job job : JOBS) {
			job.burst = job.runtime;
			job.priority = job.order;
		}
	}
	public void FCFS() {
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
	
	public void SJFnp() {
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
	
	
	public void SJFp() {
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
	public void Priority() {
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
	
	public void RR() {
		reset();
		Stack<Job> jobs = (Stack<Job>) JOBS.clone();
		
		System.out.print("\n\n\n<Round-Robin>\n\n");
		System.out.print("Input RR's T = ");
		int T = scanner.nextInt();
		
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
