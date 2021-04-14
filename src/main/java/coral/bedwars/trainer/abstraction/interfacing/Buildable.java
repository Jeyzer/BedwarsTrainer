package coral.bedwars.trainer.abstraction.interfacing;

public interface Buildable<T> {

    void build(boolean scratch);
    boolean isBuilt();

    T getBuilt();

}
