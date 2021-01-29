package chernyj.jdatepicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
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
	
	public static final Color DEFAULT_PRIMARY = new Color(0xAAAAAA);
	public static final Color DEFAULT_SECONDARY = new Color(0x222222);

	private JTextField tfDate;
	private JButton btnSelect = new JButton();
	private JDateDialog dateDialog;

	public JDatePicker() {
		initComponents();
		add(tfDate);
		add(btnSelect);

		setVisible(true);
	}

	private void colorEmblem(Color color) {
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/emblem.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int width = image.getWidth();
		int height = image.getHeight();
		WritableRaster raster = image.getRaster();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int[] pixels = raster.getPixel(x, y, (int[]) null);
				pixels[0] = color.getRed();
				pixels[1] = color.getGreen();
				pixels[2] = color.getBlue();
				raster.setPixel(x, y, pixels);
			}
		}

		setIcon(new ImageIcon(image));
		
	}

	public void setSecondaryColor(Color color) {
		btnSelect.setForeground(color);
		colorEmblem(color);
	}

	public void setPrimaryColor(Color color) {
		btnSelect.setBackground(color);
	}

	@Override
	public Color getSecondaryColor() {
		return btnSelect.getForeground();
	}

	@Override
	public Color getPrimaryColor() {
		return btnSelect.getBackground();
	}

	public ImageIcon getIcon() {
		return (ImageIcon) btnSelect.getIcon();
	}

	public void setIcon(ImageIcon icon) {
		btnSelect.setIcon(icon);
	}

	private void initComponents() {
		setPrimaryColor(DEFAULT_PRIMARY);
		setSecondaryColor(DEFAULT_SECONDARY);
		
		btnSelect.setPreferredSize(BTN_SELECT_DIM);
		btnSelect.addActionListener((l) -> doBtnSelectClick());

		tfDate = new JTextField();
		tfDate.setPreferredSize(TF_DATE_DIM);
		tfDate.addActionListener((l) -> doTfDateEnterHit());

	}
	
	public void setDate(Date date) {
		tfDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(date));
	}

	private void doTfDateEnterHit() {
		Date date = getDate();
		if (date == null)
			tfDate.setText(null);
		else
			tfDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(date));
	}

	public Date getDate() {
		return StringToDateParser.parse(tfDate.getText());
	}
	
	public boolean isDateSet() {
		return tfDate.getText().isEmpty() ? false: true;
	}

	private void doBtnSelectClick() {
		int xPos = (int) (btnSelect.getLocationOnScreen().getX() + btnSelect.getWidth() - JDateDialog.DEFAULT_WIDHT);
		int yPos = (int) (btnSelect.getLocationOnScreen().getY() + btnSelect.getHeight());

		dateDialog = new JDateDialog(this, xPos, yPos, getDate());

	}

	public void clear() {
		tfDate.setText(null);
		if (dateDialog != null)
			dateDialog.setDateSelected(null);
	}

	@Override
	public void update() {
		String dateStr = new SimpleDateFormat("dd.MM.yyyy").format(dateDialog.getDate());
		tfDate.setText(dateStr);
	}

}
