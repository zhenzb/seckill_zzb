package wx;

import java.util.Random;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

import com.alibaba.fastjson.JSONObject;

public class WXConfig{
	public static CloseableHttpClient https;
	
	public static CookieStore cookieStore = new BasicCookieStore();
	
	public static String skey = null;
	public static String wxsid = null;
	public static String wxuin = null;
	public static String pass_ticket = null;
	public static String isgrayscale;
	
	public static String UserName;
	
	public static HttpGet creatHttpGet(String url) {
		HttpGet httpPost = new HttpGet(url);
		httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		httpPost.setHeader("Connection", "keep-alive");
		return httpPost;
	}
	
	public static String characters = "0123456789";
    public static String randomNum(int factor){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < factor; i++) {
            // nextInt(10) = [0, 10)
            sb.append(characters.charAt(random.nextInt(10)));
        }
        return sb.toString();
    }
	
    public static JSONObject SyncKey;
}