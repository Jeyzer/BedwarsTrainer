package coral.bedwars.trainer.faster;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

// Made by Jeyzer with passion and love (Completely not forced) <3
public class Nullation<E> {

    private final E e;
    private final boolean isNull;
    private @Setter @Accessors(chain = true, fluent = true) E withDefault;

    private Nullation(E e) {
        this.e = e;
        isNull = isNull(e);
    }

    public static <E> Nullation<E> nonNull(@Nullable E e) {
        return new Nullation<>(e);
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public Operable operable() {
        return new Operable();
    }

    public static <E> E antiNull(E e, E withDefault) {
        return e == null ? withDefault : e;
    }

    public static <K, V> void forEach(Map<K, V> map, BiConsumer<K, V> consumer) {
        for (Map.Entry<K, V> entry : map.entrySet())
            if (isNull(entry.getKey())) consumer.accept(null, entry.getValue());
    }

    public static <E> void forEach(Collection<E> collection, Consumer<E> consumer) {
        for (E e : collection) if (isNull(e)) consumer.accept(e);
    }

    @NonNull
    public Nullation<E> then(Runnable runnable) {
        if (!isNull) runnable.run();
        return this;
    }

    @NonNull
    public Nullation<E> then(Consumer<E> consumer) {
        if (!isNull) consumer.accept(e);
        return this;
    }

    @NonNull
    public <R> Nullation<R> get(Function<E, R> function) {
        if (!isNull) return nonNull(function.apply(e));
        return nonNull(null);
    }

    @Nullable
    public E finalized() {
        return e;
    }

    @RequiredArgsConstructor
    public class Operable {

        public <S> NullableOperation<S> operation(S s) {
            return new NullableOperation<>(s);
        }

        public <S> Nullation<S>.Operable.FinalizerOperation<E> finalize(S s) {
            return Nullation.nonNull(s).operable().operation(e);
        }

        public <R> Nullation<R> nonNull(Function<E, R> function) {
            return get(function);
        }

        public class FinalizerOperation<S> {

            protected final S s;
            protected final boolean isNull;

            public FinalizerOperation(S s) {
                this.s = s;
                isNull = isNull(s);
            }

            public Operable run(Consumer<S> consumer) {
                if (isNull || Nullation.this.isNull)
                    return Operable.this;

                consumer.accept(s);
                return Operable.this;
            }

        }

        public class NullableOperation<S> extends FinalizerOperation<S> {

            public NullableOperation(S s) {
                super(s);
            }

            public Operable run(BiConsumer<E, S> consumer) {
                if (isNull || Nullation.this.isNull) return Operable.this;
                consumer.accept(Nullation.this.e, s);
                return Operable.this;
            }
        }
    }

}
