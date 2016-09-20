package com.liang.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @Introduce java.util.concurrent.atomic包是一个小工具包，支持在单个变量上解除锁的线程安全编程.
 *            原子类共可分为以下四类：<br/>
 *            1. 基本类型: AtomicInteger, AtomicLong, AtomicBoolean;
 * 
 *            2. 数组类型: AtomicIntegerArray, AtomicLongArray,
 *            AtomicReferenceArray;
 * 
 *            3. 引用类型: AtomicReference（存在ABA问题）, AtomicStampedRerence（加了时间戳解决ABA问题）,
 *            AtomicMarkableReference;
 * 
 *            4. 对象的属性修改类型: AtomicIntegerFieldUpdater, AtomicLongFieldUpdater,
 *            AtomicReferenceFieldUpdater
 * 
 * @Description AtomicIntegerArray API使用示例
 * @Date 2016年4月16日 下午12:52:56
 */
public class AtomicIntegerArrayDemo {

	public static void main(String[] args) {

		// 创建给定长度的新 AtomicIntegerArray,默认值都是0
		AtomicIntegerArray aia = new AtomicIntegerArray(10);

		// 创建与给定数组具有相同长度的新 AtomicIntegerArray，并从给定数组复制其所有元素。
		AtomicIntegerArray aia2 = new AtomicIntegerArray(new int[] { 1, 2, 3,
				4, 5 });

		print(aia);
		print(aia2);

		// 以原子方式将给定值添加到索引 i 的元素。
		int result = aia.addAndGet(3, 100);
		System.out.println("以原子方式将给定值添加到索引 3的元素 : " + result);

		// 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
		boolean flag = aia.compareAndSet(0, 0, 10);
		System.out.println("修改操作是否成功 : " + flag);

		// 以原子方式将索引 i 的元素减1。
		result = aia2.decrementAndGet(4);
		System.out.println("以原子方式将索引 4 的元素减1: " + result);

		// 获取位置 i 的当前值。
		result = aia2.get(1);
		System.out.println("获取下标为1的元素值:  " + result);

		// 以原子方式将给定值与索引 i 的元素相加。
		result = aia2.getAndAdd(1, 99);
		System.out.println("下标为1的元素为： " + result + ", 加上99后为： " + aia2.get(1));

		// 以原子方式将索引 i 的元素减 1。
		result = aia2.getAndDecrement(1);
		System.out.println("101减去1之前 : " + result + ", 减一之后： " + aia2.get(1));

		// 以原子方式将索引 i 的元素加 1。
		result = aia2.getAndIncrement(1);
		System.out.println("以原子方式将下标为1 的元素加 1,结果是: " + aia2.get(1));

		// 以原子方式将位置 i 的元素设置为给定值，并返回旧值。
		result = aia2.getAndSet(1, 1000);
		System.out.println("旧值是： " + result + "新值是: " + aia2.get(1));

		// 以原子方式将索引 i 的元素加1。
		aia2.incrementAndGet(0);

		print(aia2);

		// 最终将位置 i 的元素设置为给定值。
		aia2.lazySet(0, 100);
		aia2.set(3, 100);

		// 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
		aia2.weakCompareAndSet(2, 3, 10000);
		aia2.weakCompareAndSet(3, aia2.get(3), 100000);
		aia2.weakCompareAndSet(4, aia2.get(4), 1000000);
		print(aia2);
	}

	private static void print(AtomicIntegerArray aia) {
		System.out.println("当前数组元素为： " + aia);
	}
}
