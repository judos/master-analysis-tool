package launcher;

import javares.files.config.Config;
import model.ConfigSettings1;
import model.Data;
import view.MenuFrame;
import controller.DataProvider;
import controller.Main1;
import controller.Main1Interface;

/**
 * @since 18.11.2013
 * @author Julian Schelker
 */
public class LauncherAnalyzeTool {
	public static void main(String[] args) {
		Data data = new Data();
		Config config = new Config(new ConfigSettings1());
		MenuFrame frame = new MenuFrame(data, config);
		Main1 main1 = new Main1(data, config);
		frame.initAll((DataProvider) frame, (Main1Interface) main1);
	}
}