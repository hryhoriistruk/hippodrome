import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class HorseTest {

    @Test
    void constructor_NullNameParam_ThrowsIllegalArgumentExceptionAndCorrectMessage() {
        String expectedMessage = "Name cannot be null.";
        String name = null;
        double speed = 1;
        double distance = 2;

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Horse(name, speed, distance));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\n", "\t"})
    void constructor_BlankNameParam_ThrowsIllegalArgumentExceptionAndCorrectMessage(String name) {
        String expectedMessage = "Name cannot be blank.";
        double speed = 1;
        double distance = 2;

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Horse(name, speed, distance));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void constructor_NegativeSpeedParam_ThrowsIllegalArgumentExceptionAndCorrectMessage() {
        String expectedMessage = "Speed cannot be negative.";
        String name = "TestName";
        double speed = -1;
        double distance = 2;

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Horse(name, speed, distance));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void constructor_NegativeDistanceParam_ThrowsIllegalArgumentExceptionAndCorrectMessage() {
        String expectedMessage = "Distance cannot be negative.";
        String name = "TestName";
        double speed = 1;
        double distance = -2;

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Horse(name, speed, distance));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void getName_CreateNewHorse_GetCorrectName() {
        String name = "TestName";
        double speed = 1;
        double distance = 2;
        Horse horse = new Horse(name, speed, distance);

        String actualName = horse.getName();

        assertEquals(name, actualName);
    }

    @Test
    void getSpeed_CreateNewHorse_GetCorrectSpeed() {
        String name = "TestName";
        double speed = 1;
        double distance = 2;
        Horse horse = new Horse(name, speed, distance);

        double actualSpeed = horse.getSpeed();

        assertEquals(speed, actualSpeed);

    }

    @Test
    void getDistance_CreateNewHorse_GetCorrectDistance() {
        String name = "TestName";
        double speed = 1;
        double distance = 2;
        Horse horse = new Horse(name, speed, distance);

        double actualDistance = horse.getDistance();

        assertEquals(distance, actualDistance);

    }

    @Test
    void getDistance_CreateNewHorseWithoutDistance_GetZeroDistance() {
        String name = "TestName";
        double speed = 1;
        Horse horse = new Horse(name, speed);

        double actualDistance = horse.getDistance();

        assertEquals(0, actualDistance);
    }

    @Test
    void move_CallsGetRandomDouble() {
        String name = "TestName";
        double speed = 1;
        double distance = 2;
        Horse horse = new Horse(name, speed, distance);

        try (MockedStatic<Horse> horseMockedStatic = Mockito.mockStatic(Horse.class)) {
            horse.move();

            horseMockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            0.2, 0.5, 0.8, 10, 435
    })
    void move_CalculateCorrectDistance(double fakeReturnRandomValue) {
        double min = 0.2;
        double max = 0.9;
        String name = "TestName";
        double speed = 1;
        double distance = 2;
        Horse horse = new Horse(name, speed, distance);
        double expectedDistance = distance + speed * fakeReturnRandomValue;

        try (MockedStatic<Horse> horseMockedStatic = Mockito.mockStatic(Horse.class)) {
            horseMockedStatic.when(() -> Horse.getRandomDouble(min, max)).thenReturn(fakeReturnRandomValue);
            horse.move();
        }

        double actualDistance = horse.getDistance();
        assertEquals(expectedDistance, actualDistance);
    }
}