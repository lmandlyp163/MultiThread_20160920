package com.liang.basic.thread_impl;

/**
 * @Description 使用继承Thread类的方式实现多线程,示例代码模拟的是多站点卖票的场景
 *
 * @Advantage 
 *            对于每个线程中的普通成员变量，各个线程之间是单独占有一份，因此不需要额外考虑普通成员变量的线程安全问题,另外如果要覆盖线程类中的其他方法
 *            （当然一般是不需要的）,采用继承可以方便复写
 * 
 * @Disadvantage 
 *               使用继承Thread类的方式有局限性而且扩展性不高(java不支持多继承),并且创建的多个UseThread对象中的资源不能共享
 * 
 * @Date 2016年4月4日 下午3:19:18
 */
public class UseThread extends Thread {

	private int ticket = 10; // 非静态，即每个线程对象都有10张票,资源不能共享

	@Override
	public void run() { // 开启 线程底层是调用了线程中的run()方法，因此必须重写run方法已实现具体的业务需求
		sellTicket();
	}

	private void sellTicket() {
		Thread t = Thread.currentThread();
		while (ticket > 0) {
			System.out.println("id: " + t.getId() + "\t\tname: " + t.getName()
					+ "\t\tticketNum" + ticket--);
		}
	}

	public static void main(String[] args) {
		Thread t1 = new UseThread();
		Thread t2 = new UseThread();
		Thread t3 = new UseThread();
		t1.start();
		t2.start();
		t3.start();
		// 结果可以看出各个线程交替执行卖票，且每个线程10张票都是自己持有非共享的。
	}
}
