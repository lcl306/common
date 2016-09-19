package com.it.common.component.check.group;

import java.util.Map;

import com.it.common.component.check.base.CheckName;
import com.it.common.component.check.base.DateCheck;
import com.it.common.component.check.base.FigureCheck;
import com.it.common.component.check.base.MasterCheck;
import com.it.common.component.check.base.MatchCheck;
import com.it.common.component.check.base.NumCheck;
import com.it.common.component.check.base.StringCheck;
import com.it.common.component.lang.date.DateFormat;


/**
 * @author liu
 * */
public class CheckBo {
	
	private String byteCode;
	
	public CheckBo(){}
	
	public CheckBo(String byteCode){
		this.byteCode = byteCode;
	}

	public boolean checkByCode(CheckDto checkDto, String value, ErrListDto errs){
		checkDto.setByteCode(this.byteCode);
		return check(checkDto, value, errs);
	}
	
	/**
	 * check item value by checkDto, error information into errs
	 * */
	public static boolean check(CheckDto checkDto, String value, ErrListDto errs){
		return check(checkDto, value, errs, null);
	}

	/**
	 * check item value by checkDto, error information into errs
	 * */
	public static boolean check(CheckDto checkDto, String value, ErrListDto errs, Map<String, String> dbExistExcludes){
		boolean rtn = true;
		rtn = rtn && chkEmpty(checkDto, value, errs);
		boolean hanchk = true;
		if(rtn) hanchk = chkHan(checkDto, value, errs);
		rtn = rtn && hanchk;
		rtn = rtn && chkZen(checkDto, value, errs);
		boolean numchk = true;
		if(rtn) numchk = chkNum(checkDto, value, errs);
		rtn = rtn && numchk;
		boolean figChk = true;
		if(rtn) figChk = chkFigure(checkDto, value, errs);
		rtn = rtn && chkDotFig(checkDto, value, errs);
		rtn = rtn && figChk;
		rtn = rtn && chkDate(checkDto, value, errs);
		rtn = rtn && chkKin(checkDto, value, errs);
		if(hanchk && numchk && (checkDto.getRange()==null || checkDto.getRange().trim().length()==0)) rtn = rtn && chkMaster(checkDto, value, errs, dbExistExcludes);
		rtn = rtn && chkMatch(checkDto, value, errs);
		rtn = rtn && chkRegex(checkDto, value, errs);
		rtn = rtn && chkTime(checkDto, value, errs);
		return rtn;
	}
	
