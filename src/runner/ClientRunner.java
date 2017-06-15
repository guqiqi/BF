package runner;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javafx.application.Application;
import javafx.stage.Stage;
import rmi.RemoteHelper;
import ui.MainFrame;

public class ClientRunner extends Application{
	static private RemoteHelper remoteHelper;
	MainFrame mainFrame = new MainFrame();
	
	private static void linkToServer() {
		try {
			remoteHelper = RemoteHelper.getInstance();
			remoteHelper.setRemote(Naming.lookup("rmi://127.0.0.1:8887/DataRemoteObject"));
			System.out.println("linked");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	public void test(){
		try {
			System.out.println(remoteHelper.getUserService().login("admin", "123456a"));
			System.out.println(remoteHelper.getIOService().writeFile("2", "admin", "testFile"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception{
		mainFrame.mainStage.show();
	}
	public static void main(String[] args) {
		ClientRunner.linkToServer();
	
		Application.launch(args);
	}
	
}
