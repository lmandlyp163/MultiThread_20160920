package com.liang.basic.syn_useage;


/**
 * @Description synchronized实现的是线程之间的互斥访问，由于每个对象都有且只有一个同步锁，
 *              但一个线程获得了某对象的同步锁之后才能执行该对象的同步方法或同步代码块
 *              ，从而实现了多个线程之间对同步代码的互斥访问,但其他线程仍能正常访问非同步方法
 * 
 *              简而言之，两条规则：执行任何同步方法和同步代码块必须先获得同步锁，否则阻塞等待;执行非同步方法不管有没有获得同步锁都能执行
 * 
 * @Date 2016年4月5日 下午9:10:58
 */
public class SynchronizedDemo {

	public static void main(String[] args) throws InterruptedException {
		Target target = new Target();

		Thread t1 = new Thread(target);
		Thread t2 = new Thread(target);
		Thread t3 = new Thread(target);
		Thread t4 = new Thread(target);

		t1.start(); // t1线程持有对象锁,其他线程无法进行卖票
		t2.start();
		t3.start();
		t4.start();

		Thread.sleep(200);
		target.open(); //非同步方法可以正常访问
		target.increase(); //能进入非同步区域，但不能进入同步代码块,需等获取到同步锁
		target.close(); //即使主线程调用同步方法也不能立即进入,必须等到其他线程释放同步锁并被主线程获取
	}

}

class Target implements Runnable {

	private int ticket = 20;

	public void run() {
		synchronized (this) {
			Thread t = Thread.currentThread();
			System.out.println(t.getName() + " obtained the synchronized lock");
			while (ticket > 0) {
				System.out.println(t.getName() + " sell the ticket: "
						+ ticket--);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

	public synchronized void close() {
		System.out.println("all tickets have been sold out!");
	}

	public void open() {
		System.out.println("starting sell tickets....");
	}

	public void increase() {
		System.out.println("current remaining tickets are " + ticket +  ", increase the number of ticket...");
		synchronized (this) {
			ticket += 10;
			System.out.println("the number of ticket have been increased");
		}
	}
}