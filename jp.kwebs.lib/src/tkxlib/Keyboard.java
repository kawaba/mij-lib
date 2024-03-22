package tkxlib;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Keyboard {
	private  int keyCode=0;
	
    public  int _keyCode() {
    	_waiting();
    	return keyCode;
    }
    
    public  boolean _isTrue() {
    	while(keyCode!=KeyEvent.VK_ENTER && keyCode!=KeyEvent.VK_ESCAPE&& keyCode!=KeyEvent.VK_SPACE) {
    		_waiting();
    	}
    	return switch(keyCode) {
    				case	KeyEvent.VK_ENTER, 
    						KeyEvent.VK_ESCAPE	->	true;
    				default						->	false;
    			};
    }    	
	
    public  void _waiting() {
		
    	keyCode = 0;
    	
        // JFrameを作成
        JFrame frame = new JFrame("Non-Visible Window Example");

        // キー入力を待つKeyListenerを追加
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // キーがタイプされたときの処理
                keyCode = e.getKeyChar();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // キーが押されたときの処理
                //System.out.println("Key Pressed: " + e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // キーが離されたときの処理
                //System.out.println("Key Released: " + e.getKeyChar());
            }
        });

        // JFrameの表示設定
        frame.setUndecorated(true);
        frame.setSize(1, 1);  			// ウィンドウサイズを1x1に設定
        frame.setVisible(true);
        frame.setFocusable(true);		// JFrameがフォーカスを持つようにする

        while(true) {
        	if(keyCode!=0) {
            	break;
            }
            // 少し待機
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }            
        }
    	frame.dispose();

    }

    public static void waiting(){
    	var kb = new Keyboard();
    	kb._waiting();
    }
    public static boolean isTrue() {
    	var kb = new Keyboard();
    	return kb._isTrue();
    }
    public static int keyCode() {
    	var kb = new Keyboard();
    	return kb._keyCode();
    	
    }
}
