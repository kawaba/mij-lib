package tkxlib;
/**
 * 画像を、GUIウィンドウの中でアニメーションで表示する
 * ライブラリとして使う場合は、画像データはwebフォルダに入れておくこと
 * 
 * display() --- 拡大
 * disp() ------ 拡大＋回転
 * 
 * 2024.1.15 ver1.0 T.kawaba with ChatGPT
 * 2024.2.26 ver2.0 T.kawaba  Swingのスレッドで起動するように改訂
 */

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

/*
 * Iconイメージの生成方法を持つインタフェース
 * リソースから生成するか、ローカルフォルダから生成するか
 * ２つの方法を切り替えるために作成
 */
interface CreateIcon{
	ImageIcon create(String imgPath);
}
/*
 * リソースフォルダから生成する実装
 */
class CreateIconFromResourece implements CreateIcon {
	@Override
	public ImageIcon create(String imgPath) {
        // クラスローダーを使用してリソースを取得
        URL imgURL = Animator.class.getClassLoader().getResource(imgPath);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Could not find file: " + imgPath);
            return null;
        }
	}
}
/*
 * ローカルフォルダから生成する実装
 */
class CreateIconImpl implements CreateIcon {
	@Override
	public ImageIcon create(String imgPath) {
		return new ImageIcon(imgPath);
	}
}
public class Animator {

    
    /**
     * 回転しながらズームするアニメーション
     * 
     * @param icon					表示する画像イメージ
     * @param _frameWidth			GUIウィンドウのサイズ
     * @param _initialWidth			画像イメージの初期表示サイズ
     * @param _targetWidth			画像イメージの最終表示サイズ
     * @param _animationDuration	アニメーションする時間
     * @param _rotationAngle		総回転角度
     * @param _delay				完了してから綴じるまでの時間
     */
    public void disp(String icon, int _frameWidth, int _initialWidth, int _targetWidth, int _animationDuration, double _rotationAngle, int _delay, CreateIcon ci) {

        JFrame frame = new JFrame("Image animation");
        frame.setSize(_frameWidth, _frameWidth);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // アイコンの初期サイズ
        int initialWidth = _initialWidth;

        // アイコンの読み込み
        ImageIcon originalIcon = ci.create(icon); 

        // アイコンの初期表示サイズを設定
        Image img = originalIcon.getImage().getScaledInstance(initialWidth, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(img);

        // ラベルにアイコンを設定
        JLabel label = new JLabel(scaledIcon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        // フレームにラベルを追加
        frame.add(label);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // タイマーでアニメーションを制御
        int targetWidth = _targetWidth;
        int animationDuration = _animationDuration; // 0.5秒間
        double rotationAngle = _rotationAngle; // 360度回転

        Timer timer = new Timer(10, new ActionListener() {
            private long startTime = -1;
            private boolean animationFinished = false; // アニメーションが終了したかどうかのフラグ

            @Override
            public void actionPerformed(ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;

                if (elapsedTime >= animationDuration) {
                    if (!animationFinished) {
                        // アニメーションが終了したら一度だけ実行
                        animationFinished = true;

                        // 元の画像を横幅 targetWidth ピクセルで表示
                        Image originalImage = originalIcon.getImage().getScaledInstance(targetWidth, -1, Image.SCALE_SMOOTH);
                        ImageIcon originalScaledIcon = new ImageIcon(originalImage);
                        label.setIcon(originalScaledIcon);

                        // タイマーで_delay秒後にウィンドウを閉じる処理を追加
                        Timer closeTimer = new Timer(_delay, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                frame.dispose(); // ウィンドウを閉じる
                                ((Timer) e.getSource()).stop();
                                
            					/////////////////////////////////////////////////////
            					// 呼び出したスレッドを再開させるためにnotify
                                synchronized (Animator.this) {
                                	Animator.this.notify();
                                }
                                /////////////////////////////////////////////////////	                                
                                
                            }
                        });
                        closeTimer.start();
                    }
                    ((Timer) e.getSource()).stop();
                    
                } else {
                    double progress = (double) elapsedTime / animationDuration;
                    int currentWidth = (int) (initialWidth + progress * (targetWidth - initialWidth));

                    // アイコンの拡大
                    Image currentImage = originalIcon.getImage().getScaledInstance(currentWidth, -1, Image.SCALE_SMOOTH);
                    ImageIcon currentIcon = new ImageIcon(currentImage);

                    // アイコンの回転
                    double angle = progress * rotationAngle;
                    AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(angle), currentIcon.getIconWidth() / 2.0, currentIcon.getIconHeight() / 2.0);
                    BufferedImage rotatedImage = new BufferedImage(currentIcon.getIconWidth(), currentIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = rotatedImage.createGraphics();
                    g2d.setTransform(transform);
                    g2d.drawImage(currentImage, 0, 0, null);
                    g2d.dispose();

                    label.setIcon(new ImageIcon(rotatedImage));
                }
            }
        });

        timer.start();
    }
 
