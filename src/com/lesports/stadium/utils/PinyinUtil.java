package com.lesports.stadium.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
/**
 * 手机联系人解析Utility
 * @author 盛之刚
 *
 */
public class PinyinUtil {
	
	/**
	 * 根据汉字获取对应的拼音[汉字的大写首字母]
	 * @param str
	 * @return
	 */
	public static String getPinyin(String str) throws Exception {
		// 设置输出配置
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		// 设置大写
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		// 设置不需要音调
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		StringBuilder sb = new StringBuilder();

		// 获取字符数组
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			// 如果是空格, 跳过当前的循环
			if (Character.isWhitespace(c)||!checkChar(c)) {
				continue;
			}

			if (c > 128 || c < -127) {
				// 可能是汉字
				try {
					// 根据字符获取对应的拼音. 白 -> BAI , 单 -> DAN , SHAN
					String s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
					sb.append(s);
				} catch (Exception e) {
					sb.append(String.valueOf(c));
				}
			} else {
				// *&$^*@654654LHKHJ
				// 不需要转换, 直接添加
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 检查char是否是中文符号
	 * 
	 * @param content
	 * @return
	 */
	public static boolean checkChar(char ch) {
		boolean isChinese = true;
		int[] noChar = {65292,12290,65311,65281, 	// ， 。 ？ ！
						126,12289,65306,			// ~ 、 ： #(排除了英文中的#)
						65307,37,42,8212,			// ； % * ——
						8230,38,183,36,				//…… & · $
						65288,65289,8217,8216,		//（     ）‘ ’
						8220,8221,91,93,			// “ ” [ ]
						123,125,12304,12305,		// { } 【  】
						65509,12298,12299,124};		// ￥《     》|
		for(int j=0;j<noChar.length;j++){
			if(ch==noChar[j]){
				isChinese = false;
				break;
			}
		}
		return isChinese;
	}
}
