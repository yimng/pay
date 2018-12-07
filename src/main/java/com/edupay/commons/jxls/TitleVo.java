/**
 *
 * @Title UserVo.java
 * @author wanglu
 * @date 2018年4月24日
 */
package com.edupay.commons.jxls;

import java.io.Serializable;
import java.util.List;

/**  
 * excel动态表头数据封装
 * Title UserVo 
 * @author wanglu  
 * @date 2018年4月24日  
 */
public class TitleVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String t1;
	List<Object> t2;
	public String getT1() {
		return t1;
	}
	public void setT1(String t1) {
		this.t1 = t1;
	}
	public List<Object> getT2() {
		return t2;
	}
	public void setT2(List<Object> t2) {
		this.t2 = t2;
	}
	
}
