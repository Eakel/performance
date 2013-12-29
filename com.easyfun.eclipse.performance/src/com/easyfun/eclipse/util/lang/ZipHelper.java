package com.easyfun.eclipse.util.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Handles the zip of the project files in to a single zip file.
 * 
 * @author linzhaoming
 * 
 */
public class ZipHelper {
	static final int BUFFER = 2048;
	public static void copyFileFromZipedSource(IFolder targetFolder,
			String zipFiles, String pathOffileToCopy, IProgressMonitor monitor)
			throws IOException, CoreException {

		ZipFile zipFile = new ZipFile(zipFiles);
		try {
			Enumeration<?> enumeration = zipFile.entries();
			while (enumeration.hasMoreElements() == true) {
				ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
				String entryName = zipEntry.getName();
				if (entryName.startsWith(pathOffileToCopy)) {
					String fileName = pathOffileToCopy;
					if (fileName.indexOf("/") != -1) {
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
					}
					// create the file for the
					IFile file = targetFolder.getFile(fileName);
					InputStream inputStream = null;
					inputStream = zipFile.getInputStream(zipEntry);
					if (file.exists() == false) {
						file.create(inputStream, true, monitor);
					} else {
						file.setContents(inputStream, true, false, monitor);
					}
					if (inputStream != null)
						inputStream.close();
				}
			}
		} finally {
			zipFile.close();
		}
	}
}