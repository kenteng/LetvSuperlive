package com.lesports.stadium.utils;
import java.util.LinkedHashMap;
import java.util.Map;

import com.lesports.stadium.R;

/**
 * 
 * @ClassName:  FaceUtils   
 * @Description:表情 管理类   
 * @author: 王新年 
 * @date:   2015-12-28 下午6:20:46   
 *
 */
public class FaceUtils {
	/**
	 * 总共有多少页
	 */
	public static final int NUM_PAGE = 6;
	/**
	 * 每页20个表情,还有最后一个删除button
	 */
	public static int NUM = 20;
	/**
	 * map集合
	 */
	private static Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
	static{
		initFaceMap();
	}
	/**
	 * 添加表情
	 */
	private static void initFaceMap() {
		mFaceMap.put("[微笑]", R.drawable.expression_1_2x);
		mFaceMap.put("[憋嘴]", R.drawable.expression_2_2x);
		mFaceMap.put("[色]", R.drawable.expression_3_2x);
		mFaceMap.put("[发呆]", R.drawable.expression_4_2x);
		mFaceMap.put("[得意]", R.drawable.expression_5_2x);
		mFaceMap.put("[流泪]", R.drawable.expression_6_2x);
		mFaceMap.put("[害羞]", R.drawable.expression_7_2x);
		mFaceMap.put("[闭嘴]", R.drawable.expression_8_2x);
		mFaceMap.put("[睡]", R.drawable.expression_9_2x);
		mFaceMap.put("[大哭]", R.drawable.expression_10_2x);
		mFaceMap.put("[尴尬]", R.drawable.expression_11_2x);
		mFaceMap.put("[发怒]", R.drawable.expression_12_2x);
		mFaceMap.put("[调皮]", R.drawable.expression_13_2x);
		mFaceMap.put("[呲牙]", R.drawable.expression_14_2x);
		mFaceMap.put("[惊讶]", R.drawable.expression_15_2x);
		mFaceMap.put("[难过]", R.drawable.expression_16_2x);
		mFaceMap.put("[酷]", R.drawable.expression_17_2x);
		mFaceMap.put("[冷汗]", R.drawable.expression_18_2x);
		mFaceMap.put("[抓狂]", R.drawable.expression_19_2x);
		mFaceMap.put("[吐]", R.drawable.expression_20_2x);

		mFaceMap.put("[偷笑]", R.drawable.expression_21_2x);
		mFaceMap.put("[愉快]", R.drawable.expression_22_2x);
		mFaceMap.put("[白眼]", R.drawable.expression_23_2x);
		mFaceMap.put("[傲慢]", R.drawable.expression_24_2x);
		mFaceMap.put("[饥饿]", R.drawable.expression_25_2x);
		mFaceMap.put("[困]", R.drawable.expression_26_2x);
		mFaceMap.put("[惊恐]", R.drawable.expression_27_2x);
		mFaceMap.put("[流汗]", R.drawable.expression_28_2x);
		mFaceMap.put("[憨笑]", R.drawable.expression_29_2x);
		mFaceMap.put("[悠闲]", R.drawable.expression_30_2x);
		mFaceMap.put("[奋斗]", R.drawable.expression_31_2x);
		mFaceMap.put("[咒骂]", R.drawable.expression_32_2x);
		mFaceMap.put("[疑问]", R.drawable.expression_33_2x);
		mFaceMap.put("[嘘]", R.drawable.expression_34_2x);
		mFaceMap.put("[晕]", R.drawable.expression_35_2x);
		mFaceMap.put("[疯了]", R.drawable.expression_36_2x);
		mFaceMap.put("[衰]", R.drawable.expression_37_2x);
		mFaceMap.put("[骷髅]", R.drawable.expression_38_2x);
		mFaceMap.put("[敲打]", R.drawable.expression_39_2x);
		mFaceMap.put("[再见]", R.drawable.expression_40_2x);
		mFaceMap.put("[擦汗]", R.drawable.expression_41_2x);
		mFaceMap.put("[抠鼻]", R.drawable.expression_42_2x);
		mFaceMap.put("[鼓掌]", R.drawable.expression_43_2x);

		mFaceMap.put("[糗大了]", R.drawable.expression_44_2x);
		mFaceMap.put("[坏笑]", R.drawable.expression_45_2x);
		mFaceMap.put("[左哼哼]", R.drawable.expression_46_2x);
		mFaceMap.put("[右哼哼]", R.drawable.expression_47_2x);
		mFaceMap.put("[哈欠]", R.drawable.expression_48_2x);
		mFaceMap.put("[鄙视]", R.drawable.expression_49_2x);
		mFaceMap.put("[委屈]", R.drawable.expression_50_2x);
		mFaceMap.put("[快哭了]", R.drawable.expression_51_2x);
		mFaceMap.put("[阴险]", R.drawable.expression_52_2x);
		mFaceMap.put("[亲亲]", R.drawable.expression_53_2x);
		mFaceMap.put("[吓]", R.drawable.expression_54_2x);
		mFaceMap.put("[可怜]", R.drawable.expression_55_2x);
		mFaceMap.put("[菜刀]", R.drawable.expression_56_2x);
		mFaceMap.put("[西瓜]", R.drawable.expression_57_2x);
		mFaceMap.put("[啤酒]", R.drawable.expression_58_2x);
		mFaceMap.put("[篮球]", R.drawable.expression_59_2x);
		mFaceMap.put("[乒乓]", R.drawable.expression_60_2x);
		
		mFaceMap.put("[咖啡]", R.drawable.xpression_61_2x);
		mFaceMap.put("[饭]", R.drawable.xpression_62_2x);
		mFaceMap.put("[猪头]", R.drawable.xpression_63_2x);
		mFaceMap.put("[玫瑰]", R.drawable.xpression_64_2x);
		mFaceMap.put("[凋谢]", R.drawable.xpression_65_2x);
		mFaceMap.put("[示爱]", R.drawable.xpression_66_2x);
		mFaceMap.put("[爱心]", R.drawable.xpression_67_2x);
		mFaceMap.put("[心碎]", R.drawable.xpression_68_2x);
		mFaceMap.put("[蛋糕]", R.drawable.xpression_69_2x);
		mFaceMap.put("[闪电]", R.drawable.xpression_70_2x);
		
		mFaceMap.put("[炸弹]", R.drawable.xpression_71_2x);
		mFaceMap.put("[刀]", R.drawable.xpression_72_2x);
		mFaceMap.put("[足球]", R.drawable.xpression_73_2x);
		mFaceMap.put("[瓢虫]", R.drawable.xpression_74_2x);
		mFaceMap.put("[便便]", R.drawable.xpression_75_2x);
		mFaceMap.put("[月亮]", R.drawable.xpression_76_2x);
		mFaceMap.put("[太阳]", R.drawable.xpression_77_2x);
		mFaceMap.put("[礼物]", R.drawable.xpression_78_2x);
		mFaceMap.put("[拥抱]", R.drawable.xpression_79_2x);
		mFaceMap.put("[强]", R.drawable.xpression_80_2x);
		
		mFaceMap.put("[弱]", R.drawable.xpression_81_2x);
		mFaceMap.put("[握手]", R.drawable.xpression_82_2x);
		mFaceMap.put("[胜利]", R.drawable.xpression_83_2x);
		mFaceMap.put("[抱拳]", R.drawable.xpression_84_2x);
		mFaceMap.put("[勾引]", R.drawable.xpression_85_2x);
		mFaceMap.put("[拳头]", R.drawable.xpression_86_2x);
		mFaceMap.put("[差劲]", R.drawable.xpression_87_2x);
		mFaceMap.put("[爱你]", R.drawable.xpression_88_2x);
		mFaceMap.put("[NO]", R.drawable.xpression_89_2x);
		mFaceMap.put("[OK]", R.drawable.xpression_90_2x);
		
		mFaceMap.put("[爱情]", R.drawable.xpression_91_2x);
		mFaceMap.put("[飞吻]", R.drawable.xpression_92_2x);
		mFaceMap.put("[跳跳]", R.drawable.xpression_93_2x);
		mFaceMap.put("[发抖]", R.drawable.xpression_94_2x);
		mFaceMap.put("[怄火]", R.drawable.xpression_95_2x);
		mFaceMap.put("[转圈]", R.drawable.xpression_96_2x);
		mFaceMap.put("[磕头]", R.drawable.xpression_97_2x);
		mFaceMap.put("[回头]", R.drawable.xpression_98_2x);
		mFaceMap.put("[跳绳]", R.drawable.xpression_99_2x);
		mFaceMap.put("[挥手]", R.drawable.xpression_100_2x);
		
		mFaceMap.put("[激动]", R.drawable.xpression_101_2x);
		mFaceMap.put("[街舞]", R.drawable.xpression_102_2x);
		mFaceMap.put("[献吻]", R.drawable.xpression_103_2x);
		mFaceMap.put("[左太极]", R.drawable.xpression_104_2x);
		mFaceMap.put("[右太极]", R.drawable.xpression_105_2x);
		mFaceMap.put("[钱]", R.drawable.xpression_106_2x);
		mFaceMap.put("[美女]", R.drawable.xpression_107_2x);
	}
	
	/**
	 * 拿到表情的map集合
	 * 
	 * @return
	 */
	public static Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}

}
