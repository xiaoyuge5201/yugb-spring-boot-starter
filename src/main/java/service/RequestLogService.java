package service;

import bean.RequestLog;
import dao.RequestLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RequestLogService {
	@Autowired
	private RequestLogDao requestLogDao;
	
	/**
	 * 保存
	 * @param requestLog
	 */
    public void save(RequestLog requestLog) {
    	requestLogDao.save(requestLog);
    }
    
	/**
	 * 修改
	 * @param requestLog
	 */
    public void update(RequestLog requestLog) {
    	requestLogDao.update(requestLog);
    }

}
