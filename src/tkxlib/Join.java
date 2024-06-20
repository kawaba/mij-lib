package tkxlib;

/**
 * 文字列を連結してCSV文字列を作る
 * 
 * @author T.Kawaba
 *
 */
public class Join {
	private String csv;
	private String delimiter;

	public Join(String str, String delimiter) {
		this.csv = str;
		this.delimiter = delimiter;
	}

	public Join(String delimiter) {
		this.csv = "";
		this.delimiter = delimiter;
	}

	public Join() {
		this.csv = "";
		this.delimiter = ",";
	}

	public String add(String s) {
		csv += csv.isEmpty() ? s : delimiter + s;
		return csv;
	}

	public String addBR() {
		csv += "<br>";
		return csv;
	}

	public String csv() {
		return csv;
	}
}
