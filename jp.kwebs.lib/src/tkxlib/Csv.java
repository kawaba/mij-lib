package tkxlib;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Csv implements Iterator<String>{
	
	private List<String> items;
	/**
	 * pos は現在のindex
	 */
	private int pos = 0;	
	
	public Csv(String csvString) {
		/*
		 * "(\\s*,\\s*)|\n" は、
		 * 0文字以上の空白文字で囲まれたコンマ、または改行文字の位置で分割する
		 * という区切り文字指定
		 */
		var array = csvString.split("(\\s*,\\s*)|\n");
		items = Arrays.asList(array);
	}
	
	public String get(int index) {
		return items.get(index);
	}
	
	public int getInt(int index) {
		return Integer.parseInt(get(index));
	}
	
	public double getDouble(int index) {
		return Double.parseDouble(get(index));
	}
	
	public int size() {
		return items.size();
	}

	public List<String> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return items.toString();
	}

	@Override
	public boolean hasNext() {
		return pos < size() ? true : false;
	}

	@Override
	public String next() {
		return pos < size() ? get(pos++) : null;
	}
	public int nextInt() {
		return Integer.parseInt(next());
	}
	public double nextDouble() {
		return Double.parseDouble(next());
	}
	/**
	 * posを0に初期化する
	 */
	public void reset() {
		pos=0;
	}
}
