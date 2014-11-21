package br.dev.controller.business;

import java.awt.Component;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenFileChooser {
	
	private Component parent;
	private String defaultExtensionWihoutDot;
	private String defaultExtensionWithDot;
	private String defaultExtensionDescription;
	private File selectedFile[];
	private File initialFolder;
	private boolean multipleSelection;
	
	public OpenFileChooser(Component parent, String defaultExtension, String defaultExtensionDescription, boolean multipleSelection) {
		this.parent = parent;
		this.defaultExtensionWihoutDot = defaultExtension;
		this.defaultExtensionWithDot = "." + defaultExtension;
		this.defaultExtensionDescription = defaultExtensionDescription;
		this.multipleSelection = multipleSelection;
	}
	
	public void setInitialFolder(File folder) {
		this.initialFolder = folder;
	}
	
	public boolean showDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.setMultiSelectionEnabled(multipleSelection);
		fileChooser.setFileFilter(new FileNameExtensionFilter(defaultExtensionDescription + " (" + defaultExtensionWithDot + ")", defaultExtensionWihoutDot));
		
		if (initialFolder != null)
			fileChooser.setCurrentDirectory(initialFolder);
		
		
		File[] files;
		boolean exists = true;
		
		do {			
			if (fileChooser.showOpenDialog(this.parent) != JFileChooser.APPROVE_OPTION) {
				return false;
			}			
			File[] selectedFile;
			selectedFile = fileChooser.getSelectedFiles();
			
			files = new File[0];
			for (File file : selectedFile) {
				if(file.isDirectory()){
					files = arrayMerge(files, file.listFiles(new extensionFilter(".pdf")));
				}
			}			
			
			files = (files.length > 0 ? files : selectedFile);
			for (File file : files) {
				if (!file.exists()) {
					JOptionPane.showMessageDialog(this.parent, "O arquivo #"+file.getName()+" não existe");
					exists = false;
					break;
				}
			}		
		
		} while (!exists);
		
		this.selectedFile = files;	
		
		return true;
	}
	
	public File[] getSelectedFiles(){
		return selectedFile;
	}
	
	public File getSelectedFile() {
		return selectedFile[0];
	}
	
	public File[] arrayMerge(File[] original, File[] toMerge){
		File[] array = new File[original.length + toMerge.length];
		System.arraycopy(original, 0, array, 0, original.length);
		System.arraycopy(toMerge, 0, array, original.length, toMerge.length);			
		return array;
	}	
}

class extensionFilter implements FilenameFilter{
	private String extension;
	
	@Override
	public boolean accept(File dir, String name) {
		return name.toLowerCase().endsWith(extension);
	}

	public extensionFilter(String extension) {
		super();
		this.extension = extension;
	}	
}
