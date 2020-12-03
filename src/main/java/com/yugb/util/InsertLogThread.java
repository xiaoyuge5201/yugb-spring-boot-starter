package com.yugb.util;

import com.yugb.bean.RequestLog;
import com.yugb.dao.RequestLogDao;

/**
 * 更新线程
 * @author hzz
 *
 */
public class InsertLogThread extends Thread {

	private RequestLog requestLog;

	private RequestLogDao logService;

	public InsertLogThread(RequestLog requestLog, RequestLogDao logService) {
		 this.requestLog = requestLog;
		 this.logService = logService;
	}

	@Override
	public void run() {
		logService.save(requestLog);
	}	 
}
