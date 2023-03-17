import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.swing.*;

public class LoginPage extends JFrame {

	private JPanel usernamepanel, passwordnamepanel, buttonPanel, layoutpanel, usefulPanel, beautyPanel, pcPanel;
	private JLabel username, password, beauty, backgroundLabel;
	private JTextField usernamefield;
	private JPasswordField passwordfield;
	private JButton enrollButton, loginButton;
	protected String loginname = "";
	private String query = "";
	Connection conn;
	Statement stat;
	ImageIcon background;

	public LoginPage(Connection conn) throws SQLException {
		this.conn = conn;
		createLayout();
		createButton();
		setSize(600, 400);
		setResizable(false);
		setTitle("登入頁面");
		setVisible(true);
		Integer obj = Integer.valueOf(Integer.MIN_VALUE);
		this.add(backgroundLabel, obj);

	}

	public void createLayout() {
		layoutpanel = new JPanel(new GridLayout(3, 1));
		layoutpanel.setOpaque(false);// panel的背景設定為透明

		beautyPanel = new JPanel();
		beautyPanel.setOpaque(false);

		background = new ImageIcon("排球背景.jpg");
		backgroundLabel = new JLabel(background);
		backgroundLabel.setSize(600, 400);

		beauty = new JLabel("排球紀錄器");
		beauty.setFont(new Font("TimesNewRoman", Font.BOLD, 80));
		beauty.setForeground(Color.white);
		username = new JLabel("帳號: ");
		username.setOpaque(true);
		username.setFont(new Font("TimesNewRoman", Font.BOLD, 16));
		password = new JLabel("密碼: ");
		password.setOpaque(true);
		password.setFont(new Font("TimesNewRoman", Font.BOLD, 16));

		usernamefield = new JTextField(20);
		passwordfield = new JPasswordField(20);

		usernamepanel = new JPanel();
		usernamepanel.add(username);
		usernamepanel.add(usernamefield);
		usernamepanel.setOpaque(false);

		passwordnamepanel = new JPanel();
		passwordnamepanel.add(password);
		passwordnamepanel.add(passwordfield);
		passwordnamepanel.setOpaque(false);

		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);

		usefulPanel = new JPanel(new GridLayout(3, 1));
		usefulPanel.setOpaque(false);
		usefulPanel.add(usernamepanel);
		usefulPanel.add(passwordnamepanel);
		usefulPanel.add(buttonPanel);

		beautyPanel.add(beauty);
		layoutpanel.add(beautyPanel);
		layoutpanel.add(usefulPanel);

		add(layoutpanel);
	}

	public void createButton() {
		enrollButton = new JButton("註冊");
		loginButton = new JButton("登入");

		class enrollbuttonlistener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String username = usernamefield.getText();
				@SuppressWarnings("deprecation")
				String password = new String(passwordfield.getPassword());
				String server = "jdbc:mysql://140.119.19.73:3315/";
				String database = "110306047"; // change to your own database
				String url = server + database + "?useSSL=false";
				String dbusername = "110306047"; // change to your own username
				String dbpassword = "wughu"; // change to your own password
				try (Connection conn = DriverManager.getConnection(url, dbusername, dbpassword);) {
					password = getSHA256StrJava(password);
					Statement stat = conn.createStatement();
					String query = "SELECT COUNT(*) FROM `ACPW` WHERE `account` = '" + usernamefield.getText() + "'";
					ResultSet rs1 = stat.executeQuery(query);
					rs1.next();
					int count = rs1.getInt(1);
					if (count == 0) {
						String query2 = "INSERT INTO `ACPW`(account, password) VALUES (" + "'" + username + "'" + ", "
								+ "'" + password + "'" + ")";
						boolean hasResultSet = stat.execute(query2);
						if (!hasResultSet) {
							usernamefield.setText("");
							passwordfield.setText("");
							JOptionPane.showMessageDialog(null, "註冊成功", "註冊成功", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "此帳號已被註冊過", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		enrollButton.addActionListener(new enrollbuttonlistener());

		class loginbuttonlistener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String username = usernamefield.getText();
				String password = new String(passwordfield.getPassword());
				String server = "jdbc:mysql://140.119.19.73:3315/";
				String database = "110306047"; // change to your own database
				String url = server + database + "?useSSL=false";
				String dbusername = "110306047"; // change to your own username
				String dbpassword = "wughu"; // change to your own password
				try (Connection conn = DriverManager.getConnection(url, dbusername, dbpassword);) {
					stat = conn.createStatement();
					String query = "SELECT COUNT(*) FROM ACPW WHERE account = '" + usernamefield.getText() + "'";
					ResultSet rs1 = stat.executeQuery(query);
					rs1.next();
					int count = rs1.getInt(1);
					if (count == 1) {
						String newpassword = getSHA256StrJava(new String(passwordfield.getPassword()));
						String query2 = "SELECT COUNT(*) FROM ACPW WHERE password = '" + newpassword + "'";
						ResultSet rs2 = stat.executeQuery(query2);
						rs2.next();
						int count2 = rs2.getInt(1);
						if (count2 == 1) {
							setVisible(false);
							JOptionPane.showMessageDialog(null, "登入成功", "登入成功", JOptionPane.INFORMATION_MESSAGE);
							FirstPage firstPage = new FirstPage(usernamefield.getText());
							firstPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							firstPage.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(null, "密碼錯誤", "錯誤", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "無法找到此帳號", "錯誤", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		loginButton.addActionListener(new loginbuttonlistener());

		buttonPanel.add(enrollButton);
		buttonPanel.add(loginButton);

	}

	public String getSHA256StrJava(String str) {
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			encodeStr = byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodeStr;
	}

	private String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
}
