import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import chernyj.jdatepicker.JDatePicker;

public class Test {

	public static void main(String[] args) {
		JDatePicker picker = new JDatePicker();

		JFrame frame = new JFrame();
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		
		JButton btn = new JButton("Clear");
		
		btn.addActionListener((l)->{
			picker.clear();
		});
		
		frame.setLayout(new FlowLayout());
		frame.add(picker);
		frame.add(btn);
		
		

		frame.setVisible(true);
	}

}
