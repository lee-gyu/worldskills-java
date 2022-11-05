package app;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

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
		
	}
	
	public static void main(String[] args) {
		new EmploymentInfoFrame().setVisible(true);

	}

}
