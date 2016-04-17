package com.yjh.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static void print(Object obj) {
		System.out.println(obj);
	}

	public static String showInts(int[] nums) {
		StringBuilder buffer = new StringBuilder();
		for (int n : nums)
			buffer.append(n);
		return buffer.toString();
	}

	public static String standardTitle(String title) {
		return title.replaceAll(" |[*|?|？|─|&|、|/|\\\\|:|\\-|\"|'|\\||《|》|“|”|‘|’|！|!|~|(|)|_|,]+","");
	}
	
	/**
	 * 是否包含，正则表达式表示的子字符串
	 * @param regex
	 * @param str
	 * @return
	 */
	public static boolean containsRegex(String regex, String str) {
		boolean flag = false;
		
		Matcher matcher = Pattern.compile(regex).matcher(str);
		if (matcher.find()) {
			flag = true;
		}
		
		return flag;
	}

	/**
	 * 转换成标准文件名
	 * @param name
	 * @return
	 */
	public static String standardFileName(String name) {
		return name == null ?
				null :
				name.replaceAll("\"|\\\\|\\*|\\|\\?|/|<|>|\\.", "").replaceAll(":", "：").replace("?", "");
	}

	/**
	 * 返回输入字符串第一个匹配正则表达式的子串
	 * @param str
	 * @param regex
	 * @param filter
	 * @return
	 */
	public static String findFirst(String str, String regex, StrFilter filter) {
		Pattern resultCountP = Pattern.compile(regex);
		Matcher resultCountM = resultCountP.matcher(str);
		if (resultCountM.find()) {
			if (filter == null)
				filter = DEFAULT_FILTER;
			return filter.filter(resultCountM);
		}
		return null;
	}
	public static final StrFilter DEFAULT_FILTER = new StrFilter() {
		@Override
		public String filter(Matcher matcher) {
			return matcher.group();
		}
	};
	public interface StrFilter {
		String filter(Matcher matcher);
	}
}
