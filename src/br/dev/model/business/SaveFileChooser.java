package br.dev.model.business;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveFileChooser {
	
	private Component parent;
	private String defaultExtensionWihoutDot;
	private String defaultExtensionWithDot;
	private String defaultExtensionDescription;
	private File selectedFile;
	
	private String fileName;

	public SaveFileChooser(Component parent, String defaultExtension, String defaultExtensionDescription) {
		this.parent = parent;
		this.defaultExtensionWihoutDot = defaultExtension;
		this.defaultExtensionWithDot = "." + defaultExtension;
		
		this.defaultExtensionDescription = defaultExtensionDescription;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public boolean showDialog() {
		JFileChooser fileChooser = new JFileChooser();
		
		if(defaultExtensionWihoutDot == null)
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		else
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
		fileChooser.setAcceptAllFileFilterUsed(true);
		
		if(defaultExtensionWihoutDot != null)
			fileChooser.setFileFilter(new FileNameExtensionFilter(defaultExtensionDescription + " (" + defaultExtensionWithDot + ")", defaultExtensionWihoutDot));

		File selectedFile;
		boolean overwriteFile = false;
		do {
			if (fileChooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
				return false;
			}
			
			selectedFile = fileChooser.getSelectedFile();
			
			if(fileName != null)
				selectedFile = new File(String.format("%s\\%s",selectedFile.toPath(), fileName));
						
			if (defaultExtensionWihoutDot != null && !selectedFile.getPath().toLowerCase().endsWith(defaultExtensionWithDot.toLowerCase())) {
				selectedFile = new File(selectedFile.getPath() + defaultExtensionWithDot);
			}
			
			if (selectedFile.exists()) {
				int option = JOptionPane.showConfirmDialog(parent, "O arquivo selecionado já existe. Deseja sobrescrevê-lo?", "Alerta", JOptionPane.YES_NO_CANCEL_OPTION);
				if (option == JOptionPane.CANCEL_OPTION) {
					return false;
				}
				
				overwriteFile = (option == JOptionPane.YES_OPTION);
			}
		} while (selectedFile.exists() && !overwriteFile);
		
		this.selectedFile = selectedFile;
		return true;
	}
	
	public File getSelectedFile() {
		return selectedFile;
	}
}
