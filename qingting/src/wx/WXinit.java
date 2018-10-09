package wx;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WXinit{
	
	public WXinit() {
		String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r="+System.currentTimeMillis()+"&lang=ch_ZN&pass_ticket="+WXConfig.pass_ticket;
		System.out.println(url);
		HttpPost method = new HttpPost(url);
		method.setHeader("Accept", "application/json, text/plain, */*");
		method.setHeader("Content-Type", "application/json;charset=UTF-8");
		method.setHeader("Host", "wx.qq.com");
		method.setHeader("Pragma", "no-cache");
		method.setHeader("Referer", "https://wx.qq.com/?&lang=zh_CN");
		method.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		method.setHeader("Connection", "keep-alive");
		
		JSONObject joRoot = new JSONObject();
		JSONObject job = new JSONObject();
		
		job.put("Uin",WXConfig.wxuin );
		job.put("Sid",WXConfig.wxsid );
		job.put("Skey",WXConfig.skey );
		job.put("DeviceID",  "e" + WXConfig.randomNum(15));
		joRoot.put("BaseRequest", job);
		String html = null;
		try {
			StringEntity stringEntity = new StringEntity(joRoot.toString());
			method.setEntity(stringEntity);
			HttpResponse response = WXConfig.https.execute(method);
			HttpEntity entitySort = response.getEntity();
			html = EntityUtils.toString(entitySort, "utf-8");
			System.out.println(html);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONObject jhtml = JSONObject.parseObject(html);
		String UserName = jhtml.getJSONObject("User").getString("UserName");
		System.out.println("UserName:"+UserName);
		WXConfig.UserName = UserName;
		WXConfig.SyncKey = jhtml.getJSONObject("SyncKey");
		String toName = "";
		JSONArray friendList = jhtml.getJSONArray("ContactList");
		for(int i=0;i<friendList.size();i++) {
			JSONObject friend = friendList.getJSONObject(i);
			String friendNickName = friend.getString("NickName");
			String friendUserName = friend.getString("UserName");
			System.out.println(friendNickName + "---************---" + friendUserName);
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		String urltz = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxstatusnotify?lang=zh_CN&pass_ticket="+WXConfig.pass_ticket;
		System.out.println(urltz);
		HttpPost methodtz = new HttpPost(urltz);
		methodtz.setHeader("Accept", "application/json, text/plain, */*");
		methodtz.setHeader("Content-Type", "application/json;charset=UTF-8");
		
		joRoot.put("Code", 3);
		joRoot.put("FromUserName", UserName);
		joRoot.put("ToUserName", UserName);
		joRoot.put("ClientMsgId", System.currentTimeMillis());
		
		System.out.println(joRoot.toString());
		try {
			StringEntity stringEntity = new StringEntity(joRoot.toString());
			methodtz.setEntity(stringEntity);
			HttpResponse response = WXConfig.https.execute(methodtz);
			HttpEntity entitySort = response.getEntity();
			html = EntityUtils.toString(entitySort, "utf-8");
			System.out.println(html);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}