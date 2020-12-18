/**
 * 
 */
package espresso.models;

import com.solab.iso8583.IsoMessage;

/**
 * @author deva
 *
 */
public class RequestResponseLog implements Comparable<RequestResponseLog>{
	
	public RequestResponseLog(IsoMessage request, IsoMessage response, long requestTime, long reponseTime) {
		super();
		this.request = request;
		this.response = response;
		this.requestTime = requestTime;
		this.reponseTime = reponseTime;
	}
	
	private IsoMessage request;
	
	private IsoMessage response;
	
	private long requestTime;
	
	private long reponseTime;

	/**
	 * @return the request
	 */
	public IsoMessage getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(IsoMessage request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public IsoMessage getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(IsoMessage response) {
		this.response = response;
	}

	/**
	 * @return the requestTime
	 */
	public long getRequestTime() {
		return requestTime;
	}

	/**
	 * @param requestTime the requestTime to set
	 */
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	/**
	 * @return the reponseTime
	 */
	public long getReponseTime() {
		return reponseTime;
	}

	/**
	 * @param reponseTime the reponseTime to set
	 */
	public void setReponseTime(long reponseTime) {
		this.reponseTime = reponseTime;
	}

	@Override
	public int compareTo(RequestResponseLog o) {
		// TODO Auto-generated method stub
		return (int) (this.requestTime - o.requestTime);
	}

	

	
}
