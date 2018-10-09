package wx;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import common.HttpClient;

public class WXFilter {
	public static void filter(String Content, String FromUserName, String ToUserName) {
		if (Content.equals("会员")) {
			WXSendmsg.sendMsg(1, "您好，您需要什么会员,请回复001，爱奇艺。。。", FromUserName, ToUserName);
		} else if (Content.equals("001")) {
			//String phone = HttpClient.sendHttp("http://172.16.40.187/wx/gp");
			//if (phone.equals("0")) {
				WXSendmsg.sendMsg(1, "暂时没有空闲，请稍后重试", FromUserName, ToUserName);
			/*} else {
				new Thread() {
					public void run() {
						WXSendmsg.sendMsg(1, "账号:" + phone + ";请尽快登录。", FromUserName, ToUserName);
						for (int i = 0; 1 < 100; i++) {
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							String sms = HttpClient.sendHttp("http://172.16.40.187/wx/gsms?phone=" + phone);
							if (sms.equals("")) {
								continue;
							} else {
								WXSendmsg.sendMsg(1, sms, FromUserName, ToUserName);
								i = 100;
								break;
							}
						}
					};
				}.start();
			}*/
		}else {
			WXSendmsg.sendMsg(1, "您好，我的主人今年不在线有事明年联系。。。", FromUserName, ToUserName);
		}
	}
}