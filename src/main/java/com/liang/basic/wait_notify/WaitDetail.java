package com.liang.basic.wait_notify;

/**
 * @Description wait方法的一些小细节:调用某对象wait方法,如
 *              t.wait(),则阻塞的线程是当前运行的线程，阻塞的条件是t对象,只有在其他线程调用了t对象的notify等方法才能
 *              唤醒因为该对象而阻塞的线程,而当某个线程如k终止时,会调用该对象的notifyAll方法，唤醒所有因为该对象而被阻塞的线程
 * @Date 2016年4月8日 下午10:26:55
 */
public class WaitDetail {

	class Thread1 extends Thread {
		public void run() {
			Thread t = Thread.currentThread();
			System.out.println(t.getName() + " entry the run method");
			try {
				Thread.sleep(1000); // 睡眠1s
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(t.getName() + " exit the run method");
			// 注意：此处并没有显式的调用notifyAll()方法,但是在线程销毁的时候会唤醒所有因为该线程对象而阻塞的阻塞队列。因此该线程运行结束后主线程能转为就绪状态
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread t = new WaitDetail().new Thread1();
		t.start(); // 开启子线程

		synchronized (t) { // 子线程对象作为同步锁,由主线程持有
			t.wait(); // 主线程调用wait方法,主线程阻塞,阻塞条件是t对象——>其他线程调用了t的notify方法或notifyAll才能唤醒主线程
		}
		System.out.println("exit the main thread...");
	}
}
