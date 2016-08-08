/**
 * 
 */
package com.lesports.stadium.bean;

/**
 * ***************************************************************
 * 
 * @Desc : 现场活动歌曲列表实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 *     "activityId": 1,
        "embraceCount": 50,
        "flowerCount": 421,
        "id": 1,
        "kissCount": 32,
        "loveCount": 30,
        "musicName": "红日",
        "musicsort": 1,
        "singer": "李克勤"
        
        
          "activityId": 1,
        "embraceCount": 50,
        "flowerCount": 421,
        "id": 1,
        "kissCount": 32,
        "loveCount": 30,
        "lyric":
           "musicName": "红日",
        "musicsort": 1,
        "singer": "李克勤"

 * 
 * ***************************************************************
 */

public class LyricsListBean {
	/**
	 * 当前歌曲的歌词
	 */
	private String lyric;
	/**
	 * 活动id
	 */
	private String activityId;
	/**
	 * 拥抱数量
	 */
	private String embraceCount;
	/**
	 * 鲜花数量
	 */
	private String flowerCount;
	/**
	 * 歌曲id
	 */
	private String id;
	/**
	 * 接吻数量
	 */
	private String kissCount;
	/**
	 * 爱心数量
	 */
	private String loveCount;
	/**
	 * 歌曲名称
	 */
	private String musicName;
	/**
	 * 演唱者
	 */
	private String singer;
	
	/**
	 * musicsort
	 */
	private String musicsort;
	public String getLyric() {
		return lyric;
	}
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}
	public String getMusicsort() {
		return musicsort;
	}
	public void setMusicsort(String musicsort) {
		this.musicsort = musicsort;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getEmbraceCount() {
		return embraceCount;
	}
	public void setEmbraceCount(String embraceCount) {
		this.embraceCount = embraceCount;
	}
	public String getFlowerCount() {
		return flowerCount;
	}
	public void setFlowerCount(String flowerCount) {
		this.flowerCount = flowerCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKissCount() {
		return kissCount;
	}
	public void setKissCount(String kissCount) {
		this.kissCount = kissCount;
	}
	public String getLoveCount() {
		return loveCount;
	}
	public void setLoveCount(String loveCount) {
		this.loveCount = loveCount;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	
	
/**
 * 

 */

}
