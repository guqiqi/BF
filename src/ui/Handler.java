package ui;

import java.rmi.RemoteException;
import java.util.ArrayList;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import rmi.RemoteHelper;

public class Handler {
	//��ʱ�ļ�����
	private static String type = "";
	//����ʱ���ļ�����ȡ������1���½�֮���tfFileName�л�ã�2����ȡ�ļ�ʱ��fileList�л��
	private static int state = 0;
	//�������е����룬ֻҪ�и��¾ͱ���
	private static ArrayList<String> input = new ArrayList<String>();
	//��¼Ŀǰundo����
	private static int undoCount = 0;
	//��¼���ǲ��ǵ�һ�γ���
	private static int isFirstUndo = 1;
	
	//-------------------------------�������н���--------------------------------
	@FXML
	private Pane codePane;
	
	//�˵���file
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
	
	//�˵���run
	@FXML
	private Menu menuRun;
	@FXML
	private MenuItem menuExecute;

	//�˵���version��Ӧ��3���汾
	@FXML
	private MenuItem menuLastOne;
	@FXML
	private MenuItem menuLastTwo;
	@FXML
	private MenuItem menuLastThree;
	
	//�˵���more��Ӧ��������ť
	@FXML
	private MenuItem menuUndo;
	@FXML
	private MenuItem menuRedo;
	
	//���а�ť
	@FXML
	private Button btRun;
	//������������һ�������
	@FXML
	private TextArea taCode;
	@FXML
	private TextField tfInput;
	@FXML
	private TextField tfResult;
	//���½ǵ��û���Ϣ
	@FXML //�˳�����Ҫ�Լ�д
	private Label logOut;
	@FXML
	private Label userName;

	//----------------------------------ע��͵�¼����---------------------------------------
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
	
	//------------------------------------��ʾ����-----------------------------------------
	@FXML
	private Pane promptPane;
	@FXML
	private Button btPromptOk;
	@FXML
	private Text textPrompt;
	
	//------------------------------------�½��ļ�����---------------------------------------
	@FXML
	private Pane newPane;
	@FXML
	private Button btCreateNew;
	@FXML
	private Button btCancelCreateNew;
	@FXML
	private TextField tfFileName;

	//------------------------------------ѡ���ļ�����---------------------------------------
	@FXML
	private Pane openFilePane;
	@FXML
	private Button btOpenFile;
	@FXML
	private Button btCancelOpenFile;
	@FXML
	private ComboBox<String> fileList;
	
	//-----------------------------------������ʾ����--------------------------------------
	@FXML
	private Pane exitPane;
	@FXML
	private Button btConfirmExit;
	@FXML
	private Button btCancelExit;
	
	//----------------------------------�˴���ʼ���¼�����------------------------------------
	//------------------------------------�˵����-----------------------------------------
	//����ʱ��ֱ��ȷ���ļ����ƣ�Ҫ����һ����������ʾ��
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
		
		menuUndo.setDisable(true);
		menuRedo.setDisable(true);
		input.clear();
		undoCount = 0;
		
