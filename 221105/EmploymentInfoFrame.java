package app;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class EmploymentInfoFrame extends CommonFrame {

	public EmploymentInfoFrame() {
		super(740, 550, "채용정보");
		
		add( setBounds(new TitleLabel( "채용정보", 24), 0, 0, 740, 60) );
		
		add( setBounds(new JLabel( "공고명", 0), 10, 60, 60, 20) );
		add( setBounds(new JLabel( "직종", 0), 10, 90, 60, 20) );
		add( setBounds(new JLabel( "지역", 0), 10, 120, 60, 20) );
		
		var tfTitle = new JTextField();
		var tfSkill= new JTextField();
		var cbList = new JComboBox[] {
			new JComboBox<String>("전체,서울,부산,대구,인천,광주,대전,울산,세종,경기,강원,충북,충남,전북,전남,경북,경남,제주".split(",")),
			new JComboBox<String>("전체,대학교 졸업,고등학교 졸업,중학교 졸업,무관".split(",")),
			new JComboBox<String>("전체,남자,여자,무관".split(","))
		};
		
		add( setBounds(tfTitle, 80, 60, 150, 20) );
		add( setBounds(tfSkill, 80, 90, 150, 20) );
		add( setBounds(cbList[0], 80, 120, 80, 28) );
		add( setBounds(new JLabel( "학력", 0), 160, 120, 60, 28) );
		add( setBounds(cbList[1], 220, 120, 100, 28) );
		add( setBounds(new JLabel( "성별", 0), 320, 120, 60, 28) );
		add( setBounds(cbList[2], 380, 120, 60, 28) );
		
		var btnSearch = new JButton("검색하기");
		var btnApply = new JButton("지원하기");
		
		add( setBounds(btnSearch, 520, 120, 90, 28) );
		add( setBounds(btnApply, 620, 120, 90, 28) );
		
		var model = new DefaultTableModel("이미지,공고명,모집정원,시급,직종,지역,학력,성별".split(","), 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		var table = new JTable(model);
		
		var renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				var comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				if (column == 0) {
					var img = (BufferedImage) value;
					
					this.setIcon(new ImageIcon( img.getScaledInstance(40, 40, 4) ));
					this.setText("");
				} else {
					this.setIcon(null);
				}
				
				return comp;
			}
		};
		
		int[] widthList = {80, 200, 100, 60, 160, 180, 100, 50};
		
		renderer.setHorizontalAlignment(0);
		
		for (int i = 0; i < 8; i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
			table.getColumnModel().getColumn(i).setPreferredWidth(widthList[i]);
		}
		
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(40);
				
		add( setBounds(new JScrollPane(table), 10, 170, 710, 330) );
		
		btnSearch.addActionListener(e -> {
			
			try(var rs = getResulSet("SELECT e.e_no, e_title, COUNT(1) AS cnt, e_people, e_pay, c_category, c_address, e_graduate, e_gender, c_img\r\n"
					+ "FROM 2022지방_2.employment e\r\n"
					+ "INNER JOIN company c ON e.c_no = c.c_no\r\n"
					+ "INNER JOIN applicant a ON e.e_no = a.e_no\r\n"
					+ "WHERE a.a_apply != 2\r\n"
					+ "GROUP BY e.e_no;")) {
				
				// 기존 데이터 삭제
				model.setRowCount(0);
				
				while (rs.next()) {
					var img = ImageIO.read( rs.getBlob(10).getBinaryStream() );
							
					model.addRow(new Object[] {
						img,
						rs.getString(2),
						rs.getInt(3) + "/" + rs.getInt(4),
						String.format("%,d", rs.getInt(5)),
						rs.getString(6),
						rs.getString(7),
						rs.getInt(8),
						rs.getInt(9)
					});
				}
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		
		btnSearch.doClick();
	}
	
	public static void main(String[] args) {
		new EmploymentInfoFrame().setVisible(true);

	}

}
