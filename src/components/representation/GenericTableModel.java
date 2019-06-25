package components.representation;

import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * TableModel that you can manage as a list. It takes an {@link Representor} to create the table
 * @param <TModel> The model class used for storing the data
 */
public class GenericTableModel<TModel> extends AbstractTableModel implements List<TModel> {

    private List<TModel> storage;
    private Representor<TModel> representor;

    public Representor<TModel> getRepresentor() {
        return representor;
    }

    public GenericTableModel(List<TModel> list, Representor<TModel> representor) {
        this.storage = list;
        this.representor = representor;
    }

    public GenericTableModel(Representor<TModel> representor) {
        this(new ArrayList<>(), representor);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return representor.getColumnName(column);
    }

    //region AbstractTableModel

    @Override
    public int getRowCount() {
        return storage.size();
    }

    @Override
    public int getColumnCount() {
        return representor.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TModel model = storage.get(rowIndex);
        return representor.invokeOn(model)[columnIndex];
    }

    //endregion AbstractTableModel

    //region List

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return storage.contains(o);
    }

    @Override
    public Iterator<TModel> iterator() {
        return storage.iterator();
    }

    @Override
    public Object[] toArray() {
        return storage.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return storage.toArray(a);
    }

    @Override
    public boolean add(TModel tModel) {
        storage.add(tModel);
        fireTableRowsInserted(storage.size() -1, storage.size());
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = storage.indexOf(o);
        if(index == -1) {
            // No such element
            return false;
        } else {
            storage.remove(index);
            fireTableRowsDeleted(index, index);
            return true;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return storage.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends TModel> c) {
        int startIndex = (storage.size());
        boolean result = storage.addAll(c);

        if(result) {
            int endIndex = (storage.size());
            fireTableRowsInserted(startIndex, endIndex);
        }

        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends TModel> c) {
        if(index < (storage.size() -1))
            throw new IndexOutOfBoundsException();

        boolean result = storage.addAll(index, c);
        if(result) {
            // Calculate the offset
            int lastIndex = index + c.size() - 1;
            fireTableRowsInserted(index, lastIndex);
        }

        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        int lastIndex = (storage.size() - 1);
        storage.clear();
        fireTableRowsDeleted(1, lastIndex);
    }

    @Override
    public TModel get(int index) {
        return storage.get(index);
    }

    @Override
    public TModel set(int index, TModel element) {
        TModel result = storage.set(index, element);
        fireTableRowsUpdated(index, index);
        return result;
    }

    @Override
    public void add(int index, TModel element) {
        storage.add(index, element);
        fireTableRowsInserted(index, index);
    }

    @Override
    public TModel remove(int index) {
        TModel result = storage.remove(index);
        fireTableRowsDeleted(index, index);
        return result;
    }

    @Override
    public int indexOf(Object o) {
        return storage.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return storage.lastIndexOf(o);
    }

    @Override
    public ListIterator<TModel> listIterator() {
        return storage.listIterator();
    }

    @Override
    public ListIterator<TModel> listIterator(int index) {
        return storage.listIterator(index);
    }

    @Override
    public List<TModel> subList(int fromIndex, int toIndex) {
        return storage.subList(fromIndex, toIndex);
    }

    //endregion List
}
