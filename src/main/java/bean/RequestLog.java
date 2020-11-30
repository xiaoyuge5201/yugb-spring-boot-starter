package bean;

import com.sd.cloud.bean.common.BasePO;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Map;

/**
 * 记录用户的操作记录
 * @author huzz
 *
 */
@Entity
@Table(name = "t_log")
public class RequestLog extends BasePO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户ID
	private Integer user_id;
	
	//用户名
	private String operater_username;
	
	//操作类型：0，代表添加，1代表删除，2代表修改，3代表查看
	private String operater_type;
	
	//日志级别
	private String log_type;
	
	//操作名称：例如数据上架，数据库下架等比较重要的步骤名称
	private String name;
	
	//请求地址
	private String remoteAddr;            
	
	//URI
    private String requestUri;             
    
    //请求方式
    private String method;            
   
    //提交参数  
    private String params;          
    
    //异常
	@Type(type="text")
    private String exception;           
    
    //返回参数
    private String resultParams;

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getOperater_username() {
		return operater_username;
	}

	public void setOperater_username(String operater_username) {
		this.operater_username = operater_username;
	}

	public String getOperater_type() {
		return operater_type;
	}

	public void setOperater_type(String operater_type) {
		this.operater_type = operater_type;
	}
	
	public String getLog_type() {
		return log_type;
	}

	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	

    /**
     * 设置请求参数
     * @param paramMap
     */
    public void setMapToParams(Map<String, String[]> paramMap) {
        if (paramMap == null){
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue);
        }
        this.params = params.toString();
    }

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getResultParams() {
		return resultParams;
	}

	public void setResultParams(String resultParams) {
		this.resultParams = resultParams;
	}

	@Override
	public String toString() {
		return "Log [user_id=" + user_id + ", operater_username=" + operater_username + ", operater_type="
				+ operater_type + ", log_type=" + log_type + ", name=" + name + ", remoteAddr=" + remoteAddr
				+ ", requestUri=" + requestUri + ", method=" + method + ", params=" + params + ", exception="
				+ exception + ", resultParams=" + resultParams + "]";
	}
	
	
}
