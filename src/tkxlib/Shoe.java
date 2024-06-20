package tkxlib;
/**
 * カードシューをシミュレートする
 * シューにカードがなくなると、あたらしいカードを52枚ランダムに追加する
 * カードシューからカードがなくなることはない
 * 
 * shoe.deal(5)  --- ディーラーが、5枚のカードを取り出して配る 
 * shoe.deal()   --- ディーラーが、1枚のカード取り出して配る
 * 
 * dealの代わりにdrawを使うこともできる
 * 
 * (c) WEBS/Takashi Kawaba 2004 
 * 
 */
import java.util.ArrayList;
import java.util.Collections;

public class Shoe {
	private ArrayList<Integer> shoe;

	public Shoe() {
		init();
	}
	public void init(){
		shoe = new ArrayList<Integer>();
		for (int i = 1; i <= 52; i++) {
			shoe.add(i);
		}
		// 要素をランダムにシャッフル
		Collections.shuffle(shoe);
	}
	/**
	 * ファクトリーメソッド
	 * @return　Shoeを返す
	 */
	public static Shoe create() {
		return new Shoe();
	}

	/**
	 * n枚のカードのリストを返す
	 * @param n
	 * @return
	 */
	public	ArrayList<Integer> deal(int n){
		var list = new ArrayList<Integer>();
		for(int i=0; i<n; i++) {
			list.add(draw());
		}
		return list;
	}
	
	/**
	 * １枚のカードを返す
	 * @return
	 */
	public Integer deal() {
		return draw();
	}
	public Integer draw() {
		if(shoe.isEmpty()) {
			init();
		}
		int n = shoe.remove(0);
		return n;
	}

	public int size() {
		return shoe.size();
	}
	
	// test
	public static void main(String[] args) {
		var shoe = Shoe.create();

	}
}	
