package tkxlib;

import java.awt.Desktop;
import java.net.URI;
/**
 * ウェブページを開く
 * @author kawaba
 *
 */
public class Browser {
	/**
	 * 指定したURIのウェブページを開く
	 * @param uriString		開くページのURI
	 */
	public static void open(String uriString) {
		Desktop desktop = Desktop.getDesktop();
		try {
			URI uri = new URI(uriString);
			desktop.browse(uri);
			
		} catch (Exception e) {
			display("https://k-webs.jp/");
		}		
	}
	public static void display(String uriString) {
		open(uriString);
	}
}
