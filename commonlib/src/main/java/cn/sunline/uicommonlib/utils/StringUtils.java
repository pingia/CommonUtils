
package cn.sunline.uicommonlib.utils;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {

	/**
	 * 将list中的元素 的字符串表现形式 拼接起来
	 * @param list
	 * @return
	 */
		public static String joinListElementsToString(List list){
			StringBuffer sb = new StringBuffer();
			for (Object o : list){
				sb.append(o.toString());
			}

			return sb.toString();
		}
		
		public static boolean matchRegex(CharSequence input, String regex){
			Pattern pattern = Pattern.compile(regex);
			
			return pattern.matcher(input).matches();
		}

	public static boolean findMatchRegex(CharSequence input, String regex){
		Pattern pattern = Pattern.compile(regex);

		return pattern.matcher(input).find();
	}

	public static int toInt(String str){
		try {
			return Integer.parseInt(str);
		}
		catch (Exception exp){
			return 0;
		}
	}

	public static double toDouble(String str){
		try {
			return Double.parseDouble(str);
		}
		catch (Exception exp){
			return 0d;
		}
	}

	public static double toDouble(String str, double def) {
		double ret = def;
		try{
			ret = Double.parseDouble(str);
		}catch(Exception ex) {
			ret = def;
		}
		return ret;
	}

	public static float toFloat(String str){
		try {
			return Float.parseFloat(str);
		}
		catch (Exception exp){
			return 0f;
		}
	}


	public static long toLong(String str, long def) {
		long ret = def;
		if(str != null) {
			try{
				if(str.toUpperCase().startsWith("0X")) {
					str = str.substring(2);
					ret = Long.parseLong(str, 10);
				} else {
					int index = str.indexOf(".");
					if(index != -1) {
						str = str.substring(0, index);
					}
					ret = Long.parseLong(str);
				}
			}catch(Exception ex){
				ret = def;
			}
		}
		return ret;
	}

	public static boolean toBoolean(String str, boolean def) {
		boolean ret = def;
		if(str != null) {
			try{
				ret = Boolean.parseBoolean(str);
			}catch(Exception ex) {
				ret = def;
			}
		}
		return ret;
	}

	/**
	 * 字符串是否全为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 字符串是否全为字母
	 * @param str
	 * @return
	 */
	public static boolean isLetter(String str){
		Pattern pattern = Pattern.compile("[A-Za-z]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isBalance(String balance) {
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		Matcher isNum = pattern.matcher(balance);
		return isNum.matches();
	}

	/**
	 * 判断字符是否为中文
	 *
	 * @param c
	 * @return
     */
	public static boolean isChinese(char c){
		if((c >= 0x4e00) && (c <= 0x9fbb)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 计算一个字符串中中文字符的个数
	 *
	 * @param str
	 * @return	中文字符个数
     */
	public static int getChineseCharsCount(String str){
		int count = 0;

		if(!TextUtils.isEmpty(str)){
			for (char c: str.toCharArray()){
				if(isChinese(c)){
					count ++;
				}
			}
		}
		return count;
	}

	/**
	 * 获取字符串所占字符个数
	 *
	 * @param str	字符串
	 * @return		字符个数，字符中的中文按2个字符来计算，其他字符按1个字符来计算
     */
	public static int getStringCharLength(String str){

		if(TextUtils.isEmpty(str)){
			return 0 ;
		}

		int charsCount = 0;
		int len = str.length();
		for (int i =0; i <len;i++){
			char c = str.charAt(i);

			if(isChinese(c)){
				charsCount += 2;	//中文字符占两个字符
			}else{
				charsCount += 1;
			}
		}

		return charsCount;

	}

	/**
	 * 检测字符串是否匹配Luhn算法：可用于检测银行卡卡号
	 * @param cardNo
	 * @return
	 */
	public static boolean matchLuhn(String cardNo) {
		int[] cardNoArr = new int[cardNo.length()];
		for (int i=0; i<cardNo.length(); i++) {
			cardNoArr[i] = Integer.valueOf(String.valueOf(cardNo.charAt(i)));
		}
		for(int i=cardNoArr.length-2;i>=0;i-=2) {
			cardNoArr[i] <<= 1;
			cardNoArr[i] = cardNoArr[i]/10 + cardNoArr[i]%10;
		}
		int sum = 0;
		for(int i=0;i<cardNoArr.length;i++) {
			sum += cardNoArr[i];
		}
		return sum % 10 == 0;
	}

	/**
	 * 手机号码的掩码显示，掩码规则：从第四个数字到第七个数字用星号替换，12345678900掩码后，显示为123****8900
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static String getMaskPhoneNumberString(String phoneNumber){
		int startIndex = 3;
		if(TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < startIndex) {
			return phoneNumber;
		}else{
			int endIndex = Math.min(phoneNumber.length(), 7);
			int rangeLen = endIndex - startIndex;

			StringBuilder replaceStarSb = new StringBuilder();
			for (int i =0; i< rangeLen; i++) {
				replaceStarSb.append("*");
			}

			StringBuilder sb = new StringBuilder(phoneNumber);
			sb.replace(startIndex, endIndex, replaceStarSb.toString());
			return sb.toString();
		}
	}

	public static String getSlashSplitFirstString(String str){
		if(!TextUtils.isEmpty(str) && str.contains("/")){
			return str.split("/")[0];
		}

		return "";
	}

	public static String getSlashSplitSecondString(String str){
		if(!TextUtils.isEmpty(str) && str.contains("/")){
			return str.split("/")[1];
		}

		return "";
	}

	public static String getContractString(List<String> stringList,String contractString){
		if(null == stringList) return "";
		StringBuilder sb  = new StringBuilder();
		for (String s : stringList){
			sb.append(s);
			sb.append(contractString);
		}

		int len = sb.length();
		if(len > 0){
			sb.deleteCharAt(len-1);
		}
		return sb.toString();
	}

	public static String getContractString(Map<String,String> paramMap,String contractString){

		StringBuilder sb = new StringBuilder();
		for(String name : paramMap.keySet()){

			sb.append(contractString);

			sb.append(name);

			sb.append('=');

			String value = paramMap.get(name);

			sb.append(value);

		}

		if(sb.length() > 0) {
			sb.deleteCharAt(0);	//删掉第一个contractString.
		}
		return sb.toString();

	}

}
