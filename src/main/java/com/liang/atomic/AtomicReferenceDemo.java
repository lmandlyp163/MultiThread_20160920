package com.liang.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description AtomicReference 是对对象进行原子操作的原子类,以下是相关API使用示例
 * @Date 2016年4月16日 下午1:21:37
 */
public class AtomicReferenceDemo {

	public static void main(String[] args) {
		
		// 使用 null 初始值创建新的 AtomicReference。
		AtomicReference<String> ar = new AtomicReference<String>();
		
		/**
		 * 使用给定的初始值创建新的 AtomicReference
		 * 这里使用字符串常量，那么在对比的时候是使用==比较地址，因此如果使用新建的String对象，即使该String对象值为abc也是不等的
		 */
		AtomicReference<String> ar2 = new AtomicReference<String>("abc"); 

		// 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
		String str = new String("abc");
		boolean flag = ar2.compareAndSet(str, "cdf");
		System.out.println("不同String对象,compare 结果为： " + flag + ", ar2 内容为: " + ar2);
		//使用常量去比较
		str = "abc";
		flag = ar2.compareAndSet(str, "cdf");
		System.out.println("使用字符串常量compare 结果为： " + flag + ", ar2 内容为: " + ar2);
		
		// 获取当前值。
		System.out.println(ar.get());
		
		// 以原子方式设置为给定值，并返回旧值。
		String old = ar2.getAndSet("xyz");
		System.out.println("old value is " + old + ", new value is " + ar2.get());
		
		// 最终设置为给定值。
		ar.lazySet("lazySet");
		
		// 设置为给定值。
		ar2.set("set");
		
		// 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
		ar2.weakCompareAndSet("set", "weakCompareAndSet");
		System.out.println(ar2);
	}
}
