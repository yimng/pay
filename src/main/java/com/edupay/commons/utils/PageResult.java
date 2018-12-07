package com.edupay.commons.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PageResult implements Serializable{
	/**
	 * 总记录数
	 */
    private int totalRecord;
    /**
     * 结果集
     */
    @SuppressWarnings("rawtypes")
	private List pageResult;
    
    private int pageNumPY=10;
    /**
     * 当前页
     */
    private int currentPage=1;
    /**
     * 每页记录数
     */
    private int pageSize = 10;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * 获取offset
     * @return
     * @return int
     * @author fengxiaoshuai
     * @date  2011-11-8-涓嬪崍01:32:26
     */
    public int getStartRecord(){
    	if((currentPage-1)*pageSize<0)
    	{
    		return 0;
    	}
        return (currentPage-1)*pageSize;
    }
    /**
     * 鑾峰緱缁撴潫璁板綍缂栧彿
     * @return
     * @return int
     * @author fengxiaoshuai
     * @date  2011-11-8-涓嬪崍01:32:48
     */
    public int getEndRecord(){
        return getStartRecord()+pageSize-1;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalRecord/getPageSize()*getPageSize() == totalRecord?totalRecord/getPageSize():totalRecord/getPageSize()+1;
    }

    @SuppressWarnings("unchecked")
	public <T> List<T> getPageResult() {
        return pageResult;
    }

    @SuppressWarnings("rawtypes")
	public void setPageResult(List pageResult) {
        this.pageResult = pageResult;
    }

    public boolean isFirst(){
        return getCurrentPage()<=1;
    }

    public boolean isLast(){
        return getCurrentPage()>=getTotalPage();
    }

    public List<Integer> getPageNums(){
        List<Integer> returnList = new ArrayList<>();
        int startNum = getCurrentPage()-pageNumPY<1?1:getCurrentPage()-pageNumPY;
        int endNum  = getCurrentPage()+pageNumPY>getTotalPage()?getTotalPage():getCurrentPage()+pageNumPY;
        for(int i=startNum;i<=endNum;i++){
            returnList.add(i);
        }
        return returnList;
    }
}
