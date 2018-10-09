package wx;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class WXSendmsg{
	public static void sendMsg(Integer Type,String Content,String FromUserName,String ToUserName) {
		JSONObject msg = new JSONObject();
		msg.put("Type", Type);
		msg.put("Content",Content);
		msg.put("FromUserName", FromUserName);
		msg.put("ToUserName", ToUserName);
		msg.put("LocalID", System.currentTimeMillis()+"0132");
		msg.put("ClientMsgId", System.currentTimeMillis()+"0132");
		
		JSONObject jsxsj = new JSONObject();
		JSONObject job = new JSONObject();
		job.put("Uin",WXConfig.wxuin );
		job.put("Sid",WXConfig.wxsid );
		job.put("Skey",WXConfig.skey );
		job.put("DeviceID",  "e" + WXConfig.randomNum(15));
		
		jsxsj.put("BaseRequest", job);
		jsxsj.put("Msg", msg);
		jsxsj.put("Scene", 0);
		
		String url2 = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?lang=zh_CN&pass_ticket="+WXConfig.pass_ticket;
		HttpPost method2 = new HttpPost(url2);
		method2.setHeader("Accept", "application/json, text/plain, */*");
		method2.setHeader("Content-Type", "application/json;charset=UTF-8");
		System.out.println("-----------------------------------");
		System.out.println(jsxsj.toString());
		
		StringEntity stringEntityx = new StringEntity(jsxsj.toString(),"utf-8");
		method2.setEntity(stringEntityx);

		try {
			HttpResponse responsex = WXConfig.https.execute(method2);
			HttpEntity entitySortx = responsex.getEntity();
			String senthtml = EntityUtils.toString(entitySortx, "utf-8");
			System.out.println(senthtml);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}