import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class HippodromeTest {

    @Test
    void constructor_NullListParam_ThrowsIllegalArgumentExceptionAndCorrectMessage() {
        List<Horse> horses = null;
        String expectedMessage = "Horses cannot be null.";

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(horses));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void constructor_EmptyListParam_ThrowsIllegalArgumentExceptionAndCorrectMessage() {
        List<Horse> horses = new ArrayList<>(0);
        String expectedMessage = "Horses cannot be empty.";

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(horses));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getHorses_CreateNewHippodrome_ReturnsListWithSameHorsesInOrder() {
        AtomicInteger ai = new AtomicInteger(0);
        List<Horse> horses = Stream.generate(() -> new Horse("Horse" + ai.getAndIncrement(), ai.get(), ai.get()))
                .limit(30)
                .collect(Collectors.toList());
        Hippodrome hippodrome = new Hippodrome(horses);

        List<Horse> actualHorses = hippodrome.getHorses();

        assertEquals(horses, actualHorses);
        assertEquals(30, actualHorses.size());
        assertEquals("Horse0", actualHorses.get(0).getName());
        assertEquals("Horse10", actualHorses.get(10).getName());
        assertEquals("Horse20", actualHorses.get(20).getName());
        assertEquals("Horse29", actualHorses.get(29).getName());
    }

    @Test
    void move_CallsMoveMethodOnEachHorse() {
        List<Horse> list = Stream.generate(() -> Mockito.mock(Horse.class))
                .limit(50)
                .collect(Collectors.toList());
        Hippodrome hippodrome = new Hippodrome(list);

        hippodrome.move();

        List<Horse> actualHorses = hippodrome.getHorses();
        actualHorses.forEach((horse -> Mockito.verify(horse, Mockito.times(1)).move()));
    }

    @Test
    void getWinner_ReturnsTheCorrectWinner() {

        Horse h1 = new Horse("TestName1", 1, 1);
        Horse h2 = new Horse("TestName2", 3, 3);
        Horse h3 = new Horse("TestName3", 2, 2);
        Hippodrome hippodrome = new Hippodrome(List.of(h1, h2, h3));

        try (MockedStatic<Horse> horseMockedStatic = Mockito.mockStatic(Horse.class)) {
            horseMockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(0.5);
            hippodrome.move();
        }

        assertEquals(h2, hippodrome.getWinner());
    }
}