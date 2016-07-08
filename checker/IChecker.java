package com.cloume.hsep.web.verify.checker;

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
}
