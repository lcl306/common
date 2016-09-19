package com.it.common.component.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.it.common.component.json.JsonUtil;
import com.it.common.component.lang.date.DBTime;
import com.it.common.share.GlobalName;
import com.it.common.share.SpringBeanStore;

/**
 * @author liu
 * */
public class BaseDao {
	
	private JdbcTemplate jdbcTemplate;
	
	private HibernateTemplate hibernateTemplate;
	
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * get BaseDao from spring content
	 * @use    BaseDao baseDao = BaseDao.getInstance();
	 * */
	public static BaseDao getInstance(){
		if("web".equals(GlobalName.BASEDAO_FROM))
			return (BaseDao)SpringBeanStore.getBean("baseDao");
		else
			return (BaseDao)SpringBeanStore.getPathBean("baseDao");
	}

	/**
	 * get hibernate session
	 * */
	protected Session getSession(){
		return DaoUtil.getSession(hibernateTemplate);
	}

	/**
	 * get jdbcTemplate
	 * */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * get hibernateTemplate
	 * */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * get List of map<String, Object>, use PreparedStatement
	 * */
	public List<Map<String, Object>> getMapBySql(String sql, Map<String, ?> params){
		return DaoUtil.getMapBySql(jdbcTemplate, sql, params);
	}

	/**
	 * get Object List from a sql
	 * NOTICE:  not used for BATCH select
	 * @param sql
	 * @param cls is object class
	 * */
	public <T> List<T> getObjectsBySql(String sql, Class<T> cls) {
		return DaoUtil.getObjectsBySql(jdbcTemplate, sql, cls);
	}
	
	/**
	 * get Object from a sql
	 * NOTICE:  not used for BATCH select
	 * @param sql
	 * @param cls is object class
	 * */
	public <T> T getObjectBySql(String sql, Class<T> cls) {
		T t = null;
		List<T> list = DaoUtil.getObjectsBySql(jdbcTemplate, sql, cls);
		if(list!=null && !list.isEmpty()){
			t = list.get(0);
		}
		return t;
	}

	/**
	 * get Object List from a sql
	 * NOTICE:  not used for BATCH select
	 * @param sql
	 * @param cls is object class
	 * @param idCls is object id class
	 * */
	public <T,I> List<T> getObjectsBySql(String sql, Class<T> beanCls, Class<I> idCls){
		return DaoUtil.getObjectsBySql(jdbcTemplate, sql, beanCls, idCls);
	}
	
	/**
	 * get Object from result set
	 * @param resultSet
	 * @param bean
	 * */
	public static <T> T getObjectByResultSet(ResultSet rs, T bean){
		try {
			return DaoUtil.getObjectByResultSet(rs, bean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * get json List from a sql
	 * NOTICE:  not used for BATCH select
	 * @param sql
	 * */
	public String getJsonBysql(String sql){
		List<?> list = jdbcTemplate.queryForList(sql);
		if(list!=null && list.size()==1){
			Object o = list.get(0);
			return JsonUtil.toJson(o);
		}
		return JsonUtil.toJsons(list);
	}
	
	/**
	 * get name by sql
	 * */
	public String getNameBySql(String sql){
		String name = jdbcTemplate.queryForObject(sql, String.class);
		if(name==null) name ="";
		else name = name.trim();
		return name;
	}
	
	/**
	 * get database sequence nextval
	 * */
	public Long getNextVal(String seqName) {
		String sql = "select " + seqName + ".nextval from dual";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	/**
	 * get Database time
	 * */
	public DBTime getDBTime(){
		String sql = " select current_timestamp() ";
		Timestamp timestamp = (Timestamp) jdbcTemplate.queryForObject(sql, Timestamp.class);
		DBTime dt = new DBTime();
		dt.tt = timestamp;
		dt.date = new SimpleDateFormat(DBTime.format).format(dt.tt);
		return dt;
	}
	

}
