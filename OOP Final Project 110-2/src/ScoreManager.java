 	import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ScoreManager {

	public int ateamscore = 0;
	public int bteamscore = 0;
	private String winner = "";
	public StringBuffer winnerorder = new StringBuffer();
	private int order = 1;
	private boolean serve;
	public int enemy_error = 0;

	public int getorder() {
		return order;
	}

	public boolean getserve() {
		return serve;
	}

	public void setserve(boolean serve) {
		this.serve = serve;
	}



	public int getAteamScore() {
		return ateamscore;
	}

	public int getBteamScore() {
		return bteamscore;
	}

	public void addservespoint(int number) {
		winnerorder.append("1");
		ateamscore++;
	}

	public void addblockspoint(int number) {
		winnerorder.append("1");
		ateamscore++;
	}

	public void addspikespoint(int number) {
		winnerorder.append("1");
		ateamscore++;
	}

	public void adderrorspoint(int number) {
		winnerorder.append("0");
		bteamscore++;
	}

	public void addopponentpoint() {
		winnerorder.append("0");
		bteamscore++;
	}

	public String checkwin(int a, int b) {
		this.ateamscore = a;
		this.bteamscore = b;
		// deuce considered
		if (a == 25 && b <= 23 || a > 24 && b > 24 && a - b >= 2) {
			JOptionPane.showMessageDialog(null, "比賽結束，我方贏了！", "比賽結束！", JOptionPane.INFORMATION_MESSAGE);
			System.out.println("Output successfully");
			return "win";

		} else if (b == 25 && a <= 23 || a > 24 && b > 24 && b - a >= 2) {
			JOptionPane.showMessageDialog(null, "比賽結束，敵隊贏了：（", "比賽結束！", JOptionPane.INFORMATION_MESSAGE);
			System.out.println("Output successfully");
			return "lose";
		} else {
			return "null";
		}
	}

	public void checkorder() {
		String orderString = winnerorder.toString();
		if (orderString.substring(orderString.length() - 1).equals("0")) {
			serve = false;
		} else if (orderString.substring(orderString.length() - 1).equals("1")) {
			serve = true;
		}
		if (orderString.length() >= 2) {
			if (orderString.substring(orderString.length() - 2).equals("01")) {
				order = changeorder();
			} else {
				return;
			}
		}

	}

	public int changeorder() {
		if (order == 1) {
			order = 2;
		} else if (order == 2) {
			order = 3;
		} else if (order == 3) {
			order = 4;
		} else if (order == 4) {
			order = 5;
		} else if (order == 5) {
			order = 6;
		} else if (order == 6) {
			order = 1;
		}
		return order;
	}

}
