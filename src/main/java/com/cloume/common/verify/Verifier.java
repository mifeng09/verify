package com.cloume.common.verify;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cloume.common.verify.checker.IChecker;
import com.cloume.common.verify.checker.RegexChecker;

public class Verifier {
	
	class SimpleRule implements IRule {
		private String key;
		private IChecker checker;
		private boolean optional;
		
		SimpleRule(String key, IChecker checker) {
			this(key, false, checker);
		}
		
		SimpleRule(String key, boolean optional, IChecker checker) {
			this.key = key;
			this.optional = optional;
			this.checker = checker;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public IChecker getChecker() {
			return checker == null ? IChecker.AlwaysTrueChecker : checker;
		}

		@Override
		public boolean isOptional() {
			return optional;
		}
	}
	
	private List<IRule> rules;
	
	protected List<IRule> getRules() {
		return rules == null ? rules = new ArrayList<IRule>() : rules;
	}
	
	public Verifier rule(String key, IChecker checker) {
		return rule(key, false, checker);
	}
	
	public Verifier rule(String key, String pattern) {
		return rule(key, false, pattern);
	}
	
	public Verifier rule(String key) {
		return rule(key, false, IChecker.AlwaysTrueChecker);
	}
	
	public Verifier rule(String key, boolean optional, IChecker checker) {
		getRules().add(new SimpleRule(key, optional, checker));
		return this;
	}
	
	public Verifier rule(String key, boolean optional, String pattern) {
		return rule(key, optional, new RegexChecker(pattern));
	}
	
	/**
	 * 校验结束时通过回调(@callback)告知校验结果
	 * @param callback
	 * @return
	 */
	public Verifier result(IResultCallback callback) {
		this.resultCallback = callback;
		return this;
	}
	
	private IResultCallback resultCallback;
	
	public boolean verify(Map<String, Object> target){
		if(target == null){
			throw new NullPointerException();
		}
		
		boolean result = true;
		String reason = "";
		
		Iterator<IRule> iterator = getRules().iterator();
		while(iterator.hasNext()){
			IRule rule = iterator.next();
			Object value = target.get(rule.getKey());
			if(value == null && !rule.isOptional()){
				result = false;
				reason = String.format("key [%s] not optional", rule.getKey());
				break;
			}
			
			if(value != null){
				if(rule.getChecker() != null){
					if(false == rule.getChecker().check(value)){
						result = false;
						reason = String.format("key [%s] failed on checker %s with value [%s]", rule.getKey(), rule.getChecker(), value);
						break;
					}
				}
			}
		}
		
		if(resultCallback != null){
			resultCallback.result(result, reason);
		}
		
		return result;
	}
	
	public boolean verify(Object target){
		if(target instanceof Map) {
			return verify((Map<String, Object>) target);
		}

		List<Field> fields = org.apache.commons.lang3.reflect.FieldUtils.getAllFieldsList(target.getClass());
		Map<String, Object> map = new HashMap<String, Object>();
		
		fields.forEach(e -> {
				try {
					e.setAccessible(true);
					map.put(e.getName(), e.get(target));
				} catch (Exception ex) {
					System.err.println("failed to get value from target, " + ex.getMessage());
				}
		});
		
		return verify(map);
	}
}
