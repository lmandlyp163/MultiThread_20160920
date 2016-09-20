package com.liang.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 基本类型原子类API使用示例：内部主要依靠volatile和compareAndSet保证原子性
 * 
 *              CAS(Compare and Swap) 比较并交换操作是一个三元操作:
 *              目标地址的值T(arget)，期望值E(xpected)，实际值R(eal) <br/>
 *              1. 只有当目标值T == 期望值E时，才会把目标值T设置为实际值R，否则不改变目标值 <br/>
 *              2. 不管目标值是否改变，都返回之前的目标值T
 * 
 * @Date 2016年4月13日 上午11:07:22
 */
public class AtomicIntegerDemo {
	public static void main(String[] args) {
		AtomicInteger ai = new AtomicInteger(); // 不指定初始值,默认是0
		AtomicInteger ai2 = new AtomicInteger(100); // 指定初始值
		print(ai);
		print(ai2);

		ai.set(50); // 设置新值
		ai.addAndGet(2); // 类似于++i,先增加指定值再返回结果,内部就是调用了下面的compareAndSet方法
		print(ai);
		System.out.println(ai.compareAndSet(52, 30));
		print(ai);
		/**
		 * 使用weakCompareAndSet方法只能保证对当前变量的
		 * 修改对于其他线程是可见的，但不能保证在调用weakCompareAndSet方法之前对其他变量的 修改也是对于其他线程可见的
		 * ,简单的说weakCompareAndSet提供更好的性能
		 * ，但是提供的语义更弱，适用范围比较窄，只适合于用来对值不依赖于其他变量的变量进行修改,可能发生意外失败
		 */
		System.out.println(ai.weakCompareAndSet(30, 100));
		print(ai);
		ai.decrementAndGet(); // 相当于--i,先减1再返回结果
		print(ai);
		int initvalue = ai.getAndAdd(3); // 先返回原值,然后原值+3
		System.out.println("begin add the value is : " + initvalue);
		System.out.println("after add 3 the value is : " + ai);
		/**
		 * lazySet()与set()一样能设置新值，不同之处在于，set是立刻修改原值，保证其他线程立即可见新值，
		 * 而使用lazySet不会立刻修改原值但最终会修改原值，并且不限制指令不能进行重排序
		 */
		ai.lazySet(40);
		print(ai);
	}

	private static void print(AtomicInteger ai) {
		System.out.println("the current value is : " + ai);
	}
}
