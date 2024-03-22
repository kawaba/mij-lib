package tkxlib;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Server {

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

	// test
	public static void main(String[] args) throws Exception {

		// ウェブサービスを呼び出して通貨レートを得る
		// 
		String url = "https://openexchangerates.org/api/latest.json?app_id=2c2a442738aa4f37bf82054c3369eb14&base=USD&symbols=JPY,EUR,KRW";

		Exchange ratedata = Server.read(url, Exchange.class);
		System.out.println(ratedata);

		///////////
		// ローカルでmijserviceプロジェクトを起動して、レコードを取得する

		url = "http://localhost:8080/book/1";
		Book book = Server.read(url, Book.class);
		System.out.println(book);

	}

}

/**
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

class Book {

	private Integer id;
	private String title;
	private LocalDate date;
	private String author;
	private String genre;
	private Integer price;
	private boolean stock;
	private List<String> sales;

	public String toCsv(List<String> sales) {
		if (!sales.isEmpty()) {
			return String.join(",", sales);
		} else {
			return "No Sales";
		}
	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getAuthor() {
		return author;
	}

	public String getGenre() {
		return genre;
	}

	public Integer getPrice() {
		return price;
	}

	public boolean isStock() {
		return stock;
	}

	public List<String> getSales() {
		return sales;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public void setStock(boolean stock) {
		this.stock = stock;
	}

	public void setSales(List<String> sales) {
		this.sales = sales;
	}

	@Override
	public String toString() {
		return "Book [Id=" + id + ", title=" + title + ", date=" + date + ", author=" + author + ", genre=" + genre
				+ ", price=" + price + ", stock=" + stock + ", sales=" + sales + "]";
	}

}
