java-httpwrapper
================

Powerful HTTP URLConnection wrapper for Java. Includes custom header as well as cookie persistence.

```java
    import src.*;
    
    public class Main{
        public requests = new Requests();
        
        public static void main(String[] args){
    		String post =
    		  "client=API_KEY&"
    		+ "email=EMAIL&"
    		+ "ticket[state]=open&"
    		+ "ticket[subject]=SUBJECT&"
    		+ "ticket[message]=MESSAGE";
    		
    		String res = Main.requests.post("https://test.uservoice.com/api/v1/tickets.json", post);
    		
    		System.out.println( "Result :: " + res );
        }
    }
```
