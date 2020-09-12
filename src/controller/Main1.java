package controller;

import javares.files.FileUtils;
import javares.files.config.Config;
import model.Data;

/**
 * @since 18.11.2013
 * @author Julian Schelker
 */
/**
 * @since 18.11.2013
 * @author Julian Schelker
 */
public class Main1 implements Main1Interface {

	private Config config;
	private Data data;

	public Main1(Data data, Config config) {
		this.config = config;
		this.data = data;
		FileUtils.checkOrCreateDir("data");
	}

	@Override
	public void closeMenu() {
		this.config.save();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		System.exit(0);
	}

	@Override
	public void importMinimap() {
		// handles everything of the import
		new ImportMiniMapController();
	}

	@Override
	public void importQuestions() {
		new ImportQuestionsController(data);
	}

}
