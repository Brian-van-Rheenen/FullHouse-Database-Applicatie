package components;

import java.util.List;
import java.util.function.Function;

/**
 * Converts a TModel to the required representation
 *
 * <p>
 *     Use {@link RepresentationBuilder} to create a new {@link Representor}
 * </p>
 * @param <TModel> The Model that needs to be converted to table data
 */
public class Representor<TModel> {

    private List<Function<TModel, Object>> chain;
    private List<String> columnNames;

    public Representor(List<String> names, List<Function<TModel, Object>> callChain) {
        if(callChain.size() != names.size())
            throw new IllegalArgumentException("The amount of names and the amount of functions should be the same!");

        this.chain = callChain;
        this.columnNames = names;
    }

    public int getColumnCount() {
        return chain.size();
    }


    public Object[] invokeOn(TModel model) {
        Object[] props = new Object[chain.size()];

        for (int i = 0; i < chain.size(); i++) {
            props[i] = chain.get(i).apply(model);
        }

        return props;
    }

    public String getColumnName(int columnIndex) {
        return columnNames.get(columnIndex);
    }
}
