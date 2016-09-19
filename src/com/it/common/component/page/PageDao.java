package com.it.common.component.page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.hql.FilterTranslator;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.it.common.component.db.BaseDao;

/**
 * @author liu
 * */
public class PageDao {
	
	/**
	 * get sql for one page view, by jdbc
	 * */
	public static String getPageSql(String sql, String noOrderbySql){
		PageDto pdto = PageUtil.get();
		String pageSql = PageUtil.getPageSql(pdto, sql);
		getSqlPageInfo(pdto, noOrderbySql);
		return pageSql;
	}
	
	/**
	 * get data list for one page view, by hibernate
	 * */
	public static List<?> getList(final String hql, String noOrderbyHql){
		final PageDto pdto = PageUtil.get();
		List<?> list = new ArrayList<Object>();
		if(pdto.getPageRow()>0){
			list = BaseDao.getInstance().getHibernateTemplate().executeFind(new HibernateCallback<List<?>>() {     
				public List<?> doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery(hql);
					PageUtil.getPageQuery(pdto, query);
					return query.list();
				}
			});
		}else{
			list = BaseDao.getInstance().getHibernateTemplate().find(hql);
		}
		getHqlPageInfo(pdto, noOrderbyHql);
		return list;     
	}
	
	/**
	 * page a list
	 * */
	public static <T> List<T> page(List<T> datas){
		PageDto pdto = PageUtil.get();
		if(datas!=null){
			pdto.endFlush(datas.size());
			if(pdto.getPageRow()>0){
				return datas.subList(pdto.getStartRow(), pdto.getEndRow());
			}
		}
		return datas;
	}
	
	/**
	 * page a map
	 * */
	public static <K,V> Map<K,V> page(Map<K,V> datas){
		PageDto pdto = PageUtil.get();
		Map<K,V> rtn = null;
		if(datas instanceof LinkedHashMap){
			rtn = new LinkedHashMap<K,V>();
		}else if(datas instanceof TreeMap){
			rtn = new TreeMap<K,V>();
		}else{
			rtn = new HashMap<K,V>();
		}
		if(datas!=null){
			if(pdto.getPageRow()>0){
				int pos = 0;
				for(K key : datas.keySet()){
					if(pos>=pdto.getStartRow() && pos<pdto.getEndRow()){
						rtn.put(key, datas.get(key));
					}
					if(pos==pdto.getEndRow()) break;
					pos++;
				}
			}else{
				rtn = datas;
			}
			pdto.endFlush(datas.size());
		}
		datas = rtn;
		return datas;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	
	static PageDto getSqlPageInfo(PageDto pdto, String noOrderbySql){
		String totalSql = PageUtil.getTotalSql(noOrderbySql);
		Integer totalRow = BaseDao.getInstance().getJdbcTemplate().queryForObject(totalSql, Integer.class);
		pdto.endFlush(totalRow);
		return pdto;
	}
	
	static PageDto getHqlPageInfo(PageDto pdto, String noOrderbyHql) {
		SessionFactoryImpl si = (SessionFactoryImpl)BaseDao.getInstance().getHibernateTemplate().getSessionFactory();
		FilterTranslator qt = si.getSettings().getQueryTranslatorFactory().createFilterTranslator(noOrderbyHql, noOrderbyHql, null, si);
		qt.compile(null, false);
		return getSqlPageInfo(pdto, qt.getSQLString());
	}    

	

}