		menuSave.setDisable(false);
		menuExecute.setDisable(false);
		
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
	//��ʱ��Ҫ����һ����ʾ��ǰ�û��������ļ����������Թ�ѡ��
	//-------------------file------------------------
	@FXML
	private void onMenuOpen(ActionEvent event){
		String[] files = null;
		
		//�������е��ļ���
		try {
			files = RemoteHelper.getInstance().getIOService().getFileList(tfUserName.getText());
		} catch (RemoteException e) {
			promptPane.setVisible(true);
			textPrompt.setText("Remote Error!");
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
		//���롢���롢����ı�
		taCode.setDisable(false);
		tfInput.setDisable(false);
		tfResult.setDisable(false);
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");
		
		menuUndo.setDisable(true);
		menuRedo.setDisable(true);
		input.clear();
		undoCount = 0;
		
		menuSave.setDisable(false);
		menuExecute.setDisable(false);
		
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
			promptPane.setVisible(true);
			textPrompt.setText("Remote Error!");
		}
		
		try {
			String file = RemoteHelper.getInstance().getIOService().readFile(tfUserName.getText(), fileList.getValue());
			
			if(file.equals(""))
				taCode.setText("");	
			else if(file.split(";").length == 1)
				taCode.setText(file.substring(0, file.length() - 1));
			else if(file.split(";").length == 2){
				taCode.setText(file.split(";")[0]);
				tfInput.setText(file.split(";")[1]);
			}
		} catch (RemoteException e) {
			promptPane.setVisible(true);
			textPrompt.setText("Remote Error!");
		}
	}
	@FXML
	private void onCancelOpenFile(ActionEvent event){
		openFilePane.setVisible(false);
	}
	@FXML
	private void onMenuSave(ActionEvent event){
		menuUndo.setDisable(true);
		menuRedo.setDisable(true);
		//input.clear();
		//undoCount = 0;
		
		try {
			RemoteHelper.getInstance().getIOService().writeFile
				(taCode.getText() + ";" + tfInput.getText() + System.lineSeparator(), tfUserName.getText(), getFileName());
			
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
			textPrompt.setText("Remote Error!");
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
	
	@FXML
	private void onMenuExit(ActionEvent event){
		exitPane.setVisible(true);
	}
	@FXML
	private void onExit(ActionEvent event){
		System.exit(0);
	}
	@FXML
	private void onCancelExit(ActionEvent event){
		exitPane.setVisible(false);
	}
	
	//-------------------run-------------------------
	@FXML
	private void onRun(ActionEvent event){
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
		menuUndo.setDisable(true);
		menuRedo.setDisable(true);
		input.clear();
		undoCount = 0;
		
		//���롢���롢����ı�
		taCode.setDisable(false);
		tfInput.setDisable(false);
		tfResult.setDisable(false);
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");	
		
		try {
			String file = RemoteHelper.getInstance().getIOService().getSpecifiedVersion(1, tfUserName.getText(), getFileName());
	
			if(file.split(";").length == 1)
				taCode.setText(file.substring(0, file.length() - 1));
				
			else if(file.split(";").length == 2){
				taCode.setText(file.split(";")[0]);
				tfInput.setText(file.split(";")[1]);
			}
		} catch (RemoteException e) {
			promptPane.setVisible(true);
			textPrompt.setText("Remote Error!");
		}
		
	}
	@FXML
	private void onLastTwo(ActionEvent event){
		menuUndo.setDisable(true);
		menuRedo.setDisable(true);
		input.clear();
		undoCount = 0;
		
		taCode.setDisable(false);
		tfInput.setDisable(false);
		tfResult.setDisable(false);
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");	
		
		try {
			String file = RemoteHelper.getInstance().getIOService().getSpecifiedVersion(2, tfUserName.getText(), getFileName());
		
			if(file.split(";").length == 1)
				taCode.setText(file.substring(0, file.length() - 1));
				
			else if(file.split(";").length == 2){
				taCode.setText(file.split(";")[0]);
				tfInput.setText(file.split(";")[1]);
			}
		} catch (RemoteException e) {
			promptPane.setVisible(true);
			textPrompt.setText("Remote Error!");
		}
	}
	@FXML
	private void onLastThree(ActionEvent event){
		menuUndo.setDisable(true);
		menuRedo.setDisable(true);
		input.clear();
		undoCount = 0;
		
		taCode.setDisable(false);
		tfInput.setDisable(false);
		tfResult.setDisable(false);
		taCode.setText("");
		tfInput.setText("");
		tfResult.setText("");	
		
		try {
			String file = RemoteHelper.getInstance().getIOService().getSpecifiedVersion(3, tfUserName.getText(), getFileName());
		
			if(file.split(";").length == 1)
				taCode.setText(file.substring(0, file.length() - 1));
				
			else if(file.split(";").length == 2){
				taCode.setText(file.split(";")[0]);
				tfInput.setText(file.split(";")[1]);
			}
		} catch (RemoteException e) {
			promptPane.setVisible(true);
			textPrompt.setText("Remote Error!");
		}
	}
	//---------------------more-----------------------
	//isFirstUndo��¼�ǲ��ǵ�һ�γ���
	//1:�������̵�һ�γ�������ʱ��Ҫ����ǰ���ȥ������Ҫ�����ڵļ���ȥ
	//2:ĳ�γ���֮����ʱ��������ǳ����Ļ����䣬���ֻ�ǼӶ����Ļ�Ҫ�����һ����¼ȥ��
	//3:�����������̵ĵ�һ�γ�������ǰ��һ�β����ǼӶ�������ʱ��Ҫ�����ڵ�����
	@FXML //����
	private void onUndo(ActionEvent event){
		if(isFirstUndo == 1){
			isFirstUndo = 2;
			input.remove(0);
			input.add(taCode.getText() + ";" + tfInput.getText());
		}
		else if(isFirstUndo == 3){
			input.add(taCode.getText() + ";" + tfInput.getText());
			isFirstUndo = 2;
		}
		
		undoCount ++;
		String file = "";
		if(input.size() > undoCount)
			file = input.get(input.size() - undoCount - 1);
		else
			menuUndo.setDisable(true);
		
		if(file.equals(""))
			taCode.setText("");	
		else if(file.split(";").length == 1){
			taCode.setText(file.substring(0, file.length() - 1));
			tfInput.setText("");
		}
		else if(file.split(";").length == 2){
			taCode.setText(file.split(";")[0]);
			tfInput.setText(file.split(";")[1]);
		}
		
		menuRedo.setDisable(false);
	}
	@FXML //����
	private void onRedo(ActionEvent event){
		undoCount --;
		String file = input.get(input.size() - undoCount - 1);
		
		if(file.equals(""))
			taCode.setText("");	
		else if(file.split(";").length == 1)
			taCode.setText(file.substring(0, file.length() - 1));
		else if(file.split(";").length == 2){
			taCode.setText(file.split(";")[0]);
			tfInput.setText(file.split(";")[1]);
		}
		
		if(undoCount == 0)
			menuRedo.setDisable(true);
		
	}
	@FXML
	private void onRecordInputChanged(KeyEvent event){
		menuRedo.setDisable(true);
		
		if(isFirstUndo == 2){
			input.remove(input.size() - 1);
			isFirstUndo = 3;
		}
		
		String content = taCode.getText() + ";" + tfInput.getText();
		if(input.size() == 0){
			input.add(content);
		}
		else{
			if(!input.get(input.size() - 1).equals(content)){
				input.add(content);
			}
		}
			
		if(undoCount != 0){
			int flag = input.size() - undoCount;
			for(int i = input.size() - 1; i >= flag; i --){
				input.remove(i);
			}
			undoCount = 0;
		}
		
		if(input.size() > 0)
			menuUndo.setDisable(false);
	}
	//---------------------------------��¼���--------------------------------------
	@FXML
	private void onLogIn(ActionEvent event){
		boolean flag = false;
		
		//temp��ʾ����Ĳ�ͬԭ��0���쳣��1��û�и��û�,2���������3��û����
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
        
        menuSave.setDisable(true);
        menuExecute.setDisable(true);
        
        menuLastOne.setDisable(true);
		menuLastTwo.setDisable(true);
		menuLastThree.setDisable(true);
		
		menuUndo.setDisable(true);
		menuRedo.setDisable(true);
		input.clear();
		undoCount = 0;
          
        tfUserName.setText("");
        tfPassword.setText("");
        
        taCode.setText("");
        tfInput.setText("");
        tfResult.setText("");
	}
	
	//--------------------------------��ʾ����ֻ��һ��OK��ť-------------------------------------
	@FXML
	private void onPromptOk(ActionEvent event){
		promptPane.setVisible(false);
	}
}
