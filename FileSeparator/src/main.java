import javax.swing.UIManager;

public class main {

	public static void main(String[] args) {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
		
		mainWindow frame = new mainWindow();
		frame.setVisible(true);
	}

}
