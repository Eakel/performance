package com.easyfun.eclipse.common.view.item.content;

import java.lang.reflect.Constructor;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.easyfun.eclipse.common.config.cfg.Item;
import com.easyfun.eclipse.common.config.cfg.ItemWrapper;
import com.easyfun.eclipse.common.util.ClassUtil;
import com.easyfun.eclipse.common.view.item.pub.ItemComposite;
import com.easyfun.eclipse.common.view.item.welcome.WelcomeComposite;

/**
 * MainContentView View
 * @author linzhaoming
 *
 * 2011-4-2
 *
 */
public class MainContentView extends ViewPart {
	//TODO:Ϊ�����Ч�ʣ����Ѿ�������Composite������������ֻ��ʼ��һ��
	
	private StackLayout stackLayout = new StackLayout();
	
	/** View�����ؼ�*/
	private Composite stackComposite = null;
	
	/** View�з��õĿؼ�*/
	private ItemComposite componentComposite;
	
	private String compositeName = "";
	
	private ItemWrapper itemWrapper = null;
	
	public void createPartControl(Composite parent) {
		stackComposite = new Composite(parent, SWT.NULL);
		stackComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		stackComposite.setLayout(stackLayout);
		setCompositeName(null);
		initializeToolBar();
	}

	public void setFocus() {
		if(componentComposite != null){
			((Composite)componentComposite).setFocus();
		}
	}
	
	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
	}
	
	public void setCompositeName(ItemWrapper itemWrapper) {
		if (itemWrapper == null) {
			this.itemWrapper = null;
			componentComposite = new WelcomeComposite(stackComposite, SWT.NONE, null);
			stackLayout.topControl = componentComposite;
			stackComposite.layout();
			setPartName("EasyFun");
		} else {
			if(itemWrapper.equals(this.itemWrapper) == false){	//ͬһ��������ˢ��
				this.itemWrapper = itemWrapper;
				this.compositeName = itemWrapper.getComposite();
				try {
					//���п��������ڲ�ͬ��Plugin��λ�ڲ�ͬ��Bundle��������ֱ��Class.forNamt()
					Class clazz = ClassUtil.loadBundleClass(itemWrapper.getItem().getPluginId(), compositeName); //bundle.loadClass(compositeName);
					Constructor c = clazz.getConstructor(new Class[] {Composite.class, int.class, Item.class });
					componentComposite = (ItemComposite) c.newInstance(new Object[] { stackComposite, SWT.NONE, itemWrapper.getItem() });
					stackLayout.topControl = componentComposite;
					stackComposite.layout();
					setPartName(itemWrapper.getItem().getTitle());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setImage(Image image){
		setTitleImage(image);
	}
	
}
