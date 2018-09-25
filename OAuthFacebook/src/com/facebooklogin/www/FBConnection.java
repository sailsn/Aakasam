package com.facebooklogin.www;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FBConnection {

	// getFacebookAuthorizationURL=Invoking the Login Dialog and Setting the
	// Redirect URL

	// getGraphURL
	// getAccessToken

	static String APP_ID = "299691937496109";
	static String APP_SECRET = "f9c179e1d3246a686e2a992491ca88a0";
	static String REDIRECT_URL = "http://localhost:8080/OAuthFacebook/fbhome";
	static String accessToken = "";

	public String getFBAuthUrl() {
		String fbLoginUrl = "";
		try {
			fbLoginUrl = "https://www.facebook.com/v3.1/dialog/oauth?" + "client_id=" + FBConnection.APP_ID
					+ "&redirect_uri=" + URLEncoder.encode(REDIRECT_URL, "UTF-8") + "&scope=email";

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fbLoginUrl;
	}

	public String getFBGraphUrl(String code) {
		String fbGraphUrl = "";
		try {
			fbGraphUrl = "https://graph.facebook.com/v3.1/oauth/access_token?" + "client_id=" + FBConnection.APP_ID
					+ "&redirect_uri=" + URLEncoder.encode(REDIRECT_URL, "UTF-8") + "&client_secret="
					+ FBConnection.APP_SECRET + "&code=" + code;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fbGraphUrl;
	}

	public String getAccessToken(String code) throws IOException{

		if ("".equals(accessToken)) {
			URL fbGraphUrl;
			try {
				fbGraphUrl = new URL(getFBGraphUrl(code));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new RuntimeException("Invalid code received " + e);
			}
			URLConnection fbConnection;
			StringBuffer b = null;
			try {
				fbConnection = fbGraphUrl.openConnection();
				BufferedReader in;
				in = new BufferedReader(new InputStreamReader(fbConnection.getInputStream()));
				String inputLine;
				b = new StringBuffer();
				while ((inputLine = in.readLine()) != null)
					b.append(inputLine + "\n");
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to connect with Facebook " + e);
			}
			accessToken = b.toString();
			
			System.out.println(accessToken);
			try {
				JSONObject json = new JSONObject(accessToken);
				accessToken =	(String) json.get("access_token");
				System.out.println(accessToken);
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return accessToken;
	}

}
