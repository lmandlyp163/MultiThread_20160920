package com.liang.basic.thread_method;

/**
 * @Description 守护线程与线程优先级
 * 
 *              java中包含两类线程：用户线程User Thread和守护线程Daemon Thread,<br/>
 *              守护线程的作用是为用户线程的运行提供一定的服务，只要JVM中还有一个用户线程未结束, 守护线程就全部工作,
 *              当JVM中剩余的线程全都是守护线程时，JVM结束退出。最常见的守护线程就是GC线程。
 * 
 *              创建守护线程可以在start一个线程之前设置线程对象属于守护线程, 或者在守护线程下新建任意新线程，都会是守护线程.
 *              守护线程不能用来执行读写或者计算逻辑，因为当所有用户线程结束后守护线程和JVM也就退出了
 * 
 * 				
 */
public class DaemonThread{

	public static void main(String[] args) {
		Target target = new Target();
		Thread t = new Thread(target);
		t.setDaemon(true); //必须在start之前设置为守护线程,start之后设置该属性会抛出异常
		t.start();
		for(int i = 0; i < 5; i++) {
			System.out.println("当前count = " + target.count);
		} //主线程结束后所有用户线程已结束,不需等待守护线程结束,因此退出虚拟机
	}
}

class Target implements Runnable {
	
	public int count = 0;
	
	public void run() {
		while(true) {
			count++;
		}
	}
}