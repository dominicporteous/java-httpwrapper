package src;

public class Requests {
	public HttpWrapper wrapper = new HttpWrapper();
	
	public String get(String url, String headers){
		try{
			wrapper.setHeaders(headers);
			wrapper.get(url);
			return wrapper.getHtml();
		}catch(IllegalStateException e){
			Misc.debugWrite(lastDebugInfo());
			Misc.debugWrite(wrapper.toString());
			wrapper.setLastException(e.getMessage());
			return "";
		}
		
	}
	
	public String get(String url){
		return get(url,"");
	}
	
	public String getUrl(){
		return wrapper.getLastPage();
	}
	
	public String post(String url, String postdata, String headers){
		try{
			wrapper.setHeaders(headers);
			wrapper.post(url,postdata);
			return wrapper.getHtml();
		}catch(Exception e){
			Misc.debugWrite(lastDebugInfo());
			Misc.debugWrite(wrapper.toString());
			wrapper.setLastException(e.getMessage());
			return "";
		}
	}
	
	public String post(String url, String postdata){
		return post(url,postdata,"");
	}
	
	public void simple(){
		wrapper.simple = true;
	}
	
	public void overrideReq(String req){
		wrapper.setOverrideReq(req);
	}
	
	public String lastDebugInfo(){
		String ret =  
		  "Debug info for " + getUrl() + "\n\n"
		+ "Request headers for " + getUrl() + "\n"
		+ wrapper.getHeaders() + "\n"
		+ "\n\n"
		+ "Response headers for " + getUrl() + "\n"
		+ wrapper.getLastHeaders() + "\n"
		+ "Response body for " + getUrl() + "\n"
		+ wrapper.getHtml() + "\n"
		+ "\n\n";
		
		return ret;
	}
}
