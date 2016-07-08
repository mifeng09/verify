package com.cloume.hsep.web.verify;

import com.cloume.hsep.web.verify.checker.IChecker;

public interface IRule {
	String getKey();
	IChecker getChecker();
	
	boolean isOptional();
}
