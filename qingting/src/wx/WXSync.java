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

public class WXSync {
	public WXSync() {
		while (true) {
			String url4 = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid=" + WXConfig.wxsid + "&skey=" + WXConfig.skey + "&lang=zh_CN&pass_ticket=" + WXConfig.pass_ticket;
			System.out.println(url4);
			HttpPost method4 = new HttpPost(url4);
			method4.setHeader("Accept", "application/json, text/plain, */*");
			method4.setHeader("Content-Type", "application/json;charset=UTF-8");
			System.out.println("-----------------------------------");

			
			JSONObject job = new JSONObject();
			
			job.put("Uin", WXConfig.wxuin);
			job.put("Sid", WXConfig.wxsid);
			job.put("Skey", WXConfig.skey);
			job.put("DeviceID", "e" + WXConfig.randomNum(15));

			JSONObject webwxsyncJ = new JSONObject();
			webwxsyncJ.put("BaseRequest", job);
			webwxsyncJ.put("SyncKey", WXConfig.SyncKey);
			int now = (int) System.currentTimeMillis();
			webwxsyncJ.put("rr", (~now) + "");

			//System.out.println(webwxsyncJ.toString());
			try {
				StringEntity stringEntity = new StringEntity(webwxsyncJ.toString());
				method4.setEntity(stringEntity);

				HttpResponse response = WXConfig.https.execute(method4);
				HttpEntity entitySort = response.getEntity();
				String html = EntityUtils.toString(entitySort, "utf-8");
				//System.out.println(html);

				JSONObject jooo = JSONObject.parseObject(html);
				WXConfig.SyncKey = jooo.getJSONObject("SyncKey");

				JSONArray AddMsgList = jooo.getJSONArray("AddMsgList");
				for (int i = 0; i < AddMsgList.size(); i++) {
					JSONObject AddMsg = AddMsgList.getJSONObject(i);
					String Content = AddMsg.getString("Content");
					System.out.println(Content);
					
					WXFilter.filter(Content, AddMsg.getString("ToUserName"), AddMsg.getString("FromUserName"));
					
				}
				Thread.sleep(5000);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}