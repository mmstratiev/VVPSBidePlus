import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmLineTest {

    @Test
    void addCurrentItemSet() {
        AlgorithmLine algorithmLine = new AlgorithmLine();
        algorithmLine.AddCurrentItemSet(1);
        algorithmLine.AddCurrentItemSet(2);
        algorithmLine.AddCurrentItemSet(1);
        algorithmLine.AddCurrentItemSet(3);

        assertEquals("1 2 3 -2", algorithmLine.toString());
    }

    @Test
    void addNewItemSet() {
        AlgorithmLine algorithmLine = new AlgorithmLine();
        algorithmLine.AddNewItemSet(1);
        algorithmLine.AddNewItemSet(2);

        assertEquals("1 -1 2 -2", algorithmLine.toString());
    }

    @Test
    void isEmpty() {
        AlgorithmLine algorithmLine = new AlgorithmLine();
        assertEquals(true, algorithmLine.IsEmpty());

        algorithmLine.AddCurrentItemSet(1);

        assertEquals(false, algorithmLine.IsEmpty());
    }
}