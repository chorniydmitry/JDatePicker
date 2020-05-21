package chernyj.jdatepicker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import chernyj.jdatepicker.utils.CalendarUtils;
import chernyj.jdatepicker.utils.ComponentObserver;

/**
 * @author Chernyj Dmitry
 *
 */
public class JDateDialog extends JDialog {
	private static final long serialVersionUID = -1702669759908181547L;
	private static final String[] DAYS_OF_WEEK = { "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс" };
	private static final String[] MONTHS = { "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
			"Сентябрь", "Октябрь", "Ноябрь", "Декабрь" };
	
	private static final Color HOVER_COLOR = new Color(0xdddddd);
	
	private static final Color DEFAULT_FOREGROUND = new Color(0x333333);
	private static final Color DEFAULT_BAKCGROUND = new Color(0xdddddd);

	private Color sideLabelForeground;
	private Color currentDayBackground;
	private Color currentDayForeground;
	private Color hoverDayBackground;
	private Color commonDayBackground;
	private Color commonDayForeground;

	private static final int MINIMAL_WIDTH = 190;
	private static final int MINIMAL_HEIGHT = 150;

	public static final int DEFAULT_WIDHT = 200;
	public static final int DEFAULT_HEIGHT = 175;

	private static final int MINIMAL_YEAR = 1900;
	private static final int MAXIMAL_YEAR = 2100;

	private Date dateSelected;

	private JPanel pnlUpper = new JPanel();
	private JComboBox<String> cbMonthList = new JComboBox<>(MONTHS);
	private JSpinner spinMonths = new JSpinner();
	private JSpinner spinYears = new JSpinner();
	private JPanel pnlCalendar = new JPanel();
	private JLabel[] lblsDayOfWeak = new JLabel[CalendarUtils.DAYS_IN_WEEK];
	private JLabel[] lblsWeaksInMonth = new JLabel[CalendarUtils.WEEAKS_IN_MONTH_SHOWING];
	private JLabel[][] days = new JLabel[CalendarUtils.WEEAKS_IN_MONTH_SHOWING][CalendarUtils.DAYS_IN_WEEK];
	private ComponentObserver componentObserver;

	private int selectedDay;

	
	private void commonConstructor(ComponentObserver observer) {
		this.setMinimumSize(new Dimension(MINIMAL_WIDTH, MINIMAL_HEIGHT));
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.componentObserver = observer;
		
		this.setUndecorated(true);
		
		initColors();

		initUpperPanel();

		layoutUpperPanel();

		initPanelCalendar();

		initContentPane();

		updateLabelsForDate(Calendar.getInstance().getTime());
		
		this.addWindowFocusListener(new JDialogController());
		
		this.setAlwaysOnTop(true);

		this.setVisible(true);
	}
	
	private void initColors() {
		Color primary = componentObserver.getPrimaryColor();
		Color secondary = componentObserver.getSecondaryColor();
				
		primary = (primary.equals(new JPanel().getBackground()) || primary.equals(JDatePicker.DEFAULT_PRIMARY))? secondary : primary;
		secondary = (secondary.equals(new JPanel().getForeground()) || secondary.equals(JDatePicker.DEFAULT_SECONDARY)) ? primary : secondary;
		
		sideLabelForeground = primary.getRGB() > new JLabel().getBackground().getRGB() ? secondary : primary;
		currentDayBackground = primary;
		currentDayForeground = secondary;
		hoverDayBackground = HOVER_COLOR;
		commonDayBackground = new JLabel().getBackground();
		commonDayForeground = new JLabel().getForeground();
	}
	
	public JDateDialog(ComponentObserver observer, int xPos, int yPos) {
		this.setSize(new Dimension(DEFAULT_WIDHT, DEFAULT_HEIGHT));

		this.setLocation(xPos, yPos);
		
		commonConstructor(observer);
		
	}
	
	public JDateDialog(ComponentObserver observer, int xPos, int yPos, Date date) {
		
		this.setSize(new Dimension(DEFAULT_WIDHT, DEFAULT_HEIGHT));

		this.setLocation(xPos, yPos);
		
		dateSelected = date;
		
		commonConstructor(observer);
	}

	public JDateDialog(ComponentObserver observer, int xPos, int yPos, int width, int height) {
		this.setSize(new Dimension(width, height));

		this.setLocation(xPos, yPos);

		commonConstructor(observer);
	}

	public JDateDialog(ComponentObserver observer) {
		this.setSize(new Dimension(DEFAULT_WIDHT, DEFAULT_HEIGHT));
		commonConstructor(observer);
	}

	public Date getDate() {
		return dateSelected;
	}

	private void initContentPane() {
		this.add(pnlUpper, BorderLayout.NORTH);
		this.add(pnlCalendar, BorderLayout.CENTER);
	}

	private void initUpperPanel() {
		cbMonthList.setSelectedIndex(CalendarUtils.getMonthIndex(dateSelected));

		SpinnerNumberModel spinModel = new SpinnerNumberModel(CalendarUtils.getYear(dateSelected), MINIMAL_YEAR,
				MAXIMAL_YEAR, 1);
		spinYears = new JSpinner(spinModel);
		spinYears.setEditor(new JSpinner.NumberEditor(spinYears, "####"));
		spinYears.addChangeListener(new JDialogController());

		spinMonths.setValue(CalendarUtils.getMonthIndex(dateSelected) + 1);
		spinMonths.addChangeListener(new JDialogController());

		cbMonthList.addActionListener(new JDialogController());
	}