    /**
     * ズームするだけのアニメーション
     * 
     * @param icon
     * @param _frameWidth
     * @param _initialWidth
     * @param _targetWidth
     * @param _animationDuration
     * @param _delay
     * @param ci
     */
    public void display(String icon, int _frameWidth, int _initialWidth, int _targetWidth, int _animationDuration, int _delay, CreateIcon ci) {
    	System.out.println("★display(String icon, int _frameWidth, int _initialWidth, int _targetWidth, int _animationDuration, int _delay, CreateIcon ci)");
    	
    	JFrame frame = new JFrame("Image animation");
    	frame.setSize(_frameWidth, _frameWidth);
        //frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // アイコンの初期サイズ
        int initialWidth = _initialWidth;

        // アイコンの読み込み
        ImageIcon originalIcon = ci.create(icon);		// 画像データを得る

        // アイコンの初期表示サイズを設定
        Image img = originalIcon.getImage().getScaledInstance(initialWidth, -1, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(img);

        // ラベルにアイコンを設定
        JLabel label = new JLabel(scaledIcon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        // フレームにラベルを追加
        frame.add(label);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // タイマーでアニメーションを制御
        int targetWidth = _targetWidth;
        int animationDuration = _animationDuration; // 0.5秒間

        Timer timer = new Timer(10, new ActionListener() {
            private long startTime = -1;
            private boolean animationFinished = false; // アニメーションが終了したかどうかのフラグ

            @Override
            public void actionPerformed(ActionEvent e) {
                if (startTime < 0) {
                    startTime = System.currentTimeMillis();
                }

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;

                if (elapsedTime >= animationDuration) {
                	if (!animationFinished) {
                		animationFinished = true;
                		
						// タイマーで_delay秒後にウィンドウを閉じる処理を追加
						Timer closeTimer = new Timer(_delay, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								frame.dispose(); // ウィンドウを閉じる
								((Timer) e.getSource()).stop();

                                
            					/////////////////////////////////////////////////////
            					// 呼び出したスレッドを再開させるためにnotify
                                synchronized (Animator.this) {
                                	Animator.this.notify();
                                }
                                /////////////////////////////////////////////////////	                                
                                
 
							}
						});
						closeTimer.start();
                	}
                	((Timer) e.getSource()).stop();
                	
                } else {
                    double progress = (double) elapsedTime / animationDuration;
                    int currentWidth = (int) (initialWidth + progress * (targetWidth - initialWidth));

                    Image currentImage = originalIcon.getImage().getScaledInstance(currentWidth, -1, Image.SCALE_SMOOTH);
                    ImageIcon currentIcon = new ImageIcon(currentImage);
                    label.setIcon(currentIcon);
                }
            }
        });

        timer.start();
    }

