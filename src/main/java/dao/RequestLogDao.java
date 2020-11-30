package dao;

import bean.RequestLog;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 源数据源的Dao操作
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class RequestLogDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 保存
     *
     * @param requestLog
     */
    public void save(RequestLog requestLog) {
        sessionFactory.getCurrentSession().saveOrUpdate(requestLog);
    }

    /**
     * 修改
     *
     * @param requestLog
     */
    public void update(RequestLog requestLog) {
        sessionFactory.getCurrentSession().saveOrUpdate(requestLog);
    }

}
