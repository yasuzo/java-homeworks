package hr.fer.zemris.java.hw07.observer2;

/**
 * Demo of observer pattern for {@link IntegerStorage} and {@link IntegerStorageObserver}.
 *
 * @author Jan Capek
 */
public class ObserverExample {
    public static void main(String[] args) {
        IntegerStorage istorage = new IntegerStorage(20);
        IntegerStorageObserver squareObserver = new SquareValue();
        IntegerStorageObserver doubleObserver = new DoubleValue(4);
        IntegerStorageObserver changeCountObserver = new ChangeCounter();
        istorage.addObserver(changeCountObserver);
        istorage.addObserver(doubleObserver);
        istorage.addObserver(squareObserver);

        istorage.setValue(5);
        istorage.setValue(2);
        istorage.setValue(25);
        istorage.setValue(13);
        istorage.setValue(22);
        istorage.setValue(15);
    }
}
