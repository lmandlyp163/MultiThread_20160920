package com.liang.basic.thread_impl;

/**
 * @Description 使用实现Runnable接口的方式实现多线程
 * 
 * @Advantage 使用接口扩展性高，因为一个类只能有一个父类，但可以实现多个接口。同时该种方式创建多线程是基于同一个Runnable对象，
 *            即多个线程对象共享Runnable对象的资源。
 *            
 * @Disadvantage 由于基于同一个Runnable建立的线程共享Runnable对象中的资源，因此需要注意是否有线程安全问题
 * 
 * @Date 2016年4月4日 下午3:39:48
 */
public class UseRunnable implements Runnable {

	private int ticket = 10; //基于同一个Runnable对象建立的线程对象共享该资源，对于同一个Runnable对象的线程相当于static变量，此时要解决同步问题

	@Override
	public void run() {
		sellTicket();
	}

	//此处不解决线程安全问题，只需要理解资源共享即可
	private void sellTicket() {
		Thread t = Thread.currentThread();
		while (ticket > 0) {
			System.out.println("id: " + t.getId() + "\t\tname:" + t.getName()
					+ "\t\tticketNum: " + ticket--);
		}
	}
	
	public static void main(String[] args) {
		Runnable target = new UseRunnable();
		Thread t1 = new Thread(target); //基于同一个Runnable对象创建的线程
		Thread t2 = new Thread(target);
		Thread t3 = new Thread(target);
		t1.start();
		t2.start();
		t3.start();
		//从结果可以看出，3个线程共享10张票，此时可能会出现买了重复票，或者卖出不存在的票
	}

}
