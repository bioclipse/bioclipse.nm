package net.bioclipse.nm.ui.editors;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.nm.domain.Material;
import net.bioclipse.nm.ui.editors.domain.Property;
import net.bioclipse.nm.ui.editors.domain.ViewModel;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.*;
import org.eclipse.ui.part.EditorPart;

/**
 * 
 * @author ola
 *
 */
public class NMEditorCelleditor extends EditorPart{

	public static final String ID = "net.bioclipse.nm.ui.editors.nmeditor";
	Material material;
	//	  private NMEditorInput input;
	private IFileEditorInput input;
	private Table table;
	private Label selectedTable;
	private ViewModel viewModel;

	static String[] COLUMN_NAMES = new String[] { "FOO", "BAR" };
    static int[] COLUMN_WIDTHS = new int[] { 300, 200 };
    static String[] COLUMNS_PROPERTIES = new String[] { "foo_prop", "bar_prop" };


	/**
	 * Editing support that uses JFace Data Binding to control the editing
	 * lifecycle. The standard EditingSupport get/setValue(...) lifecycle is not
	 * used.
	 * 
	 * @since 3.3
	 */
	private static class InlineEditingSupport extends
	ObservableValueEditingSupport {
		private CellEditor cellEditor;

		/**
		 * @param viewer
		 * @param dbc
		 */
		public InlineEditingSupport(ColumnViewer viewer, DataBindingContext dbc) {

			super(viewer, dbc);
			cellEditor = new TextCellEditor((Composite) viewer.getControl());
		}

		protected CellEditor getCellEditor(Object element) {
			return cellEditor;
		}

		protected IObservableValue doCreateCellEditorObservable(
				CellEditor cellEditor) {

			return SWTObservables.observeText(cellEditor.getControl(),
					SWT.Modify);
		}

		protected IObservableValue doCreateElementObservable(Object element,
				ViewerCell cell) {
			return BeansObservables.observeValue(element, "name");
		}
	}	

	// Will be called before createPartControl
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		if (!(input instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		this.input = (IFileEditorInput) input;

		viewModel = new ViewModel(null);


		//	    if (!(input instanceof NMEditorInput)) {
		//	      throw new RuntimeException("Wrong input");
		//	    }
		//	    this.input = (NMEditorInput) input;

		setSite(site);
		setInput(input);
		//	    person = MyModel.getInstance().getPersonById(this.input.getId());
		//	    setPartName("Person ID: " + person.getId());
		setPartName("NM Editor");
	}

	@Override
	public void createPartControl(Composite parent) {
		 TableViewer tableViewer = new TableViewer(parent);
         Table table = tableViewer.getTable();
         table.setHeaderVisible(true);
         table.setLinesVisible(true);

         TableColumn column1 = new TableColumn(table, SWT.LEFT);
         column1.setText("Key");
         column1.setWidth(200);

         TableColumn column2 = new TableColumn(table, SWT.LEFT);
         column2.setText("Value");
         column2.setWidth(200);

         tableViewer.setContentProvider(new ModelContentProvider());
         tableViewer.setLabelProvider(new ModelLabelProvider());

         tableViewer.setColumnProperties(COLUMNS_PROPERTIES);

         tableViewer.setCellEditors(new CellEditor[] {
                 new TextCellEditor(table), new TextCellEditor(table) });
         tableViewer.setCellModifier(new ModelCellModifier(tableViewer));

         
         final List<Model> models = new ArrayList<Model>();
         models.add(new Model("a", "b"));
         models.add(new Model("x", "y"));
         
         tableViewer.setInput(models);
	}


	 static class Model {
	        private String foo;
	        private String bar;

	        public Model(String foo, String bar) {
	            super();
	            this.foo = foo;
	            this.bar = bar;
	        }

	        public String getFoo() {
	            return foo;
	        }

	        public void setFoo(String foo) {
	            this.foo = foo;
	        }

	        public String getBar() {
	            return bar;
	        }

	        public void setBar(String bar) {
	            this.bar = bar;
	        }
	    }

	    static class ModelContentProvider implements IStructuredContentProvider {

	        @SuppressWarnings("unchecked")
	        @Override
	        public Object[] getElements(Object inputElement) {
	            // The inputElement comes from view.setInput()
	            if (inputElement instanceof List) {
	                List models = (List) inputElement;
	                return models.toArray();
	            }
	            return new Object[0];
	        }

	        @Override
	        public void dispose() {
	        }

	        @Override
	        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	        }

	    }

	    static class ModelLabelProvider extends LabelProvider implements
	            ITableLabelProvider {

	        @Override
	        public Image getColumnImage(Object element, int columnIndex) {
	            // no image to show
	            return null;
	        }

	        @Override
	        public String getColumnText(Object element, int columnIndex) {
	            // each element comes from the ContentProvider.getElements(Object)
	            if (!(element instanceof Model)) {
	                return "";
	            }
	            Model model = (Model) element;
	            switch (columnIndex) {
	            case 0:
	                return model.getFoo();
	            case 1:
	                return model.getBar();
	            default:
	                break;
	            }
	            return "";
	        }
	    }

	    static class ModelCellModifier implements ICellModifier {
	        TableViewer viewer;

	        public ModelCellModifier(TableViewer viewer) {
	            this.viewer = viewer;
	        }

	        @Override
	        public boolean canModify(Object element, String property) {
	            // property is defined by viewer.setColumnProperties()
	            // allow the FOO column can be modified.
	            return "foo_prop".equals(property);
	        }

	        @Override
	        public Object getValue(Object element, String property) {
	            if ("foo_prop".equals(property)) {
	                return ((Model) element).getFoo();
	            }
	            if ("bar_prop".equals(property)) {
	                return ((Model) element).getBar();
	            }
	            return "";
	        }

	        @Override
	        public void modify(Object element, String property, Object value) {
	            if ("foo_prop".equals(property)) {
	                TableItem item = (TableItem) element;
	                ((Model) item.getData()).setFoo("" + value);

	                // refresh the viewer to show the changes to our user.
	                viewer.refresh();
	            }
	        }
	    }
	
	
	
	
	
	
	
	
	@Override
	public void doSave(IProgressMonitor arg0) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
	}

}
