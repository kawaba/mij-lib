package tkxlib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Webtool {
	
	public static  String getPage(String urlString) throws IOException{

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            // 接続タイムアウトを設定します。
            connection.setConnectTimeout(5000); // 5秒のタイムアウト
            connection.setReadTimeout(5000); // 5秒のタイムアウト
            
            // リクエストメソッドをGETに設定します。
            connection.setRequestMethod("GET");
            
            // 接続を確立します。
            int responseCode = connection.getResponseCode();
            
            // レスポンスコードをチェックします。
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // レスポンスデータを読み取ります。
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                
                String line;
                StringBuilder response = new StringBuilder();
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }
                
                reader.close();
                
                // レスポンスの内容を表示します。
                return response.toString();                
            	
            } else {
                throw new IOException("レスポンスコード: " + responseCode);
            }
            
        } catch (IOException e) {
        	throw new IOException();
        }
	}
	
	public static void main(String[] args) {
		
        try {
            System.out.println(getPage("https://www.google.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
