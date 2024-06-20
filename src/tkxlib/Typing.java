package tkxlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

public class Typing {
	/**
	 * コンソールから複数行のテキストを入力する
	 * 
	 * @return　入力したテキスト
	 */
	public static String text() {
		return text("Text", 600, 100, 16);
	}
	public static String text(String title) {
		return text(title, 600, 100, 16);
	}
	/**
	 * コンソールから複数行のテキストを入力する
	 * 
	 * @param s		入力ガイド
	 * @return		入力したテキスト
	 */
	public static String text(String title, int width, int height, int fontSize) {
		
        // KeyboardInputクラスのインスタンスを生成
        var application = new KeyboardTextInput(title, width, height, fontSize);

        // メソッドを呼び出してGUIを表示
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                application.showGUI();
            }
        });

        // イベント処理が終わるまで待つ
        synchronized (application) {
            try {
                application.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // showGUIメソッドで取得したデータを取得
        return application.getInputData();
	}

	/**
	 * コンソールから複数行のテキストを入力する
	 * 
	 * @return 入力したテキストのリスト（両端の空白は除去）
	 */
	public static ArrayList<String> lines(String title) {
		// テキストを取得
		String str = text(title);
		
		// 改行で分割
		String[] lines = str.split("[\r\n]+");
		var list = new ArrayList<String>();
		
		// 空行を除去してリストに追加
		for (var s : lines) {
			if (!s.strip().isEmpty()) {
				list.add(s.strip());
			}
		}
		return list;
	}
	public static ArrayList<String> lines() {
		return lines("Text to List");
	}
	/**
	 * コンソールから文字を入力する
	 * 
	 * @return 入力した文字（１文字）
	 */
	public static char charValue() {
		return charValue("char");
	}

	/**
	 * コンソールから文字を入力する
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力した文字（１文字）
	 */
	public static char charValue(String s) {

		char c = 0;
		String str = getStr(s);
		if (str == null)
			return 0;
		try {
			c = str.charAt(0);

		} catch (Exception e) {
			c = 0;
		}
		return c;
	}

	/**
	 * コンソールからbyteを入力する
	 * 
	 * @return 入力したbyte
	 */
	public static byte byteValue() {
		return byteValue("byte");
	}

	/**
	 * コンソールからbyteを入力する
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力したbyte
	 */
	public static byte byteValue(String s) {

		byte b = 0;
		String str = getStr(s);
		if (str == null)
			return 0;
		try {
			b = Byte.parseByte(str);

		} catch (Exception e) {
			b = 0;
		}
		return b;

	}

	/**
	 * コンソールからshort整数を入力する
	 * 
	 * @return 入力した整数
	 */
	public static short shortValue() {
		return shortValue("short");
	}

	/**
	 * コンソールからshort整数を入力する
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力した整数
	 */
	public static short shortValue(String s) {

		short n = 0;
		String str = getStr(s);
		if (str == null)
			return 0;
		try {
			n = Short.parseShort(str);

		} catch (Exception e) {
			n = 0;
		}
		return n;

	}

	/**
	 * コンソールから整数を入力する
	 * 
	 * @return 入力した整数
	 */
	public static int intValue() {
		return intValue("int");
	}

	/**
	 * コンソールから整数を入力する
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力した整数
	 */
	public static int intValue(String s) {

		int n = 0;
		String str = getStr(s);
		/*
		 * エンターキーだけの入力では0を返す
		 */
		if (str == null) {
			return 0;
		} 

		/*
		 * 数値にパースする。非数値だった場合は0を返す
		 */
		try {
			n = Integer.parseInt(str);

		} catch (Exception e) {
			n = 0;
		}
		return n;

	}

	/**
	 * 16進数形式でのbyte値の入力
	 * 
	 * @return 入力したbyte値
	 */
	public static byte hexValue() {
		return hexValue("Hex");
	}

	/**
	 * 16進数形式でのbyte値の入力
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力したbyte値
	 */
	public static byte hexValue(String s) {
		byte n = 0;
		String str = getStr(s);

		/*
		 * エンターキーだけの入力では0を返す
		 */
		if (str == null) {
			return 0;
		}
		/*
		 * 2文字以上あると、先頭の2文字だけを使う
		 */
		if (str.length() > 2) {
			str = str.substring(0, 2);
		}
		try {
			n = (byte) Long.parseLong(str, 16);

		} catch (Exception e) {
			n = 0;
		}
		return n;
	}

	/*
	 * 2進数形式でのbyte値の入力
	 * 
	 */
	/**
	 * 2進数形式でのbyte値の入力
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力したbyte値
	 */
	public static byte binaryValue() {
		return binaryValue("Bin");
	}

	public static byte binaryValue(String s) {
		byte n = 0;
		String str = getStr(s);
		/*
		 * エンターキーだけの入力では0を返す
		 */
		if (str == null) {
			return 0;
		}
		/*
		 * 8文字以上あると、先頭の8文字だけを使う
		 */
		if (str.length() > 8) {
			str = str.substring(0, 8);
		}
		try {
			n = (byte) Long.parseLong(str, 2);

		} catch (Exception e) {
			n = 0;
		}
		return n;
	}

	/**
	 * コンソールからlong整数を入力する
	 * 
	 * @return 入力した整数
	 */
	public static long longValue() {
		return longValue("long");
	}

	/**
	 * コンソールからlong整数を入力する
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力した整数
	 */
	public static long longValue(String s) {

		long n = 0;
		String str = getStr(s);
		if (str == null)
			return 0L;
		try {
			n = Long.parseLong(str);

		} catch (Exception e) {
			n = 0;
		}
		return n;

	}

	/**
	 * コンソールからDoubleを入力する
	 * 
	 * @return 入力した実数
	 */
	public static double doubleValue() {
		return doubleValue("double");
	}

	/**
	 * コンソールからDoubleを入力する
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力した実数
	 */
	public static double doubleValue(String s) {

		double x = 0;
		String str = getStr(s);
		if (str == null)
			return 0.0;
		try {
			x = Double.parseDouble(str);

		} catch (Exception e) {
			x = 0.0;
		}
		return x;

	}

	/**
	 * コンソールからFloatを入力する
	 * 
	 * @return 入力した実数
	 */
	public static float floatValue() {
		return floatValue("float");
	}

	/**
	 * コンソールからFloatを入力する
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力した実数
	 */
	public static float floatValue(String s) {

		float x = 0f;
		String str = getStr(s);
		if (str == null)
			return 0.0f;
		try {
			x = Float.parseFloat(str);

		} catch (Exception e) {
			x = 0.0f;
		}
		return x;

	}

	/**
	 * コンソールからbooleanを入力する<br/>
	 * true 以外はすべてfalseとなる
	 * 
	 * @return true または false（既定値）
	 */
	public static boolean booleanValue() {
		return booleanValue("boolean");
	}

	/**
	 * コンソールからbooleanを入力する<br/>
	 * true 以外はすべてfalseとなる
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return true または false（既定値）
	 */
	public static boolean booleanValue(String s) {

		String str = getStr(s);
		if (str != null && str.trim().toLowerCase().equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * コンソールから文字列を入力する
	 * 
	 * @return 入力した文字列
	 */
	public static String stringValue() {

		return stringValue("String");
	}

	/**
	 * コンソールから文字列を入力する
	 * 
	 * @param s 表示する入力ガイド文字列
	 * @return 入力した文字列
	 */
	public static String stringValue(String s) {

		String str = getStr(s);

		if (str == null || str.length() == 0) {
			return null;

		} else {
			return str;
		}
	}
	/* ******************************************************************
		
		複数のデータを入力する
		パース出来ないデータは0になる
		
	*********************************************************************/
	/**
	 * 複数の整数を入力する
	 * @return	Pool型のインスタンス
	 */
	public static Iterator<Integer> intValues(){
		return  intValues("Integers");
	}
	/**
	 * 複数の整数を入力する
	 * @param s		入力ガイド文字列	
	 * @return		Pool型のインスタンス
	 */
	public static Iterator<Integer> intValues(String s){
		var ls = toGenericList(getStringList(s), Integer::parseInt, 0);
		return Pool.with(ls);
	}
	/**
	 * 複数の浮動小数点数を入力する
	 * @return	Pool型のインスタンス
	 */
	public static Iterator<Double> doubleValues(){
		return  doubleValues("Doulbes");
	}
	/**
	 * 複数の浮動小数点数を入力する
	 * @param s		入力ガイド文字列	
	 * @return		Pool型のインスタンス
	 */
	public static Iterator<Double> doubleValues(String s){
		var ls = toGenericList(getStringList(s), Double::parseDouble, 0.0);
		return Pool.with(ls);
	}	
	/**
	 * 複数の長整数を入力する
	 * @return	Pool型のインスタンス
	 */
	public static Iterator<Long> longValues(){
		return  longValues("Longs");
	}
	/**
	 * 複数の長整数を入力する
	 * @param s		入力ガイド文字列	
	 * @return		Pool型のインスタンス
	 */
	public static Iterator<Long> longValues(String s){
		var ls = toGenericList(getStringList(s), Long::parseLong, 0L);
		return Pool.with(ls);
	}
	/**
	 * 複数の文字列を入力する
	 * @return	Pool型のインスタンス
	 */
	public static Iterator<String> stringValues(){
		return  stringValues("Strings");
	}
	/**
	 * 複数の文字列を入力する
	 * @param s		入力ガイド文字列	
	 * @return		Pool型のインスタンス
	 */
	public static Iterator<String> stringValues(String s){
		var ls = getStringList(s);
		return Pool.with(ls);
	}
		
	
	////////////////////////////////////////////////////////////////////
	

	/**
	 * GUIウィンドウで文字列を入力する
	 * 
	 * @param s
	 * @return
	 */
	private static String getStr(String s) {

        // WindowsLookAndFeelSampleクラスのインスタンスを生成
        KeyboardInput application = new KeyboardInput(s);

        // メソッドを呼び出してGUIを表示
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                application.showGUI();
            }
        });

        // イベント処理が終わるまで待つ
        synchronized (application) {
            try {
                application.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // showGUIメソッドで取得したデータを取得
        String inputData = application.getInputData();
        return inputData;
	}
	/**
	 * GUIウィンドウで
	 * 空白で区切られた複数の値からなる文字列を入力すると
	 * 文字列のリストに変換して返す
	 * 
	 * @param s		複数の値を含む文字列
	 * @return		文字列のリスト
	 */
	private static List<String> getStringList(String s) {
		String str = getStr(s);
		String[] words = str.split("[\\s\u3000]+");
		return Arrays.asList(words);
	}
	/**
	 * Stringの要素を持つリストをT型の要素を持つリストに変換して返す
	 * 
	 * @param <T>				変換先の要素型
	 * @param list				変換するStringのリスト
	 * @param mapper			StringをTに変換するFunction
	 * @param defaultValue		変換できない場合に返すデフォルト値
	 * @return					T型の要素に変換したリスト
	 */
	private static <T> List<T> toGenericList(List<String> list, Function<String, T> mapper, T defaultValue) {
	    return list.stream()
	            .map(el -> {
	                try {
	                    return mapper.apply(el);
	                } catch (NumberFormatException e) {
	                    return defaultValue;
	                }
	            })
	            .collect(Collectors.toList());
	}	
	
	//////////////////////////////////////////////////////

	public static void main(String[] args) {
		
	//	sub21();
	//	sub22();
	//	sub23();
	//	sub24();
		sub25();
	}
	public static void sub21() {
		var p = Typing.intValues();
		while(p.hasNext()) {
			System.out.print( p.next() + " ");
		}
	}
	public static void sub22() {
		var p = Typing.doubleValues();
		while(p.hasNext()) {
			System.out.print( p.next() + " ");
		}
	}
	public static void sub23() {
		var p = Typing.longValues();
		while(p.hasNext()) {
			System.out.print( p.next() + " ");
		}
	}
	public static void sub24() {
		var p = Typing.stringValues();
		while(p.hasNext()) {
			System.out.print( p.next() + " ");
		}
	}
	public static void sub25() {
		var p = Typing.lines("");
		p.forEach(System.out::println);
		System.out.println(p.size());
	}	
	public static void sub1() {
		String str = Typing.stringValue();
		System.out.println("★"+str);
		double x = Typing.doubleValue();
		System.out.println("★"+x);
		int age = Typing.intValue("年齢");
		System.out.println("★"+ age);
	}

}
