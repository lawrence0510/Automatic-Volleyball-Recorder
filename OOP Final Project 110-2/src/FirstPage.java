import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FirstPage extends JFrame {

	private static final int FRAME_WIDTH = 1280;
	private static final int FRAME_HEIGHT = 800;

	public ArrayList<Players> players = new ArrayList<>();

	public ArrayList<Players> getplayers() {
		return players;
	}

	private String accountname;
	private JPanel fillinpanel, leftPanel, vacantPanel;
	private JPanel menuPanel, recorderPanel, historyPanel, changePanel, btnPanel;
	private JTextArea historyArea;
	private JLabel userLabel, teamNameLabel, enemyLabel, mixtureLabel, vacant, placeLabel, timeLabel, historygame,
			loginlabel;
	private JLabel numb1, numb2, numb3, numb4, numb5, numb6, name, num, location, sex;
	private JTextField recorders_name, place_name, time, team_name, enemyteam_name;
	private JTextField numb1numField, numb1nameField, numb2numField, numb2nameField, numb3numField, numb3nameField,
			numb4numField, numb4nameField, numb5numField, numb5nameField, numb6numField, numb6nameField;
	public String teamnamestring, enemyteamnamestring, timeString, recorder_nameString, placeString;
	private JButton submit, history, back, record, back2;
	private JComboBox<String> numb1locationBox, numb2locationBox, numb3locationBox, numb4locationBox, numb5locationBox,
			numb6locationBox, numb1sexBox, numb2sexBox, numb3sexBox, numb4sexBox, numb5sexBox, numb6sexBox;
	CardLayout cardlayout;
	Connection conn;
	Statement stat;

	public FirstPage(String accountanme) {
		this.accountname = accountanme;
		this.setTitle("自動化排球紀錄系統");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		cardlayout = new CardLayout();
		createTextField();
		createComboBox();
		createLabel();
		createItemComp();
		createButton();
		createPanel();
		createLayout();
	}

	public void createItemComp() {

		userLabel = new JLabel("請輸入您的名字\n");
		recorders_name = new JTextField();

		teamNameLabel = new JLabel("請輸入我方隊伍名稱");
		team_name = new JTextField();

		enemyLabel = new JLabel("請輸入敵方隊伍名稱");
		enemyteam_name = new JTextField();

		String mixture = team_name.getText();
		mixture.concat("v.s." + enemyteam_name.getText());
		mixtureLabel = new JLabel(mixture);
		vacant = new JLabel("");

		placeLabel = new JLabel("比賽地點");
		place_name = new JTextField();

		timeLabel = new JLabel("比賽時間");
		time = new JTextField();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		time.setText(dtf.format(LocalDateTime.now()));

		loginlabel = new JLabel("");
		loginlabel.setText(String.format("您好%s，請選擇創建一個紀錄表或是查看歷史紀錄", accountname));
		loginlabel.setFont(new Font("TimesNewRoman", Font.PLAIN, 20));

		historyArea = new JTextArea();
		historyArea.append(String.format("%-34s%-42s%-10s%-5s%-10s%-5s %-8s\n","時間", "地點", "A隊名稱:","A隊得分", "B隊名稱:", "B隊得分", "結果"));
		historyArea.setEditable(false);
		String server = "jdbc:mysql://140.119.19.73:3315/";
		String database = "110306047"; // change to your own database
		String url = server + database + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
		String dbusername = "110306047"; // change to your own username
		String dbpassword = "wughu"; // change to your own password
		try (Connection conn = DriverManager.getConnection(url, dbusername, dbpassword);) {
			stat = conn.createStatement();
			String query = String.format("SELECT * FROM `records` WHERE accountname = '%s';", accountname);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				historyArea.append(String.format("%-10s%-10s%28s: %12s分%12s:%14s分 %-8s\n", rs.getString("time"), rs.getString("place"), rs.getString("teamAname"), rs.getInt("teamApoints"), rs.getString("teamBname"), rs.getInt("teamBpoints"), rs.getString("consequence"))) ;
			}
			historyArea.append("--------------------------------以下空白--------------------------------");
			rs.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void createButton() {
		history = new JButton("歷史紀錄");
		class historyListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				cardlayout.last(changePanel);
			}
		}
		history.addActionListener(new historyListener());

		back = new JButton("返回");
		class backListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				cardlayout.first(changePanel);
			}
		}
		back.addActionListener(new backListener());
		
		back2 = new JButton("返回");
		class back2Listener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				cardlayout.first(changePanel);
			}
		}
		back2.addActionListener(new back2Listener());
		record = new JButton("創建一個新的記錄表");
		class recordListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				cardlayout.next(changePanel);
			}
		}
		record.addActionListener(new recordListener());

		submit = new JButton("開始");

		class SubmitListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (numb1nameField.getText().equals("") || numb2nameField.getText().equals("")
						|| numb3nameField.getText().equals("") || numb4nameField.getText().equals("")
						|| numb5nameField.getText().equals("") || numb6nameField.getText().equals("")
						|| numb1numField.getText().equals("") || numb2numField.getText().equals("")
						|| numb3numField.getText().equals("") || numb4numField.getText().equals("")
						|| numb5numField.getText().equals("") || numb6numField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "請至少填入六位球員之姓名與背號", "資料不夠完整", JOptionPane.ERROR_MESSAGE);
				} else if (team_name.getText().equals("") || enemyteam_name.equals("")) {
					JOptionPane.showMessageDialog(null, "請完整輸入我方及敵隊名稱", "資料不夠完整", JOptionPane.ERROR_MESSAGE);
				} else {
					teamnamestring = team_name.getText();
					enemyteamnamestring = enemyteam_name.getText();
					timeString = time.getText();
					recorder_nameString = recorders_name.getText();
					placeString = place_name.getText();

					Players player1 = new Players(numb1nameField.getText(), Integer.parseInt(numb1numField.getText()),
							(String) numb1locationBox.getSelectedItem(), (String) numb1sexBox.getSelectedItem());
					Players player2 = new Players(numb2nameField.getText(), Integer.parseInt(numb2numField.getText()),
							(String) numb2locationBox.getSelectedItem(), (String) numb2sexBox.getSelectedItem());
					Players player3 = new Players(numb3nameField.getText(), Integer.parseInt(numb3numField.getText()),
							(String) numb3locationBox.getSelectedItem(), (String) numb3sexBox.getSelectedItem());
					Players player4 = new Players(numb4nameField.getText(), Integer.parseInt(numb4numField.getText()),
							(String) numb4locationBox.getSelectedItem(), (String) numb4sexBox.getSelectedItem());
					Players player5 = new Players(numb5nameField.getText(), Integer.parseInt(numb5numField.getText()),
							(String) numb5locationBox.getSelectedItem(), (String) numb5sexBox.getSelectedItem());
					Players player6 = new Players(numb6nameField.getText(), Integer.parseInt(numb6numField.getText()),
							(String) numb6locationBox.getSelectedItem(), (String) numb6sexBox.getSelectedItem());
					players.add(player1);
					players.add(player2);
					players.add(player3);
					players.add(player4);
					players.add(player5);
					players.add(player6);
					players.add(new Players(teamnamestring, 0, "", ""));
					players.add(new Players(enemyteamnamestring, 0, "", ""));
					players.add(new Players(timeString, 0, "", ""));
					players.add(new Players(recorder_nameString, 0, "", ""));
					players.add(new Players(placeString, 0, "", ""));
					players.add(new Players(accountname, 0, "", ""));

					int i = JOptionPane.showConfirmDialog(null, "是否要添加自由球員(至多兩位)", "添加自由球員",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					// no= 1 yes = 0
					if (i == 1) {
						setVisible(false);
						SecondPage secondPage = new SecondPage(players);
						secondPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						secondPage.setVisible(true);
					} else if (i == 0) {
						String[] sexStrings = { "男", "女" };
						String libro1name = JOptionPane.showInputDialog(null, "請輸入第一名自由球員姓名", "自由球員資料",
								JOptionPane.INFORMATION_MESSAGE);
						if (libro1name != null) {
							String libro1num = JOptionPane.showInputDialog(null, "請輸入第一名自由球員背號", "自由球員資料",
									JOptionPane.INFORMATION_MESSAGE);
							if (libro1num != null) {
								String libro1sex = (String) JOptionPane.showInputDialog(null, "請輸入第一名自由球員性別", "自由球員資料",
										JOptionPane.INFORMATION_MESSAGE, null, sexStrings, "");
								Players libro1 = new Players(libro1name, Integer.parseInt(libro1num), "自由", libro1sex);
								players.add(libro1);
								int k = JOptionPane.showConfirmDialog(null, "是否還要再添加自由球員（至多1位)", "添加自由球員",
										JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
								if (k == 1) {
									setVisible(false);
									SecondPage secondPage = new SecondPage(players);
									secondPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
									secondPage.setVisible(true);
								} else if (k == 0) {
									String libro2name = JOptionPane.showInputDialog(null, "請輸入第二位自由球員姓名", "自由球員資料",
											JOptionPane.INFORMATION_MESSAGE);
									if (libro2name != null) {
										String libro2num = JOptionPane.showInputDialog(null, "請輸入第二位自由球員背號", "自由球員資料",
												JOptionPane.INFORMATION_MESSAGE);
										if (libro2num != null) {
											String libro2sex = (String) JOptionPane.showInputDialog(null,
													"請輸入第二位自由球員性別", "自由球員資料", JOptionPane.INFORMATION_MESSAGE, null,
													sexStrings, "");
											Players libro2 = new Players(libro2name, Integer.parseInt(libro2num), "自由",
													libro2sex);
											players.add(libro2);
											setVisible(false);
											SecondPage secondPage = new SecondPage(players);
											secondPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
											secondPage.setVisible(true);
										}
									}
								}
							}
						}
					}

				}
			}
		}
		submit.addActionListener(new SubmitListener());
	}

	public void createPanel() {
		fillinpanel = new JPanel(new GridLayout(7, 5));
		// fillinpanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		fillinpanel.setVisible(true);

		fillinpanel.add(vacant);
		fillinpanel.add(name);
		fillinpanel.add(num);
		fillinpanel.add(location);
		fillinpanel.add(sex);

		fillinpanel.add(numb1);
		fillinpanel.add(numb1nameField);
		fillinpanel.add(numb1numField);
		fillinpanel.add(numb1locationBox);
		fillinpanel.add(numb1sexBox);

		fillinpanel.add(numb2);
		fillinpanel.add(numb2nameField);
		fillinpanel.add(numb2numField);
		fillinpanel.add(numb2locationBox);
		fillinpanel.add(numb2sexBox);

		fillinpanel.add(numb3);
		fillinpanel.add(numb3nameField);
		fillinpanel.add(numb3numField);
		fillinpanel.add(numb3locationBox);
		fillinpanel.add(numb3sexBox);

		fillinpanel.add(numb4);
		fillinpanel.add(numb4nameField);
		fillinpanel.add(numb4numField);
		fillinpanel.add(numb4locationBox);
		fillinpanel.add(numb4sexBox);

		fillinpanel.add(numb5);
		fillinpanel.add(numb5nameField);
		fillinpanel.add(numb5numField);
		fillinpanel.add(numb5locationBox);
		fillinpanel.add(numb5sexBox);

		fillinpanel.add(numb6);
		fillinpanel.add(numb6nameField);
		fillinpanel.add(numb6numField);
		fillinpanel.add(numb6locationBox);
		fillinpanel.add(numb6sexBox);

		leftPanel = new JPanel(new GridLayout(11, 1));
		// leftPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		leftPanel.add(userLabel);
		leftPanel.add(recorders_name);
		leftPanel.add(teamNameLabel);
		leftPanel.add(team_name);
		leftPanel.add(enemyLabel);
		leftPanel.add(enemyteam_name);
		leftPanel.add(placeLabel);
		leftPanel.add(place_name);
		leftPanel.add(timeLabel);
		leftPanel.add(time);

		vacantPanel = new JPanel();
		menuPanel = new JPanel(new GridLayout(2, 2));
		recorderPanel = new JPanel(new FlowLayout());
		historyPanel = new JPanel(new BorderLayout());
		changePanel = new JPanel(new CardLayout());
		recorderPanel.add(leftPanel);
		recorderPanel.add(vacantPanel);
		recorderPanel.add(fillinpanel);
		recorderPanel.add(submit);
		recorderPanel.add(back2);
		menuPanel.add(loginlabel);
		menuPanel.add(new JLabel(""));
		menuPanel.add(record);
		menuPanel.add(history);
		btnPanel = new JPanel();
		btnPanel.add(back);
		historyPanel.add(btnPanel, BorderLayout.SOUTH);
		historyPanel.add(historyArea, BorderLayout.CENTER);
		historyPanel.add(historygame, BorderLayout.NORTH);
		changePanel.setLayout(cardlayout);
		changePanel.add(menuPanel, "first");
		changePanel.add(recorderPanel, "second");
		changePanel.add(historyPanel, "third");

	}

	public void createLayout() {
		setLayout(new FlowLayout());
		add(changePanel);
	}

	public void createLabel() {
		numb1 = new JLabel("一號位");
		numb2 = new JLabel("二號位");
		numb3 = new JLabel("三號位");
		numb4 = new JLabel("四號位");
		numb5 = new JLabel("五號位");
		numb6 = new JLabel("六號位");
		name = new JLabel("姓名");
		num = new JLabel("背號");
		location = new JLabel("位置");
		sex = new JLabel("性別");
		historygame = new JLabel(String.format("%s的歷史紀錄", accountname));
		historygame.setFont(new Font("TimesNewRoman", Font.PLAIN, 20));
	}

	public void createTextField() {
		numb1numField = new JTextField();
		numb1nameField = new JTextField();
		numb2numField = new JTextField();
		numb2nameField = new JTextField();
		numb3numField = new JTextField();
		numb3nameField = new JTextField();
		numb4numField = new JTextField();
		numb4nameField = new JTextField();
		numb5numField = new JTextField();
		numb5nameField = new JTextField();
		numb6numField = new JTextField();
		numb6nameField = new JTextField();
	}

	public void createComboBox() {

		numb1locationBox = new JComboBox<String>();
		numb1locationBox.addItem("大砲");
		numb1locationBox.addItem("攔中");
		numb1locationBox.addItem("舉球");
		numb1locationBox.addItem("舉對");

		numb2locationBox = new JComboBox<String>();
		numb2locationBox.addItem("大砲");
		numb2locationBox.addItem("攔中");
		numb2locationBox.addItem("舉球");
		numb2locationBox.addItem("舉對");

		numb3locationBox = new JComboBox<String>();
		numb3locationBox.addItem("大砲");
		numb3locationBox.addItem("攔中");
		numb3locationBox.addItem("舉球");
		numb3locationBox.addItem("舉對");

		numb4locationBox = new JComboBox<String>();
		numb4locationBox.addItem("大砲");
		numb4locationBox.addItem("攔中");
		numb4locationBox.addItem("舉球");
		numb4locationBox.addItem("舉對");

		numb5locationBox = new JComboBox<String>();
		numb5locationBox.addItem("大砲");
		numb5locationBox.addItem("攔中");
		numb5locationBox.addItem("舉球");
		numb5locationBox.addItem("舉對");

		numb6locationBox = new JComboBox<String>();
		numb6locationBox.addItem("大砲");
		numb6locationBox.addItem("攔中");
		numb6locationBox.addItem("舉球");
		numb6locationBox.addItem("舉對");

		numb1sexBox = new JComboBox<String>();
		numb1sexBox.addItem("男");
		numb1sexBox.addItem("女");

		numb2sexBox = new JComboBox<String>();
		numb2sexBox.addItem("男");
		numb2sexBox.addItem("女");

		numb3sexBox = new JComboBox<String>();
		numb3sexBox.addItem("男");
		numb3sexBox.addItem("女");

		numb4sexBox = new JComboBox<String>();
		numb4sexBox.addItem("男");
		numb4sexBox.addItem("女");

		numb5sexBox = new JComboBox<String>();
		numb5sexBox.addItem("男");
		numb5sexBox.addItem("女");

		numb6sexBox = new JComboBox<String>();
		numb6sexBox.addItem("男");
		numb6sexBox.addItem("女");
	}

}
