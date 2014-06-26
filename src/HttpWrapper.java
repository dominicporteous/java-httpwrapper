package misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpWrapper {
    private Cookie cookie;
    private SSLTrust trust;
	private String url = "";
	private String overrideReq = "";
	private Proxy proxy;
	private String lastException = "";
	private String lastpage = "";
    private String html = "";
    private String headers = "";
	private String lastHeaders = "";

    public HttpWrapper() {
        cookie = new Cookie();
        html = "";
        noProxy();
        overrideReq = "";
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        
        trust = new SSLTrust();
        trust.install();
    }
    
    public boolean simple = true;
 
    public void get(String url, String referer) throws IllegalStateException {
        try {
        	this.url = url;
            URL url_ = new URL(url);
            
            HttpURLConnection conn;
            conn = (HttpURLConnection) url_.openConnection(getProxy());
            
            if("".equals(this.overrideReq)){
            	conn.setRequestMethod("GET");
            }else{
            	conn.setRequestMethod(this.overrideReq);
            	this.overrideReq = "";
            }
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(false);
            conn.setInstanceFollowRedirects(false);
            conn.setUseCaches(false);
 
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:14.0) Gecko/20100101 Firefox/14.0.1");
            if(!this.simple){
	            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	            conn.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
	            //conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
	            conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
	            conn.setRequestProperty("Connection", "keep-alive");
            }
            if(!referer.toString().isEmpty()) conn.setRequestProperty("Referer", referer);
            if(!cookie.toString().isEmpty()) conn.setRequestProperty("Cookie", cookie.toString().replaceAll("\\r\\n|\\r|\\n", " "));
            conn.setRequestProperty("Cache-Control", "no-cache");
            
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            
            if (this.headers.trim() != ""){
	            String[] h = this.headers.split(java.util.regex.Pattern.quote("||"), -1);
				if (h.length > 0)
				{
					for (String header : h)
					{
						try{
							String[] hs = header.split(java.util.regex.Pattern.quote(": "), -1);
							String key = hs[0].toString();
							hs[0] = "";
							String val = String.format(new String(new char[(hs).length]).replace("\0", "%s" + ":").replaceFirst("(.*)" + ":" + "$","$1"), hs).substring(1);;
							if(key.trim() != ""){
								conn.setRequestProperty(key,val.replaceAll("\\r\\n|\\r|\\n", " "));
							}
						}catch(Exception ex){
							//System.out.println("Exception whilst parsing headers: " + ex.getMessage());
						}
					}
				}
            }
            
            this.setHeaders(implodeHeaders("\n", conn.getRequestProperties()));
 
            // Get response-headers
            String headers = "";
            for(String key: conn.getHeaderFields().keySet())
                headers += ((key != null)?key + ": ":"") + conn.getHeaderField(key) + "\n";
            
            this.setLastHeaders(headers);
        	
            // Get content
            BufferedReader d = new BufferedReader(new InputStreamReader(new DataInputStream(conn.getInputStream())));
            String result = "";
            String line = null;
            while ((line = d.readLine()) != null) result += line +"\n";
            d.close();
            
            this.cookie.setCookies(conn);
            this.setLastPage(conn.getURL().toString());
            this.setHtml(result);
            
            this.simple = true;
        } catch (IOException e) {
           	System.out.println(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
    }
 
    public void get(String url) throws IllegalStateException {
        get(url, getLastPage());
    }
 
    public void post(String url, String postdata, String referer) throws IllegalStateException {
        try {
        	this.url = url;
            URL url_ = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) url_.openConnection(getProxy());
 
            if("".equals(this.overrideReq)){
            	conn.setRequestMethod("POST");
            }else{
            	conn.setRequestMethod(this.overrideReq);
            	this.overrideReq = "";
            }
            conn.setAllowUserInteraction(false);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setUseCaches(false);
            
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:14.0) Gecko/20100101 Firefox/14.0.1");
            if(!this.simple){
	            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	            conn.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
	            //conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
	            conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
	            //conn.setRequestProperty("Connection", "keep-alive");
            }
            
            if(!this.cookie.toString().isEmpty()) conn.setRequestProperty("Cookie", this.cookie.toString().replaceAll("\\r\\n|\\r|\\n", " "));
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(postdata.length()));
            conn.setRequestProperty("Cache-Control", "no-cache");
            
            conn.setConnectTimeout(20000);
           conn.setReadTimeout(20000);
            	
            if (this.headers.trim() != ""){
	            String[] h = this.headers.split(java.util.regex.Pattern.quote("||"), -1);
				if (h.length > 0)
				{
					for (String header : h)
					{
						try{
							String[] hs = header.split(java.util.regex.Pattern.quote(": "), -1);
							String key = hs[0].toString();
							hs[0] = "";
							String val = String.format(new String(new char[(hs).length]).replace("\0", "%s" + ":").replaceFirst("(.*)" + ":" + "$","$1"), hs).substring(1);;
							if(key.trim() != ""){
								conn.setRequestProperty(key,val.replaceAll("\\r\\n|\\r|\\n", " "));
							}
						}catch(Exception ex){
							//System.out.println("Exception whilst parsing headers: " + ex.getMessage());
						}
					}
				}
            }
            
            this.setHeaders(implodeHeaders("\n", conn.getRequestProperties()));
 
            // Write postdata
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(postdata);
            wr.flush();
            wr.close();
 
            // Get response-headers
            String headers = "";
            for(String key: conn.getHeaderFields().keySet())
                headers += ((key != null)?key + ": ":"") + conn.getHeaderField(key) + "\n";
            
            this.setLastHeaders(headers);
 
            // Get content
            String result = "";
            String line = null;
            BufferedReader d = new BufferedReader(new InputStreamReader(new DataInputStream(conn.getInputStream())));
            while ((line = d.readLine()) != null) result += line +"\n";
            d.close();
 
            this.cookie.setCookies(conn);
            this.setLastPage(conn.getURL().toString());
            this.setHtml(result);
            
            this.simple = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
    }
    
    public void post(String url, String postdata) throws IllegalStateException {
        post(url, postdata, getLastPage());
    }

    public HttpWrapper setLastPage(String lastpage) {
        this.lastpage = lastpage;
		return this;
    }

    public String getLastPage() {
        return lastpage;
    }
 
    public HttpWrapper clearLastPage() {
    	return this.setLastPage("");
    }
    
    public HttpWrapper setCookie(Cookie cookie) {
        this.cookie = cookie;
		return this;
    }
    
    public Cookie getCookie() {
        return cookie;
    }
    
    public String getHtml() {
        return this.html;
    }
 
    private HttpWrapper setHtml(String html) {
        this.html = html;
		return this;
    }    
    
    public String getHeaders() {
		return headers;
	}

	public HttpWrapper setHeaders(String headers) {
		this.headers = headers;
		return this;
	}
	
	public String getLastHeaders() {
		return lastHeaders;
	}

	public HttpWrapper setLastHeaders(String lastHeaders) {
		this.lastHeaders = lastHeaders;
		return this;
	}

    public HttpWrapper setProxy(String host, int port) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        return this;
    }
 
    public HttpWrapper noProxy() {
        this.proxy = Proxy.NO_PROXY;
        return this;
    }
    
    private Proxy getProxy() {
        return this.proxy;
    }
    
    public String getLastException() {
		return lastException;
	}
	
	public HttpWrapper setLastException(String lastException) {
		this.lastException = lastException;
		return this;
	}
    
    private String implodeHeaders(String delimiter, Map<String, List<String>> map){
	   boolean first = true;
	   StringBuilder sb = new StringBuilder();

	   for(Entry<String, List<String>> e : map.entrySet()){
	      if (!first) sb.append(delimiter);
	      sb.append(e.getKey() + " : " + e.getValue().toString());
	      first = false;
	   }

	   return sb.toString();
	}
    
    public void removeCookie(String st){
    	cookie.removeCookie(st);
    }
    
    public HttpWrapper setOverrideReq(String req){
    	overrideReq = req;
    	return this;
    }

	public String getUrl() {
		return url;
	}
}
