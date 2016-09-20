package com.liang.basic.thread_method;

/**
 * @Description join()方法的作用是等待子线程终止————阻塞当前线程直到子线程t完成（此处的t由t.join()决定）
 * 
 * @Theory 
 *         join方法的实现是通过判断判断子线程是否还存活，如果存活调用“主线程”的wait方法，将主线程阻塞起来，只有当子线程运行结束才会唤醒主线程
 *        
 * 
 * @Date 2016年4月8日 下午8:01:23
 */
public class Join {

	private int result = 0;

	public static void main(String[] args) throws Exception {

		Join j = new Join();
		Thread t1 = new Thread(j.new Target1());
		Thread t2 = new Thread(j.new Target2());
		System.out.println("initData = " + j.result);
		t1.start();
		t1.join(); // 调用join方法的必须是线程对象,调用join方法的线程必须等待该线程对象代表的子线程执行完成,此处主线程必须等待t1线程执行完毕
		System.out.println("t1 Thread is finished..."); // 即使t1线程在执行过程中阻塞了,主线程也不能运行，必须等到t1线程结束
		t2.start();
		t2.join(); // 同上
		System.out.println("result = " + j.result);
	}

	// 线程1任务：将a的值赋为1
	class Target1 implements Runnable {
		public void run() {
			result = 1;
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 线程2任务：将a的值扩大十倍变为10
	class Target2 implements Runnable {
		public void run() {
			while (result != 1) {
				// doNothing
			}
			result *= 10;
		}
	}
}
