import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.filechooser.FileFilter;

import javax.swing.*;
import javax.swing.border.Border;

public class SecondPage extends JFrame {
	private static final int FRAME_WIDTH = 1280;
	private static final int FRAME_HEIGHT = 800;

	private int libropos, midpos;

	private JPanel dataJPanel, buttonJPanel, vacantJPanel, scoreJPanel, number1panel, number2panel, number3panel,
			number4panel, number5panel, number6panel, libro1panel, libro2panel, p1Panel, p2Panel, p3Panel, p4Panel,
			p5Panel, p6Panel;
	private JLabel turn, score;
	private JPanel buttonJPanelUp, buttonJPanelDown;
	private JButton enemyscore, enemyerrorscore;
	private ScoreManager scoreManager = new ScoreManager();
	StringBuffer middleblocker = new StringBuffer();
	private ArrayList<Players> players = new ArrayList<Players>();

	public ArrayList<Players> getPlayers() {
		return players;
	}

	ArrayList<Integer> serves = new ArrayList<>();
	ArrayList<Integer> blocks = new ArrayList<>();
	ArrayList<Integer> spikes = new ArrayList<>();
	ArrayList<Integer> errors = new ArrayList<>();
	private boolean libro1onbackrow, libro2onbackrow = false;
	private ArrayList<Integer> librosubstitution = new ArrayList<>();
	private String winner = "null";

	private JLabel numb1numLabel, numb1nameLabel, numb1orderLabel, numb1sexLabel, numb2numLabel, numb2nameLabel,
			numb2orderLabel, numb2sexLabel, numb3numLabel, numb3nameLabel, numb3orderLabel, numb3sexLabel,
			numb4numLabel, numb4nameLabel, numb4orderLabel, numb4sexLabel, numb5numLabel, numb5nameLabel,
			numb5orderLabel, numb5sexLabel, numb6numLabel, numb6nameLabel, numb6orderLabel, numb6sexLabel,
			libro1numLabel, libro1nameLabel, libro1orderLabel, libro1sexLabel, libro2numLabel, libro2nameLabel,
			libro2orderLabel, libro2sexLabel;

	private JButton numb1serve, numb1block, numb1spike, numb1fault, numb2serve, numb2block, numb2spike, numb2fault,
			numb3serve, numb3block, numb3spike, numb3fault, numb4serve, numb4block, numb4spike, numb4fault, numb5serve,
			numb5block, numb5spike, numb5fault, numb6serve, numb6block, numb6spike, numb6fault, libro1serve,
			libro1block, libro1spike, libro1fault, libro2serve, libro2block, libro2spike, libro2fault;

	Border blackline = BorderFactory.createLineBorder(Color.black);

	private int opt;

	public SecondPage(ArrayList<Players> secondpageplayers) {
		this.setTitle("自動化排球紀錄系統");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		for (Players p : secondpageplayers) {
			players.add(p);
		}
		String[] options = { "先發球", "後發球" };
		opt = JOptionPane.showOptionDialog(null, "請問我方球隊是先發還是後發？", "請選擇", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, "接受");
		if (opt == JOptionPane.YES_OPTION) { // opt = 0
			JOptionPane.showMessageDialog(null, "您選擇的是 : 先發球 ");
			scoreManager.winnerorder.append("1");
		}
		if (opt == JOptionPane.NO_OPTION) { // opt = 1
			JOptionPane.showMessageDialog(null, "您選擇的是 : 後發球 ");
			scoreManager.winnerorder.append("0");
		}
		initialize();
		createButton();
		createLabel();
		createPanel();
		checkLibroSubstitution();
		iniDataJPanel();
		createLayout();
	}

	public void createLayout() {
		setLayout(new GridLayout(4, 1));
		add(scoreJPanel);
		add(vacantJPanel);
		add(buttonJPanel);
		add(dataJPanel);
	}