	private void layoutUpperPanel() {
		spinMonths.setEditor(cbMonthList);

		pnlUpper.setLayout(new BorderLayout());

		pnlUpper.add(spinMonths, BorderLayout.WEST);

		pnlUpper.add(spinYears, BorderLayout.EAST);

		pnlUpper.setVisible(true);

	}

	private void initDaysOfWeek() {
		LabelBuilder lblBuilder = new LabelBuilder();
		for (int i = 0; i < CalendarUtils.DAYS_IN_WEEK; i++)
			lblsDayOfWeak[i] = lblBuilder.getSideLabel(DAYS_OF_WEEK[i]);
	}

	private void initWeekNums() {
		LabelBuilder lblBuilder = new LabelBuilder();
		for (int i = 0; i < CalendarUtils.WEEAKS_IN_MONTH_SHOWING; i++)
			lblsWeaksInMonth[i] = lblBuilder.getSideLabel("");
	}

	private void initPanelCalendar() {
		pnlCalendar
				.setLayout(new GridLayout(CalendarUtils.WEEAKS_IN_MONTH_SHOWING + 1, CalendarUtils.DAYS_IN_WEEK + 1));

		initDaysOfWeek();
		initWeekNums();

		pnlCalendar.add(new JLabel());

		selectedDay = CalendarUtils.getDay(dateSelected);

		LabelBuilder lblBuilder = new LabelBuilder();

		for (int i = 0; i < CalendarUtils.WEEAKS_IN_MONTH_SHOWING; i++) {
			if (i == 0) {
				for (int k = 0; k < lblsDayOfWeak.length; k++)
					pnlCalendar.add(lblsDayOfWeak[k]);
			}

			for (int j = 0; j < CalendarUtils.DAYS_IN_WEEK; j++) {
				if (j == 0)
					pnlCalendar.add(lblsWeaksInMonth[i]);

				days[i][j] = lblBuilder.getCommonLabel("");
				days[i][j].setOpaque(true);
				pnlCalendar.add(days[i][j]);
			}
		}

	}

	private void selectDayLabel(JLabel lblDay, boolean action) {
		if (action) {
			lblDay.setBackground(currentDayBackground);
			lblDay.setForeground(currentDayForeground);
		} else {
			lblDay.setBackground(commonDayBackground);
			lblDay.setForeground(commonDayForeground);
		}

	}

	public void updateLabelsForDate(Date date) {
		int day = CalendarUtils.getDay(dateSelected);

		String[][] daysInMonthCaps = CalendarUtils.getDays(date);
		for (int i = 0; i < CalendarUtils.WEEAKS_IN_MONTH_SHOWING; i++)
			for (int j = 0; j < CalendarUtils.DAYS_IN_WEEK; j++) {
				days[i][j].setText(daysInMonthCaps[i][j]);
				selectDayLabel(days[i][j], false);
				if (String.valueOf(day).equals(daysInMonthCaps[i][j]))
					selectDayLabel(days[i][j], true);
			}

		String[] weeksNums = CalendarUtils.getNumbersOfWeeks(date);
		for (int i = 0; i < CalendarUtils.WEEAKS_IN_MONTH_SHOWING; i++) {
			lblsWeaksInMonth[i].setText(weeksNums[i]);
		}

	}
	
	public void setDateSelected(Date newDate) {
		this.dateSelected = newDate;
	}

	class LabelBuilder {

		private JLabel getCommonLabel(String text) {
			JLabel label = new JLabel(text);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.addMouseListener(new JDialogController());
			return label;
		}

		private JLabel getSideLabel(String text) {
			JLabel label = new JLabel(text);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setForeground(sideLabelForeground);
			return label;

		}

		@SuppressWarnings("unused")
		private JLabel getSelectedLabel(String text) {
			JLabel label = new JLabel(text);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setOpaque(true);
			label.setBackground(currentDayBackground);
			label.setForeground(currentDayForeground);
			return label;
		}
	}

	class JDialogController extends MouseAdapter implements ActionListener, ChangeListener, WindowFocusListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbMonthList) {
				updateLabelsForDate(updateDate());
			}

		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == spinYears) {

				updateLabelsForDate(updateDate());
			}

			if (e.getSource() == spinMonths) {
				if ((int) spinMonths.getValue() == 0) {
					spinMonths.setValue(CalendarUtils.MONTHS_IN_YEAR);
					spinYears.setValue((int) spinYears.getValue() - 1);
				}
				if ((int) spinMonths.getValue() == CalendarUtils.MONTHS_IN_YEAR + 1) {
					spinMonths.setValue(1);
					spinYears.setValue((int) spinYears.getValue() + 1);
				}
				cbMonthList.setSelectedIndex((int) (spinMonths.getValue()) - 1);
				updateLabelsForDate(updateDate());
			}

		}

		private Date updateDate() {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

			String dateInString = String.valueOf(selectedDay) + "." + (cbMonthList.getSelectedIndex() + 1) + "."
					+ spinYears.getValue();
			Date date = null;
			try {
				date = sdf.parse(dateInString);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			return date;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			JLabel lbl = (JLabel) e.getSource();
			if (lbl.getText() == "")
				return;
			selectedDay = Integer.parseInt(lbl.getText());
			dateSelected = updateDate();

			componentObserver.update();

			dispose();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			JLabel lbl = (JLabel) e.getSource();
			if (lbl.getText() == "" || lbl.getBackground() == currentDayBackground)
				return;
			lbl.setBackground(hoverDayBackground);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			JLabel lbl = (JLabel) e.getSource();
			if (lbl.getText() == "" || lbl.getBackground() == currentDayBackground)
				return;
			lbl.setBackground(new JLabel().getBackground());
		}

		@Override
		public void windowGainedFocus(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			dispose();
		}

	}

}
