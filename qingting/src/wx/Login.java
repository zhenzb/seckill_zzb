package wx;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Login {
	private String iamgeImg = "/Users/jiangshidi/Documents/img";
	String redirect_uri = "";
	
	public static void index() {
		
		WXConfig.https = HttpClients.custom().setDefaultCookieStore(WXConfig.cookieStore).build();
        
		System.setProperty("jsse.enableSNIExtension", "false");
		Login l = new Login();
		l.initpage();
		String appid = l.getPng1();
		if (!"".equals(appid)) {
			l.getPng2(appid);
		}
		while (true) {
			int cf = l.checklogin(appid);
			if (cf == 3) {
				System.out.println("已在手机端确认");
				break;
			}
			if (cf == 2) {
				appid = l.getPng1();
				if (!"".equals(appid)) {
					l.getPng2(appid);
				}
			}
			if (cf == 1) {
				continue;
			}
			try {
				Thread.sleep(13000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		l.login();

	}

	/**
	 * 初始化页面
	 */
	public void initpage() {
		HttpGet httpPost = new HttpGet("https://wx.qq.com/");

		String html = "";
		try {
			HttpResponse response = WXConfig.https.execute(httpPost);
			HttpEntity entitySort = response.getEntity();
			html = EntityUtils.toString(entitySort, "utf-8");
			// System.out.println(html);
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载二维码之获取参数
	 */
	public String getPng1() {
		String url = "https://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_="
				+ System.currentTimeMillis();
		HttpGet httpPost = new HttpGet(url);

		String html = "";
		try {
			HttpResponse response = WXConfig.https.execute(httpPost);
			HttpEntity entitySort = response.getEntity();
			html = EntityUtils.toString(entitySort, "utf-8");
			System.out.println(html);
			if (html.indexOf("window.QRLogin.code = 200") != -1) {
				return html.replace("window.QRLogin.code = 200; window.QRLogin.uuid = \"", "").replace("\";", "");
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 下载二维码
	 * 
	 * @param appid
	 * @return
	 */
	public void getPng2(String appid) {
		String url = "https://login.weixin.qq.com/qrcode/" + appid;
		HttpGet httpget = new HttpGet(url);
		System.out.println("获取二维码：Executing request " + httpget.getURI());// 开始
		String html = "";
		FileOutputStream fos;
		try {
			HttpResponse response = WXConfig.https.execute(httpget);
			System.out.println(response.getStatusLine());
			InputStream inputStream = response.getEntity().getContent();
			File file = new File(this.iamgeImg);
			if (!file.exists()) {
				file.mkdirs();
			}
			fos = new FileOutputStream("/Users/jiangshidi/Documents/img/test.jpg");
			byte[] data = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(data)) != -1) {
				fos.write(data, 0, len);
			}
			
			//App.lblNewLabel.redraw();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(html);
	}

	public int checklogin(String appid) {
		String url = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=" + appid
				+ "&tip=0&r="+System.currentTimeMillis()+"&_=" + System.currentTimeMillis();
		System.out.println(url);
		HttpGet httpPost = new HttpGet(url);
		httpPost.setHeader("Host", "login.wx.qq.com");
		httpPost.setHeader("Pragma", "no-cache");
		httpPost.setHeader("Referer", "https://wx.qq.com/");
		httpPost.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		httpPost.setHeader("Connection", "keep-alive");
		int timeout = 200000;
		RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout).build();
		httpPost.setConfig(config);
		String html = "";
		try {
			HttpResponse response = WXConfig.https.execute(httpPost);
			HttpEntity entitySort = response.getEntity();
			html = EntityUtils.toString(entitySort, "utf-8");
			System.out.println(html);
			if (html.indexOf("408") != -1) {
				return 1;
			}
			if (html.indexOf("400") != -1) {

				return 2;
			}
			if (html.indexOf("200") != -1) {
				int start = html.indexOf("https");
				html = html.substring(start).replace("\";", "");
				this.redirect_uri = html;
				System.out.println(this.redirect_uri);
				return 3;
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void login() {
		HttpGet httpPost = new HttpGet(this.redirect_uri+"&fun=new&version=v2");
		httpPost.setHeader("Host", "wx.qq.com");
		httpPost.setHeader("Pragma", "no-cache");
		httpPost.setHeader("Referer", "https://wx.qq.com/?&lang=zh_CN");
		httpPost.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		httpPost.setHeader("Connection", "keep-alive");
		String html = "";
		try {
			HttpResponse response = WXConfig.https.execute(httpPost);
			HttpEntity entitySort = response.getEntity();
			html = EntityUtils.toString(entitySort, "utf-8");
			System.out.println("登录成功："+html);
		
			// 创建saxReader对象  
			SAXReader reader = new SAXReader();  
			// 通过read方法读取一个文件 转换成Document对象  
			Document document = reader.read(new ByteArrayInputStream(html.getBytes("UTF-8")));  
			//获取根节点元素对象  
			Element node = document.getRootElement();
			
			WXConfig.skey = node.elementText("skey");
			WXConfig.wxsid = node.elementText("wxsid");
			WXConfig.wxuin = node.elementText("wxuin");
			WXConfig.pass_ticket = node.elementText("pass_ticket");
			WXConfig.isgrayscale = node.elementText("isgrayscale");
			
//			System.out.println("skey:"+skey);
//			System.out.println("wxsid:"+wxsid);
//			System.out.println("wxuin:"+wxuin);
//			System.out.println("pass_ticket:"+pass_ticket);
//			System.out.println("isgrayscale:"+isgrayscale);
				
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    }
}