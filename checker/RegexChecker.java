package com.cloume.hsep.web.verify.checker;

public class RegexChecker implements IChecker {
	
	private String regex;
	
	public RegexChecker(String regex) {
		this.regex = regex;
	}
	
	@Override
	public boolean check(Object value) {
		String try2Match = String.valueOf(value);
		return try2Match.matches(regex);
	}

}
