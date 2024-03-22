package tkxlib;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 * 整数またはCardオブジェクトを使って複数のトランプのカードを表示する 表示位置をbias属性で指定できる
 * 
 * @Copyright 2024 WEBS/Takashi_Kawaba
 *
 */
public class CardView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 表示位置を決めるデータ
	 */
	private static final int MIDDLE = 0; 			// 表示位置からの縦方向のバイアス
	private static final int ABOVE = -320; 			//
	private static final int BELOW = 320; 			//
	
	private static final int MAX_SIZE = 5;			// 表示できる最大カード枚数
	
	private static final int WINDOW_HEIGHT = 320; 	// ウィンドウの縦幅
	private static final int CARD_WIDTH = 180; 		// カードの横幅
	private static final int CARD_HEIGHT = 240; 		// カードの縦幅
	
	private int 	number_of_cards; 					// 表示するカードの枚数
	private int 	windows_width; 						// ウィンドウの横幅
	private String	biasname; 							// バイアスを表す名前 Windowタイトルとしても使う
	private int 	bias = MIDDLE;						// 縦方向のバイアス値
	/*
	 * カードシュー（表示するカードを取り出した）
	 * ・重複なく無限にカードを引ける。 
	 * ・カードを追加する時に使うため保管する 
	 * ・ゲーム参加者は同じカードシューを持つようにすること
	 * 
	 * ・ウィンドをクリックした時、
	 * 　shoeからカードを取り出し表示する処理が必要な場合に
	 * 　setterでshoeにインスタンスをセットする
	 * 　それ以外では、nullが入っている
	 */
	private Shoe shoe;	
	/**
	 * setterでしかセットできない
	 * @param shoe
	 */
	public void setShoe(Shoe shoe) {
		this.shoe = shoe;
	}
	
	/*
	 * 表示するカードのリスト。最初は空
	 */
	private ArrayList<Integer> cardList = new ArrayList<>();
	/*
	 * クリックイベントでセットされるデータ
	 */
	private String	cl_owner; 					// クリックされたウィンドウのタイトル（biasname）
	private int 	cl_index; 					// クリックされたカードのインデックス番号
	private int	cl_cardNumber; 				// クリックされてカードのカード番号
	private int 	displayedCardCount = 0; 	// 表示されたカードの枚数

	/**
	 * ********************************************************************** 
	 * コンストラクタ
	 * ・number_of_cardsのサイズで表示ようWindowを作成する 
	 * ・マウスイベントリスナーを登録する 
	 * ・タイマーイベントを登録し、実行する -- 0.3秒に一度、カードを表示する
	 * 
	 * @param biasname        タイトル＋表示位置
	 * @param number_of_cards 表示するカード枚数
	 * 
	 *************************************************************************/
	public CardView(String biasname, int number_of_cards) {

		this.biasname = biasname;

		/*
		 * ***********************************
		 * 
		 * 上下の表示位置のバイアスを指定する。
		 * 
		 ************************************/
		String bn = biasname.toLowerCase();
		bias = switch (bn) {
			case "above", "banker" 	-> ABOVE;
			case "middle", "player" -> MIDDLE;
			case "below" 			-> BELOW;
			default 				-> MIDDLE;
		};
		/*
		 * ***********************************
		 * 
		 * ウィンドウを表示用にセットする
		 * 
		 * 
		 *************************************/
		this.number_of_cards = number_of_cards;
		
		windows_width = this.number_of_cards * (180 + 14) + 90; // ウィンドウの横幅

		setTitle(biasname);

		setSize(windows_width, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEADING, 20, 20));
		//setLayout(null);

		// Windowsのlook and feelを設定
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 座標を設定してウィンドウを表示
		int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		int x = (screenWidth - windows_width) / 2;
		int y = (screenHeight - WINDOW_HEIGHT) / 2 + bias; // 中央からbiasピクセル下に表示
		setLocation(x, y);
		/*
		 * ****************************************
		 * 
		 * マウスイベントの登録
		 * 
		 *****************************************/
		addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {

				// どのカードがクリックされたかインデックスを得る
				int x = evt.getX();
				int cardWidthWithMargin = CARD_WIDTH + 20; // カードの横幅 + マージン
				int clickedCardIndex = (x) / cardWidthWithMargin;

				// インデックスが正当なら処理を実行する
				if (clickedCardIndex >= 0 && clickedCardIndex < cardList.size()) {

					// ウィンドウ名、インデックス、カードの値を表示
					cl_owner = biasname;
					cl_index = clickedCardIndex;
					cl_cardNumber = cardList.get(clickedCardIndex);
					System.out.println("Clicked " + cl_owner + "/" + cl_index + ": " + cl_cardNumber);

					/*
					 * カードシューから一枚を引いて、表示リストに加える 全カードを再表示する
					 * フィールドにShoeオブジェクトが必要なので、setterでセットしておく
					 */
					if(shoe!=null) {
						display(shoe.deal());
					}
				}
			}
		});
		/*
		 * ****************************************
		 * 
		 * タイマーイベントの登録・開始
		 * 
		 * displayedCardCountは初期値は0
		 * displayedCardCountがゼロの場合、updateDisplay()は、1枚のカードを表示する。
		 * updateDisplay()のループはゼロオリジンなので。
		 * 
		 * 登録してあるカード枚数よりも表示済カード枚数（displayedCardCount）が小さい場合は、
		 * 　　　0.3秒に一回、
		 * 　　　updateDisplay()を実行してdisplayedCardCount枚のカードを表示し
		 * 		 displayedCardCountを１増やす
		 *       ⇒繰り返し処理になる
		 * 
		 * updateDisplay()は、表示をクリアしてdisplayedCardCount枚までのカードを表示する
		 * 
		 * 
		 *****************************************/
		Timer timer = new Timer(300, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (displayedCardCount < cardList.size()) {
					updateDisplay();
					displayedCardCount++;
				} else {
					((Timer) e.getSource()).stop(); // 表示するカードがなくなったらタイマーを停止
					
					/////////////////////////////////////////////////////
					// 呼び出したスレッドを再開させるためにnotify
                    synchronized (CardView.this) {
                    	CardView.this.notify();
                    }
                    /////////////////////////////////////////////////////					
				}
			}
		});
		timer.start();
	}
	/** **********************************************************************
	 * show_card()メソッド
	 * 
	 * displayメソッドを使って、カードリストをウィンドウに表示する
	 * 
	 * @param list　表示するカードのリスト
	 * 
	 ************************************************************************/
	public void show_cards(List<Integer> list) {
		cardList.addAll(list);
		//updateDisplay();	// タイマーで実行するので不要
		setVisible(true);
	}
	
	/**
	 * ****************************************************************
	 * display()メソッド
	 * 
	 * カードを表示リストに１枚追加し、表示リストにあるカードをすべて表示する
	 * 
	 * @param cardNumber 追加するカード番号
	 *
	 *******************************************************************/
	public void display(int cardNumber) {

		/*
		 * クリックイベントで、必要以上にカードが追加されるのを防ぐ
		 */
		if (cardList.size() == number_of_cards) {
			return;
		}

		// 新規にカードを追加して、全カードリストを再表示する
		cardList.add(cardNumber);
		updateDisplay();	// タイマーイベントは終了しているので、これは必要。
	}
	public void displayWin() {
		display(60);
	}
	public void displayTie() {
		display(61);
	}
	
	/**
	 * ウィンドウを綴じる
	 * 
	 */
	public void close() {
		 dispose();	
	}
	/*
	 * ****************************************************************
	 * 
	 * 表示リストにあるカードをすべて表示する
	 *
	 *******************************************************************/
	private void updateDisplay() {
		getContentPane().removeAll(); // コンテンツをクリア

		for (int i = 0; i <= displayedCardCount; i++) {
			int cardNumber = cardList.get(i);
			
			ImageIcon cardImage = createImageIcon(cardNumber + ".png");
			Image scaledImage = cardImage.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
			cardImage = new ImageIcon(scaledImage);
			
			JLabel cardLabel = new JLabel(cardImage);
			getContentPane().add(cardLabel);
		}
		revalidate();
		repaint();
	}
	
	/*
	 * ****************************************************************
	 * 
	 * jarファイル内のリソースにアクセスするメソッド
	 * 
	 * @param path
	 * @return
	 * 
	 ******************************************************************/
	protected ImageIcon createImageIcon(String path) {
		// クラスローダーを使用してリソースを取得
		URL imgURL = CardView.class.getClassLoader().getResource(path);

		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Could not find file: " + path);
			return null;
		}
	}
	/**
	 * *************************************************************************
	 * show()メソッド
	 * 
	 * 引数に指定したカードを表示する
	 * 表示可能な最大枚数はMAX_SIZE。これを超えるカードは削除される。 
	 * 
	 * @param biasname 表示位置、タイトル
	 * @param list     表示するカードのリスト
	 * @param shoe     マウスクリックでカードを追加表示するためのカードシュー
	 * 
	 * @return このカードオブジェクトを返す（更新の時に使う）
	 * 
	 ****************************************************************************/	
	public static CardView show(String biasname, List<Integer> list, Shoe shoe) {
		
		// 表示枚数の制限
		if(list.size()>MAX_SIZE) {
			list.subList(MAX_SIZE, list.size()).clear();
		}
		
		// cardsインスタンスを生成する 表示幅はカード枚数＋１、Shoeはstatic
		var cards = new CardView(biasname, list.size()+1);
		cards.setShoe(shoe);
		
		// swingのスレッドでカードを表示する
		// 表示終了を待たずにmainスレッドに直ちにリターンする
		SwingUtilities.invokeLater(() -> {
			cards.show_cards(list);
		});
		
		//////////////////////////////////////////////////////
        // イベント処理が終わるまで待つ
        synchronized (cards) {
            try {
            	cards.wait();	// cardsが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //////////////////////////////////////////////////////		
		return cards;
	}
	/**
	 * *************************************************************************
	 * show()メソッド
	 * 
	 * 引数に指定したカードを表示する
	 * 
	 * maxは、追加で配るカードを表示できるようにウィンドウの横幅を
	 * あらかじめきめておくために使う。表示するカードの数よりも大きいことがある
	 * 
	 * @param biasname 表示位置、タイトル
	 * @param list     表示するカードのリスト
	 * 
	 * @return このカードオブジェクトを返す（更新の時に使う）
	 * 
	 ****************************************************************************/
	public static CardView show(String biasname, List<Integer> list) {
		
		return show(biasname, list, null);

	}
	
	/** *************************************************************************
	 * 簡易版 show()メソッド
	 * 
	 * カードを表示するだけの機能
	 * 
	 * @param list
	 * @return
	 ****************************************************************************/
	public static CardView show(List<Integer> list) {
		return CardView.show("Cards", list);
		
	}
	public static CardView show(List<Integer> list, Shoe shoe) {
		return CardView.show("Cards", list, shoe);
		
	}
	
	/// テスト
	public static void main(String[] args) {
		var cards = List.of(22,31,5,6,8);
		CardView.show(cards);
	}

}
