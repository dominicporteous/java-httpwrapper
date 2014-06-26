package src;

import java.io.Serializable;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
 
public class Cookie implements Serializable {
 
    private static final long serialVersionUID = 1L;
    private Hashtable<String, String> cookies = new Hashtable<String, String>();
 

    public Cookie() { }
    public Cookie(String cookie) {
        setCookies(cookie);
    }
 

    public void setCookies(URLConnection conn) {
        int i=1;
        String hdrKey, hdrString, aCookie;
        while ((hdrKey = conn.getHeaderFieldKey(i)) != null) {
            if (hdrKey.equals("Set-Cookie")) {
                hdrString = conn.getHeaderField(i);
                StringTokenizer st = new StringTokenizer(hdrString, ",");
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    aCookie = (s.indexOf(";") > -1) ? s.substring(0, s.indexOf(";")) : s;
                    int j = aCookie.indexOf("=");
                    if (j > -1)  cookies.put(aCookie.substring(0, j), aCookie.substring(j + 1));
                }
            }
            i++;
        }
    }
 
    public void setCookies(String cookie) {
        String cookies[] = cookie.split("; ");
        String parts[];
        for(String c: cookies) {
            if(c.contains("=")) {
                parts = c.split("=",2);
                this.cookies.put(parts[0], parts[1]);
            }
        }
    }
 
    public void clearCookies() {
        cookies.clear();
    }

    public void removeCookie(String name) {
        cookies.remove(name);
    }
 
    @Override
    public String toString() {
        String result = "";
        Enumeration<String> keys = cookies.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            result += key + "=" + cookies.get(key);
            if (keys.hasMoreElements()) result += "; ";
        }
        return result;
    }
    
    @Override
    public Cookie clone() {
        Cookie clone = new Cookie();
        clone.setCookies(this.toString());
        return clone;
    }
 
}
