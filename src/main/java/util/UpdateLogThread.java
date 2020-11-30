package util;

import bean.RequestLog;
import service.RequestLogService;

/**
 * 更新线程
 */
public class UpdateLogThread extends Thread {

	private RequestLog requestLog;
	
	private RequestLogService logService;

	public UpdateLogThread(RequestLog requestLog, RequestLogService logService) {
		 this.requestLog = requestLog;
		 this.logService = logService;
	}

	@Override
	public void run() {
		logService.update(requestLog);
	}	 
}