	/**
	 * check start <= end, error information into errs
	 * */
	public static boolean rangeChk(String start, String end, String itemName, ErrListDto errs){
		if(start==null || end==null){
			return true;
		}
		boolean rtn = true;
		int result = start.compareTo(end);
		if(result>0){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.COMPARISON_ERR, itemName));
			rtn = false;
		}
		return rtn;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean chkMatch(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		boolean matchRtn = true;
		if(!checkDto.getCheckMatch().isEmpty()){
			matchRtn = MatchCheck.chkMatchs(value, checkDto.getCheckMatch());
			rtn &= matchRtn;
		}
		boolean rangeRtn = MatchCheck.chkMatchRange(value, checkDto.getMatchStart(), checkDto.getMatchEnd());
		rtn &= rangeRtn;
		if(!matchRtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.MATCH_ERR, 
					BaseErrMessage.getMatchMsg(checkDto.getCheckMatch()), checkDto));
		}
		if(!rangeRtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.MATCH_ERR, 
					BaseErrMessage.getRangeMsg(checkDto.getMatchStart(), checkDto.getMatchEnd()), checkDto));
		}
		return rtn;
	}

	private static boolean chkEmpty(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		if("1".equals(checkDto.getEmpty())){
			rtn = StringCheck.chkEmpty(value);
		}else if("2".equals(checkDto.getEmpty())){
			rtn = StringCheck.chkZeroEmpty(value);
		}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.EMPTY_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}
	
	private static boolean chkRegex(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		if(checkDto.getCheckRegex()!=null){
			rtn = MatchCheck.chkRegex(checkDto.getCheckRegex(), value);
		}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.REGEX_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}

	private static boolean chkHan(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		if("1".equals(checkDto.getCheckType().get(CheckName.HAN_CHECK))){
			rtn = StringCheck.chkHan(value);
		}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.HAN_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}

	private static boolean chkZen(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		if("1".equals(checkDto.getCheckType().get(CheckName.ZAN_CHECK))){
			rtn = StringCheck.chkZen(value);
		}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.ZEN_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}

	protected static String getTrimValue(CheckDto checkDto, String value){
		if(checkDto.isTrimBlank()){
			if("1".equals(checkDto.getCheckType().get(CheckName.CODE_CHECK)) || "1".equals(checkDto.getCheckType().get(CheckName.DOT_CHECK)) 
					|| "1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_NUM_CHECK)) || "1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_DOT_CHECK))){
				if(value!=null) value= value.trim();
			}
		}
		return value;
	}

	private static boolean chkNum(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		if("1".equals(checkDto.getCheckType().get(CheckName.CODE_CHECK))){
			rtn = NumCheck.chkCode(value);
		}else if("1".equals(checkDto.getCheckType().get(CheckName.DOT_CHECK))){
			rtn = NumCheck.chkNum(value, true, false);
		}else if("1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_NUM_CHECK))){
			rtn = NumCheck.chkNum(value, false, true);
		}else if("1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_DOT_CHECK))){
			rtn = NumCheck.chkNum(value, true, true);
		}
		if(!rtn && ("1".equals(checkDto.getCheckType().get(CheckName.CODE_CHECK)))){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.CODE_ERR, checkDto.getRange(), checkDto));
		}else if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.NUM_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}

	protected static boolean chkFigure(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		value = getTrimValue(checkDto, value);
		if(value!=null && value.length()>0 && Character.getType(value.charAt(value.length()-1))==28) rtn = false;
		if(checkDto.getCheckLength()!=null){
			if(value!=null && value.trim().length()>1 && (value.startsWith("-") || value.startsWith("+") || value.startsWith(" ") ) 
					&& ("1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_NUM_CHECK)) || "1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_DOT_CHECK)))){
				value = value.substring(1);
			}
			if(CheckName.INT_LESS.equals(checkDto.getFigureType()) || CheckName.INT_LESS.toUpperCase().equals(checkDto.getFigureType())){
				rtn = FigureCheck.chkTrimIntFig(value, checkDto.getCheckLength(), false);
			}else if("1".equals(checkDto.getCheckType().get(CheckName.DOT_CHECK)) || "1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_DOT_CHECK))){
				if(CheckName.EQUAL.equals(checkDto.getFigureType()) || CheckName.EQUAL.toUpperCase().equals(checkDto.getFigureType())){
					rtn = FigureCheck.chkIntFig(value, checkDto.getCheckLength(), true);
				}else{
					rtn = FigureCheck.chkIntFig(value, checkDto.getCheckLength(), false);
				}
			}else{
				if(CheckName.EQUAL.equals(checkDto.getFigureType()) || CheckName.EQUAL.toUpperCase().equals(checkDto.getFigureType())){
					rtn &= FigureCheck.chkEqual(value, checkDto.getCheckLength(), checkDto.getByteCode());
				}else if(CheckName.LESS.equals(checkDto.getFigureType()) || CheckName.LESS.toUpperCase().equals(checkDto.getFigureType())){
					rtn &= FigureCheck.chkOver(value, checkDto.getCheckLength(), checkDto.getByteCode());
				}else if(CheckName.OVER.equals(checkDto.getFigureType()) || CheckName.OVER.toUpperCase().equals(checkDto.getFigureType())){
					rtn &= FigureCheck.chkLess(value, checkDto.getCheckLength(), checkDto.getByteCode());
				}else{
					rtn &= FigureCheck.chkOver(value, checkDto.getCheckLength(), checkDto.getByteCode());
				}
			}
			if("1".equals(checkDto.getCheckType().get(CheckName.DOT_CHECK)) || "1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_DOT_CHECK))){
				if(!rtn && (CheckName.EQUAL.equals(checkDto.getFigureType()) || CheckName.EQUAL.toUpperCase().equals(checkDto.getFigureType()))){
					errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.FIG_EQ_ERR, checkDto.getCheckLength()+"", checkDto));
				}else if(!rtn){
					errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.FIG_OVER_ERR, checkDto.getCheckLength()+"", checkDto));
				}
			}else if(!rtn && (CheckName.INT_LESS.equals(checkDto.getFigureType()) || CheckName.INT_LESS.toUpperCase().equals(checkDto.getFigureType()))){
				errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.FIG_INTFIG_ERR, checkDto.getCheckLength()+"", checkDto));
			}else{
				if(!rtn && (CheckName.EQUAL.equals(checkDto.getFigureType()) || CheckName.EQUAL.toUpperCase().equals(checkDto.getFigureType()))){
					errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.FIGTEXT_EQ_ERR, checkDto.getCheckLength()+"", checkDto));
				}else if(!rtn){
					errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.FIGTEXT_OVER_ERR, checkDto.getCheckLength()+"", checkDto));
				}
			}
		}
		return rtn;
	}

	protected static boolean chkDotFig(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		if("1".equals(checkDto.getCheckType().get(CheckName.DOT_CHECK)) || "1".equals(checkDto.getCheckType().get(CheckName.SYMBOL_DOT_CHECK))){
			if(checkDto.getCheckDotLength()!=null){
				rtn = FigureCheck.chkTrimDotFig(value, checkDto.getCheckDotLength(), false);
			}
		}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.DOT_FIG_OVER_ERR, checkDto.getCheckDotLength()+"", checkDto));
		}
		return rtn;
	}

	private static boolean chkDate(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		if("1".equals(checkDto.getCheckType().get(CheckName.DATE_CHECK))){
			if(CheckName.DATE_YM.equals(checkDto.getDateCheckType())){
				if(checkDto.getDateFormat()!=0){
					value = DateFormat.parseYm(value);
				}
				value = value +"01";
			}else if(checkDto.getDateFormat()!=0){
				value = DateFormat.parseDate(value);
			}
			if(checkDto.getRange()!=null && checkDto.getRange().trim().length()>0){
				rtn = DateCheck.chkZnDate(value, checkDto.isDay6());
			}else{
				if(CheckName.DATE_Z.equals(checkDto.getDateCheckType()) || CheckName.DATE_Z.toUpperCase().equals(checkDto.getDateCheckType())){
					rtn = DateCheck.chkZDate(value, checkDto.isDay6());
				}else if(CheckName.DATE_N.equals(checkDto.getDateCheckType()) || CheckName.DATE_N.toUpperCase().equals(checkDto.getDateCheckType())){
					rtn = DateCheck.chkNDate(value, checkDto.isDay6());
				}else if(CheckName.DATE_ZN.equals(checkDto.getDateCheckType()) || CheckName.DATE_ZN.toUpperCase().equals(checkDto.getDateCheckType())){
					rtn = DateCheck.chkZnDate(value, checkDto.isDay6());
				}else{
					rtn = DateCheck.chkDate(value, checkDto.isDay6());
				}
			}
		}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.DATE_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}
	
	private static boolean chkTime(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		if("1".equals(checkDto.getCheckType().get(CheckName.TIME_CHECK))){
			if(checkDto.getDateFormat()!=0){
				value = DateFormat.parseTime(value);
			}
			rtn = DateCheck.chkTime(value);
		}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.DATE_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}

	private static boolean chkKin(CheckDto checkDto, String value, ErrListDto errs){
		boolean rtn = true;
		//if("1".equals(checkDto.getCheckType().get(CheckName.KIN_CHECK))){
			rtn = StringCheck.chkKin(value);
		//}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.KIN_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}

	private static boolean chkMaster(CheckDto checkDto, String value, ErrListDto errs, Map<String, String> dbExistExcludes){
		boolean rtn = true;
		boolean multiParams = checkDto.getSqlParams()!=null&&checkDto.getSqlParams().size()>2?true:false;
		if(value!=null && value.trim().length()!=0){
			if(("2".equals(checkDto.getEmpty()) && !"0".equals(value)) || !"2".equals(checkDto.getEmpty())){
				MasterCheck mc = MasterCheck.getInstance();
				if(CheckName.MST_EXIST.equals(checkDto.getMstType()) || CheckName.MST_EXIST.toUpperCase().equals(checkDto.getMstType())){
					rtn = mc.existChk(checkDto.getCheckMstSql(), checkDto.getSqlParams());
				}else if(CheckName.MST_NO_EXIST.equals(checkDto.getMstType()) || CheckName.MST_NO_EXIST.toUpperCase().equals(checkDto.getMstType())){
					boolean chk = true;
					if(dbExistExcludes!=null && !dbExistExcludes.isEmpty() && checkDto.getSqlParams()!=null && !checkDto.getSqlParams().isEmpty()){
						chk = false;
						for(String itemName : dbExistExcludes.keySet()){
							if(!dbExistExcludes.get(itemName).equals(checkDto.getSqlParams().get(itemName))){
								chk = true;break;
							}
						}
					}
					if(chk) rtn = mc.noExistChk(checkDto.getCheckMstSql(), checkDto.getSqlParams());
				}
				if(!rtn){
					if(CheckName.MST_EXIST.equals(checkDto.getMstType()) || CheckName.MST_EXIST.toUpperCase().equals(checkDto.getMstType())){
						if(multiParams){
							errs.add(BaseErrMessage.getErrMessage().getError("99992"));
						}else{
							errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.NO_MST_ERR, "", checkDto));
						}
					}
					if(CheckName.MST_NO_EXIST.equals(checkDto.getMstType()) || CheckName.MST_NO_EXIST.toUpperCase().equals(checkDto.getMstType())){
						if(multiParams){
							errs.add(BaseErrMessage.getErrMessage().getError("99994"));
						}else{
							errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.EXIST_MST_ERR, "", checkDto));
						}
					}
				}
			}
		}
		return rtn;
	}
	
	public static boolean isPositive(CheckDto checkDto, String value, ErrListDto errs){
		if(value==null || value.trim().length()==0) return true;
		boolean rtn = true;
		try {
			Double d = Double.parseDouble(value.trim());
			if(d<=0) rtn = false;
		} catch (Exception e) {
			rtn = false;
		}
		if(!rtn){
			errs.add(BaseErrMessage.getErrMessage().getError(BaseErrMessage.NUM_ERR, checkDto.getRange(), checkDto));
		}
		return rtn;
	}

}
