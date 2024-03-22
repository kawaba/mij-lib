package tkxlib;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

class KeyboardInput {

    private String inputData; // テキストデータを保持するフィールド
    private JFrame frame;
    private String prompt;

    public KeyboardInput(String prompt) {
    	this.prompt=prompt;
    }
    public KeyboardInput() {
    	this("Data");
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
        JLabel label = new JLabel(prompt+":");
        label.setFont(new Font("メイリオ", Font.PLAIN, 16)); // フォントサイズを変更

        // テキストフィールドを作成します。
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 30)); // テキストフィールドの高さを調整
        textField.setFont(new Font("メイリオ", Font.PLAIN, 16)); // フォントサイズを変更

        // Enter キーが押されたときの処理を追加
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // テキストフィールドの入力データを取得します。
                    inputData = textField.getText();

                    // テキストフィールドの入力データを返します。
                    //System.out.println("入力データ: " + inputData);

                    // Windowsを閉じます。
                    frame.dispose();

                    // メインスレッドを再開させるためにnotify
                    synchronized (KeyboardInput.this) {
                        KeyboardInput.this.notify();
                    }
                }
            }
        });

        // ボタンを作成します。
        JButton button = new JButton("Enter");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // テキストフィールドの入力データを取得します。
                inputData = textField.getText();

                // テキストフィールドの入力データを返します。
                //System.out.println("入力データ: " + inputData);

                // Windowsを閉じます。
                frame.dispose();

                // メインスレッドを再開させるためにnotify
                synchronized (KeyboardInput.this) {
                    KeyboardInput.this.notify();
                }                
            }
        });
        button.setFont(new Font("メイリオ", Font.PLAIN, 16)); // フォントサイズを変更

        // パネルにコンポーネントを追加します。
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);

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
        // WindowsLookAndFeelSampleクラスのインスタンスを生成
        KeyboardInput application = new KeyboardInput();

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
