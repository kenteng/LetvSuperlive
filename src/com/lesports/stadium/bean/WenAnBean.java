package com.lesports.stadium.bean;

/**
 * 
 * @ClassName: WenAnBean
 * 
 * @Description: 文案对象
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-6-25 下午5:09:40
 * 
 * 
 */
public class WenAnBean {
	private String id;
	private String duration;
	private String promptDocumentContent;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getPromptDocumentContent() {
		return promptDocumentContent;
	}

	public void setPromptDocumentContent(String promptDocumentContent) {
		this.promptDocumentContent = promptDocumentContent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
