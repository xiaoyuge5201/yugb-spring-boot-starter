package util;

import bean.RequestLog;
import service.RequestLogService;

/**
 * 更新线程
 * @author hzz
 *
 */
public class InsertLogThread extends Thread {

	private RequestLog requestLog;
	
	private RequestLogService logService;

	public InsertLogThread(RequestLog requestLog, RequestLogService logService) {
		 this.requestLog = requestLog;
		 this.logService = logService;
	}

	@Override
	public void run() {
		logService.save(requestLog);
	}	 
}
