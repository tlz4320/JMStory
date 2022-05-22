package cn.treeh;

import cn.treeh.Util.Configure;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import cn.treeh.JMStory;
import org.lwjgl.util.lz4.LZ4;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowSizeLimits(Configure.screenWidth, Configure.screenHeight,
				Configure.screenWidth, Configure.screenHeight);
		config.setTitle("JMStory");
		new Lwjgl3Application(new JMStory(), config);
	}
}
