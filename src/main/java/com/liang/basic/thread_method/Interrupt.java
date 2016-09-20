package com.liang.basic.thread_method;

/**
 * @Description 线程中断Interrupt：中断是一种线程协作机制,java中断机制中通过一个boolean类型的变量标志是否接受到了中断信号.
 *              调用interrupt方法对线程进行中断时：<br/>
 *              1.如果被中断的线程处于阻塞状态，调用interrupt()进行中断时，首先会将中断状态设置为true
 *              ，然后由于线程是阻塞状态，此时会清除中断状态，即重新设置为false，最后抛出一个InterruptedException异常。
 *              2.如果线程被阻塞在一个Selector选择器中，那么通过interrupt()中断该线程时，线程中断标记会置为true，
 *              并从选择操作中返回。
 *              3.如果中断一个普通的就绪状态线程，那么只会将中断标记设置为true，已经终止的线程不会对中断产生任何响应。
 * 
 *              void interrupt()：中断一个线程 <br/>
 *              boolean interrupted(): 测试当前线程是否已经中断，并且清除中断状态 <br/>
 *              boolean isInterrupted()：测试当前线程是否已经中断，不改变中断状态。
 * 
 * @Date 2016年4月11日 下午10:56:57
 */
public class Interrupt {

	public static int product = 0; // 产品数

	public static void main(String[] args) {
		
		Target1 target1 = new Target1();
		Target2 target2 = new Target2();

		Thread t1 = new Thread(target1);
		Thread t2 = new Thread(target2);

		t1.start(); //开启线程1，此时没有中断信号无法生产产品
		t2.start(); //开启线程2
		while (true) { // 循环100次后生产1个产品,
			for (int i = 0; i < 100; i++) {
			}
			t1.interrupt(); //产生中断信号增加产品数
			if (product > 5) { //有可能t1线程还没生产到5就结束了线程，此时主线程结束不了
				t2.interrupt();
			}
			if(!t1.isAlive() && !t2.isAlive()) {
				System.out.println("结束主线程......");
				System.exit(0);
			}
		}

	}
}

/**
 * @Description 子线程任务：每当产生一个中断信号，将产品数+1
 * @Date 2016年4月12日 上午12:15:45
 */
class Target1 implements Runnable {
	public void run() {
		//运行状态线程被中断不会抛出异常
		while (Thread.interrupted()) { // 不断检测中断状态，如果有中断信号则将产品数+1,注意interrupted会清除中断状态
			System.out.println("现有产品数: " + Interrupt.product++);
		}
		System.out.println("生产线程结束......");
	}
}

/**
 * @Description 子线程任务：等到产品数大于5时进行消费，如果在阻塞状态下被中断，将抛出异常
 * @Date 2016年4月12日 上午12:21:00
 */
class Target2 implements Runnable {
	public void run() {
		while (Interrupt.product < 5) { // 如果商品数小于5则睡眠1秒
			try {
				Thread.sleep(3000); //当处于阻塞状态时被其他线程中断会抛出InterruptedException,执行while循环外的消费代码，然后结束该子线程
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.print("消费前产品数: " + Interrupt.product);
		Interrupt.product -= 5;
		System.out.println(", 消费后产品数: " + Interrupt.product); //消费的同时仍可以生产，所以前后可能相差小于5
		System.out.println("消费线程结束......");
	}
}

