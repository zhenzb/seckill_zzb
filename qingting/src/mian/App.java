package mian;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import mian.model.Person;
import wx.Login;
import wx.WXGetcontact;
import wx.WXSync;
import wx.WXinit;

public class App {

	static public Shell shell;
	static public Table table_1;
	static public Label lblNewLabel;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Thread() {
				public void run() {
					Login.index();
					new WXinit();
					new WXGetcontact();
					new WXSync();
				};
			}.start();
			
			Thread.sleep(10000);
			
			App window = new App();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(584, 447);
		shell.setText("蜻蜓");
		
		ArrayList<Person> persons = new ArrayList();
		
		for(int i=0;i<100;i++) {
			Person p = new Person();
			p.setUserName("aaa"+i);
			persons.add(p);
		}
		
		
		CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(shell, SWT.BORDER | SWT.FULL_SELECTION);
		checkboxTableViewer.setLabelProvider(new ListLabelProvider());
		checkboxTableViewer.setContentProvider(new ContentProvider());
		checkboxTableViewer.setInput(persons);
		
		table_1 = checkboxTableViewer.getTable();
		table_1.setBounds(0, 0, 194, 425);
		final Display display=Display.getDefault();
		lblNewLabel = new Label(shell, SWT.NONE);
		Image image=new Image(display,"/Users/jiangshidi/Documents/img/test.jpg");
		lblNewLabel.setBounds(286, 71, 210, 210);
		lblNewLabel.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent arg0) {
				// TODO Auto-generated method stub
				final Rectangle bounds = image.getBounds();
				int picwidth = bounds.width; // 图片宽
				int picheight = bounds.height; // 图片高
				double H = 200; // label的高
				double W = 150; // label的宽
				double ratio = 1; // 缩放比率
				double r1 = H / picheight;
				double r2 = W / picwidth;
				ratio = Math.min(r1, r2);
				arg0.gc.drawImage(image, 0, 0, picwidth, picheight, 0, 0, (int) (picwidth * ratio),(int) (picheight * ratio));
			}
		});
	}
	
	static class ListLabelProvider extends LabelProvider {
        public String getText(Object element) {
            Person person = (Person)element;
            return person.getUserName();
        }
        public Image getImage(Object element) {
            return null;
        }
    }
    static class ContentProvider implements IStructuredContentProvider {
        public Object[] getElements(Object inputElement) {
            if(inputElement instanceof List){
                List list = (List)inputElement;
                return list.toArray();
            }
            return new Object[0];
        }
        public void dispose() {
        }
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }
}

