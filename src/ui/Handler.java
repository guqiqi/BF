package ui;

import java.rmi.RemoteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import rmi.RemoteHelper;

public class Handler {
	//此时文件类型
	private static String type = "";
	//保存时候文件名读取方法：1，新建之后从tfFileName中获得；2，读取文件时从fileList中获得
	private static int state = 0;
	
	//-------------------------------正常运行界面--------------------------------
	@FXML
	private Pane codePane;
	
	//菜单栏file
	@FXML
	private MenuItem menuNewBF;
	@FXML
	private MenuItem menuNewOok;
	@FXML
	private MenuItem menuOpen;
	@FXML
	private MenuItem menuSave;
	@FXML
	private MenuItem menuExit;
	
	//菜单栏run
	@FXML
	private Menu menuRun;
	@FXML
	private MenuItem menuExecute;

	//菜单栏version对应的3个版本
	@FXML
	private MenuItem menuLastOne;
	@FXML
	private MenuItem menuLastTwo;
	@FXML
	private MenuItem menuLastThree;
	
	//菜单栏more对应的两个按钮
	@FXML
	private MenuItem menuUndo;
	@FXML
	private MenuItem menuRedo;
	
	//运行按钮
	@FXML
	private Button btRun;
	//两个输入栏和一个输出栏
	@FXML
	private TextArea taCode;
	@FXML
	private TextField tfInput;
	@FXML
	private TextField tfResult;
	//右下角的用户信息
	@FXML //退出可能要自己写
	private Label logOut;
	@FXML
	private Label userName;

	//----------------------------------注册和登录界面---------------------------------------
	@FXML
	private Pane logAndRegisterPane;
	@FXML
	private PasswordField tfPassword;
	@FXML
	private TextField tfUserName;
	@FXML
	private Button btLogIn;
	@FXML
	private Button btRegister;
	
	//------------------------------------提示界面-----------------------------------------
	@FXML
	private Pane promptPane;
	@FXML
	private Button btPromptOk;
	@FXML
	private Text textPrompt;
	
	//------------------------------------新建文件界面---------------------------------------
	@FXML
	private Pane newPane;
	@FXML
	private Button btCreateNew;
	@FXML
	private Button btCancelCreateNew;
	@FXML
	private TextField tfFileName;

	//------------------------------------选择文件界面---------------------------------------
	@FXML
	private Pane openFilePane;
	@FXML
	private Button btOpenFile;
	@FXML
	private Button btCancelOpenFile;
	@FXML
	private ComboBox<String> fileList;
	
	//----------------------------------此处开始有事件处理------------------------------------
	//------------------------------------菜单相关-----------------------------------------
	//创建时候直接确定文件名称（要出现一个命名的提示框）
	@FXML
	private void onMenuNewBF(ActionEvent event){
		type = "bf";
		
		tfFileName.setText("");
		newPane.setVisible(true);
	}
	@FXML
	private void onMenuNewOok(ActionEvent event){
		type = "ook";
		
		tfFileName.setText("");
		newPane.setVisible(true);
	}
	