	public void createButton() {
		numb1serve = new JButton("發球得分");
		class numb1serveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addservespoint(1);
				serves.set(1, serves.get(1) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
					System.out.println("Output successfully");
				} else if (winner.equals("lose")) {
					write();
					System.out.println("Output successfully");
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}
			}
		}
		numb1serve.addActionListener(new numb1serveListener());
		numb1block = new JButton("攔網得分");
		class numb1blockListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addblockspoint(1);
				blocks.set(1, serves.get(1) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb1block.addActionListener(new numb1blockListener());
		numb1spike = new JButton("攻擊得分");
		class numb1spikeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addspikespoint(1);
				spikes.set(1, serves.get(1) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb1spike.addActionListener(new numb1spikeListener());
		numb1fault = new JButton("失誤失分");
		class numb1errorsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.adderrorspoint(1);
				errors.set(1, serves.get(1) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb1fault.addActionListener(new numb1errorsListener());

		numb2serve = new JButton("發球得分");
		class numb2serveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addservespoint(2);
				serves.set(2, serves.get(2) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb2serve.addActionListener(new numb2serveListener());
		numb2block = new JButton("攔網得分");
		class numb2blockListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addblockspoint(2);
				blocks.set(2, serves.get(2) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb2block.addActionListener(new numb2blockListener());
		numb2spike = new JButton("攻擊得分");
		class numb2spikeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addspikespoint(2);
				spikes.set(2, serves.get(2) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb2spike.addActionListener(new numb2spikeListener());
		numb2fault = new JButton("失誤失分");
		class numb2errorsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.adderrorspoint(2);
				errors.set(2, serves.get(2) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb2fault.addActionListener(new numb2errorsListener());

		numb3serve = new JButton("發球得分");
		class numb3serveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addservespoint(3);
				serves.set(3, serves.get(3) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}
			}
		}
		numb3serve.addActionListener(new numb3serveListener());
		numb3block = new JButton("攔網得分");
		class numb3blockListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addblockspoint(3);
				blocks.set(3, serves.get(3) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb3block.addActionListener(new numb3blockListener());
		numb3spike = new JButton("攻擊得分");
		class numb3spikeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addspikespoint(3);
				spikes.set(3, serves.get(3) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb3spike.addActionListener(new numb3spikeListener());
		numb3fault = new JButton("失誤失分");
		class numb3errorsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.adderrorspoint(3);
				errors.set(3, serves.get(3) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb3fault.addActionListener(new numb3errorsListener());

		numb4serve = new JButton("發球得分");
		class numb4serveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addservespoint(4);
				serves.set(4, serves.get(4) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb4serve.addActionListener(new numb4serveListener());
		numb4block = new JButton("攔網得分");
		class numb4blockListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addblockspoint(4);
				blocks.set(4, serves.get(4) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb4block.addActionListener(new numb4blockListener());
		numb4spike = new JButton("攻擊得分");
		class numb4spikeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addspikespoint(4);
				spikes.set(4, serves.get(4) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb4spike.addActionListener(new numb4spikeListener());
		numb4fault = new JButton("失誤失分");
		class numb4errorsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.adderrorspoint(4);
				errors.set(4, serves.get(4) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb4fault.addActionListener(new numb4errorsListener());

		numb5serve = new JButton("發球得分");
		class numb5serveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addservespoint(5);
				serves.set(5, serves.get(5) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb5serve.addActionListener(new numb5serveListener());
		numb5block = new JButton("攔網得分");
		class numb5blockListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addblockspoint(5);
				blocks.set(5, serves.get(5) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb5block.addActionListener(new numb5blockListener());
		numb5spike = new JButton("攻擊得分");
		class numb5spikeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addspikespoint(5);
				spikes.set(5, serves.get(5) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb5spike.addActionListener(new numb5spikeListener());
		numb5fault = new JButton("失誤失分");
		class numb5errorsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.adderrorspoint(5);
				errors.set(5, serves.get(5) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb5fault.addActionListener(new numb5errorsListener());

		numb6serve = new JButton("發球得分");
		class numb6serveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addservespoint(6);
				serves.set(6, serves.get(6) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb6serve.addActionListener(new numb6serveListener());
		numb6block = new JButton("攔網得分");
		class numb6blockListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addblockspoint(6);
				blocks.set(6, serves.get(6) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb6block.addActionListener(new numb6blockListener());
		numb6spike = new JButton("攻擊得分");
		class numb6spikeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addspikespoint(6);
				spikes.set(6, serves.get(6) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb6spike.addActionListener(new numb6spikeListener());
		numb6fault = new JButton("失誤失分");
		class numb6errorsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.adderrorspoint(6);
				errors.set(6, serves.get(6) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		numb6fault.addActionListener(new numb6errorsListener());

		libro1serve = new JButton("發球得分");
		class libro1serveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addservespoint(7);
				serves.set(7, serves.get(7) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		libro1serve.addActionListener(new libro1serveListener());
		libro1block = new JButton("攔網得分");
		class libro1blockListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addblockspoint(1);
				blocks.set(7, serves.get(7) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		libro1block.addActionListener(new libro1blockListener());
		libro1spike = new JButton("攻擊得分");
		class libro1spikeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addspikespoint(1);
				spikes.set(7, serves.get(7) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		libro1spike.addActionListener(new libro1spikeListener());
		libro1fault = new JButton("失誤失分");
		class libro1errorsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.adderrorspoint(1);
				errors.set(7, serves.get(7) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		libro1fault.addActionListener(new libro1errorsListener());

		libro2serve = new JButton("發球得分");
		class libro2serveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addservespoint(1);
				serves.set(8, serves.get(8) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		libro2serve.addActionListener(new libro2serveListener());
		libro2block = new JButton("攔網得分");
		class libro2blockListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addblockspoint(1);
				blocks.set(8, serves.get(8) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		libro2block.addActionListener(new libro2blockListener());
		libro2spike = new JButton("攻擊得分");
		class libro2spikeListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addspikespoint(1);
				spikes.set(8, serves.get(8) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		libro2spike.addActionListener(new libro2spikeListener());
		libro2fault = new JButton("失誤失分");
		class libro2errorsListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.adderrorspoint(1);
				errors.set(8, serves.get(8) + 1);
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		libro2fault.addActionListener(new libro2errorsListener());

		enemyscore = new JButton("敵方得分");
		class enemyscoreListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.addopponentpoint();
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}

			}
		}
		enemyscore.addActionListener(new enemyscoreListener());

		enemyerrorscore = new JButton("敵方失誤我方得分");
		class enemyerrorscoreListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				scoreManager.ateamscore++;
				scoreManager.winnerorder.append("1");
				scoreManager.enemy_error++;
				score.setText(players.get(6).getname() + " : " + players.get(7).getname() + " = "
						+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore());
				winner = scoreManager.checkwin(scoreManager.getAteamScore(), scoreManager.getBteamScore());
				if (winner.equals("win")) {
					write();
				} else if (winner.equals("lose")) {
					write();
				} else if (winner.equals("null")) {
					scoreManager.checkorder();
					createdataJPanel();
				}
			}
		}
		enemyerrorscore.addActionListener(new enemyerrorscoreListener());
		
		

		libro1serve.setEnabled(false);
		libro1block.setEnabled(false);
		libro2serve.setEnabled(false);
		libro2block.setEnabled(false);
	}

	public void createPanel() {

		scoreJPanel = new JPanel();
		scoreJPanel.add(score);

		buttonJPanel = new JPanel(new GridLayout(2, 1));
		buttonJPanelUp = new JPanel();
		buttonJPanelUp.add(turn);

		buttonJPanelDown = new JPanel(new GridLayout(1, 3));
		buttonJPanelDown.add(enemyscore);
		buttonJPanelDown.add(enemyerrorscore);
		
		buttonJPanel.add(buttonJPanelUp);
		buttonJPanel.add(buttonJPanelDown);

		vacantJPanel = new JPanel();

		number1panel = new JPanel(new GridLayout(2, 4));
		number1panel.setBorder(BorderFactory.createLineBorder(Color.black));
		number1panel.add(numb1numLabel);
		numb1numLabel.setBorder(blackline);
		number1panel.add(numb1nameLabel);
		numb1nameLabel.setBorder(blackline);
		number1panel.add(numb1orderLabel);
		numb1orderLabel.setBorder(blackline);
		number1panel.add(numb1sexLabel);
		numb1sexLabel.setBorder(blackline);
		number1panel.add(numb1serve);
		numb1serve.setBorder(blackline);
		number1panel.add(numb1block);
		numb1block.setBorder(blackline);
		number1panel.add(numb1spike);
		numb1spike.setBorder(blackline);
		number1panel.add(numb1fault);
		numb1fault.setBorder(blackline);

		number2panel = new JPanel(new GridLayout(2, 4));
		number2panel.setBorder(BorderFactory.createLineBorder(Color.black));
		number2panel.add(numb2numLabel);
		numb2numLabel.setBorder(blackline);
		number2panel.add(numb2nameLabel);
		numb2nameLabel.setBorder(blackline);
		number2panel.add(numb2orderLabel);
		numb2orderLabel.setBorder(blackline);
		number2panel.add(numb2sexLabel);
		numb2sexLabel.setBorder(blackline);
		number2panel.add(numb2serve);
		numb2serve.setBorder(blackline);
		number2panel.add(numb2block);
		numb2block.setBorder(blackline);
		number2panel.add(numb2spike);
		numb2spike.setBorder(blackline);
		number2panel.add(numb2fault);
		numb2fault.setBorder(blackline);

		number3panel = new JPanel(new GridLayout(2, 4));
		number3panel.setBorder(BorderFactory.createLineBorder(Color.black));
		number3panel.add(numb3numLabel);
		numb3numLabel.setBorder(blackline);
		number3panel.add(numb3nameLabel);
		numb3nameLabel.setBorder(blackline);
		number3panel.add(numb3orderLabel);
		numb3orderLabel.setBorder(blackline);
		number3panel.add(numb3sexLabel);
		numb3sexLabel.setBorder(blackline);
		number3panel.add(numb3serve);
		numb3serve.setBorder(blackline);
		number3panel.add(numb3block);
		numb3block.setBorder(blackline);
		number3panel.add(numb3spike);
		numb3spike.setBorder(blackline);
		number3panel.add(numb3fault);
		numb3fault.setBorder(blackline);

		number4panel = new JPanel(new GridLayout(2, 4));
		number4panel.setBorder(BorderFactory.createLineBorder(Color.black));
		number4panel.add(numb4numLabel);
		numb4numLabel.setBorder(blackline);
		number4panel.add(numb4nameLabel);
		numb4nameLabel.setBorder(blackline);
		number4panel.add(numb4orderLabel);
		numb4orderLabel.setBorder(blackline);
		number4panel.add(numb4sexLabel);
		numb4sexLabel.setBorder(blackline);
		number4panel.add(numb4serve);
		numb4serve.setBorder(blackline);
		number4panel.add(numb4block);
		numb4block.setBorder(blackline);
		number4panel.add(numb4spike);
		numb4spike.setBorder(blackline);
		number4panel.add(numb4fault);
		numb4fault.setBorder(blackline);

		number5panel = new JPanel(new GridLayout(2, 4));
		number5panel.setBorder(BorderFactory.createLineBorder(Color.black));
		number5panel.add(numb5numLabel);
		numb5numLabel.setBorder(blackline);
		number5panel.add(numb5nameLabel);
		numb5nameLabel.setBorder(blackline);
		number5panel.add(numb5orderLabel);
		numb5orderLabel.setBorder(blackline);
		number5panel.add(numb5sexLabel);
		numb5sexLabel.setBorder(blackline);
		number5panel.add(numb5serve);
		numb5serve.setBorder(blackline);
		number5panel.add(numb5block);
		numb5block.setBorder(blackline);
		number5panel.add(numb5spike);
		numb5spike.setBorder(blackline);
		number5panel.add(numb5fault);
		numb5fault.setBorder(blackline);

		number6panel = new JPanel(new GridLayout(2, 4));
		number6panel.setBorder(BorderFactory.createLineBorder(Color.black));
		number6panel.add(numb6numLabel);
		numb6numLabel.setBorder(blackline);
		number6panel.add(numb6nameLabel);
		numb6nameLabel.setBorder(blackline);
		number6panel.add(numb6orderLabel);
		numb6orderLabel.setBorder(blackline);
		number6panel.add(numb6sexLabel);
		numb6sexLabel.setBorder(blackline);
		number6panel.add(numb6serve);
		numb6serve.setBorder(blackline);
		number6panel.add(numb6block);
		numb6block.setBorder(blackline);
		number6panel.add(numb6spike);
		numb6spike.setBorder(blackline);
		number6panel.add(numb6fault);
		numb6fault.setBorder(blackline);

		dataJPanel = new JPanel(new GridLayout(2, 3));

		buttonJPanel = new JPanel(new FlowLayout());
		buttonJPanel.add(turn);
		buttonJPanel.add(enemyscore);
		buttonJPanel.add(enemyerrorscore);
		

		if (libro1numLabel != null) {
			libro1panel = new JPanel(new GridLayout(2, 4));
			libro1panel.setBorder(BorderFactory.createLineBorder(Color.black));
			libro1panel.add(libro1numLabel);
			libro1numLabel.setBorder(blackline);
			libro1panel.add(libro1nameLabel);
			libro1nameLabel.setBorder(blackline);
			libro1panel.add(libro1orderLabel);
			libro1orderLabel.setBorder(blackline);
			libro1panel.add(libro1sexLabel);
			libro1sexLabel.setBorder(blackline);
			libro1panel.add(libro1serve);
			libro1serve.setBorder(blackline);
			libro1panel.add(libro1block);
			libro1block.setBorder(blackline);
			libro1panel.add(libro1spike);
			libro1spike.setBorder(blackline);
			libro1panel.add(libro1fault);
			libro1fault.setBorder(blackline);
		}

		if (libro2numLabel != null) {
			libro2panel = new JPanel(new GridLayout(2, 4));
			libro2panel.setBorder(BorderFactory.createLineBorder(Color.black));
			libro2panel.add(libro2numLabel);
			libro2numLabel.setBorder(blackline);
			libro2panel.add(libro2nameLabel);
			libro2nameLabel.setBorder(blackline);
			libro2panel.add(libro2orderLabel);
			libro2orderLabel.setBorder(blackline);
			libro2panel.add(libro2sexLabel);
			libro2sexLabel.setBorder(blackline);
			libro2panel.add(libro2serve);
			libro2serve.setBorder(blackline);
			libro2panel.add(libro2block);
			libro2block.setBorder(blackline);
			libro2panel.add(libro2spike);
			libro2spike.setBorder(blackline);
			libro2panel.add(libro2fault);
			libro2fault.setBorder(blackline);
		}

	}

	public void iniDataJPanel() {
		p1Panel = new JPanel(new GridLayout(1, 1));
		p2Panel = new JPanel(new GridLayout(1, 1));
		p3Panel = new JPanel(new GridLayout(1, 1));
		p4Panel = new JPanel(new GridLayout(1, 1));
		p5Panel = new JPanel(new GridLayout(1, 1));
		p6Panel = new JPanel(new GridLayout(1, 1));
		p4Panel.add(number4panel);
		dataJPanel.add(p4Panel);
		p3Panel.add(number3panel);
		dataJPanel.add(p3Panel);
		p2Panel.add(number2panel);
		dataJPanel.add(p2Panel);
		p5Panel.add(number5panel);
		dataJPanel.add(p5Panel);
		p6Panel.add(number6panel);
		dataJPanel.add(p6Panel);
		p1Panel.add(number1panel);
		dataJPanel.add(p1Panel);
	}

	public void createdataJPanel() {
		if (scoreManager.getorder() == 1) {
			for (int i = 0; i < 100; i++) {// i是攔中的背號
				if (librosubstitution.get(i) == 1 && scoreManager.getserve() == false) {
					for (int k = 0; k < players.size(); k++) {
						if (players.get(k).getnumber() == i) { // if k = 0, 攔中就是在number1panel
							if (k + 1 == 1) {
								dataJPanel.add(number4panel);
								dataJPanel.add(number3panel);
								dataJPanel.add(number2panel);
								dataJPanel.add(number5panel);
								dataJPanel.add(number6panel);
								dataJPanel.add(libro1panel);
							} else if (k + 1 == 5) {
								dataJPanel.add(number4panel);
								dataJPanel.add(number3panel);
								dataJPanel.add(number2panel);
								dataJPanel.add(libro1panel);
								dataJPanel.add(number6panel);
								dataJPanel.add(number1panel);
							} else if (k + 1 == 6) {
								dataJPanel.add(number4panel);
								dataJPanel.add(number3panel);
								dataJPanel.add(number2panel);
								dataJPanel.add(number5panel);
								dataJPanel.add(libro1panel);
								dataJPanel.add(number1panel);
							}
						}
					}
				} else if (librosubstitution.get(i) == 2 && scoreManager.getserve() == false) {
					for (int k = 0; k < players.size(); k++) {
						if (players.get(k).getnumber() == i) { // if k = 0, 攔中就是在number1panel
							if (k + 1 == 1) {
								p1Panel.remove(number1panel);
								p1Panel.add(libro2panel);
								dataJPanel.updateUI();
								dataJPanel.repaint();
								libropos = 1;
								midpos = 1;
							} else if (k + 1 == 5) {
								p5Panel.remove(number5panel);
								p5Panel.add(libro2panel);
								dataJPanel.updateUI();
								dataJPanel.repaint();
								libropos = 5;
								midpos = 5;
							} else if (k + 1 == 6) {
								p6Panel.remove(number6panel);
								p6Panel.add(libro2panel);
								dataJPanel.updateUI();
								dataJPanel.repaint();
								libropos = 6;
								midpos = 6;
							}
						}
					}
				} else {

				}
			}
			if (scoreManager.getserve() == true) {
				turn.setText(String.format("現在輪到%d號%s發球", Integer.parseInt(numb1numLabel.getText()),
						numb1nameLabel.getText()));
				turn.setFont(new Font("TimesNewRoman", Font.PLAIN, 80));
				numb1serve.setEnabled(true);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			} else {
				turn.setText("現在是敵方發球");
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			}
		} else if (scoreManager.getorder() == 2) {
			dataJPanel.add(p5Panel);
			if (libropos == 5) {
				p5Panel.removeAll();
				p5Panel.add(number5panel);
				p5Panel.updateUI();
			}
			dataJPanel.add(p4Panel);
			dataJPanel.add(p3Panel);
			dataJPanel.add(p6Panel);
			dataJPanel.add(p1Panel);
			dataJPanel.add(p2Panel);
			if (scoreManager.getserve() == true) {
				turn.setText(String.format("現在輪到%d號%s發球", Integer.parseInt(numb2numLabel.getText()),
						numb2nameLabel.getText()));
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(true);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			} else {
				turn.setText("現在是敵方發球");
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			}
		} else if (scoreManager.getorder() == 3) {

			dataJPanel.add(p6Panel);
			if (libropos == 6) {
				p6Panel.removeAll();
				p6Panel.add(number6panel);
				p6Panel.updateUI();
			}
			dataJPanel.add(p5Panel);
			dataJPanel.add(p4Panel);
			dataJPanel.add(p1Panel);
			dataJPanel.add(p2Panel);
			dataJPanel.add(p3Panel);
			if (scoreManager.getserve() == true) {
				turn.setText(String.format("現在輪到%d號%s發球", Integer.parseInt(numb3numLabel.getText()),
						numb3nameLabel.getText()));
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(true);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			} else {
				turn.setText("現在是敵方發球");
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			}
		} else if (scoreManager.getorder() == 4) {

			dataJPanel.add(p1Panel);
			if (libropos == 1) {
				p1Panel.removeAll();
				p1Panel.add(number1panel);
				p1Panel.updateUI();
			}
			dataJPanel.add(p6Panel);
			dataJPanel.add(p5Panel);
			dataJPanel.add(p2Panel);
			dataJPanel.add(p3Panel);
			dataJPanel.add(p4Panel);
			if (scoreManager.getserve() == true) {
				turn.setText(String.format("現在輪到%d號%s發球", Integer.parseInt(numb4numLabel.getText()),
						numb4nameLabel.getText()));
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(true);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			} else {
				turn.setText("現在是敵方發球");
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			}
		} else if (scoreManager.getorder() == 5) {

			dataJPanel.add(p2Panel);
			if (libropos == 2) {
				p2Panel.removeAll();
				p2Panel.add(number2panel);
				p2Panel.updateUI();
			}
			dataJPanel.add(p1Panel);
			dataJPanel.add(p6Panel);
			dataJPanel.add(p3Panel);
			dataJPanel.add(p4Panel);
			dataJPanel.add(p5Panel);
			if (scoreManager.getserve() == true) {
				turn.setText(String.format("現在輪到%d號%s發球", Integer.parseInt(numb5numLabel.getText()),
						numb5nameLabel.getText()));
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(true);
				numb6serve.setEnabled(false);
			} else {
				turn.setText("現在是敵方發球");
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			}
		} else if (scoreManager.getorder() == 6) {

			dataJPanel.add(p3Panel);
			if (libropos == 3) {
				p3Panel.removeAll();
				p3Panel.add(number3panel);
				p3Panel.updateUI();
			}
			dataJPanel.add(p2Panel);
			dataJPanel.add(p1Panel);
			dataJPanel.add(p4Panel);
			dataJPanel.add(p5Panel);
			dataJPanel.add(p6Panel);
			if (scoreManager.getserve() == true) {
				turn.setText(String.format("現在輪到%d號%s發球", Integer.parseInt(numb6numLabel.getText()),
						numb6nameLabel.getText()));
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(true);
			} else {
				turn.setText("現在是敵方發球");
				numb1serve.setEnabled(false);
				numb2serve.setEnabled(false);
				numb3serve.setEnabled(false);
				numb4serve.setEnabled(false);
				numb5serve.setEnabled(false);
				numb6serve.setEnabled(false);
			}
		}
	}

	public void createLabel() {
		String scoreLine = players.get(6).getname() + " : " + players.get(7).getname() + " = "
				+ scoreManager.getAteamScore() + " : " + scoreManager.getBteamScore();
		score = new JLabel(scoreLine);
		score.setFont(new Font("TimesNewRoman", Font.PLAIN, 120));

		numb1numLabel = new JLabel("", JLabel.CENTER);
		numb1numLabel.setText(Integer.toString(players.get(0).getnumber()));
		numb1nameLabel = new JLabel("", JLabel.CENTER);
		numb1nameLabel.setText(players.get(0).getname());
		numb1orderLabel = new JLabel("", JLabel.CENTER);
		numb1orderLabel.setText(players.get(0).getposition());
		numb1sexLabel = new JLabel("", JLabel.CENTER);
		numb1sexLabel.setText(players.get(0).getsex());

		numb2numLabel = new JLabel("", JLabel.CENTER);
		numb2numLabel.setText(Integer.toString(players.get(1).getnumber()));
		numb2nameLabel = new JLabel("", JLabel.CENTER);
		numb2nameLabel.setText(players.get(1).getname());
		numb2orderLabel = new JLabel("", JLabel.CENTER);
		numb2orderLabel.setText(players.get(1).getposition());
		numb2sexLabel = new JLabel("", JLabel.CENTER);
		numb2sexLabel.setText(players.get(1).getsex());

		numb3numLabel = new JLabel("", JLabel.CENTER);
		numb3numLabel.setText(Integer.toString(players.get(2).getnumber()));
		numb3nameLabel = new JLabel("", JLabel.CENTER);
		numb3nameLabel.setText(players.get(2).getname());
		numb3orderLabel = new JLabel("", JLabel.CENTER);
		numb3orderLabel.setText(players.get(2).getposition());
		numb3sexLabel = new JLabel("", JLabel.CENTER);
		numb3sexLabel.setText(players.get(2).getsex());

		numb4numLabel = new JLabel("", JLabel.CENTER);
		numb4numLabel.setText(Integer.toString(players.get(3).getnumber()));
		numb4nameLabel = new JLabel("", JLabel.CENTER);
		numb4nameLabel.setText(players.get(3).getname());
		numb4orderLabel = new JLabel("", JLabel.CENTER);
		numb4orderLabel.setText(players.get(3).getposition());
		numb4sexLabel = new JLabel("", JLabel.CENTER);
		numb4sexLabel.setText(players.get(3).getsex());

		numb5numLabel = new JLabel("", JLabel.CENTER);
		numb5numLabel.setText(Integer.toString(players.get(4).getnumber()));
		numb5nameLabel = new JLabel("", JLabel.CENTER);
		numb5nameLabel.setText(players.get(4).getname());
		numb5orderLabel = new JLabel("", JLabel.CENTER);
		numb5orderLabel.setText(players.get(4).getposition());
		numb5sexLabel = new JLabel("", JLabel.CENTER);
		numb5sexLabel.setText(players.get(4).getsex());

		numb6numLabel = new JLabel("", JLabel.CENTER);
		numb6numLabel.setText(Integer.toString(players.get(5).getnumber()));
		numb6nameLabel = new JLabel("", JLabel.CENTER);
		numb6nameLabel.setText(players.get(5).getname());
		numb6orderLabel = new JLabel("", JLabel.CENTER);
		numb6orderLabel.setText(players.get(5).getposition());
		numb6sexLabel = new JLabel("", JLabel.CENTER);
		numb6sexLabel.setText(players.get(5).getsex());

		if (players.size() == 13) {
			libro1numLabel = new JLabel("", JLabel.CENTER);
			libro1numLabel.setText(Integer.toString(players.get(12).getnumber()));
			libro1nameLabel = new JLabel("", JLabel.CENTER);
			libro1nameLabel.setText(players.get(12).getname());
			libro1orderLabel = new JLabel("", JLabel.CENTER);
			libro1orderLabel.setText("自由");
			libro1sexLabel = new JLabel("", JLabel.CENTER);
			libro1sexLabel.setText(players.get(12).getsex());
		} else if (players.size() == 14) {
			libro1numLabel = new JLabel("", JLabel.CENTER);
			libro1numLabel.setText(Integer.toString(players.get(11).getnumber()));
			libro1nameLabel = new JLabel("", JLabel.CENTER);
			libro1nameLabel.setText(players.get(12).getname());
			libro1orderLabel = new JLabel("", JLabel.CENTER);
			libro1orderLabel.setText("自由");
			libro1sexLabel = new JLabel("", JLabel.CENTER);
			libro1sexLabel.setText(players.get(12).getsex());

			libro2numLabel = new JLabel("", JLabel.CENTER);
			libro2numLabel.setText(Integer.toString(players.get(13).getnumber()));
			libro2nameLabel = new JLabel("", JLabel.CENTER);
			libro2nameLabel.setText(players.get(13).getname());
			libro2orderLabel = new JLabel("", JLabel.CENTER);
			libro2orderLabel.setText("自由");
			libro2sexLabel = new JLabel("", JLabel.CENTER);
			libro2sexLabel.setText(players.get(13).getsex());
		}

		turn = new JLabel();
		if (opt == 1) {
			scoreManager.setserve(false);
		} else if (opt == 0) {
			scoreManager.setserve(true);
		}

	}

	public void checkLibroSubstitution() {
		// initialize the ArrayList
		for (int i = 0; i < 100; i++) {
			librosubstitution.add(i, 0);
		}
		if (players.size() == 13) {
			String changeString[] = { "換", "不換" };
			for (Players p : this.players) {
				if (p.getposition().equals("攔中")) {
					int i = JOptionPane.showOptionDialog(null,
							String.format("背號%d %s是否要與自由球員背號%d %s做替換？", p.getnumber(), p.getname(),
									Integer.parseInt(libro1numLabel.getText()), libro1nameLabel.getText()),
							"請選擇", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, changeString, "");
					if (i == 0) {
						librosubstitution.add(p.getnumber(), 1);
					}
				}
			}
		} else if (players.size() == 14) {
			String changeString[] = { "不換", libro1nameLabel.getText(), libro2nameLabel.getText() };
			for (Players p : this.players) {
				if (p.getposition().equals("攔中")) {
					int i = JOptionPane.showOptionDialog(null,
							String.format("背號%d %s要與哪位球員做替換？", p.getnumber(), p.getname()), "請選擇",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, changeString, "");
					if (i == 0) {
					} else if (i == 1) {
						librosubstitution.add(p.getnumber(), 1);
					} else if (i == 2) {
						librosubstitution.add(p.getnumber(), 2);
					}
				}
			}

		}
	}

	public void write() {
		JFileChooser fs = new JFileChooser(new File(""));
		fs.setDialogTitle("儲存檔案");
		fs.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "CSV Files(.csv)";
			}

			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
			}
		});
		int result = fs.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			String status = "";
			String server = "jdbc:mysql://140.119.19.73:3315/";
			String database = "110306047"; // change to your own database
			String url = server + database + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
			String dbusername = "110306047"; // change to your own username
			String dbpassword = "wughu"; // change to your own password
			try (Connection conn = DriverManager.getConnection(url, dbusername, dbpassword);) {
				Statement stat = conn.createStatement();
				String query = "";
				if (winner.equals("win")) {
					query = String.format(
							"INSERT INTO `records`(`accountname`, `time`, `place`, `teamAname`, `teamApoints`, `teamBname`, `teamBpoints`, `consequence`) VALUES ('%s','%s','%s','%s','%d','%s','%d','%s')",
							players.get(11).getname(), players.get(8).getname(), players.get(10).getname(),
							players.get(6).getname(), scoreManager.getAteamScore(), players.get(7).getname(),
							scoreManager.getBteamScore(), players.get(6).getname() + "勝");
				} else if (winner.equals("lose")) {
					query = String.format(
							"INSERT INTO `records`(`accountname`, `time`, `place`, `teamAname`, `teamApoints`, `teamBname`, `teamBpoints`, `consequence`) VALUES ('%s','%s','%s','%s','%d','%s','%d','%s')",
							players.get(11).getname(), players.get(8).getname(), players.get(10).getname(),
							players.get(6).getname(), scoreManager.getAteamScore(), players.get(7).getname(),
							scoreManager.getBteamScore(), players.get(7).getname() + "勝");
				}
				boolean hasResultSet = stat.execute(query);
				if (!hasResultSet) {
					status += "Record Uploaded Successfully\n";
				} else {
					status += "Didn't upload the record!\n";
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			sb.append("排球自動化紀錄系統報表");
			sb.append("\n");
			sb.append(String.format("地點: %s", players.get(10).getname()));
			sb.append("\n");
			sb.append(String.format("時間: %s", players.get(8).getname()));
			sb.append("\n");
			sb.append(String.format("記錄者: %s", players.get(9).getname()));
			sb.append("\n");
			if (winner.equals("win")) {
				sb.append(String.format("結果: %s勝", players.get(6).getname()));
			} else if (winner.equals("lose")) {
				sb.append(String.format("結果: %s勝", players.get(7).getname()));
			}
			sb.append("\n");
			sb.append(String.format("最終比數 %s %d : %s %d", players.get(6).getname(), scoreManager.getAteamScore(),
					players.get(7).getname(), scoreManager.getBteamScore()));
			sb.append("\n");
			sb.append("\n");
			sb.append("球員姓名, 背號, 位置, 性別, 發球得分, 攔網得分, 攻擊得分, 失誤失分");
			sb.append("\n");
			int i = 1;
			for (Players p : players) {
				if (p.getposition() != "") {
					sb.append(String.format("%s, %d, %s, %s, %d, %d, %d, -%d", p.getname(), p.getnumber(),
							p.getposition(), p.getsex(), serves.get(i), blocks.get(i), spikes.get(i), errors.get(i)));
					sb.append("\n");
					i++;
				}
			}
			sb.append(String.format("對方失誤導致我方得分, %d", scoreManager.enemy_error));

			String content = sb.toString();
			File fi = fs.getSelectedFile();
			try {
				if (fi.getPath().endsWith(".csv")) {
					FileWriter fw = new FileWriter(fi.getPath());
					fw.write(content);
					fw.flush();
					fw.close();
					status += String.format("檔案已匯出至%s\n謝謝您的使用！", fi.getPath());
					JOptionPane.showMessageDialog(null, status);
					System.exit(0);
				} else {
					FileWriter fw = new FileWriter(fi.getPath().concat(".csv"));
					fw.write(content);
					fw.flush();
					fw.close();
					status += String.format("檔案已匯出至%s\n謝謝您的使用！", fi.getPath().concat(".csv"));
					JOptionPane.showMessageDialog(null, status);
					System.exit(0);
				}

			} catch (Exception e) {
				System.out.println("ouput failed!");
			}
		}
	}

	public void initialize() {
		for (int i = 0; i < 9; i++) {
			serves.add(i, 0);
			blocks.add(i, 0);
			spikes.add(i, 0);
			errors.add(i, 0);
		}
	}
}
