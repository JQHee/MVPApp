package com.example.testmvpapp.sections.common.entity;

import java.io.Serializable;

/**
 * 检查版本实体类
 * 创建时间：2015-12-17
 * @author pjw
 *
 */
public class CheckUpdateEntity {

	private static final long serialVersionUID = 1L;
	
	
	private Data data;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data implements Serializable{
		private static final long serialVersionUID = 1L;
		private String Version;
		private int VersionCode;
		private String PublishDate;
		private String Desc; 
		
		public String getVersion() {
			return Version;
		}
		public void setVersion(String version) {
			Version = version;
		}
		public int getVersionCode() {
			return VersionCode;
		}
		public void setVersionCode(int versionCode) {
			VersionCode = versionCode;
		}
		public String getPublishDate() {
			return PublishDate;
		}
		public void setPublishDate(String publishDate) {
			PublishDate = publishDate;
		}
		public String getDesc() {
			return Desc;
		}
		public void setDesc(String desc) {
			Desc = desc;
		}
	}
}