	@FXML
	private void onCreateNew(ActionEvent event){
		state = 1;
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");
		
		menuLastOne.setDisable(true);
		menuLastTwo.setDisable(true);
		menuLastThree.setDisable(true);
		
		boolean flag = false;

		if(tfFileName.getText().equals("")){
			promptPane.setVisible(true);
			textPrompt.setText("No file name. Please rename it.");

			return;
		}

		if(type.equals("bf")){
			if(tfFileName.getText().equals(".bf")){
				promptPane.setVisible(true);
				textPrompt.setText("No file name. Please rename it.");

				return;
			}
			else if(tfFileName.getText().length() > 3){
				if(tfFileName.getText().substring(tfFileName.getText().length() - 4).equals(".ook")){
					promptPane.setVisible(true);
					textPrompt.setText("You must create a .bf file");
				
					return;
				}
			}
		}
		else if(type.equals("ook")){
			if(tfFileName.getText().equals(".ook")){
				promptPane.setVisible(true);
				textPrompt.setText("No file name. Please rename it.");
				
				return;
			}
			else if(tfFileName.getText().length() > 4){
				if(tfFileName.getText().substring(tfFileName.getText().length() - 3).equals(".bf")){
					promptPane.setVisible(true);
					textPrompt.setText("You must create a .ook file");
				
					return;
				}
			}
		}
		
		String fileName = getFileName();
			
		try {
			flag = RemoteHelper.getInstance().getIOService().createFile(tfUserName.getText(), fileName);
		} catch (RemoteException e) {
			flag = false;
			e.printStackTrace();
		}
		
		if(flag == true){
			newPane.setVisible(false);
			taCode.setDisable(false);
			tfInput.setDisable(false);
			tfResult.setDisable(false);
		}
		else{
			promptPane.setVisible(true);
			textPrompt.setText("Duplicate filenames. Please rename it.");
		}
	}
	@FXML
	private void onCancelCreateNew(ActionEvent event){
		newPane.setVisible(false);
	}
	//打开时候要出现一个显示当前用户的所有文件的下拉框以供选择
	//-------------------file------------------------
	@FXML
	private void onMenuOpen(ActionEvent event){
		String[] files = null;
		
		//出现所有的文件名
		try {
			files = RemoteHelper.getInstance().getIOService().getFileList(tfUserName.getText());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(files.length == 0){
			promptPane.setVisible(true);
			textPrompt.setText("There is no file. Please create one.");
		}
		else{
			openFilePane.setVisible(true);
			fileList.getItems().clear();
			fileList.setValue(files[0]);
			for(int i = 0; i < files.length; i ++){
				fileList.getItems().add(files[i]);
			}
		}
	}
	@FXML
	private void onOpenFile(ActionEvent event){
		state = 2;
		
		openFilePane.setVisible(false);
		//代码、输入、输出改变
		taCode.setDisable(false);
		tfInput.setDisable(false);
		tfResult.setDisable(false);
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");
		
		try {
			int versionNumber = RemoteHelper.getInstance().getIOService().getVersionNumber(tfUserName.getText(), fileList.getValue());
			
			if(versionNumber >= 3){
				menuLastOne.setDisable(false);
				menuLastTwo.setDisable(false);
				menuLastThree.setDisable(false);
			}
			else if(versionNumber == 2){
				menuLastOne.setDisable(false);
				menuLastTwo.setDisable(false);
				menuLastThree.setDisable(true);
			}
			else if(versionNumber == 1){
				menuLastOne.setDisable(false);
				menuLastTwo.setDisable(true);
				menuLastThree.setDisable(true);
			}
			else{
				menuLastOne.setDisable(true);
				menuLastTwo.setDisable(true);
				menuLastThree.setDisable(true);
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			String file = RemoteHelper.getInstance().getIOService().readFile(tfUserName.getText(), fileList.getValue());
			
			if(file.split("~").length == 1)
				taCode.setText(file);
				
			else if(file.split("~").length == 2){
				taCode.setText(file.split("~")[0]);
				tfInput.setText(file.split("~")[1]);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	private void onCancelOpenFile(ActionEvent event){
		openFilePane.setVisible(false);
	}
	@FXML
	private void onMenuSave(ActionEvent event){
		try {
			RemoteHelper.getInstance().getIOService().writeFile
				(taCode.getText() + "~" + tfInput.getText() + "\r\n", tfUserName.getText(), getFileName());
			
			int versionNumber = RemoteHelper.getInstance().getIOService().getVersionNumber(tfUserName.getText(), getFileName());
			
			if(versionNumber >= 3){
				menuLastOne.setDisable(false);
				menuLastTwo.setDisable(false);
				menuLastThree.setDisable(false);
			}
			else if(versionNumber == 2){
				menuLastOne.setDisable(false);
				menuLastTwo.setDisable(false);
				menuLastThree.setDisable(true);
			}
			else if(versionNumber == 1){
				menuLastOne.setDisable(false);
				menuLastTwo.setDisable(true);
				menuLastThree.setDisable(true);
			}
			else{
				menuLastOne.setDisable(true);
				menuLastTwo.setDisable(true);
				menuLastThree.setDisable(true);
			}
		} catch (RemoteException e) {
			promptPane.setVisible(true);
			textPrompt.setText("Failure!");
		}
	}
	
	private String getFileName(){
		String fileName = tfFileName.getText();
		
		if(state == 1){
			if(type.equals("bf")){
				if(fileName.length() > 3){
					if((!fileName.substring(fileName.length() - 3).equals(".bf")))
						fileName = fileName + "." + type;
				}
				else
					fileName = fileName + "." + type;
			}
			else if(type.equals("ook")){
				if(fileName.length() > 4){
					if((!fileName.substring(fileName.length() - 4).equals(".ook")))
						fileName = fileName + "." + type;
				}
				else
					fileName = fileName + "." + type;
			}
		}
		else if(state == 2){
			fileName = fileList.getValue();
		}
		
		return fileName;
	}
	
	//提示是否确定退出
	@FXML
	private void onMenuExit(ActionEvent event){
		System.exit(0);
	}
	//-------------------run-------------------------
	@FXML
	private void onRun(ActionEvent event){
		System.out.println("run");
		String result = "";
		try {
			result = RemoteHelper.getInstance().getExecuteService().execute(taCode.getText(), tfInput.getText());
		} catch (RemoteException e) {
			result = "Error!";
		}
		
		if(result.equals("Error!")){
			promptPane.setVisible(true);
			textPrompt.setText("Error!");
			
			tfResult.setText("");
		}
		else
			tfResult.setText(result);
	}
	//-------------------version---------------------
	@FXML
	private void onLastOne(ActionEvent event){
		//代码、输入、输出改变
		taCode.setDisable(false);
		tfInput.setDisable(false);
		tfResult.setDisable(false);
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");	
		
		try {
			String file = RemoteHelper.getInstance().getIOService().getSpecifiedVersion(1, tfUserName.getText(), getFileName());
	
			if(file.split("~").length == 1)
				taCode.setText(file);
				
			else if(file.split("~").length == 2){
				taCode.setText(file.split("~")[0]);
				tfInput.setText(file.split("~")[1]);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@FXML
	private void onLastTwo(ActionEvent event){
		taCode.setDisable(false);
		tfInput.setDisable(false);
		tfResult.setDisable(false);
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");	
		
		try {
			String file = RemoteHelper.getInstance().getIOService().getSpecifiedVersion(2, tfUserName.getText(), getFileName());
		
			if(file.split("~").length == 1)
				taCode.setText(file);
				
			else if(file.split("~").length == 2){
				taCode.setText(file.split("~")[0]);
				tfInput.setText(file.split("~")[1]);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	private void onLastThree(ActionEvent event){
		taCode.setDisable(false);
		tfInput.setDisable(false);
		tfResult.setDisable(false);
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");	
		
		try {
			String file = RemoteHelper.getInstance().getIOService().getSpecifiedVersion(3, tfUserName.getText(), getFileName());
		
			if(file.split("~").length == 1)
				taCode.setText(file);
				
			else if(file.split("~").length == 2){
				taCode.setText(file.split("~")[0]);
				tfInput.setText(file.split("~")[1]);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//---------------------more-----------------------
	@FXML
	private void onUndo(ActionEvent event){
		System.out.println("undo");
	}
	@FXML
	private void onRedo(ActionEvent event){
		System.out.println("redo");
	}
	//---------------------------------登录相关--------------------------------------
	@FXML
	private void onLogIn(ActionEvent event){
		boolean flag = false;
		
		//temp表示错误的不同原因，0是异常，1是没有该用户,2是密码错误，3是没填满
		int temp = 0;
		
		if(tfUserName.getText().equals("") || tfPassword.getText().equals("")){
			temp = 3;
		}
		else{
			try {
				flag = RemoteHelper.getInstance()
						.getUserService().login(tfUserName.getText(), tfPassword.getText());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	
		if(flag == true){
			logAndRegisterPane.setVisible(false);
			codePane.setVisible(true);
			
			promptPane.setVisible(true);
			textPrompt.setText("You must create or open a new file first");
			
			userName.setText(tfUserName.getText());
		}
		else{
			try {
				temp = RemoteHelper.getInstance()
						.getUserService().logInError(tfUserName.getText(), tfPassword.getText());
			} catch (RemoteException e) {
				temp = 0;
			}
			
			if(temp == 1)
				textPrompt.setText("We don't have a user in this name. Please register one.");
			else if(temp == 2)
				textPrompt.setText("Please confirm your password.");
			else if(temp == 3)
				textPrompt.setText("Please fill both name and password blanks");
			
			promptPane.setVisible(true);
		}
		
		taCode.setDisable(true);
		tfInput.setDisable(true);
		tfResult.setDisable(true);
	}
	@FXML
	private void onRegister(ActionEvent event){
		System.out.println("register");
		boolean flag = false;
		
		if(tfUserName.getText().equals("") || tfPassword.getText().equals("")){
			if(tfUserName.getText().equals("") && tfPassword.getText().equals(""))
				textPrompt.setText("sorry, Please fill both name and password blank");
			else if(tfUserName.getText().equals(""))
				textPrompt.setText("sorry, please enter your name");
			else
				textPrompt.setText("sorry, please enter your password");
		}
		else{
			try {
				flag = RemoteHelper.getInstance()
					.getUserService().register(tfUserName.getText(), tfPassword.getText());
			} catch (RemoteException e) {
				flag = false;
			}
			
			if(flag == true){
				textPrompt.setText("Congratulation. You have created your account. Please log in now.");
			}
			else{	
				textPrompt.setText("sorry, we have got a user in this name.");
			}
		}
		
		promptPane.setVisible(true);
	}
	@FXML
	private void onLogOut(MouseEvent handler){
      	codePane.setVisible(false);
        logAndRegisterPane.setVisible(true);
        
        taCode.setDisable(true);
        tfInput.setDisable(true);
        tfResult.setDisable(true);
        
        menuLastOne.setDisable(true);
		menuLastTwo.setDisable(true);
		menuLastThree.setDisable(true);
          
        tfUserName.setText("");
        tfPassword.setText("");
	}
	
	//--------------------------------提示界面只有一个OK按钮-------------------------------------
	@FXML
	private void onPromptOk(ActionEvent event){
		promptPane.setVisible(false);
	}
}
