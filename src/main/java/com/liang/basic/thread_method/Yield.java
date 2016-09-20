package com.liang.basic.thread_method;

/**
 * @Description yield()方法定义在Thread中,作用是线程让步,即调用yield的当前线程让出CPU执行权,由运行状态
 *              转变为就绪状态,从而让其他具有相同等级的线程有机会获得CPU执行权;也有可能是当前线程又获得CPU执行权，
 *              由就绪状态转为运行状态。
 * 
 *              与wait方法的两点不同：1.调用wait方法的线程由运行状态变为阻塞状态,而调用yield方法则由运行状态变为就绪状态;
 *              2.调用wait方法的线程会放弃同步锁,而yield不会释放同步锁。
 * @Date 2016年4月8日 上午9:36:00
 */
public class Yield {
	public static void main(String[] args) {
		Runnable r = new Target();
		synchronized(r) { //主线程获得target对象的同步锁
			Thread t = new Thread(r);
			t.start();
			for(int i = 0; i < 1000; i++) {
				Thread.yield(); //进行让步，由于不释放同步锁，因此t线程无法进入同步临界区,但有可能执行run方法
			}
			System.out.println("main thread exit the critical section");
		}
	}
	
	private static class Target implements Runnable {
		public void run() {
			System.out.println("entry the run method...");
			synchronized(this) {
				System.out.println(Thread.currentThread().getName() + " run the method...");
			}
		}
	}
	
}
