package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class MainFrame extends CommonFrame {

	static int userNo = 0;
	
	JPanel companyPanel = new JPanel(null);
	Thread t;
	
	public MainFrame() {
		super(300, 420, "Main");
		
		setLayout(new BorderLayout());

		// Panel 기본 FlowLayout
		var a = new JPanel();
		var b = new JPanel(new BorderLayout());
		var c = new JPanel(null);
		
		a.add(new TitleLabel("아르바이트", 24));
		
		var b_north = new JPanel();
		var b_center = new JPanel(null);
		var tfSearch = new JTextField(12);
		var btSearch = new JButton("검색");
		
		b_north.add(new JLabel("기업검색"));
		b_north.add(tfSearch);
		b_north.add(btSearch);
		b_center.add(setBounds(new JLabel("인기기업"), 10, 0, 80, 24));
		
		try (var rs = getResulSet("SELECT *\r\n"
				+ "FROM company\r\n"
				+ "ORDER BY c_search DESC, c_no\r\n"
				+ "LIMIT 5")) {
			
			int id = 0;
			
			while (rs.next()) {
				var lbId = new JLabel(String.valueOf(id + 1));
				var lbName = new JLabel(rs.getString("c_name"));
				var lbSearch= new JLabel(rs.getString("c_search"));
				
				b_center.add(setBounds(lbId, 10, 35 + id * 30, 30, 30));
				b_center.add(setBounds(lbName, 40, 35 + id * 30, 120, 30));
				b_center.add(setBounds(lbSearch, 120, 35 + id * 30, 30, 30));
				
				++id;
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		if (userNo == 0) {
			// 로그아웃
			var btList = "로그인,회원가입,닫기".split(",");
			
			for (int i = 0; i < btList.length; i++) {
				var btn = new JButton(btList[i]);
				
				b_center.add(setBounds(btn, 160, 30 + i * 36, 120, 32));
				
				if (i == 0) {
					btn.addActionListener(e -> {
						dispose();
						new LoginFrame().showWithPrev(e1 -> new MainFrame().setVisible(true));						
					});
				} else if (i == 1) {
					btn.addActionListener(e -> {
						dispose();
						new SignUpFrame().showWithPrev(e1 -> new MainFrame().setVisible(true));
					});
				} else
					btn.addActionListener(e -> dispose());				
			}
		} else {
			// 로그인 상태
			var btList = "로그아웃,채용정보,마이페이지,닫기".split(",");
			
			// 로그인 정보 로드 (이름, 사진)
			for (int i = 0; i < btList.length; i++) {
				var btn = new JButton(btList[i]);
				
				b_center.add(setBounds(btn, 160, 30 + i * 36, 120, 32));
				
				if (i == 0) {
					btn.addActionListener(e -> {
						// 로그아웃
						userNo = 0;
						dispose();
						new MainFrame().setVisible(true);						
					});
				} else if (i == 1) {

				} else if (i == 2) {
					btn.addActionListener(e -> {
						dispose();
						
						MyPageFrame.userNo = userNo;
						
						new MyPageFrame().showWithPrev(e1 -> new MainFrame().setVisible(true));
					});
				} else
					btn.addActionListener(e -> dispose());
			}
			
			try (var rs = getResulSet("SELECT * FROM user WHERE u_no = ?", userNo)) {
				
				if (rs.next()) {
					var lbImg = new JLabel();
					var lbCaption = new JLabel(rs.getString("u_name") + "님 환영합니다.");
					var userImg = ImageIO.read( rs.getBlob("u_img").getBinaryStream() );
					
					// 배치되는 레이아웃이 있는 경우에는, setPefferedSize 
					lbImg.setPreferredSize(new Dimension(20, 20));
					lbImg.setBorder(new LineBorder(Color.BLACK));
					lbImg.setIcon( new ImageIcon( userImg.getScaledInstance(20, 20, 4) ) );
					
					
					a.add(lbImg);
					a.add(lbCaption);
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		b.add(b_north, "North");
		b.add(b_center);
		
		var cbArea = new JComboBox("전체,서울,부산,대구,인천,광주,대전,울산,세종,경기,강원,충북,충남,전북,전남,경북,경남,제주".split(","));
		
		c.add(setBounds(new JLabel("지역"), 10, 0, 40, 24));
		c.add(setBounds(cbArea, 40, 0, 100, 24));
		c.add(setBounds(companyPanel, 5, 30, 270, 80));
		c.setPreferredSize(new Dimension(0, 120));
		
		updateCompanyPanel("전체");
	
		cbArea.addActionListener(e -> updateCompanyPanel( (String) cbArea.getSelectedItem() ));
		
		add(a, "North");
		add(b);
		add(c, "South");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent e) {
				if (t != null)
					t.interrupt();
			};
		});
		
	}
	
	void updateCompanyPanel(String area) {
		String keyword = area.equals("전체") ? "%" : area + "%";
		
		companyPanel.removeAll();
		
		var panelList = new ArrayList<JPanel>();
		
		try (var rs = getResulSet("SELECT *\r\n"
				+ "FROM 2022지방_2.company\r\n"
				+ "WHERE c_address LIKE ?", keyword)) {
			
			int id = 0;
			
			while(rs.next()) {
				var panel = new JPanel(null);
				
				var img = ImageIO.read(rs.getBlob("c_img").getBinaryStream());
				var lbImg = new JLabel( new ImageIcon( img.getScaledInstance(80, 60, 4) ) );
				var lbName = new JLabel(rs.getString("c_name"), 0);
				
				panel.setBorder(new LineBorder(Color.BLACK));
				
				panel.add(setBounds(lbImg, 1, 1, 78, 60));
				panel.add(setBounds(lbName, 0, 60, 80, 20));
				
				companyPanel.add(setBounds(panel, id * 80, 0, 80, 80));
				
				panelList.add(panel);
				++id;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		companyPanel.repaint();
		
		if (t != null)
			t.interrupt();
		
		t = new Thread( () -> {
			
			try {
				while (true) {
					
					for(var panel : panelList) {
						panel.setLocation(panel.getX() - 1, 0);
					}
					
					t.sleep(30);
				}				
			} catch (Exception e) {}
			
		} );
		
		t.start();
	}
	
	public static void main(String[] args) {
		
		userNo = 1;
		
		new MainFrame().setVisible(true);
	}

}
