
package controller;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javares.files.FileUtils;
import javares.files.OpenFileDialog;
import javares.files.openImageDialog.OpenImageDialog;
import javares.gui.RequestInput;
import model.ConfigSettings1;


/**
 * @since 18.11.2013
 * @author Julian Schelker
 */
public class ImportMiniMapController {

	private File minimapImage;
	private File minimapXml;
	private Exception exceptionOccured = null;
	private String nameMinimap;

	public ImportMiniMapController() {
		try {
			FileUtils.checkOrCreateDir("data/minimaps");
			openDialogAndFindFiles();
			requestNameForMinimap();
			copyAndNameFiles();
		} catch (Exception e) {
			e.printStackTrace();
			this.exceptionOccured = e;
		}
	}

	private void copyAndNameFiles() throws IOException {
		try {
			FileUtils.copyFile(minimapImage, new File("data/minimaps/" + nameMinimap
				+ "/0." + FileUtils.getExtension(minimapImage)));
			FileUtils.copyFile(minimapXml, new File("data/minimaps/" + nameMinimap + "/"
				+ "0.xml"));
		} finally {}
	}

	public Exception getExceptionIfOccured() {
		return this.exceptionOccured;
	}

	private void openDialogAndFindFiles() throws Exception {
		Component parent = null;
		String lastPath = ConfigSettings1.lastMinimapFolder.getString();
		File lastDir = new File(lastPath);

		OpenImageDialog dialog = new OpenImageDialog(parent,
			"Choose your minimap file from CryEngine", lastDir);
		if (dialog.isCancelled())
			throw new Exception("User cancelled dialog.");

		this.minimapImage = dialog.getSelectedFile();
		lastDir = minimapImage.getParentFile();
		ConfigSettings1.lastMinimapFolder.setValue(lastDir.getPath());
		if (FileUtils.getExtension(minimapImage).equals("tif")) {
			System.out.println("Tif should be converted to png first.");
		}

		OpenFileDialog dialog2 = new OpenFileDialog(parent,
			"Choose your xml minimap config file from CryEngine (has the same name)",
			lastDir, new String[] { "xml" });
		if (dialog2.isCancelled())
			throw new Exception("User cancelled dialog.");
		this.minimapXml = dialog2.getSelectedFile();

	}

	private void requestNameForMinimap() {
		this.nameMinimap = RequestInput.getString("Enter a name for this minimap:");
	}

}
