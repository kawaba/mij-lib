package tkxlib;
/**
 * サイコロをシミュレートする
 */
import java.util.Random;

class DiceObj {
	private Random rd;
	public DiceObj() {
		rd = new Random(System.currentTimeMillis());
	}
	public int roll() {
		return rd.nextInt(6) + 1;
	}
}

public class Dice {
	public static DiceObj dice; // 1,2,3,4,5,6のどれかを返す
    /**
     * swingでアニメーションを表示し、値を返す
     * アニメーションの動作はパラメータを指定して変更できる
     * 
     * @return　サイコロの目数
     */
    public static int roll() {
        if(dice==null)	dice = new DiceObj();
    	int number = dice.roll();
    	
    	/////////////////////////////////////
    	Animator.disp("d-"+ number + ".png");
    	/////////////////////////////////////
    	
    	return number;
    }   	
	/**
	 * 値を返すだけ
	 * @return　サイコロの目数
	 */
    public static int rollx() {
        if(dice==null)	dice = new DiceObj();
        return dice.roll();
    }

    // テスト用
    public static void main(String[] args) {
    	System.out.println(Dice.roll());

    }

}





