
package com.easyfun.eclipse.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

import com.easyfun.eclipse.component.dialog.SafeMessageDialog;


/**
 * Class contains methods that are used to maniplated and retrive values
 * 
 * @author linzhaoming
 */
public class JavaElement {

	public static final String POPULATE_EVENT_WIZARD_ERROR_TITLE = "Populate Event Wizard Error";
	public static final String POPULATE_EVENT_WIZARD_ERROR_MESSAGE = "Cannot retrieve project\'s package information.\nCheck to see if .classpath file is present and valid and if project is a Java project.";


	/**
	 * Get all the MCO class files from the Studio
	 */
	static public List<String> getAllClasses(IProject project, String sourceDirectory) {
		List<String> list = new ArrayList<String>();
		IJavaProject javaProject = JavaCore.create(project);

		Path path = new Path("/" + project.getName() + sourceDirectory);

		try {
			IPackageFragmentRoot root = javaProject.findPackageFragmentRoot(path);
			if (root == null) {
				SafeMessageDialog.errorMessage(POPULATE_EVENT_WIZARD_ERROR_TITLE, POPULATE_EVENT_WIZARD_ERROR_MESSAGE);
				return list;
			}
			IJavaElement[] elements = root.getChildren();
			if (elements == null) {
				SafeMessageDialog.errorMessage(POPULATE_EVENT_WIZARD_ERROR_TITLE, POPULATE_EVENT_WIZARD_ERROR_MESSAGE);
				return list;
			}
			for (int i = 0; i < elements.length; i++) {
				IJavaElement element = elements[i];
				// see if this is a package fragment if it is check the children for a class file.
				if (element instanceof IPackageFragment) {
					IPackageFragment fragment = (IPackageFragment) element;
					ICompilationUnit[] classChildren = fragment.getCompilationUnits();
					for (int j = 0; j < classChildren.length; j++) {
						IJavaElement classObject = classChildren[j];
						// if this is a compilation unit then add it to the tree
						ICompilationUnit compilationUnit = (ICompilationUnit) classObject;
						IType[] type = compilationUnit.getTypes();
						if (type.length != 0 && type[0].exists() && type[0].getResource() != null && type[0].getResource().exists()) {
							String className = type[0].getFullyQualifiedName('.');
							list.add(className);
						}
					}
				}
			}
		} catch (Exception exception) {
			SafeMessageDialog.errorMessage(POPULATE_EVENT_WIZARD_ERROR_TITLE, POPULATE_EVENT_WIZARD_ERROR_MESSAGE);
		}
		return list;
	}

	/**
	 * Return an array of methods for the class specified search through the
	 */
	static public List<ArrayList<String>> getClassMethods(IProject project, String className) {
		List<ArrayList<String>> methodList = new ArrayList<ArrayList<String>>();
		IJavaProject javaProject = JavaCore.create(project);
		try {
			// get the type by the classname.
			IType type = javaProject.findType(className);
			//Fixed 3.5 manage events wizard: type ahead/content assist doesn't show methods for Form object /#8425
			if (type != null && type.exists()) {
				IMethod[] imethods = type.getMethods();
				// add all the methods to the method list.
				for (int i = 0; i < imethods.length; i++) {
					ArrayList<String> method = new ArrayList<String>();
					String methodName = imethods[i].getElementName();
					int flags = imethods[i].getFlags();

					// check to see if this method is public
					if (Flags.isPublic(flags) == true && !imethods[i].isConstructor()) {
						String[] parameterTypes = imethods[i].getParameterTypes();
						String[] parameterNames = imethods[i].getParameterNames();
						String functionString = functionString(methodName, parameterTypes);
						method.add(methodName);
						method.add(functionString);
						// add the parameter types to the method list
						for (int j = 0; j < parameterTypes.length; j++) {
							method.add(parameterNames[j] + "," + Signature.toString(parameterTypes[j]));
						}

						// add this method to the list;
						methodList.add(method);
					}
				}
			} else {
				return new ArrayList<ArrayList<String>> ();
			}
		} catch (JavaModelException exception) {
			SafeMessageDialog.errorMessage(POPULATE_EVENT_WIZARD_ERROR_TITLE, POPULATE_EVENT_WIZARD_ERROR_MESSAGE);
		}
		return methodList;
	}

	static public String functionString(String methodName, String[] parameterTypes) {
		String returnString = methodName + "(";
		for (int i = 0; i < parameterTypes.length; i++) {
			returnString += Signature.toString(parameterTypes[i]);
			if (i < parameterTypes.length - 1) {
				returnString += ",";
			}
		}
		returnString += ")";
		return returnString;
	}
}
