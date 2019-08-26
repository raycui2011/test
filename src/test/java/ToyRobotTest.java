
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.rea.robot.simulator.Board;
import com.rea.robot.simulator.Location;
import com.rea.robot.simulator.ToyRobot;
import com.rea.robot.simulator.SquareTable;
import com.rea.robot.simulator.Direction;
import com.rea.robot.exceptions.ToyRobotException;

@RunWith(JUnit4.class)
public class ToyRobotTest  {
	SquareTable table;
	ToyRobot testRobot;
	@Before
	public void initialise() throws ToyRobotException {
		table = new SquareTable(5,5);
		testRobot = new ToyRobot(new Location(0, 0), Direction.NORTH, table);
	}
	
	@Test
	public void testTurnLeft() throws ToyRobotException {
		assertEquals(testRobot.getFacingDirection(), Direction.NORTH);

		//This should become -90 which translates to 270 Deg
		assertEquals(testRobot.rotateLeft().getFacingDirection(), Direction.WEST);
		assertEquals(testRobot.rotateLeft().getFacingDirection(), Direction.SOUTH);
		assertEquals(testRobot.rotateLeft().getFacingDirection(), Direction.EAST);
		
		//This should flick around past 360 Deg for a second time
		assertEquals(testRobot.rotateLeft().getFacingDirection(), Direction.NORTH);
		assertEquals(testRobot.rotateLeft().getFacingDirection(), Direction.WEST);
	}

	@Test
	public void testTurnRight() throws ToyRobotException {
		assertEquals(testRobot.getFacingDirection(), Direction.NORTH);

		assertEquals(testRobot.rotateRight().getFacingDirection(), Direction.EAST);
		assertEquals(testRobot.rotateRight().getFacingDirection(), Direction.SOUTH);
		assertEquals(testRobot.rotateRight().getFacingDirection(), Direction.WEST);
		
		//This should flick around past 360 Deg from 270 to 0
		assertEquals(testRobot.rotateRight().getFacingDirection(), Direction.NORTH);
		assertEquals(testRobot.rotateRight().getFacingDirection(), Direction.EAST);
	}

	@Test
	public void testPlace() throws ToyRobotException {
		assertEquals(testRobot.getLocation(), new Location(0, 0));
		assertEquals(testRobot.getFacingDirection(), Direction.NORTH);

		testRobot.place(new Location(4, 5), Direction.SOUTH);

		assertEquals(testRobot.getLocation(), new Location(4, 5));
		assertEquals(testRobot.getFacingDirection(), Direction.SOUTH);

		placeBoundaryFailureExpected(new Location(6, 5), Direction.SOUTH, table);

		assertEquals(testRobot.getLocation(), new Location(4, 5));
		assertEquals(testRobot.getFacingDirection(), Direction.SOUTH);

		placeBoundaryFailureExpected(new Location(-1, 5), Direction.SOUTH, table);

		assertEquals(testRobot.getLocation(), new Location(4, 5));
		assertEquals(testRobot.getFacingDirection(), Direction.SOUTH);

		placeBoundaryFailureExpected(new Location(0, -1), Direction.SOUTH, table);

		assertEquals(testRobot.getLocation(), new Location(4, 5));
		assertEquals(testRobot.getFacingDirection(), Direction.SOUTH);
		
		
		//Try on a slightly bigger table and testing -ve coordinates
		
  testRobot = new ToyRobot(new Location(0, 0), Direction.NORTH, new SquareTable(6, 6));
		testRobot.place(new Location(5, 6), Direction.EAST);
                
		assertEquals(testRobot.getLocation(), new Location(5, 6));
		assertEquals(testRobot.getFacingDirection(), Direction.EAST);
		
	}
	
	@Test
	public void testMoveForward() throws ToyRobotException {
		assertEquals(testRobot.getFacingDirection(), Direction.NORTH);
		assertEquals(testRobot.getLocation(), new Location(0, 0));
		
		testRobot.rotateRight().rotateRight();
		assertEquals(testRobot.getFacingDirection(), Direction.SOUTH);
		
		moveForwardBoundaryFailureExpected(); //Testing South Boundary
		assertEquals(testRobot.getLocation(), new Location(0, 0));

		testRobot.rotateRight();
		assertEquals(testRobot.getFacingDirection(), Direction.WEST);
		moveForwardBoundaryFailureExpected(); //Testing west boundary
		
		testRobot.rotateRight();
		assertEquals(testRobot.getFacingDirection(), Direction.NORTH);
		assertEquals(testRobot.getLocation(), new Location(0, 0));

		testRobot.moveForward()
				 .moveForward()
				 .moveForward()
				 .moveForward()
				 .moveForward();
		assertEquals(
				testRobot.getLocation(), 
				new Location(0, 5));

		moveForwardBoundaryFailureExpected(); // Testing North boundary
		
		assertEquals(testRobot.getLocation(), new Location(0, 5));

		testRobot.rotateRight()
				 .moveForward()
				 .moveForward()
				 .moveForward()
				 .moveForward();

		assertEquals(testRobot.getLocation(), new Location(4, 5));
		assertEquals(testRobot.getFacingDirection(), Direction.EAST);

	}
	
	@Test
	public void testReport() throws ToyRobotException {
		assertEquals(testRobot.report(), "[0,0] NORTH");
		
		assertEquals(testRobot.place(new Location(2, 1), Direction.EAST).report(), "[2,1] EAST");
	}
	
	@Test
	public void testExampleA() throws ToyRobotException {
	/*	PLACE 0,0,ToyRobotException
		MOVE
		REPORT
		Output: 0,1,NORTH */

		testRobot.place(new Location(0, 0), Direction.NORTH) .moveForward();
		
		assertEquals(testRobot.report(), "[0,1] NORTH");
	}

	@Test
	public void testExampleB() throws ToyRobotException {
        /*PLACE 0,0,NORTH
            LEFT
            REPORT
            Output: 0,0,WEST */

		testRobot.place(new Location(0, 0), Direction.NORTH).rotateLeft();
		
		assertEquals(testRobot.report(), "[0,0] WEST");
	}
	
	@Test
	public void testExampleC() throws ToyRobotException {
	/*	PLACE 1,2,EAST
		MOVE
		MOVE
		LEFT
		MOVE
		REPORT
		Output: 3,3,NORTH */

		testRobot.place(new Location(1, 2), Direction.EAST)
				 .moveForward()
				 .moveForward()
				 .rotateLeft()
				 .moveForward();
		
		assertEquals(testRobot.report(), "[3,3] NORTH");
	}
	
	/** Asserts that there will be a failure when moving forward. */
	private ToyRobot placeBoundaryFailureExpected(Location location, Direction direction, SquareTable surface) {
		try {
			testRobot.place(location, direction);
			fail("This should cause a boundary Failure");
		} catch (ToyRobotException e) {}
		
		return this.testRobot;
	}
	
	/** Asserts that there will be a failure when moving forward. */
	private ToyRobot moveForwardBoundaryFailureExpected() {
		try {
			testRobot.moveForward();
			fail("This should cause a boundary Failure");
		} catch (ToyRobotException e) {}
		
		return this.testRobot;
	}
}
