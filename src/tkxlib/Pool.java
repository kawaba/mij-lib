package tkxlib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/**
 * 
 * Iteratorを実装して、hasNext(), next()メソッドを使えるようにしたArrayList
 * of()メソッドも実装している
 * 
 * (C) T.Kawaba 2023
 *
 * @param <T> 要素の型
 */
public class Pool<T> extends ArrayList<T> implements Iterator<T> {

	private static final long serialVersionUID = 1L;
	/**
	 * pos は現在のindex
	 */
	private int pos = 0;
	
	/**
	 * of()メソッド。要素を列挙して、インスタンスを生成する
	 * @param <T>	要素型
	 * @param a		可変長引数の要素
	 * @return		Pool<T>型のインスタンスを返す
	 */
	public static <T>Pool<T> of(T... a){
		var pool = new Pool<T>(); 
		for(T t : a) {
			pool.add(t);
		}
		return pool;
	}
	
	/**
	 * ファイルパスから、Stringを要素とするPoolを作成して返す
	 * @param pathString　ファイルパス文字列
	 * @return	Pool<String> 
	 */
	public static Pool<String> from(String pathString) {
		var pool = new Pool<String>();
		try {
			Files.lines(Path.of(pathString)).forEach(pool::add);
			
		} catch (IOException e) {
			System.err.println("ファイルが見つかりません");
			e.printStackTrace();
		}
		return pool;
	}
	/**
	 * 
	 * @param <T>		要素型
	 * @param list		要素のArrayList
	 * @return			Pool<T>型のインスタンス
	 */
	public static <T>Iterator<T> with(List<T> list){
		var pool = new Pool<T>();
		list.forEach(e->pool.add(e));
		return pool;
	}

	/**
	 * 次の要素を返す。posはその後でインクリメントする
	 * @return 次の要素
	 */
	public T read() {
		return pos < size() ? get(pos++) : null;
	}
	/**
	 * read()の別名。
	 * 次の要素を返す。posはその後でインクリメントする
	 * @return 次の要素
	 */
	@Override
	public T next() {
		return read();
	}	
	/**
	 * まだ要素が残っているか
	 * @return 残っているときtrueを返す
	 */
	@Override
	public boolean hasNext() {
		return pos < size() ? true : false;
	}
	/**
	 * posを0に初期化する
	 */
	public void reset() {
		pos=0;
	}
	/**
	 * 空白をデリミッタとして分割したリストを返す
	 * @param s
	 * @return
	 */
	private static List<String> toList(String s) {
		String[] words = s.split("(\\s*,\\s*)|\n");
		return Arrays.asList(words);
	}
	/////////////// Examples //////////////////
	
	public static void main(String[] args) {

		/*
		var p = Pool.of("aaa", "bbb", "ccc");
		System.out.println(p);
		sub_1(p);
		p.reset();
		sub_2(p);
		p.reset();
		sub_3(p);
		*/
		sub_4();
	}
	private static void sub_4() {
		var p = new Pool().toList("aaa , bb, sss,aa");
		p.forEach(System.out::println);
	}

	// Tipical use
	private static void sub_1(Pool<String> p) {
		System.out.println("-- traditional");
		String line;
		while((line=p.read()) != null) {
			System.out.println(line);
		}
	}
	// with break
	private static void sub_2(Pool<String> p) {
		System.out.println("-- with break");
		while(true) {
			String line = p.read();
			if(line==null) {
				break;
			}
			System.out.println(line);
		}
	}
	// use Iterator
	private static void sub_3(Iterator<String> iterator) {
		System.out.println("-- Iterator");
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		
	}

	
	
}
