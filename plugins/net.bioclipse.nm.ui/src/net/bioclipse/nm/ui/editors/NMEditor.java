package net.bioclipse.nm.ui.editors;

import java.util.ArrayList;
import java.util.List;

import net.bioclipse.nm.Activator;
import net.bioclipse.nm.business.INmManager;
import net.bioclipse.nm.domain.Material;
import net.bioclipse.nm.ui.editors.domain.Property;
import net.bioclipse.nm.ui.editors.domain.ViewModel;

import org.bitbucket.nanojava.data.Nanomaterial;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 * 
 * @author ola
 *
 */
public class NMEditor extends EditorPart{

	public static final String ID = "net.bioclipse.nm.ui.editors.nmeditor";
	private Material material;
	private IFileEditorInput input;
	private Table table;
	private Label selectedTable;
	private ViewModel viewModel;

	
	

	public Material getMaterial() {
		return material;
	}

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
			return BeansObservables.observeValue(element, "value");
		}
	}	

	// Will be called before createPartControl
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		if (!(input instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		this.input = (IFileEditorInput) input;

		IFile file = ((IFileEditorInput) input).getFile();
		INmManager nm = Activator.getDefault().getJavaNmManager();
		try {
			material = nm.load(file, new NullProgressMonitor());
			List<Property> props = new ArrayList<Property>();

			Nanomaterial nanom = material.getInternalModel();
			if (nanom.getChemicalComposition() != null) {
				props.add(new Property("Chemical Formula",
					MolecularFormulaManipulator.getString(nanom.getChemicalComposition()))
				);
			}
			if (nanom.getSize() != null) {
				props.add(new Property("Size", nanom.getSize().getString()));
			}
			props.add(new Property("Type", nanom.getType().name()));
			props.add(new Property("Zeta Potential", nanom.getZetaPotential().getString()));

			viewModel = new ViewModel(props);
		} catch (Exception e) {
			e.printStackTrace();
			throw new PartInitException(e.getMessage());
		}




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
		table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);

		// Set up data binding. In an RCP application, the threading
		// Realm
		// will be set for you automatically by the Workbench. In an SWT
		// application, you can do this once, wrapping your binding
		// method call.
		DataBindingContext bindingContext = new DataBindingContext();
		bindGUI(bindingContext);	  
	}

	protected void bindGUI(DataBindingContext bindingContext) {
		// Since we're using a JFace Viewer, we do first wrap our Table...
		TableViewer propertyViewer = new TableViewer(table);
		TableViewerColumn column1 = new TableViewerColumn(propertyViewer,
				SWT.NONE);
//		column1.setEditingSupport(new InlineEditingSupport(propertyViewer,
//				bindingContext));
		column1.getColumn().setWidth(200);

		TableViewerColumn column2 = new TableViewerColumn(propertyViewer,
				SWT.NONE);
		column2.setEditingSupport(new InlineEditingSupport(propertyViewer,
				bindingContext));
		column2.getColumn().setWidth(400);

		// Bind viewer to model
		ViewerSupport.bind(propertyViewer, new WritableList(viewModel
				.getProps(), Property.class), BeanProperties.values(
						new String[] { "key", "value" }));

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
