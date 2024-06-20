package tkxlib;
import java.time.LocalDate;
import java.util.List;

/*
 * 本を表すレコードです
 * 例題CalculatingExample.java、SummarizingExample.java で使います
 * クイズや演習問題でも使用します。
 */

public class Book implements Comparable<Book>{
	
	private Integer id;			// 番号　（DBでは自動生成）
	private String title;		// 書名
	private LocalDate date;		// 出版日
	private String author;		// 著者
	private String genre;		// 分野
	private int price;			// 価格
	private boolean stock;		// 在庫の有無
	
	public Book() {}
	
	// DB用
	public Book(String title, LocalDate date, String author, String genre, int price, boolean stock) {
		this.id = null;
		this.title = title;
		this.date = date;
		this.author = author;
		this.genre = genre;
		this.price = price;
		this.stock = stock;
	}

	// List用
	public Book(Integer id, String title, LocalDate date, String author, String genre, int price, boolean stock) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.author = author;
		this.genre = genre;
		this.price = price;
		this.stock = stock;
	}

	public static List<Book> getList(){
		var list = List.of(
			new Book(1,"情報倫理",LocalDate.of(2020, 3,10),"小川洋子","OTHER",1250,true),
			new Book(2,"テンプル騎士団", LocalDate.of(2023, 7,1),"水野昭二","HISTORY",1600,true),
			new Book(3,"材料工学",LocalDate.of(2021, 10,21),"田中宏","SCIENCE",3000,true),
			new Book(4,"スポーツ統計",LocalDate.of(2021, 4,11),"新森明子","SCIENCE",2100,true),
			new Book(5,"太平記縁起",LocalDate.of(2021, 1,7),"佐藤秀夫","NOVEL",1500,true),
			new Book(6,"データ分析",LocalDate.of(2022, 4,12),"千田正樹","SCIENCE",1800,true),
			new Book(7,"社会保障政策",LocalDate.of(2022, 9,20),"浦中恵子","OTHER",2200,false),
			new Book(8,"社会経済史",LocalDate.of(2023, 6,11),"木村花子","HISTORY",2200,true),
			new Book(9,"イスラム建国史",LocalDate.of(2023, 11,8),"吉村敬","HISTORY",1800,true),
			new Book(10,"鋳物の化学",LocalDate.of(2023, 2,10),"田中宏","SCIENCE",3200,true),
			new Book(11,"健康科学のはなし",LocalDate.of(2022, 7,13),"角田圭吾","SCIENCE",1200,true),
			new Book(12,"世界の鉱山",LocalDate.of(2023, 1,13),"田中宏","OTHER",2300,true),
			new Book(13,"日本史",LocalDate.of(2024, 2,1),"木村花子","HISTORY",2000,true),
			new Book(14,"正覚寺",LocalDate.of(2024, 4,10),"田中一郎","NOVEL",1000,false),
			new Book(15,"粉末冶金科学",LocalDate.of(2023, 8,8),"田中宏","SCIENCE",2800,false)	);
		return list;
	}

	@Override
	public int compareTo(Book other) {
		return Integer.compare(id, other.id);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isStock() {
		return stock;
	}

	public void setStock(boolean stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", date=" + date + ", author=" + author + ", genre=" + genre
				+ ", price=" + price + ", stock=" + stock + "]";
	}
	

}
