package com.cloume.common.verify;

import com.cloume.common.verify.checker.IChecker;

public interface IRule {
	String getKey();
	IChecker getChecker();
	
	boolean isOptional();
}
