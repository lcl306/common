package com.it.common.component.meta.token;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.it.common.share.GlobalName;

/**
 *  一览画面
	TokenUtil tu = new TokenUtil();
  	tu.saveToken(request);
  	
  	登录画面
  	TokenUtil tu = new TokenUtil();
  	if(tu.isTokenValid(request, false)){
  		// 登录
  	}else{
		// 报错
	}
	tu.saveToken(request);
	@author liu
 * */
public class TokenUtil {

	/**
     * The timestamp used most recently to generate a token value.
     */
    private long previous;

	
	/**
     * @param request The servlet request we are processing
     * @param reset Should we reset the token after checking it?
     */
    public synchronized boolean isTokenValid(HttpServletRequest request, boolean reset) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String saved = (String) session.getAttribute(GlobalName.SESSION_TOKEN);
        if (saved == null) {
            return false;
        }
        if (reset) {
            this.resetToken(request);
        }
        String token = request.getParameter(GlobalName.PARAM_TOKEN);
        if (token == null) {
            return false;
        }
        return saved.equals(token);
    }
    
    /**
     * Reset the saved transaction token in the user's session.  This
     * indicates that transactional token checking will not be needed
     * on the next request that is submitted.
     * @param request The servlet request we are processing
     */
    public synchronized void resetToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(GlobalName.SESSION_TOKEN);
    }
	
	
	/**
     * Save a new transaction token in the user's current session, creating
     * a new session if necessary.
     * @param request The servlet request we are processing
     */
    public synchronized void saveToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = generateToken(request);
        if (token != null) {
            session.setAttribute(GlobalName.SESSION_TOKEN, token);
        }

    }
    
    ///////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * Generate a new transaction token, to be used for enforcing a single
     * request for a particular transaction.
     * @param request The request we are processing
     */
    private synchronized String generateToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            byte id[] = session.getId().getBytes();
            long current = System.currentTimeMillis();
            if (current == previous) {
                current++;
            }
            previous = current;
            byte now[] = new Long(current).toString().getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(id);
            md.update(now);
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    
    
    /**
     * Convert a byte array to a String of hexadecimal digits and return it.
     * @param buffer The byte array to be converted
     */
    private String toHex(byte buffer[]) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
        }
        return sb.toString();
    }
}
