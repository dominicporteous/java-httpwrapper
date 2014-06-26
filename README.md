java-requests
================

Powerful multi-wrapper for HTTP URLConnection in Java. Includes custom header as well as cookie persistence.

Supports posting JSON as well.

Basic Example
-------------
Get contents of http://www.google.com

```java
    import src.*;
    
    public class Main{
        public requests = new Requests();
        
        public static void main(String[] args){
    		String res = Main.requests.get("http://www.google.com");
    		System.out.println( "Result :: " + res );
        }
    }
```

Simple Post Example
-------------
Posting example ticket to UserVoice API.

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
    		
    		String res = requests.post("https://test.uservoice.com/api/v1/tickets.json", post);
    		
    		System.out.println( "Result :: " + res );
        }
    }
```

Custom Header and JSON Post
------------
JSON Postdata will automatically be detected, and not parsed like regular post. Content-Length calculated automatically.

Headers are delimited by the string "||" as shown on Line 7 of the example below.

```java
	import src.*;
    
    public class Main{
        public requests = new Requests();
        
        public static void main(String[] args){
    		String url = "https://yourtestapi.app.com/api/endpoint";
    		String post = "{\"testfield\":true}";
	        String headers = "Content-Type: application/json||X-HTTP-Method-Override: PUT";
	        
	        String result = requests.post(url, post, headers);
    		
    		System.out.println( "Result :: " + res );
        }
    }
 
```
