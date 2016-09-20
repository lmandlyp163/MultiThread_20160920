package com.liang.basic.wait_notify;

/**
 * @Description 利用wait/notify解决单一生产者/消费者问题<br/>
 *              生产者与消费者线程可以并发运行,但不直接通讯,而是通过一个缓冲队列进行通信,
 *              当缓冲队列满时生产者线程阻塞;当缓冲队列空时消费者线程阻塞
 * @Date 2016年4月15日 上午10:52:20
 */
public class ProducerAndConsumer {

	public static void main(String[] args) {
		Runnable r1 = new Producer();
		Runnable r2 = new Consumer();
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();
		t2.start();
	}

}

/**
 * @Description 缓冲队列
 * @Date 2016年4月15日 上午11:06:32
 */
class BufferQueue {

	private int capacity = 10; // 缓冲区容量
	private int currentSize = 0; // 当前容量
	private static BufferQueue queue = new BufferQueue(); // 保证只有一个缓冲区

	private BufferQueue() {

	}

	public static BufferQueue getInstance() {
		return queue;
	}

	/**
	 * 生产者将生产完成的产品，添加进缓冲队列中
	 * 
	 * @param count
	 */
	public synchronized void produce(int count) {
		if (currentSize == capacity) { // 无剩余空间则将生产者线程阻塞，放弃同步锁,等待消费者线程唤醒
			try {
				System.out.println("无剩余空间,生产者等待...");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("生产者新增1个产品,当前产品数: " + ++currentSize);
		if (currentSize == 1) { // 当前容量为1时消费者线程才有可能处于阻塞状态,当然也有可能未被阻塞
			System.out.println("唤醒消费者...");
			notify();
		}
	}

	/**
	 * 消费者从缓冲队列中取出指定数量的产品进行消费
	 * 
	 * @param count
	 */
	public synchronized void consume(int count) {
		if (currentSize == 0) { // 如果当前缓冲区为空，阻塞消费者线程
			try {
				System.out.println("\t\t\t无产品,消费者等待...");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("\t\t\t消费者消费了1件产品，剩余产品数: " + --currentSize);
		if (currentSize == capacity - 1) { // 当消费了一件产品后剩余一个空间,说明生产者线程可能处于阻塞状态，需要唤醒
			System.out.println("\t\t\t唤醒生产者...");
			notify();
		}
	}

	public int getCapacity() {
		return capacity;
	}

	public int getCurrentSize() {
		return currentSize;
	}

}

class Producer implements Runnable {

	private BufferQueue queue = BufferQueue.getInstance();

	public void run() {
		while (true) {
			System.out.println("生产者：装配零件...");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("生产者：装配完成,等待放入仓库...");
			queue.produce(1);
		}
	}
}

class Consumer implements Runnable {

	private BufferQueue queue = BufferQueue.getInstance();

	public void run() {
		while (true) {
			System.out.println("\t\t\t消费者：取出产品...");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\t\t\t消费者：等待进行消费...");
			queue.consume(1);
		}
	}
}