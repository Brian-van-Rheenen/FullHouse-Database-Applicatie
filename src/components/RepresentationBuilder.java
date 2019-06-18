package components;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Create a custom representation of {@link TModel}
 * @param <TModel> Model that needs to be converted to string columns
 */
public class RepresentationBuilder<TModel> {

    private ArrayList<Function<TModel, String>> callChain = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();

    public RepresentationBuilder<TModel> addColumn(String columnName, Function<TModel, String> converter) {
        callChain.add(converter);
        names.add(columnName);
        return this;
    }

    /**
     * Convert the {@link RepresentationBuilder} to a {@link Representor}
     * @return a constructed {@link Representor} of {@link TModel}
     */
    public Representor<TModel> build() {
        return new Representor<>(names, callChain);
    }

}
