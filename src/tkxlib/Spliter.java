package tkxlib;

import java.util.Arrays;
import java.util.List;
public class Spliter {
	/*
	 * デリミッタ＝空白、漢字空白、改行、タブ、コンマ
	 */
	private String delimiter="[\\s\u3000,]+";
	/**
	 * 分割してリストに入れて返す
	 * @param s
	 * @return
	 */
	public List<String> toStringList(String s){
		
		String[] words = s.strip().split(delimiter);
		return Arrays.asList(words);		
	}
	/**
	 * 分割のデリミッタをセットする
	 * @param delimiter
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter=delimiter;
	}
	/////////////////////////////////////////////////////
	
	/**
	 * 分割してリストに入れて返す
	 * @param s
	 * @return
	 */
	public static List<String> toList(String s) {
		var t = new Spliter();
		return t.toStringList(s);
	}
	/**
	 * 分割してリストに入れて返す
	 * @param s
	 * @param delimiter
	 * @return
	 */
	public static List<String> toList(String s, String delimiter) {
		var t = new Spliter();
		t.setDelimiter(delimiter);
		return t.toStringList(s);
	}
	/**
	 * 含まれるワードの個数を返す
	 * @param s
	 * @return
	 */
	public static int wordCount(String s) {
		return words(s, "[\\s\u3000,]+");
	}
	/**
	 * 含まれるワードの個数を返す
	 * @param s
	 * @param delimiter
	 * @return
	 */
	public static int words(String s, String delimiter) {
		var t = new Spliter();
		t.setDelimiter(delimiter);
		var ls = t.toStringList(s);
		// 長さが0以上の文字列だけの要素を数える
		return ls.stream().filter(e->e.length()>0).toList().size();
	}
	
	//////////// test ///////////
	public static void main(String[] args) {
		List<String> s = Spliter.toList("100  111 aaa ");
		System.out.println(s);
	}
}
