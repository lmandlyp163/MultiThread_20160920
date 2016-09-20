package com.liang.basic.thread_method;

/**
 * @Description sleep()方法也是Thread中定义的,它与wait方法的区别是sleep是由运行状态转为睡眠阻塞状态,
 *              必须指定睡眠时间,等待时间到从阻塞变为就绪状态，并且sleep方法不放弃同步锁<br/>
 *              而wait方法是有运行状态转为等待阻塞状态,唤醒条件可以是等待时间到或者其他线程来唤醒,阻塞同时放弃同步锁资源。
 * @Date 2016年4月8日 上午9:55:55
 */
public class Sleep {

	public static void main(String[] args) {
		Runnable r = new Target();
		synchronized (r) {
			Thread t1 = new Thread(r);
			Thread t2 = new Thread(r);
			Thread t3 = new Thread(new Target());
			t1.start(); // t1线程进入run方法后会被阻塞
			t2.start(); // t2线程进入run方法后会被阻塞
			t3.start(); // t3线程不会被阻塞
			System.out.println(Thread.currentThread().getName()
					+ " thread exit the critical section...");
			// 主线程退出临界区释放同步锁才能唤醒t1、t2线程,t3线程则不需要等待主线程释放同步锁
		}
	}

	private static class Target implements Runnable {
		public void run() {
			synchronized (this) {
				try {
					System.out.println(Thread.currentThread().getName());
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
