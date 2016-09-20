package com.liang.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @Description AtomicIntegerFieldUpdater可以对指定"类的 'volatile int'类型的成员"进行原子更新。
 * 
 *              注意：必须是volatile int类型变量而不能是volatile Integer类型的变量或Integer类型变量,
 *              否则会抛出IllegalArgumentException(
 *              "Must be integer type")异常或IllegalArgumentException: Must be
 *              volatile type异常。 它是基于反射原理实现的,指定的Field访问权限不能是private
 *              (不明白为什么限定了是int.class而不能是Integer.class)
 * 
 * 
 * @Date 2016年4月16日 下午2:30:51
 */
public class AtomicIntegerFieldUpdaterDemo {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		User user = new User();
		Class<User> c = (Class<User>) user.getClass();

		// 为对象创建并返回一个具有给定字段的更新器
		AtomicIntegerFieldUpdater<User> aifu = AtomicIntegerFieldUpdater
				.newUpdater(c, "sum"); // 传递sum是在对应类中的变量名,此时User类的sum成员变量可以变成原子性操作

		// 以原子方式将给定值添加到此更新器管理的给定对象的字段的当前值。
		aifu.addAndGet(user, 1);

		// 如果当前值 == 预期值，则以原子方式将此更新器所管理的给定对象的字段设置为给定的更新值——10
		aifu.compareAndSet(user, 1, 10);

		// 以原子方式将此更新器管理的给定对象字段当前值减 1,此时变为9
		aifu.decrementAndGet(user);

		// 获取此更新器管理的在给定对象的字段中保持的当前值。
		System.out.println(aifu.get(user));

		// 以原子方式将给定值添加到此更新器管理的给定对象的字段的当前值,新值为20
		aifu.getAndAdd(user, 11);

		// 以原子方式将此更新器管理的给定对象字段当前值减 1,结果为19
		aifu.getAndDecrement(user);

		// 以原子方式将此更新器管理的给定对象字段的当前值加 1,结果为20
		aifu.getAndIncrement(user);

		// 将此更新器管理的给定对象的字段以原子方式设置为给定值，并返回旧值。
		aifu.getAndSet(user, 100);

		// 以原子方式将此更新器管理的给定对象字段当前值加 1,结果为101
		aifu.incrementAndGet(user);

		// 最后将此更新器管理的给定对象的字段设置为给定更新值,结果为1000
		aifu.lazySet(user, 1000);

		// 将此更新器管理的给定对象的字段设置为给定更新值,结果为10000
		aifu.set(user, 10000);

		// 如果当前值 == 预期值，则以原子方式将此更新器所管理的给定对象的字段设置为给定的更新值。
		aifu.weakCompareAndSet(user, 10000, 20000);

		System.out.println(aifu.get(user));
	}
}

class User {

	volatile int sum = 0; // 不能是私有成员变量

	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

}
