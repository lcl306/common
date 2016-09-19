package com.it.common.component.page;

import org.hibernate.Query;

import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class PageUtil {
	
	static ThreadLocal<PageDto> pageDtos = new ThreadLocal<PageDto>();
	static int DEFAULT_PAGENUMER=1;
	public static int DEFAULT_ROW;
	
	static{
		String pageRows = "20";
		try {
			pageRows = GlobalName.appResources.getString("pageRows");
		} catch (Exception e) {}
		if(pageRows!=null && pageRows.trim().length()>0){
			try {
				DEFAULT_ROW = Integer.parseInt(pageRows);
			} catch (Exception e) {
				LogPrint.error(e.getMessage());
			}
		}
	}
	
	/**
	 * prepare page information
	 * @param pageNumber : current page number
	 * @param pageRow : max rows in one page
	 * */
	public static PageDto prepare(Object pageNumber, Object pageRow){
		PageDto pdto = new PageDto();
		pdto.setPageNumber(pageNumber);
		pdto.setPageRow(pageRow);
		pdto.startFlush();
		pageDtos.set(pdto);
		return pdto;
	}
	
	public static PageDto prepare(){
		PageDto pdto = new PageDto();
		pdto.setPageNumber(DEFAULT_PAGENUMER);
		pdto.setPageRow(DEFAULT_ROW);
		pdto.startFlush();
		pageDtos.set(pdto);
		return pdto;
	}
	
	public static void setCurrentPageNumber(Object currentPage){
		PageDto dto=pageDtos.get();
		if(dto==null) dto=prepare(currentPage,DEFAULT_ROW);
		dto.setPageNumber(currentPage);
	}
	public static void setPageSize(Object pageSize){
		PageDto dto=pageDtos.get();
		if(dto==null) dto=prepare(DEFAULT_PAGENUMER,pageSize);
		dto.setPageRow(pageSize);
	}
	
	/**
	 * get page information
	 * */
	public static PageDto get(){
		return pageDtos.get();
	}
	
	public static void remove(){
		pageDtos.remove();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	static String getTotalSql(String sql){
		sql = "select count(1) cnt from  ( " + sql + " ) ";
		return sql;
	}
	
	static String getPageSql(PageDto pdto, String sql){
		if(pdto.getPageRow()>0){
			sql = "select * from ( select row_.*, rownum rownum_  from (  " + sql + "  ) row_  where rownum <=" 
			+ pdto.getEndRow() +"  ) where rownum_ > " + pdto.getStartRow();
		}
		return sql;
	}
	
	static Query getPageQuery(PageDto pdto, Query query) {
		query.setFirstResult(pdto.getStartRow());
		query.setMaxResults(pdto.getPageRow());
		return query;
	}
	
	
	public static int[] getDividedInfo(int page, int totalLine, int PAGE_ROWS) {
		int startline = PAGE_ROWS * (page - 1) + 1;
		if (startline > totalLine)
			startline = totalLine;
		int endline = startline - 1 + PAGE_ROWS;
		if (endline > totalLine)
			endline = totalLine;
		int totalPage = 0;
		if (totalLine % PAGE_ROWS == 0)
			totalPage = totalLine / PAGE_ROWS;
		else
			totalPage = totalLine / PAGE_ROWS + 1;
		return new int[] { startline, endline, totalLine, page, totalPage,PAGE_ROWS };
	}
	
	public static int[] getDividedInfo(int page, int totalLine) {
		int pageRows=DEFAULT_ROW;
		PageDto dto=pageDtos.get();
		if(dto!=null) pageRows=dto.getPageRow();
		int startline = pageRows * (page - 1) + 1;
		if (startline > totalLine)
			startline = totalLine;
		int endline = startline - 1 + pageRows;
		if (endline > totalLine)
			endline = totalLine;
		int totalPage = 0;
		if (totalLine % pageRows == 0)
			totalPage = totalLine / pageRows;
		else
			totalPage = totalLine / pageRows + 1;
		return new int[] { startline, endline, totalLine, page, totalPage,pageRows };
	}
	

}
