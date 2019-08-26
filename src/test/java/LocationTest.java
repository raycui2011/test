import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.rea.robot.simulator.Location;

@RunWith(JUnit4.class)
public class LocationTest  {
	@Test
	public void testChangePosition() {
    Location location = new Location(1,2);

    assertEquals(location.getXValue(), 1);
    assertEquals(location.getYValue(), 2);

    Location new_location = new Location(3,5);

    assertEquals(new_location.getXValue(), 3);
    assertEquals(new_location.getYValue(), 5);

    Location addedLocation = location.changePosition(new_location);

    assertEquals(location.getXValue(), 1);
    assertEquals(location.getYValue(), 2);
    assertEquals(new_location.getXValue(), 3);
    assertEquals(new_location.getYValue(), 5);
    assertEquals(addedLocation.getXValue(), 4);
    assertEquals(addedLocation.getYValue(), 7);
	}
}
