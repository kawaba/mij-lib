package tkxlib;

import java.util.Random;

/*
 * 整数乱数を返す
 * Anynumber.random()    -- 1,2,3,4,5,6,7,8,9,10 のどれか
 * Anynumber.roulette()  -- 0から32のどれか
 * Anynumber.card()      -- 1から13のどれか
 * Anynumber.cards()     -- 1から52のどれか
 * Anynumber.dice()      -- 1から6のどれか
 * Anynumber.random(max) -- 1からmax のどれか
 */
public class Anynumber {
	
	private static Random rd;
	private static void initialize() {
		rd = new Random(System.currentTimeMillis());
	}
	public static int random() {
		return random(10);
	}
	public static int roulette() {
		return random(33) - 1;
	}
	public static int card() {
		return random(13);
	}
	public static int cards() {
		return random(52);
	}	
	public static int dice() {
		return random(6);
	}	
	public static int random(int max) {
		if(rd==null)		initialize();
		return rd.nextInt(max) + 1;
	}
	
	public static void main(String[] args) {
		for(int n=0; n<20; n++) {
				System.out.print(Anynumber.dice()+ " ");
		}
	}
}
