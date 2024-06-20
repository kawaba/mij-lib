package tkxlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Client {

	private static <T> TypeReference<T> type(Class<T> type) {
		return new TypeReference<T>() {
		};
	}

	public static <T> T read(String urlString, Class<T> type) {

		Object o;
		try {
			o = readJson(urlString, type(type));
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.convertValue(o, type);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T read(String urlString, TypeReference<T> type) {

		Object o;
		try {
			o = readJson(urlString, type);
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			return mapper.convertValue(o, type);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 * @param <T>
	 * @param urlString
	 * @param typeReference
	 * @return
	 * @throws IOException
	 */
	private static <T> Object readJson(String urlString, TypeReference<T> typeReference) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// レスポンス取得
		try (InputStream in = con.getInputStream(); Reader reader = new InputStreamReader(in)) {

			ObjectMapper mapper = new ObjectMapper(); // 新しいインスタンスを作成
			mapper.registerModule(new JavaTimeModule()); // モジュールを登録
			Object obj = mapper.readValue(reader, typeReference);
			return obj;

		}
	}
	/**
	 * 
	 * @param <T>
	 * @param urlString
	 * @param type
	 * @return
	 */
	public static <T> int write(String urlString, T obj) {

		int status;
		try {
			status = writeJson(urlString, obj);
			return status;

		} catch (IOException e) {
			e.printStackTrace();
			return 500;
		}
	}		
	/**
	 * 
	 * @param <T>
	 * @param urlString
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	private static <T> int writeJson(String urlString, T obj) throws IOException {
		
		// 接続を開く
		URL url = new URL(urlString); 
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // POST リクエストを設定
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
		
		 // objを JSON に変換
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // モジュールを登録
        String json = mapper.writeValueAsString(obj);
        
        // リクエストボディに JSON を書き込む
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // レスポンスコードを取得
        int code = conn.getResponseCode();
        conn.disconnect();
        return code;
	}	

	// test
	public static void main(String[] args) throws Exception {

		String url = "http://localhost:8080/addbook";
		Book book = new Book("材料工学", LocalDate.of(2020, 4, 1), "田中宏","Science",5000,true);
		int status = Client.write(url, book);
		System.out.println("status=" + status);
		
		
		// ウェブサービスを呼び出して通貨レートを得る
		// 
		url = "https://openexchangerates.org/api/latest.json?app_id=2c2a442738aa4f37bf82054c3369eb14&base=USD&symbols=JPY,EUR,KRW";

		Exchange ratedata = Client.read(url, Exchange.class);
		System.out.println(ratedata);

		///////////
		// ローカルでmijserviceプロジェクトを起動して、レコードを取得する

        url = "http://localhost:8080/books";
        
		var books = Client.read(url, new TypeReference<List<Book>>() {});
		for (Book bk : books) {
			System.out.println(bk);
		}
		
		

	}

}

/**
 * 以下はmainのテスト用
 * 
 * JSONを受け取る汎用クラス
 * 
 */
class Exchange {
	private String disclaimer;
	private String license;
	private long timestamp;
	private String base;
	private HashMap<String, Double> rates;

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public HashMap<String, Double> getRates() {
		return rates;
	}

	public void setRates(HashMap<String, Double> rates) {
		this.rates = rates;
	}

	@Override
	public String toString() {
		return "base=" + base + ", \nrates=" + rates;
	}
}


