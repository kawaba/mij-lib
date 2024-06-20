package tkxlib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;

class KeyboardTextInput{

    private String inputData; // テキストデータを保持するフィールド
    private JFrame frame;
    private String prompt;
    private int 	width;
    private int		height;
    private int		fontSize;


    public KeyboardTextInput() {
        this("Text", 600, 100, 16);
    }    
    
    public KeyboardTextInput(String prompt) {
        this(prompt, 600, 100, 16);
    }
    
    public KeyboardTextInput(String prompt, int width, int height, int fontSize) {
    	this.prompt = prompt;
    	this.width = width;
    	this.height = height;
    	this.fontSize = fontSize;
    }
    public synchronized void showGUI() {
        // Windows風のlook and feelを設定します。
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // フレームを作成します。
        frame = new JFrame("Keyboard Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // フレームにボーダーを設定します。
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // パネルを作成します。
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // ラベルを作成します。
        JLabel label = new JLabel(prompt);
        label.setFont(new Font("メイリオ", Font.PLAIN, fontSize)); // フォントサイズを変更

        // テキストエリアを作成します。
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("メイリオ", Font.PLAIN, fontSize)); // フォントサイズを変更
        textArea.setLineWrap(true); // テキストが領域外に出た場合に自動で改行

        // スクロールペインを作成してテキストエリアを配置します。
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(width, height)); // スクロールペインのサイズを調整

        // ボタンを作成します。
        JButton button = new JButton("Enter");
        button.setBorder(BorderFactory.createRaisedBevelBorder()); // 立体的な影をつける
        button.setFont(new Font("メイリオ", Font.BOLD, fontSize)); // フォントサイズを変更
        
        // ボタンのUIを変更して背景色を設定する
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.LIGHT_GRAY); // ボタンの背景色を薄いグレーに設定
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
                super.paint(g2, c);
                g2.dispose();
            }
        });
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // テキストエリアの入力データを取得します。
                inputData = textArea.getText();

                // ウィンドウを閉じます。
                frame.dispose();

                // メインスレッドを再開させるためにnotify
                synchronized (KeyboardTextInput.this) {
                    KeyboardTextInput.this.notify();
                }
            }
        });
        
     // コントロールキーとエンターキーが同時に押されたときの処理を追加します。
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
                    // テキストエリアの入力データを取得します。
                    inputData = textArea.getText();

                    // ウィンドウを閉じます。
                    frame.dispose();

                    // メインスレッドを再開させるためにnotify
                    synchronized (KeyboardTextInput.this) {
                        KeyboardTextInput.this.notify();
                    }
                }
            }
        });

        // パネルにコンポーネントを追加します。
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        // フレームにパネルを追加します。
        frame.add(panel);

        // フレームを表示します。
        frame.pack();
        frame.setLocationRelativeTo(null); // ウィンドウを画面の中央に配置
        frame.setVisible(true);
    }

    public String getInputData() {
        return inputData;
    }

    public static void main(String[] args) {
        // KeyboardInputクラスのインスタンスを生成
        var application = new KeyboardTextInput();

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
        System.out.println("mainメソッドで取得したデータ: " + inputData);
    }
}

