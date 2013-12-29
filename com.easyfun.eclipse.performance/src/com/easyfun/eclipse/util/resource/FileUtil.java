package com.easyfun.eclipse.util.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class for Files operation.
 * 
 * @author linzhaoming
 */
public class FileUtil {

	/**
	 * Read the String content from File. 
	 * @param fromFile
	 * @return
	 * @throws IOException
	 */
	public static String readTextFile(File fromFile) throws IOException {
		StringBuffer buf = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fromFile)));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			buf.append(inputLine);
			buf.append('\n');
		}

		in.close();
		return buf.toString();
	}

	/**
	 * Write the content to file.
	 * @param toFile
	 * @param content
	 * @throws IOException
	 */
	public static void writeTextFile(String content, File toFile) throws IOException {
		FileWriter out = new FileWriter(toFile);
		out.write(content);
		out.close();
	}

	/**
	 * 
	 * @param inputFilename
	 * @param outputFilename
	 * @throws IOException
	 */
	public static void copy(String inputFilename, String outputFilename) throws IOException {
		FileUtil.copy(new File(inputFilename), new File(outputFilename));
	}

	/**
	 * 
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static void copy(File input, File output) throws IOException {
		if (input.isDirectory() && output.isDirectory()) {
			FileUtil.copyDir(input, output);
		} else {
			FileUtil.copyFile(input, output);
		}
	}

	/**
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @throws IOException
	 */
	public static void copyFile(File inputFile, File outputFile) throws IOException {
		if (outputFile.exists() == false) {
			outputFile.createNewFile();
		}

		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(inputFile));
		BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(outputFile));

		byte[] buf = new byte[8192];
		int n;
		while ((n = inStream.read(buf)) >= 0) {
			outStream.write(buf, 0, n);
		}

		inStream.close();
		outStream.close();
	}

	/**
	 * 
	 * @param inputDir
	 * @param outputDir
	 * @throws IOException
	 */
	public static void copyDir(File inputDir, File outputDir) throws IOException {
		if (outputDir.exists() == false) {
			outputDir.mkdirs();
		}

		File[] files = inputDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File destFile = new File(outputDir.getAbsolutePath() + File.separator + files[i].getName());
			if (!destFile.exists()) {
				if (files[i].isDirectory()) {
					destFile.mkdir();
				} else {
					destFile.createNewFile();
				}
			}
			FileUtil.copy(files[i], destFile);
		}
	}

	/**
	 * return true if the directory contains files with the extension
	 */
	public static boolean dirContainsFiles(File dir, String extension, boolean recursive) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isFile() && file.getName().endsWith(extension)){
				return true;
			}
			if (recursive && file.isDirectory())
				return FileUtil.dirContainsFiles(file, extension, recursive);
		}

		return false;
	}

	/**
	 * 
	 * @param file
	 * @param property
	 * @return
	 * @throws IOException
	 */
	public static String readPropertyInXMLFile(File file, String property)
			throws IOException {
		String content = FileUtil.readTextFile(file);
		int startTagIdx = content.indexOf("<" + property + ">");
		int endTagIdx = content.indexOf("</" + property + ">");
		if (startTagIdx == -1){
			throw new IOException("Property " + property + " not found in file " + file);
		}

		return content.substring(startTagIdx + property.length() + 2, endTagIdx);
	}

	/**
	 * Recursive delete of a directory.<br>
	 * The directory itself will be deleted
	 */
	public static void removeDir(File dir) throws IOException {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				FileUtil.removeDir(files[i]);
			} else {
				files[i].delete();
			}
		}
		dir.delete();
	}

	/**
	 * Create a file and copy the content from the template directory;
	 * 
	 * @param description
	 * @param fileName
	 * @param templateFileName
	 * @param monitor
	 */
	public static void createAndCopyTemplateFile(String description,
			IFile file, String templateFileName, IProgressMonitor monitor,
			boolean bOverwrite) throws CoreException {
		File tempFile = new File(templateFileName);
		if (!tempFile.exists()) {
			return;
		}
		monitor.beginTask(description, 1);
		InputStream stream = null;
		try {
			stream = openContentStream(templateFileName);
			if (stream != null && file.exists() == false) {
				file.create(stream, true, monitor);
			} else if (stream != null && bOverwrite == true) {
				file.setContents(stream, true, true, monitor);
			} else if (stream == null) {
				final String fileName = templateFileName;
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						Shell shell = Display.getCurrent().getActiveShell();
						MessageDialog.openError(shell, "Create File Error", "Cannot find file " + fileName);
					}
				});
			}
		} finally {
			try {
				if (stream != null){
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream openContentStream(String fileName) {
		try {
			return new FileInputStream(fileName);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Read the file and wirte the contents into the
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFile(String fileName) {
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		String string = "";
		try {
			fileReader = new FileReader(fileName);
			bufferedReader = new BufferedReader(fileReader);

			String temp = null;
			string = "";
			temp = bufferedReader.readLine();
			while (temp != null) {
				string += temp + "\r\n";
				temp = bufferedReader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null){
					bufferedReader.close();
				}
				if (fileReader != null){
					fileReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return string;
	}

	/**
	 * Creates a sub folder under the parent for a given name
	 * 
	 * @param parentFolder
	 * @param folderName
	 * @return
	 */
	public static IFolder createFolder(IContainer parentFolder,
			String folderName) throws CoreException {
		parentFolder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		IFolder newFolder = parentFolder.getFolder(new Path(folderName));
		// make sure the folder is in synch with local file system
		newFolder.refreshLocal(1, new NullProgressMonitor());
		if (!newFolder.exists()) {
			// Iterate through the folder structure and create the directories
			IPath path = newFolder.getProjectRelativePath();
			for (int i = 1; i <= path.segmentCount(); i++) {
				IPath segPath = path.uptoSegment(i);
				IFolder subFolder = parentFolder.getProject().getFolder(segPath);
				// make sure the folder is in synch with local file system
				subFolder.refreshLocal(1, new NullProgressMonitor());
				if (!subFolder.exists()) {
					subFolder.create(false, true, null);
				}
			}
		}

		return newFolder;
	}

	/**
	 * Creates a sub folder under the parent for a given name
	 */
	public static IFolder createFolder(IFolder parentFolder)
			throws CoreException {
		// get the folder for the given name. if the folder doesn't already exist then we need to
		parentFolder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

		if (parentFolder.exists() == false) {
			// need to iterate through the folder structure and create the directorys
			IPath path = parentFolder.getProjectRelativePath();
			for (int index = 1; index <= path.segmentCount(); index++) {
				// get the path to the segment.
				IPath newPath = path.uptoSegment(index);

				// get the folder associated with this folder.
				IFolder subFolder = parentFolder.getProject().getFolder(newPath);

				// chech to see if the folder is already created.
				if (subFolder.exists() == false) {
					// if the folder is not created.
					subFolder.create(true, true, null);
				}
			}
		}

		// return the new folder object
		return parentFolder;
	}

	/**
	 * Create a file for the parent resource with a given name
	 */
	public static IFile createFile(IResource parentResource, String name) {
		IFile file = null;
		if (parentResource instanceof IFolder) {
			file = ((IFolder) parentResource).getFile(name);
		} else if (parentResource instanceof IProject) {
			file = ((IProject) parentResource).getFile(name);
		}
		return file;
	}

	/**
	 * Checks to see if the string contains illegal characters that are not
	 * allowed in a directory name.
	 */
	public static boolean containsIllegalDirectoryNameCharacter(String string) {
		char[] illegalCharacters = { '\\', ':', '*', '?', '\"', '>', '<', '>' };
		for (int index = 0; index < illegalCharacters.length; index++) {
			if (string.indexOf(illegalCharacters[index]) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Copy the contents of the folder to the phyisical folder
	 */
	public static void copyFolder(IFolder folder, File folderTo)
			throws CoreException, IOException {
		if (folderTo.exists() == false) {
			folderTo.mkdirs();
		}

		IResource[] resources = folder.members();

		for (int index = 0; index < resources.length; index++) {
			if (resources[index] instanceof IFolder) {
				copyFolder(((IFolder) resources[index]), new File(folderTo
						.getAbsolutePath() + File.separator + resources[index].getRawLocation().lastSegment()));
			} else {
				copyFile(resources[index].getRawLocation().toFile(), new File(
						folderTo.getAbsolutePath() + File.separator + resources[index].getRawLocation().lastSegment()));
			}
		}

	}

	public static void copyFolder(File folder, IFolder folderTo) throws CoreException, IOException {
		copyFolder(folder, folderTo, new NullProgressMonitor());
	}

	public static void copyFolder(File folder, IFolder folderTo,
			IProgressMonitor monitor) throws CoreException, IOException {
		if (folder == null || folder.exists() == false) {
			return;
		}
		if (folderTo.exists() == false) {
			folderTo.create(false, true, monitor);
		}

		File[] resources = folder.listFiles();
		if (resources != null) {
			for (int index = 0; index < resources.length; index++) {
				if (resources[index].isDirectory()) {
					copyFolder(resources[index], folderTo.getFolder(resources[index].getName()));
				} else {
					IFile file = folderTo.getFile(resources[index].getName());
					if (file.exists() == false) {
						file.create(new FileInputStream(resources[index]), false, monitor);
					} else {
						file.setContents(new FileInputStream(resources[index]), false, false, monitor);
					}
				}
			}
		}
	}

	/**
	 * gets IFile from project file (or null if it does not belong to the
	 * project)
	 */
	public static IFile getIFileFromProjectFile(File file, IProject project) {
		IFile ifile = null;

		IPath projectPath = project.getLocation();

		// see if this file is in the project
		// if the matching segments are 0 then it is not in the project.
		Path path = new Path(file.toString());
		int iResult = path.matchingFirstSegments(projectPath);
		if (iResult != 0) {
			IPath relative = path.removeFirstSegments(iResult);
			IResource resource = project.findMember(relative);
			ifile = (IFile) resource;
		}
		return ifile;
	}

	/**
	 * checks if a file exists
	 */
	public static boolean fileExists(String filename) {
		File file = new File(filename);
		return (file != null ? file.exists() : false);
	}

	/**
	 * 
	 * @param platformDir
	 * @param platformFile
	 * @return
	 */
	public static boolean checkFileValid(String platformDir, String platformFile) {
		boolean validFile = true;
		if (platformDir == null || platformDir.equals("")) {
			return (false);
		}

		if (!platformDir.endsWith("/") && !platformDir.endsWith("\\")) {
			platformDir += File.separator;
		}

		File file = new File(platformDir);
		if (file.exists()) {
			file = new File(platformDir + platformFile);
			if (!file.exists()) {
				validFile = false;
			}
		} else {
			validFile = false;
		}

		return (validFile);
	}
	
	public static String getContentFromFile(File file) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
			sb.append('\n');
		}

		in.close();
		return sb.toString();
	}
	
	/** 将文件字节大小转换为可读性更强的格式化大小*/
	public static String getDisplayFileSize(long fSize) {
		String realSize = "";
		float size = 0f;
		String unit = "";// 单位
		if (fSize < 1024) {
			size = (float) (fSize);
			unit = " 字节";
		} else {
			float k = (float) (fSize / 1024);
			if (k < 1024) {
				size = (float) (k);
				unit = " KB";
			} else {
				float m = (float) (k / 1024);
				if (m < 1024) {
					size = (float) (m);
					unit = " MB";
				} else {
					size = (float) (m / 1024);
					unit = " GB";
				}
			}
		}
		DecimalFormat formater = new DecimalFormat("#0.##");
		realSize = formater.format(size).toString();
		if ((realSize.indexOf(".00")) != -1) {
			realSize = realSize.substring(0, realSize.indexOf(".00"));
		}
		realSize += unit;

		return realSize;
	}
}
