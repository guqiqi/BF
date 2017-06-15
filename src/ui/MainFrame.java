package ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;

public class MainFrame {
	public Stage mainStage = new Stage();
	private Parent root;
	public MainFrame(){
		try {
			root = FXMLLoader.load(getClass().getResource("mainFrame.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		Scene scene = new Scene(root, 789, 580);
		mainStage.setResizable(false);
		mainStage.initStyle(StageStyle.DECORATED);  
		mainStage.setScene(scene);
	}
}
