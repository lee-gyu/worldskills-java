package app;

import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends CommonFrame {

	public LoginFrame() {
		super(300, 150, "로그인");
		
		var tfId = new JTextField();
		var tfPw = new JPasswordField();
		var btnLogin = new JButton("로그인");
		
		add( setBounds(new TitleLabel("아르바이트", 24), 0, 0, 300, 50) );
		add( setBounds(new JLabel("아이디"), 10, 45, 60, 20) );
		add( setBounds(new JLabel("비밀번호"), 10, 75, 60, 20) );
		add( setBounds(tfId, 75, 45, 120, 20) );
		add( setBounds(tfPw, 75, 75, 120, 20) );
		add( setBounds(btnLogin, 200, 40, 80, 70) );
		
		btnLogin.addActionListener(e -> {
			
			if (tfId.getText().length() == 0 ||
				tfPw.getText().length() == 0) {
				errorMsg("빈칸이 존재합니다.");
				return;
			}
			
			// 관리자 로그인
			if (tfId.getText().equals("admin") &&
				tfPw.getText().equals("1234")) {
				infoMsg("관리자로 로그인하였습니다.");
				
				dispose();
				// 관리자 메인 폼 출력
				return;
			}
			
			try (var rs = getResulSet("SELECT * FROM user WHERE u_id = ? AND u_pw = ?", tfId.getText(), tfPw.getText())) {
				
				if (rs.next() == false) {
					errorMsg("회원 정보가 일치하지 않습니다.");
					
					tfId.setText("");
					tfPw.setText("");
					tfId.grabFocus();
					
					return;
				}
				
				infoMsg(rs.getString("u_name") + "님 환영합니다." );
				dispose();
				
				MainFrame.userNo = rs.getInt("u_no");
				
				new MainFrame().setVisible(true);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		});
		
	}
	
	public static void main(String[] args) {
		new LoginFrame().setVisible(true);
	}

}
