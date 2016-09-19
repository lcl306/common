package com.it.common.component.check.base;

import java.util.List;
import java.util.Map;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;

import com.it.common.component.db.BaseDao;

/**
 * @author liu
 * */
public class MasterCheck {
	
	private static MasterCheck mc = new MasterCheck();
	
	private MasterCheck(){}
	
	public static MasterCheck getInstance(){
		return mc;
	}
	
	public boolean existChk(String sql, Map<String, ?> params){
		return check(sql, params, true);
	}
	
	public boolean noExistChk(String sql, Map<String, ?> params){
		return check(sql, params, false);
	}
	
	boolean check(String sql, Map<String, ?> params, boolean existChk){
		List<?> list = null;
		try{
			list = BaseDao.getInstance().getMapBySql(sql, params);
		}catch(IncorrectResultSizeDataAccessException e){
			list = null;
		}catch(BadSqlGrammarException e){
			return false;
		}
		boolean rtn = true;
		if(list!=null && !list.isEmpty()){
			if(!existChk){
				rtn = false;
			}
		}else{
			if(existChk){
				rtn = false;
			}
		}
		return rtn;
	}

}
