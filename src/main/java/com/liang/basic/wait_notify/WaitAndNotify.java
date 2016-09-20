package com.liang.basic.wait_notify;

/**
 * @Description wait()方法与notify()方法使用
 * 
 *              1. 调用某对象的wait方法必须先获得该对象的同步锁,否则会抛出异常<br/>
 *              2. wait()和notify()、notifyAll()方法都是定义在Object类中的,
 *              当某一个线程调用了某一对象的wait方法时
 *              ，该线程将放弃持有的该对象的同步锁，并进入等待阻塞状态(注意是当前调用wait方法的线程阻塞
 *              ，放弃的是调用对象的同步锁),直到其他线程调用了相同对象的notify
 *              ()、notifyAll()方法或wait方法设置的等待时间到
 * 
 * @Date 2016年4月5日 下午11:40:49
 */
public class WaitAndNotify {

	public static void main(String[] args) throws Exception {
		Target target = new Target();
		synchronized (target) { // 主线程首先获得target对象的同步锁
			Thread t1 = new Thread(target);
			t1.start(); // 开启线程t1,但由于获取不到同步锁,被阻塞
			System.out.println("before wait ....");
			target.wait(); // 主线程调用target.wait(),主线程放弃target的对象锁,并进入阻塞状态，其他线程可以获得同步锁,等到其他线程调用了target对象的notify方法才能继续执行
		}
		System.out.println("after wait ..."); // 为什么此处必须等到run方法运行结束才能执行?

		/**
		 * 由于必须是在持有同步锁情况下才能调用了wait方法,即wait之前该线程并未退出临界区，
		 * 因此当唤醒的时候也必须重新获得同步锁才能继续执行, 因为可能上次获得同步锁在临界区中的代码可能还没执行完
		 */

	}
}

class Target implements Runnable {

	public void run() {
		synchronized (this) {
			Thread t = Thread.currentThread();
			System.out.println(t.getName()
					+ " entry the  critical section——run");
			notify(); // 调用target对象的notify方法，唤醒一个在其他线程调用相同对象wait方法的线程
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(t.getName() + " call the notify method...");
		}
	}

}
