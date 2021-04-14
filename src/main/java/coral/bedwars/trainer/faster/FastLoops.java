package coral.bedwars.trainer.faster;

import coral.bedwars.trainer.abstraction.interfacing.Nominable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public final class FastLoops {

    private FastLoops() {
        throw new AssertionError("Nope.");
    }

    public static <E extends Nominable> Optional<E> firstWithName(E[] nameable, String name, boolean ignoreCases) {
        if (nameable.length == 0)
            return Optional.empty();

        if (ignoreCases) {
            for (E nominable : nameable) {
                if (!nominable.getName().equalsIgnoreCase(name))
                    continue;

                return Optional.of(nominable);
            }

            return Optional.empty();
        }

        for (E nominable : nameable) {
            if (!nominable.getName().equals(name))
                continue;

            return Optional.of(nominable);
        }

        return Optional.empty();
    }

    public static <E> ChainReader<E> read(E... objects) {
        return new ChainReader<>(objects);
    }

    public static class ChainReader<E> {

        private final E[] objects;

        @SafeVarargs
        protected ChainReader(E... objects) {
            this.objects = objects;
        }

        public ChainReader<E> forEach(Consumer<E> consumer) {
            Objects.requireNonNull(consumer);

            if (objects.length == 0)
                return this;

            for (E e : objects)
                Nullation.nonNull(e).then(consumer);

            return this;
        }

        public <S> ChainReader<S> then(S... objects) {
            return new ChainReader<>(objects);
        }
    }

}
