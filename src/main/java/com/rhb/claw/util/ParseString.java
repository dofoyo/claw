package com.rhb.claw.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseString {

	public static String subString(String source, String regexp){
		List<String> list = subStrings(source,regexp);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public static List<String> subStrings(String source, String regexp){
		List<String> list = new ArrayList<String>();

		if(source == null || regexp==null || "".equals(regexp.trim())){
			return list;
		}
		String findRegexp = regexp.replace("|", ".*?");
		
		
		boolean replace = regexp.indexOf("|")==-1 ? false : true;
		
		Pattern pt = Pattern.compile(findRegexp);
		Matcher mt = pt.matcher(source);
		
		//System.out.println(mt.group());
		
		while (mt.find()){
			if(replace){
				list.add(mt.group().replaceAll(regexp, ""));				
			}else{
				list.add(mt.group());
			}
		}
		return list;
	}
	
	public static String subString(String source, String begin, String end){
		if(source==null || begin==null || end==null){
			return "";
		}
		int i = source.indexOf(begin);
		int j = source.indexOf(end);
		
		return source.substring(i+begin.length(), j);
		
	}
	
	public static Double toDouble(String str){
		Double d = 0.00;
		try{
			if(str!=null && !str.trim().isEmpty()){
				d = Double.valueOf(str);
			}
		}catch(Exception e){}
		return d;
	}
	
	public static Integer toInteger(String str){
		Integer i = 0;
		try{
			if(str!=null && !str.trim().isEmpty()){
				i = Integer.valueOf(str);
			}
		}catch(Exception e){}		
		
		return i;
	}
	
	public static boolean isDigital(String str){
		String regexp = "^\\d+$";
		
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(str);
		return m.matches();
		
	}
	
	public static LocalDate toLocalDate(String str){
		LocalDate localDate = null;
		try{
			localDate = LocalDate.parse(str);
		}catch(Exception e){}
		return localDate;
	}
	
	public static BigDecimal toBigDecimal(String str){
		BigDecimal bigDecimal = BigDecimal.ZERO;
		try{
			bigDecimal = new BigDecimal(str);
		}catch(Exception e){}
		return bigDecimal;		
	}

}
