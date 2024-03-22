package tkxgame;

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

import tkxlib.App;
import tkxlib.Card;
import tkxlib.CardView;
import tkxlib.Shoe;

/**
 * 整数またはCardオブジェクトを使って複数のトランプのカードを表示する 表示位置をbias属性で指定できる
 * 
 * @Copyright 2024 WEBS/Takashi_Kawaba
 *
 */
public class Baccarat extends JFrame {
	/*
	 * 表示するカードを取り出したカードシュー。無限にカードを引ける。 カードを追加する時に使うため保管する BankerもPlayerも同じカードシューを持つ
	 */
	public Shoe shoe;
	/*
	 * 表示するカードのリスト 最初は空
	 */
	private ArrayList<Integer> cardList = new ArrayList<>();

	private static final int MIDDLE = 0; // 表示位置からの縦方向のバイアス
	private static final int ABOVE = -320; //
	private static final int BELOW = 320; //

	private String cl_owner; // クリックされたウィンドウのタイトル（biasname）
	private int cl_index; // クリックされたカードのインデックス番号
	private int cl_cardNumber; // クリックされてカードのカード番号

	private String biasname; // バイアスを表す名前 Windowタイトルとしても使う
	private int bias = MIDDLE; // 縦方向のバイアス値

	private int MAX_CARDNUMBER; // 表示するカードの枚数
	private int WINDOW_WIDTH = MAX_CARDNUMBER * (180 + 14) + 90; // ウィンドウの横幅
	private int WINDOW_HEIGHT = 320; // ウィンドウの縦幅
	private int CARD_WIDTH = 180; // カードの横幅
	private int CARD_HEIGHT = 240; // カードの縦幅

	private int displayedCardCount = 0; // 表示されたカードの枚数

