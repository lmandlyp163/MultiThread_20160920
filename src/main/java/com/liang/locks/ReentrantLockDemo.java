package com.liang.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 
 *              ReentrantLock是一个可重入的互斥锁，又被称为独占锁————在同一个时间点只能有一个线程持有该ReentrantLock
 *              ,其他线程必须等待，并且该锁可以被单个线程多次获得。ReentrantLock通过一个FIFO的等待队列来管理获取该锁的所有线程
 * 
 *              ReentrantLock又分为“公平锁”和“非公平锁”——使用公平锁时申请获得锁的线程首先被阻塞进等待队列，
 *              按照FIFO规则获取锁 ; 而使用非公平锁则是在锁可获取状态时，不管自己是不是在队列的开头都有机会获取锁。默认是非公平锁。
 * 
 *              synchronized和ReentrantLock的异同: <br/>
 *              首先synchronized具有某些功能上的不足,如：<br/>
 *              1. 无法中断正在等候获取一个锁的线程<br/>
 *              2. 无法通过投票得到一个锁 <br/>
 *              3. 释放锁的操作只能与获得锁所在的代码块中进行，无法在别的代码块中释放锁（Lock则需要自己释放） <br/>
 *              而ReentrantLock类实现了 Lock接口,它拥有与 synchronized
 *              相同的并发性和内存语义，但是添加了类似锁投票、定时锁等候和可中断锁等候的一些特性,支持不同语义的锁,
 *              此外，它还提供了在激烈争用情况下更佳的性能。换句话说 ，当许多线程都想访问共享资源时，JVM
 *              可以花更少的时候来调度线程，把更多时间用在执行线程上。
 * 
 *              虽然ReentrantLock比synchronized功能更强大,但是要使用好比较困难,
 *              除非当同步成为可伸缩性的瓶颈或者明确需要使用synchronized所不支持的高级特性,否则还是建议使用synchronized
 * 
 * @Date 2016年4月16日 下午9:02:54
 */
public class ReentrantLockDemo { // 使用ReentrantLock单一模拟生产者/消费者模型

	public static void main(String[] args) {
		Queue q = new Queue(10, 0);
		Runnable producer = new Producer(q);
		Runnable consumer = new Consumer(q);
		new Thread(producer).start();
		new Thread(consumer).start();
	}
}

class Queue { // 缓冲队列

	private int capacity; // 容量
	private int size; // 当前空间使用数量
	private Lock lock = new ReentrantLock(); // ReentrantLock实例
	private Condition fullCondition = lock.newCondition(); // 生产条件
	private Condition emptyCondition = lock.newCondition(); // 消费条件

	public Queue(int capacity, int size) {
		this.capacity = capacity;
		this.size = size;
	}

	public void add() { // 添加产品到缓冲队列
		lock.lock(); // 当前线程申请获得锁
		try {
			if (size == capacity) { // 若无剩余空间，阻塞消费者线程
				System.out.println("producer must to wait...");
				fullCondition.await();
			}
			size += 1;
			System.out.println(Thread.currentThread().getName()
					+ " add a product into queue,size is : " + size);
			emptyCondition.signal(); //每次生产完成后都尝试唤醒消费者,此方法开销较大并且多生产者/消费者情况下仍会发生死锁
			/** !!!!!!!!!!!!!!!!!!!!!!!!!!!以下这种写法会造成死锁!!!!!!!!!!!!!!!!!!!!!!!!! **/
			// if (size == 1) { // 若仅占一个空间则尝试唤醒消费者
			// System.out.println("notify the consumer thread...");
			// emptyCondition.signal();
			// }
			/** !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! **/
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock(); // 释放锁资源
		}
	}

	public void remove() { // 从缓冲队列中获取一个产品进行消费
		lock.lock();
		try {
			if (size == 0) {
				System.out.println("consumer must to wait...");
				emptyCondition.await();
			}
			size -= 1;
			System.out.println(Thread.currentThread().getName()
					+ " remove a product from queue,size is: " + size);
			fullCondition.signal();//每次消费完成后都尝试唤醒生产者,此方法开销较大并且多生产者/消费者情况下仍会发生死锁
			/** !!!!!!!!!!!!!!!!!!!!!!!!!!!以下这种写法会造成死锁!!!!!!!!!!!!!!!!!!!!!!!!! **/
//			if (size == capacity - 1) {
//				System.out.println("notify the producer thread...");
//				fullCondition.notify();
//			}
			/** !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! **/
			
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}

class Producer implements Runnable { // 生产者线程任务

	private Queue queue;

	public Producer(Queue queue) {
		this.queue = queue;
	}

	public void run() {
		for (int i = 0; i < 20; i++) {
			queue.add();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Consumer implements Runnable { // 消费者线程任务

	private Queue queue;

	public Consumer(Queue queue) {
		this.queue = queue;
	}

	public void run() {
		for (int i = 0; i < 20; i++) {
			queue.remove();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * API说明
 */
// //创建一个 ReentrantLock ，默认是“非公平锁”。
// ReentrantLock()
// //创建策略是fair的 ReentrantLock。fair为true表示是公平锁，fair为false表示是非公平锁。
// ReentrantLock(boolean fair)
// //查询当前线程保持此锁的次数。
// int getHoldCount()
// //返回目前拥有此锁的线程，如果此锁不被任何线程拥有，则返回 null。
// protected Thread getOwner()
// //返回一个 collection，它包含可能正等待获取此锁的线程。
// protected Collection<Thread> getQueuedThreads()
// //返回正等待获取此锁的线程估计数。
// int getQueueLength()
// //返回一个 collection，它包含可能正在等待与此锁相关给定条件的那些线程。
// protected Collection<Thread> getWaitingThreads(Condition condition)
// //返回等待与此锁相关的给定条件的线程估计数。
// int getWaitQueueLength(Condition condition)
// //查询给定线程是否正在等待获取此锁。
// boolean hasQueuedThread(Thread thread)
// //查询是否有些线程正在等待获取此锁。
// boolean hasQueuedThreads()
// //查询是否有些线程正在等待与此锁有关的给定条件。
// boolean hasWaiters(Condition condition)
// //如果是“公平锁”返回true，否则返回false。
// boolean isFair()
// //查询当前线程是否保持此锁。
// boolean isHeldByCurrentThread()
// //查询此锁是否由任意线程保持。
// boolean isLocked()
// //获取锁。
// void lock()
// //如果当前线程未被中断，则获取锁。
// void lockInterruptibly()
// //返回用来与此 Lock 实例一起使用的 Condition 实例。
// Condition newCondition()
// //仅在调用时锁未被另一个线程保持的情况下，才获取该锁。
// boolean tryLock()
// //如果锁在给定等待时间内没有被另一个线程保持，且当前线程未被中断，则获取该锁。
// boolean tryLock(long timeout, TimeUnit unit)
// //试图释放此锁。
// void unlock()
