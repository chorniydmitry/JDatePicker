import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;

import chernyj.jdatepicker.JDatePicker;
import chernyj.jdatepicker.utils.CalendarUtils;

public class Test {

	public static void main(String[] args) {
		JDatePicker picker = new JDatePicker();

		JFrame frame = new JFrame();
		frame.setSize(new Dimension(200, 100));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		
		//picker.setPrimaryColor(new Color(0x036ca0));
		//picker.setSecondaryColor(new Color(0xffffff));
		
		
		JButton btn = new JButton("Clear");
		
		btn.addActionListener((l)->{
			picker.clear();
		});
		
		frame.setLayout(new FlowLayout());
		frame.add(picker);
		frame.add(btn);
		
		

		frame.setVisible(true);
		
		Date date = new Date();
		System.out.println("current " + date);
		
		Date nextWorking = CalendarUtils.getNextWorkingDate(date);
		
		System.out.println("next working " + nextWorking);
		
		
	}

}
