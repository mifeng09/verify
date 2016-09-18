package com.cloume.common.lambda;

final public class Wrapper<T> {
	private T object;
	
	Wrapper(T object) {
		this.object = object;
	}
	
	public void set(T newValue) {
		object = newValue;
	}
	public T get() {
		return object;
	}
	
	static public <R> Wrapper<R> wrap(R object) {
		return new Wrapper<R>(object);
	}
}
