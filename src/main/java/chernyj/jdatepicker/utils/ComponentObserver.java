package chernyj.jdatepicker.utils;

import java.awt.Color;

/**
 * @author Chernyj Dmitry
 *
 */
public interface ComponentObserver {
	void update();
	Color getPrimaryColor();
	Color getSecondaryColor();
}