	/**
	 * ******************************************************** コンストラクタ
	 * ・number_of_cardsのサイズで表示ようWindowを作成する ・マウスイベントリスナーを登録する ・タイマーイベントを登録し、実行する
	 * 0.3秒に一度、カードを表示する
	 * 
	 * @param biasname        タイトル＋表示位置
	 * @param number_of_cards 表示するカード枚数
	 * @param shoe            カードを取り出したカードシュー
	 * 
	 ************************************************************/
	private Baccarat(String biasname, int number_of_cards, Shoe shoe) {

		this.biasname = biasname;
		this.shoe = shoe;

		/*
		 * ***********************************
		 * 
		 * 上下の表示位置のバイアスを指定する。
		 * 
		 ************************************/
		String bn = biasname.toLowerCase();
		bias = switch (bn) {
		case "above", "banker" -> ABOVE;
		case "middle", "player" -> MIDDLE;
		case "below" -> BELOW;
		default -> MIDDLE;
		};
		/*
		 * ***********************************
		 * 
		 * ウィンドウを表示用にセットする
		 * 
		 *************************************/
		MAX_CARDNUMBER = number_of_cards;
		WINDOW_WIDTH = MAX_CARDNUMBER * (180 + 14) + 90; // ウィンドウの横幅
		WINDOW_HEIGHT = 320; // ウィンドウの縦幅

		setTitle("Cardview: " + biasname);

		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEADING, 20, 20));

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
		int x = (screenWidth - WINDOW_WIDTH) / 2;
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

					// カードシューの残り枚数を表示
					System.out.println("shoe_size =" + shoe.size());

					// ウィンドウ名、インデックス、カードの値を表示
					cl_owner = biasname;
					cl_index = clickedCardIndex;
					cl_cardNumber = cardList.get(clickedCardIndex);
					System.out.println("Clicked " + cl_owner + "/" + cl_index + ": " + cl_cardNumber);

					/*
					 * カードシューから一枚を引いて、表示リストに加える 全カードを再表示する
					 */
					display(shoe.deal());
				}
			}
		});
		/*
		 * ****************************************
		 * 
		 * タイマーイベントの登録・開始
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
				}
			}
		});
		timer.start();
	}

	/**
	 * ****************************************************************
	 * カードを表示リストに１枚追加し、 表示リストにあるカードをすべて表示する
	 * 
	 * @param cardNumber 追加するカード番号
	 *
	 *******************************************************************/
	public void display(int cardNumber) {

		/*
		 * クリックイベントで、必要以上にカードが追加されるのを防ぐ
		 */
		if (cardList.size() == MAX_CARDNUMBER) {
			return;
		}

		// 新規にカードを追加して、全カードリストを再表示する
		cardList.add(cardNumber);
		updateDisplay();
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

	/**
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
		URL imgURL = Baccarat.class.getClassLoader().getResource(path);

		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Could not find file: " + path);
			return null;
		}
	}

	/**
	 * *************************************************************************
	 * 
	 * 引数に指定したカードを表示する
	 * 
	 * maxは、追加で配るカードを表示できるようにウィンドウの横幅を あらかじめきめて おくために使う 表示するカードの数よりも大きいことがある
	 * 
	 * @param biasname 表示位置、タイトル
	 * @param list     表示するカードのリスト
	 * @param max      表示するカード枚数の最大値（Windowのサイズを決める）
	 * @param shoe     カードを取り出したカードシュー
	 * 
	 * @return このカードオブジェクトを返す（更新の時に使う）
	 * 
	 ****************************************************************************/
	public static Baccarat show(String biasname, List<Integer> list, int max, Shoe shoe) {
		var cards = new Baccarat(biasname, max, shoe);
		SwingUtilities.invokeLater(() -> {
			for (int c : list) {
				cards.display(c);
			}
			cards.setVisible(true);
		});
		return cards;
	}

	/**
	 * カードリストの得点を計算して返す
	 *
	 */
	public static int point(List<Integer> ls) {
		int point = 0;
		for (int c : ls) {
			int cardNumber = Card.toCardnumber(c);
			if (cardNumber < 11) {
				point += cardNumber;
			}
		}
		return point % 10;
	}

	//////////テスト用////////////////
	
	public static void play(String[] args) {

		var shoe = new Shoe();

		/*
		 * Playerのカード 点数計算 必要に応じてカードを＋１する
		 */
		var p = shoe.deal(2);
		int player = point(p);

		var b = shoe.deal(2);
		int banker = point(b);

		if (player >= 8 || banker >= 8) {
			Baccarat(p, player, b, banker, 1);
		} else if (player >= 6 && banker >= 6) {
			Baccarat(p, player, b, banker, 2);
		} else {
			// System.out.println("★player : " + player);
			int p3 = 0;
			if (player <= 5) {
				// カードを１増やす
				p3 = shoe.deal();
				p.add(p3);
				player = point(p);
				// System.out.println("★player<=5 ; " + player);
			}
			int dbg = 0;
			if (banker <= 5 && p3 == 0) {
				b.add(shoe.deal());
				dbg = 3;
			} else if (banker <= 2) {
				b.add(shoe.deal());
				dbg = 4;
			} else if (banker == 3 && p3 != 8) {
				b.add(shoe.deal());
				dbg = 5;
			} else if (banker == 4 && (p3 >= 2 && p3 <= 7)) {
				b.add(shoe.deal());
				dbg = 6;
			} else if (banker == 5 && (p3 >= 4 && p3 <= 7)) {
				b.add(shoe.deal());
				dbg = 7;
			} else if (banker == 6 && (p3 >= 6 && p3 <= 7)) {
				b.add(shoe.deal());
				dbg = 8;
			}
			banker = point(b);
			
			Baccarat(p, player, b, banker, dbg);
			
		}
	}

	public static void Baccarat(List<Integer> p, int player, List<Integer> b, int banker, int dbg) {

		/* 上下のバイアスを指定して表示（below=下に表示、above=上に表示） */
		var pw = CardView.show("PLAYER", p);
		App.stop(2);
		var bw = CardView.show("BANKER", b);
		App.stop(2);

		System.out.println("Banker=" + banker + " (" + dbg + ")");
		System.out.println("Player=" + player + " (" + dbg + ")");

		if (player == banker) {
			pw.display(1);
			bw.display(1);
		} else if (player <= banker) {
			bw.display(1);
		} else {
			pw.display(1);
		}

	}

}
