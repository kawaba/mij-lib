package tkxlib;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Chooser {
	
	private List<String> flist;
	private String dir;
	
	
	public List<String> getFlist() {
		return flist;
	}
	public String getDir() {
		return dir;
	}
	
	private void fileDialog() {
        try {
            // Windows風のLook and Feelを設定
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
        
        fileChooser.setDialogTitle("ファイルを選択してください");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            
            List<String> filePaths = new ArrayList<>();
            for (File file : selectedFiles) {
                filePaths.add(file.getAbsolutePath());
            }
            flist = filePaths;
        } else {
            flist = null;
        }
		/////////////////////////////////////////////////////
		// 呼び出したスレッドを再開させるためにnotify
        synchronized (this) {
        	this.notify();
        }
        /////////////////////////////////////////////////////	         
    }
	private  void directoryDialog() {
        try {
            // Windows風のLook and Feelを設定
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); 
        
        fileChooser.setDialogTitle("ディレクトリを選択してください");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            dir = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            dir = null;
        }
		/////////////////////////////////////////////////////
		// 呼び出したスレッドを再開させるためにnotify
        synchronized (this) {
        	this.notify();
        }
        /////////////////////////////////////////////////////	            
    }    
	public static String getAbsDir(String file) {
		return Path.of(file).getParent().toAbsolutePath().toString(); // 親ディレクトリのPathを取得    
    }

    public static List<String> fDialog() {
    	var chooser = new Chooser();
		SwingUtilities.invokeLater(() -> {
			chooser.fileDialog();
		});    	
		
        // イベント処理が終わるまで待つ
        synchronized (chooser) {
            try {
            	chooser.wait();	// animatorが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }     	
    	return chooser.getFlist();
    }
    public static List<String> toFlist(List<String> absList) {
    	var list = new ArrayList<String>();
    	for(String fname : absList) {
    		list.add(Path.of(fname).getFileName().toString());
    	}
    	return list;
    }
    
    
    public static String dDialog() {
    	var chooser = new Chooser();
		SwingUtilities.invokeLater(() -> {
			chooser.directoryDialog();
		});    	
		
        // イベント処理が終わるまで待つ
        synchronized (chooser) {
            try {
            	chooser.wait();	// animatorが処理の最後にnotify()するまで、このスレッドを停止する	
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }     	
    	return chooser.getDir();
    }    
    
    public static void main(String[] args) {
        // ファイルを選択するダイアログを表示
        List<String> selectedFiles = fDialog();
        if (selectedFiles != null) {
            System.out.println("選択されたファイル:");
            for (String file : selectedFiles) {
                System.out.print(file + " / ");
                System.out.println(getAbsDir(file));
            }
        } else {
            System.out.println("ファイルが選択されませんでした。");
        }
        
        var ls = toFlist(selectedFiles);
        for(String name : ls) {
        	System.out.println(name);
        }
    }
}
