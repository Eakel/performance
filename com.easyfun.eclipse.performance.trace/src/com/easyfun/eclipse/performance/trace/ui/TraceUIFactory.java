package com.easyfun.eclipse.performance.trace.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.easyfun.eclipse.performance.trace.model.AppTrace;
import com.easyfun.eclipse.performance.trace.model.ITrace;
import com.easyfun.eclipse.performance.trace.model.TraceTypeEnum;
import com.easyfun.eclipse.performance.trace.ui.trace.AppTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.BccTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.CauMemTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.DaoTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.EmptyTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.HttpTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.JdbcTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.MdbTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.MemTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.SecMemTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.SvrTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.WebTraceComposite;
import com.easyfun.eclipse.performance.trace.ui.trace.WsTraceComposite;


public class TraceUIFactory {
	/** 根据Trace类型，创建不同的Composite */
	public static Composite createCompositeByType(Composite parent, ITrace trace, AppTrace appTrace) {

		if (trace == null) {
			return new EmptyTraceComposite(parent, SWT.NULL, appTrace);
		}

		if (trace.getType().equals(TraceTypeEnum.TYPE_APP)) {
			return new AppTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_CAU)) {
			return new CauMemTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_DAO)) {
			return new DaoTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_HTTP)) {
			return new HttpTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_JDBC)) {
			return new JdbcTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_MDB)) {
			return new MdbTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_MEM)) {
			return new MemTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_SECMEM)) {
			return new SecMemTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_SVR)) {
			return new SvrTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_WEB)) {
			return new WebTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_WS)) {
			return new WsTraceComposite(parent, SWT.NULL, appTrace);
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_BCC)) {
			return new BccTraceComposite(parent, SWT.NULL, appTrace);
		}  else {
			return new EmptyTraceComposite(parent, SWT.NULL, appTrace);
		}
	}
	
	/** 根据Trace类型，创建不同的Composite */
	public static String getImageByType(ITrace trace) {
		if (trace.getType().equals(TraceTypeEnum.TYPE_APP)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_CAU)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_DAO)) {
			return "icons/trace/collection.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_HTTP)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_JDBC)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_MDB)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_MEM)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_SECMEM)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_SVR)) {
			return "icons/trace/property.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_WEB)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_WS)) {
			return "icons/trace/book.gif";
		} else if (trace.getType().equals(TraceTypeEnum.TYPE_BCC)) {
			return "icons/trace/book.gif";
		}else {
			return "icons/trace/book.gif";
		}
	}
}
