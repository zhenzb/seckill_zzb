package wx;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WXGetcontact{
	public WXGetcontact(){
		HashMap<String,String> fm = new HashMap<String,String>();
		String url3 = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?r="+System.currentTimeMillis()+"&seq=0&skey="+WXConfig.skey;
		System.out.println("好友列表："+url3);
		HttpGet method3 = WXConfig.creatHttpGet(url3);
		try {
			HttpResponse response = WXConfig.https.execute(method3);
			HttpEntity entitySort = response.getEntity();
			String html = EntityUtils.toString(entitySort, "utf-8");
			System.out.println(html);
			
			JSONObject frj = JSONObject.parseObject(html);
			JSONArray friList = frj.getJSONArray("MemberList");
			for(int i =0;i<friList.size();i++) {
				JSONObject fr = friList.getJSONObject(i);
				String NickName = fr.getString("NickName");
				String RemarkName = fr.getString("RemarkName");
				
				if(NickName.equals("Constantine") || NickName.equals("what") || NickName.equals("H") || RemarkName.equals("Constantine") || RemarkName.equals("李白") || RemarkName.equals("每天惠-惠语") || NickName.equals("疾风") || RemarkName.equals("疾风") || NickName.equals("小萁") || RemarkName.equals("小萁")  || NickName.equals("妙心（唐满红）") || RemarkName.equals("妙心（唐满红）") || NickName.startsWith("我的好好老婆")|| RemarkName.startsWith("我的好好老婆")) {
					fm.put(fr.getString("UserName"), NickName);
					System.out.println("NickName:"+NickName);
					System.out.println("RemarkName:"+RemarkName);
				}
				
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String webwxDataTicket = "";
		List<Cookie> cookies = WXConfig.cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
            if (cookies.get(i).getName().equals("webwx_data_ticket")) {
            	webwxDataTicket = cookies.get(i).getValue();
            }
        }
		
		System.out.println("--------------------------------------------------------");
		System.out.println(webwxDataTicket);
		System.out.println("--------------------------------------------------------");
	}
}