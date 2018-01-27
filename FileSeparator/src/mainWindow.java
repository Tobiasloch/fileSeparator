import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;

import javax.swing.border.EtchedBorder;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class mainWindow extends JFrame {

	private JPanel contentPane;
	private static JTextField inputField;
	private static JTextField outputField;
	private JPanel typeSelection;
	private JCheckBox chckbxNewCheckBox;
	private JList<String> list;
	
	private JButton startButton;
	
	private JCheckBox outputinInputFolder;
	
	private separator separator;
	private JTextField textField;
	private JTextField textField_2;
	private JCheckBox writeAllLines;
	
	private JTextArea console;

	private ArrayList<Pattern> separators = new ArrayList<Pattern>();
	private ArrayList<Pattern> types = new ArrayList<Pattern>();
	
	private mainWindow mainFrame = this;
	
	public mainWindow() {
		setTitle("Dateitrennsystem");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setMinimumSize(new Dimension(500, 500));
		setLocationRelativeTo(null);
		
		JPanel inputOutputArea = new JPanel();
		contentPane.add(inputOutputArea, BorderLayout.NORTH);
		inputOutputArea.setLayout(new GridLayout(0, 2, 5, 5));
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		inputPanel.setBackground(Color.WHITE);
		inputOutputArea.add(inputPanel);
		inputPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblInput = new JLabel("Input:");
		inputPanel.add(lblInput, BorderLayout.NORTH);
		
		inputField = new JTextField();
		inputField.setDropMode(DropMode.INSERT);
		inputPanel.add(inputField, BorderLayout.CENTER);
		inputField.setColumns(10);
		
		JButton btnDurchsuchen = new JButton("durchsuchen...");
		btnDurchsuchen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				File f = new File(inputField.getText());
				if (f.exists()) fc.setCurrentDirectory(f);
				
				fc.showOpenDialog(null);
				if (fc.getSelectedFile()!=null) inputField.setText(fc.getSelectedFile().getPath());
			}
		});
		inputPanel.add(btnDurchsuchen, BorderLayout.SOUTH);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		outputPanel.setBackground(Color.WHITE);
		inputOutputArea.add(outputPanel);
		outputPanel.setLayout(new BorderLayout(0, 0));
		
		outputField = new JTextField();
		outputField.setColumns(10);
		outputPanel.add(outputField, BorderLayout.CENTER);
		
		JButton button = new JButton("durchsuchen...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				File f = new File(outputField.getText());
				if (f.exists()) fc.setCurrentDirectory(f);
				
				fc.showOpenDialog(null);
				if (fc.getSelectedFile() != null) outputField.setText(fc.getSelectedFile().getPath());
			}
		});
		outputPanel.add(button, BorderLayout.EAST);
		
		JLabel lblOutput = new JLabel("Output:");
		outputPanel.add(lblOutput, BorderLayout.NORTH);
		
		outputinInputFolder = new JCheckBox("selber Ordner wie Input");
		outputinInputFolder.setBackground(Color.WHITE);
		outputPanel.add(outputinInputFolder, BorderLayout.SOUTH);
		
		JPanel settingsArea = new JPanel();
		contentPane.add(settingsArea, BorderLayout.CENTER);
		settingsArea.setLayout(new BorderLayout(5, 5));
		
		JPanel checkBoxArea = new JPanel();
		checkBoxArea.setBackground(Color.WHITE);
		checkBoxArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		settingsArea.add(checkBoxArea, BorderLayout.EAST);
		checkBoxArea.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		checkBoxArea.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		panel.add(panel_6, BorderLayout.NORTH);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.Y_AXIS));
		
		writeAllLines = new JCheckBox("Alle Zeilen drucken");
		panel_6.add(writeAllLines);
		writeAllLines.setBackground(Color.WHITE);
		
		chckbxNewCheckBox = new JCheckBox("Erweiterte Zeilenausgabe");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxNewCheckBox.isSelected()) {
					List<Component> list = getAllComponents(typeSelection);
					
					for (Component c : list) c.setEnabled(true);
				} else {
					List<Component> list = getAllComponents(typeSelection);
					
					for (Component c : list) c.setEnabled(false);
				}
			}
		});
		panel_6.add(chckbxNewCheckBox);
		chckbxNewCheckBox.setBackground(Color.WHITE);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.WHITE);
		panel.add(panel_7, BorderLayout.SOUTH);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDateiTrennenNach = new JLabel("Datei trennen nach Zeilen");
		panel_7.add(lblDateiTrennenNach, BorderLayout.NORTH);

		SpinnerModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 100);
		JSpinner spinner = new JSpinner(model);
		panel_7.add(spinner, BorderLayout.SOUTH);
		spinner.setToolTipText("Wenn der Wert 0 betraegt, dann wird das der regulaere Ausdruck verwendet.");
		
		JPanel panel_8 = new JPanel();
		checkBoxArea.add(panel_8, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("<html>Hinweis: In den Feldern<br>\"Datei trennen\" und<br>\"Zeilenausgabe\" sind<br>regul\u00E4re Ausdr\u00FCcke m\u00F6glich</html>");
		panel_8.add(lblNewLabel);
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		
		JPanel panel_1 = new JPanel();
		settingsArea.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(0, 2, 5, 5));
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		listModel.addElement("\\d\\d:\\d\\d:\\d\\d");
		types.add(Pattern.compile(listModel.get(0)));
		
		JPanel separatorSelection = new JPanel();
		separatorSelection.setBackground(Color.WHITE);
		separatorSelection.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.add(separatorSelection);
		separatorSelection.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		separatorSelection.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5, BorderLayout.SOUTH);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		
		textField_2 = new JTextField();
		panel_4.add(textField_2, BorderLayout.CENTER);
		textField_2.setColumns(10);
		
		DefaultListModel<String> listModel_2 = new DefaultListModel<String>();
		
		JList<String> list_1 = new JList<String>(listModel_2);
		list_1.setToolTipText("Wenn einer der Ausdruecke in einer Zeile gefunden wird, dann wird eine neue Datei angelegt.");
		list_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		list_1.setForeground(Color.BLUE);
		JScrollPane scrollPane_1 = new JScrollPane(list_1);
		separatorSelection.add(scrollPane_1, BorderLayout.CENTER);
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JLabel lblDateitrennformate = new JLabel("Datei trennen");
		scrollPane_1.setColumnHeaderView(lblDateitrennformate);
		listModel_2.addElement("00:00:00");
		separators.add(Pattern.compile(listModel_2.get(0)));
		
		JButton btnAufteilerHinzufgen = new JButton("hinzuf\u00FCgen");
		btnAufteilerHinzufgen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!textField_2.getText().equals("")) {
					listModel_2.addElement(textField_2.getText());
					separators.add(Pattern.compile(textField_2.getText()));
					textField_2.setText("");
				}

				else {
					JOptionPane.showMessageDialog(null, "Es muss mindestens 1 Zeichen geschrieben werden!", "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_5.add(btnAufteilerHinzufgen);
		
		JButton btnEntfernen_1 = new JButton("entfernen");
		btnEntfernen_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list_1.getSelectedIndex() != -1) {
					separators.remove(list_1.getSelectedIndex());
					listModel_2.remove(list_1.getSelectedIndex());
				}
				else {
					JOptionPane.showMessageDialog(null, "Es muss mindestens ein Objekt ausgewaehlt werden.", "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_5.add(btnEntfernen_1);
		
		typeSelection = new JPanel();
		panel_1.add(typeSelection);
		typeSelection.setBackground(Color.WHITE);
		typeSelection.setForeground(Color.LIGHT_GRAY);
		typeSelection.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		typeSelection.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		typeSelection.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		panel_2.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));
		
		list = new JList<String>(listModel);
		list.setToolTipText("Wenn einer der Ausdruecke in einer Zeile gefunden wird, dann wird sie in die Output Datei geschrieben.");
		list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		list.setForeground(Color.BLUE);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		typeSelection.add(scrollPane, BorderLayout.CENTER);
		
		JLabel lblZeilenformate = new JLabel("Zeilenausgabe");
		scrollPane.setColumnHeaderView(lblZeilenformate);
		
		JButton btnEntfernen = new JButton("hinzuf\u00FCgen");
		btnEntfernen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!textField.getText().equals("")) {
					listModel.addElement(textField.getText());
					types.add(Pattern.compile(textField.getText()));
					textField.setText("");
				}
				else {
					JOptionPane.showMessageDialog(null, "Es muss mindestens 1 Zeichen geschrieben werden!", "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(btnEntfernen);
		
		JButton btnTypHinzufgen = new JButton("entfernen");
		btnTypHinzufgen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (list.getSelectedIndex() != -1) {
					types.remove(list.getSelectedIndex());
					listModel.remove(list.getSelectedIndex());
				}
				else {
					JOptionPane.showMessageDialog(null, "Es muss mindestens ein Objekt ausgewaehlt werden.", "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(btnTypHinzufgen);
		
		JPanel label = new JPanel();
		settingsArea.add(label, BorderLayout.NORTH);
		label.setLayout(new BorderLayout(0, 0));
		
		JLabel lblEinstellungen = new JLabel("Einstellungen:");
		label.add(lblEinstellungen);
		
		JButton button_1 = new JButton("?");
		button_1.setToolTipText("Dokumentation ueber regulaere Audruecke");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					URI url = new URI("https://www.kompf.de/java/regex.html");
					
					Desktop.getDesktop().browse(url);
				} catch (URISyntaxException | IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		label.add(button_1, BorderLayout.EAST);
		
		JPanel startArea = new JPanel();
		contentPane.add(startArea, BorderLayout.SOUTH);
		startArea.setLayout(new BorderLayout(0, 0));
		
		
		console = new JTextArea();
		console.setEditable(false);
		console.setRows(3);
		
		JScrollPane sp = new JScrollPane(console);
		startArea.add(sp, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		startArea.add(buttonPanel, BorderLayout.EAST);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		startButton = new JButton("Start");
		buttonPanel.add(startButton);
		
		JButton clearConsole = new JButton ("leere Konsole");
		buttonPanel.add(clearConsole, BorderLayout.SOUTH);
		clearConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				separator.clearConsole();
				console.setText(separator.getConsoleText());
			}
		});
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (separator==null || !separator.isRunning()) {
					File input = new File(inputField.getText());
					if (input.exists() == false) {
						JOptionPane.showMessageDialog(null , "Die eingegebene Datei existiert nicht!!!\n" + "Datei befindet sich im Feld: " + "Input", "Fehler!", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					
					File output;
					if (outputinInputFolder.isSelected()) output = input.getParentFile();
					else {
						output = new File(outputField.getText());
						if (output.exists() == false) {
							JOptionPane.showMessageDialog(null , "Die eingegebene Datei existiert nicht!!!\n" + "Datei befindet sich im Feld: " + "Output",  "Fehler!", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					
					separator = new separator(input, output);
					
					separator.mainFrame = mainFrame;
					separator.setType(types);
					separator.setSeparators(separators);
					if ((Integer)spinner.getValue() > 0) {
						int separateAfterLines = (Integer)spinner.getValue();
						Object[] options = {"nach Zeilen trennen", "normal fortfahren", "abbrechen"};
						
						int selection = JOptionPane.showOptionDialog(mainFrame,
                                "Soll die Datei nach jeder " + separateAfterLines + ". Zeile getrennt werden oder soll normal fortgefahren werden",
                                "Alternativen",
                                JOptionPane.DEFAULT_OPTION, 
                                JOptionPane.INFORMATION_MESSAGE, 
                                null, options, options[0]);
						if (selection == 0) separator.setSeparateFileAfterLines(separateAfterLines);
						else if (selection == 1) separator.setSeparateFileAfterLines(0);
						else if (selection == 2) return;
					}
					
					int errorlevel = -1; // -1: kein Schleifendurchlauf
					
					if (writeAllLines.isSelected()) errorlevel = separator.separateFile(true);
					else errorlevel = separator.separateFile(false);
					
					if (errorlevel == -1) JOptionPane.showMessageDialog(null, "Das Programm konnte nicht gestartet werden.", "Fehler!", JOptionPane.ERROR_MESSAGE);
					
					
				} else {
					if (separator.isRunning()) {
						int entscheidung = JOptionPane.showConfirmDialog(mainFrame, "Eine Datei wird gerade getrennt. Soll der Vorgang abgebrochen werden?", "Fehler!", JOptionPane.YES_NO_OPTION);
						if (entscheidung == JOptionPane.YES_OPTION) separator.stop(4);
					}
				}
			}
		});
	}
	

	public void updateConsole() {
		console.setText(separator.getConsoleText());
	}
	
	private List<Component> getAllComponents(Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
}
}
