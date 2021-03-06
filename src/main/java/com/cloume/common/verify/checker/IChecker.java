package com.cloume.common.verify.checker;

public interface IChecker {
	boolean check(Object value);
	
	final static public IChecker AlwaysTrueChecker = new IChecker() {
		@Override
		public boolean check(Object value) {
			return true;
		}
	};
	
	final static public IChecker AlwaysFalseChecker = new IChecker() {
		@Override
		public boolean check(Object value) {
			return false;
		}
	};
	
	final static public IChecker IsIntegerChecker = new IChecker() {
		@Override
		public boolean check(Object value) {
			return value instanceof Integer || value instanceof Long;
		}
	};
}