    ///////////////////////////////////////////////////////////
	//////////////// ライブラリ用 /////////////////////////////
    ///////////////////////////////////////////////////////////	
    /**
     * 回転しながらズームするアニメーション
     * リソースからIconを生成して表示する
     * 表示時間は1000ミリ秒
     * 
     * @param icon		表示する画像イメージのパス
     */
    public static void disp(String icon){
		disp(icon,1000);
	}
    /**
     * 回転しながらズームするアニメーション
     * リソースからIconを生成して表示する
     * 表示時間をミリ秒単位で指定する
     * 
     * @param icon		表示する画像イメージのパス
     * @param delay		表示する時間
     */
    public static void disp(String icon, int delay){
    	var animator = new Animator();
    	
		// swingのスレッドで画像を表示する
		SwingUtilities.invokeLater(() -> {
			animator.disp(icon, 500, 50, 300, 500, 360.0, delay, new CreateIconFromResourece());
		});    	
		
        // イベント処理が終わるまで待つ
        synchronized (animator) {
            try {
            	animator.wait();	// animatorが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }		
	}
    /**
     * ズームするだけのアニメーション
     * リソースからIconを生成して表示する
     * 表示時間は2000ミリ秒
     * 
     * @param icon
     */
    private static void display(String icon) {
    	display(icon, 2000);
    }
    private static void display(String icon, int delay) {
    	
    	var animator = new Animator();
    	
		// swingのスレッドでカードを表示する
		SwingUtilities.invokeLater(() -> {
			animator.display(icon, 500, 50, 300, 500, delay, new CreateIconFromResourece());
		});
		
        // イベント処理が終わるまで待つ
        synchronized (animator) {
            try {
            	animator.wait();	// animatorが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
        
    	
    }      
    
    ///////////////////////////////////////////////////////////
	//////////////// 公開メソッド用 ///////////////////////////
    ///////////////////////////////////////////////////////////	
    
    /**
     * 回転しながらズームするアニメーション
     * ローカルフォルダからIconを生成して表示する
     * 表示時間は1000ミリ秒
     * 
     * @param icon		表示する画像イメージのパス
     */
    public static void show(String icon){
		show(icon, 1000);
	}
    /**
     * ローカルフォルダからIconを生成して表示する
     * 表示時間をミリ秒単位で指定する
     * 
     * @param icon		表示する画像イメージのパス
     * @param delay		表示する時間
     */
    public static void show(String icon, int delay){
    	var animator = new Animator();
    	
		// swingのスレッドで画像を表示する
		SwingUtilities.invokeLater(() -> {
			animator.disp(icon, 500, 50, 300, 500, 360.0, delay, new CreateIconImpl());
		});    	
		
        // イベント処理が終わるまで待つ
        synchronized (animator) {
            try {
            	animator.wait();	// animatorが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }    	
		
	}
    
    /**
     * ズームするだけのアニメーション
     * ローカルフォルダからIconを生成して表示する
     * 表示時間は1000ミリ秒
     * 
     * @param icon		表示する画像イメージのパス
     */
    public static void zooming(String icon){
		zooming(icon, 1000);
	}
    /**
     * ローカルフォルダからIconを生成して表示する
     * 表示時間をミリ秒単位で指定する
     * 
     * @param icon		表示する画像イメージのパス
     * @param delay		表示する時間
     */
    public static void zooming(String icon, int delay){
    	var animator = new Animator();
    	
		// swingのスレッドでカードを表示する
		SwingUtilities.invokeLater(() -> {
			animator.display(icon, 500, 50, 300, 500, delay, new CreateIconImpl());
		});
		
        // イベント処理が終わるまで待つ
        synchronized (animator) {
            try {
            	animator.wait();	// animatorが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }      	
		
	}    

    /* *****************************
     *    テスト用 
     *******************************/
    public static void main(String[] args) {
    	String icon = "1.png";
        disp(icon);
        display(icon);

    }
 }
