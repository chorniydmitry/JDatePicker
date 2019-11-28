package chernyj.jdatepicker;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import chernyj.jdatepicker.utils.ComponentObserver;
import chernyj.jdatepicker.utils.StringToDateParser;

/**
 * @author Chernyj Dmitry
 *
 */
public class JDatePicker extends JPanel implements ComponentObserver {
	private static final long serialVersionUID = -6453765567891059034L;

	private static final Dimension BTN_SELECT_DIM = new Dimension(20, 20);
	private static final Dimension TF_DATE_DIM = new Dimension(85, 20);

	private JTextField tfDate;
	private JButton btnSelect = new JButton();
	private JDateDialog dateDialog;

	public JDatePicker() {
		initComponents();
		add(tfDate);
		add(btnSelect);

		setVisible(true);
	}

	private void initComponents() {

		Icon icon = new ImageIcon(JDatePicker.class.getResource("/calendar_gr.png"));
		btnSelect.setIcon(icon);
		btnSelect.setPreferredSize(BTN_SELECT_DIM);
		btnSelect.addActionListener((l) -> doBtnSelectClick());

		tfDate = new JTextField();
		tfDate.setPreferredSize(TF_DATE_DIM);
		tfDate.addActionListener((l) -> doTfDateEnterHit());

	}

	private void doTfDateEnterHit() {
		Date date = getDate();
		if(date == null)
			tfDate.setText(null);
		else 
			tfDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(date));
	}
	
	public Date getDate() {
		return StringToDateParser.parse(tfDate.getText());
	}

	private void doBtnSelectClick() {
		int xPos = (int) (btnSelect.getLocationOnScreen().getX() + btnSelect.getWidth() - JDateDialog.DEFAULT_WIDHT);
		int yPos = (int) (btnSelect.getLocationOnScreen().getY() + btnSelect.getHeight());

		dateDialog = new JDateDialog(this, xPos, yPos, getDate());
		
	}
	
	public void clear() {
		tfDate.setText(null);
		if(dateDialog != null)
			dateDialog.setDateSelected(null);
	}

	@Override
	public void update() {
		String dateStr = new SimpleDateFormat("dd.MM.yyyy").format(dateDialog.getDate());
		tfDate.setText(dateStr);
	}

}
