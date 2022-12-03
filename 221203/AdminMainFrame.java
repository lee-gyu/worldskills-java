package app;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class AdminMainFrame extends CommonFrame {
	int hoveredCompanyNo = 0;
	
	public AdminMainFrame() {
		super( 600, 550, "관리자 메인" );
		
		setLayout(new BorderLayout());
		
		var cPanel = new JPanel( new GridLayout(5, 5, 5, 5) );
		var sPanel = new JPanel();
		var labelList = "채용 정보,지원자 목록,공고 등록,지원자 분석,닫기".split(",");
		
		cPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		try (var rs = getResulSet("SELECT * FROM company")) {
			
			while (rs.next()) {
				int cNo = rs.getInt("c_no");
				var image = ImageIO.read( rs.getBlob("c_img").getBinaryStream() );
				var img = new JLabel() {
					
					@Override
					public void paint(Graphics g) {
						Graphics2D g2d = (Graphics2D) g;
						
						g2d.setComposite( AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 
								cNo == hoveredCompanyNo ? 1f : 0.2f) );
						
						this.setIcon(new ImageIcon(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH)));
						
						super.paint(g);
					}
				};
			
				img.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						hoveredCompanyNo = cNo;
						img.repaint();
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						hoveredCompanyNo = 0;
						img.repaint();
					}
				});
				
				img.setToolTipText(rs.getString("c_name"));
				img.setBorder(new LineBorder(Color.BLACK));
				
				cPanel.add(img);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String lb : labelList) {
			var btn = new JButton(lb);
			
			sPanel.add(btn);
		}
		
		add(cPanel);
		add(sPanel, "South");
	}
	
	
	public static void main(String[] args) {
		new AdminMainFrame().setVisible(true);
	}
	
}
