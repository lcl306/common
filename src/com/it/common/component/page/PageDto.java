package com.it.common.component.page;

/**
 * @author liu
 * */
public class PageDto {
	
	protected Integer pageNumber;//页码
	protected Integer pageRow;//一页多少行
	
	
	protected Integer startRow;
	
	protected Integer endRow;
	
	protected Integer totalRow;
	
	protected Integer totalPage;
	
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	
	/**
	 * set pageRow pageNumber totalRow, then you can get page information
	 * */
	public void flush(){
		startFlush();
		endFlush(this.totalRow);
	}

	public void setPageNumber(Object pageNumber) {
		int pageN = 0;
		if(pageNumber!=null && pageNumber instanceof Number){
			pageN = ((Number)pageNumber).intValue();
		}else if(pageNumber!=null && pageNumber instanceof String){
			pageN = Integer.parseInt(((String)pageNumber).trim());
		}
		if(pageN<1) pageN = 1;
		this.pageNumber = pageN;
	}

	public Integer getPageRow() {
		return pageRow;
	}

	public void setPageRow(Object pageRow) {
		int pageR = 0;
		if(pageRow!=null && pageRow instanceof Number){
			pageR = ((Number)pageRow).intValue();
		}else if(pageRow!=null && pageRow instanceof String){
			String s = (String)pageRow;
			if(s.trim().length()>0)
				pageR = Integer.parseInt(s.trim());
		}
		this.pageRow = pageR;
	}

	public Integer getStartRow() {
		return startRow;
	}

	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
	}

	public Integer getEndRow() {
		return endRow;
	}

	public void setEndRow(Integer endRow) {
		this.endRow = endRow;
	}

	public Integer getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(Integer totalRow) {
		this.totalRow = totalRow;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
	void startFlush(){
		this.startRow = pageRow * (pageNumber - 1);
		this.endRow = startRow + pageRow;
	}
	
	void endFlush(Integer totalRow){
		this.totalRow = totalRow;
		if (startRow > totalRow)
			startRow = totalRow;
		if (endRow > totalRow)
			endRow = totalRow;
		if(pageRow!=0){
			if (totalRow % pageRow == 0)
				totalPage = totalRow / pageRow;
			else
				totalPage = totalRow / pageRow + 1;
		}else{
			totalPage = 0;
		}
	}
	
	

}
