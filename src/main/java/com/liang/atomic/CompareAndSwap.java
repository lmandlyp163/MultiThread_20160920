package com.liang.atomic;

/**
 * @Description CAS原理的简单说明
 * 
 *              CAS(Compare and Swap) 比较并交换操作是一个三元操作:
 *              目标地址的值T(arget)，期望值E(xpected)，实际值R(eal) <br/>
 *              1. 只有当目标值T == 期望值E时，才会把目标值T设置为实际值R，否则不改变目标值 <br/>
 *              2. 不管目标值是否改变，都返回之前的目标值T
 * 
 *              虽然CAS是原子性操作，但是CAS实际上是存在"ABA"问题的。
 *              由于CAS的比较是比较值,如果目的地址的值为A,期望值也为A那么就能成功设置新值
 *              ，但是这时候目标A可能不是原来的那个A了，它可能是A变成了B，再变成了A。所以叫ABA问题。
 *              解决ABA问题可以使用时间戳，这也是乐观锁的一般解决方法。
 * 
 * @Date 2016年4月16日 下午1:49:23
 */
public class CompareAndSwap {

	// ABA问题
	public static void main(String[] args) {
		// 基于同一个CAS对象开启两个线程
		CAS cas = new CAS();

		// 线程1获取到目的地址值后先不执行CAS操作，而是等线程2修改旧值:0——>100——>0
		cas.setExpected(cas.getTarget());
		cas.setNewValue(50);
		Thread t1 = new Thread(cas);

		cas.setExpected(cas.getTarget());
		cas.setNewValue(100);
		Thread t2 = new Thread(cas);

		t2.start();
		t1.start();
	}

}

/**
 * @Description 简单模拟CAS操作存在的ABA问题
 */
class CAS implements Runnable {

	private int target = 0; // 保存旧值
	private int expected; // 期望值
	private int newValue; // 新值

	public synchronized int compareAndSet(int expected, int newValue) {
		String thread = Thread.currentThread().getName();
		System.out.println(thread + " entry the compareAndSet Method...");
		int oldValue = target;
		if (target == expected) { // 比较
			System.out.println("can swap");
			target = newValue;
		}
		if (thread.equals("Thread-1")) { // 如果是线程2则将值设置会交换前的值
			target = oldValue;
		}
		return oldValue; // 返回旧值
	}

	public void run() {

		compareAndSet(expected, newValue);
	}

	public int getExpected() {
		return expected;
	}

	public void setExpected(int expected) {
		this.expected = expected;
	}

	public int getNewValue() {
		return newValue;
	}

	public void setNewValue(int newValue) {
		this.newValue = newValue;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

}
