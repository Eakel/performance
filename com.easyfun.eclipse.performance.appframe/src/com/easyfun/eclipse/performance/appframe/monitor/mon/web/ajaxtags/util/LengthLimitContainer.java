package com.easyfun.eclipse.performance.appframe.monitor.mon.web.ajaxtags.util;

import java.io.Serializable;

public class LengthLimitContainer implements Serializable {
	private Object[] key = null;
	private Object[] value = null;

	private int curPosition = 0;

	private int length = 10;

	public LengthLimitContainer(int length) {
		this.length = length;
		this.key = new Object[this.length];
		this.value = new Object[this.length];
		this.curPosition = (this.length - 1);
	}

	public LengthLimitContainer() {
		this.key = new Object[this.length];
		this.value = new Object[this.length];
		this.curPosition = (this.length - 1);
	}

	public void put(Object key, Object value) throws Exception {
		if ((key == null) || (value == null)) {
			throw new Exception("key或value不能为空");
		}

		if (this.curPosition >= 0) {
			this.key[this.curPosition] = key;
			this.value[this.curPosition] = value;
			this.curPosition -= 1;
		} else {
			int pos = 0;

			Object[] keyDest = new Object[this.length];
			System.arraycopy(this.key, 0, keyDest, 1, this.length - 1);
			keyDest[pos] = key;
			this.key = keyDest;

			Object[] valueDest = new Object[this.length];
			System.arraycopy(this.value, 0, valueDest, 1, this.length - 1);
			valueDest[pos] = value;
			this.value = valueDest;
		}
	}

	public Object get(Object key) {
		Object rtn = null;
		for (int i = 0; i < this.key.length; i++) {
			if ((this.key[i] != null) && (this.key[i].equals(key))) {
				rtn = this.value[i];
				break;
			}
		}

		return rtn;
	}

	public int getKeyPosition(Object key) {
		int rtn = -1;
		for (int i = 0; i < this.key.length; i++) {
			if ((this.key[i] != null) && (this.key[i].equals(key))) {
				rtn = i;
				break;
			}
		}

		return rtn;
	}

	public Object[] getKey() {
		return this.key;
	}

	public int size() {
		return this.length;
	}

	public static void main(String[] args) throws Exception {
		LengthLimitContainer container = new LengthLimitContainer(20);
		long start = System.currentTimeMillis();

		for (int k = 0; k < 2500; k++) {
			container.put(new TestYH(String.valueOf("key" + k)), new TestYH(String.valueOf(k)));
		}
		Object[] obj = container.getKey();
		for (int i = 0; i < 20; i++) {
			container.get(obj[i]);
		}

		System.out.println("耗时:" + (System.currentTimeMillis() - start) + ":ms");
		Thread.sleep(1000000000);
	}

	static class TestYH {
		String name = null;

		TestYH(String name) {
			this.name = name;
		}
	}
}