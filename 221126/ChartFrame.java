package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ChartFrame extends CommonFrame {
	int[] userCount = new int[6];

	public ChartFrame() {
		super(650, 450, "지원자 분석");
		
		setLayout(new BorderLayout());
		
		Color[] palette = { null, Color.BLACK, Color.BLUE, Color.RED, Color.green, Color.yellow };
		
		var nPanel = new JPanel();
		var cPanel = new JPanel() {
			
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				// 배열의 max 값을 구하는 Stream API
				int max = Arrays.stream(userCount).max().getAsInt();
				int barHeight = this.getHeight() - 60;
				
				
				for (int i = 1; i <= 5; i++) {
					// int / double -> 0 or 1 (double / double 0.25)
					// int / double 0.25 -> 0
					double scale = (double) userCount[i] / max;
					int height = (int) (scale * barHeight);
					int y = barHeight - height;
					
					g.setColor(palette[i]);
					
					// 수학적인 생각
					// 50: 시작 위치
					// (i-1) * 100: 간격을 의미
					g.fillRect(50 + (i-1) * 100, y, 40, height);
					g.setColor(Color.black);
					g.drawRect(50 + (i-1) * 100, y, 40, height);
					g.drawString( i + "0대", 60 + (i-1) * 100, barHeight + 20);
				}
			}
		};
		
		
		var cbCompany = new JComboBox<>();
		
		var rs2 = getResulSet("SELECT * FROM company");
		
		try {
			while (rs2.next()) {
				cbCompany.addItem(rs2.getString("c_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		nPanel.add(cbCompany);
		
		cbCompany.addActionListener(e -> {
			updateUserCount( cbCompany.getSelectedIndex() + 1 );
			cPanel.repaint();
		});
		
		updateUserCount(1);
		
		add(nPanel, "North");
		add(cPanel);
		
	}
	
	void updateUserCount(int cNo) {

		var rs = getResulSet("SELECT\r\n"
				+ "	COUNT(IF(year(now()) - year(u_birth) BETWEEN 10 AND 19, 1, null ) ) AS `10`,\r\n"
				+ "    COUNT(IF(year(now()) - year(u_birth) BETWEEN 20 AND 29, 1, null )) AS `20`,\r\n"
				+ "    COUNT(IF(year(now()) - year(u_birth) BETWEEN 30 AND 39, 1, null )) AS `30`,\r\n"
				+ "    COUNT(IF(year(now()) - year(u_birth) BETWEEN 40 AND 49, 1, null )) AS `40`,\r\n"
				+ "    COUNT(IF(year(now()) - year(u_birth) BETWEEN 50 AND 59, 1, null )) AS `50`\r\n"
				+ "FROM applicant a\r\n"
				+ "INNER JOIN user u ON a.u_no = u.u_no\r\n"
				+ "INNER JOIN employment e ON a.e_no = e.e_no\r\n"
				+ "WHERE e.c_no = ?", cNo);
		
		try {
			rs.next();
			
			for (int i = 1; i <= 5; i++) {
				userCount[i] = rs.getInt(i);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ChartFrame().setVisible(true);
	}

}
