package tkxlib;

public class App {
	public static void stop(double n) {
		try {
		    // 一定時間停止（例: 1000ミリ秒＝1秒）
		    Thread.sleep((long)(n * 1000));
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
	public static void wait(double n) {
		stop(n);
	}
}
