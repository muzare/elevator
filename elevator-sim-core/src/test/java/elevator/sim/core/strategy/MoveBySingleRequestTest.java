package elevator.sim.core.strategy;

import com.google.common.collect.ImmutableList;
import elevator.sim.core.MoveCommand;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Verifies the {@linkplain MoveBySingleRequest} algorithm implementation operates as expected.
 */
public final class MoveBySingleRequestTest
{
    /**
     * Verifies that MoveBySingleRequest throws an IllegalArgumentException when constructed with a null List of MoveCommands.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nullMoveCommands()
    {
        createMoveStrategy().getMoveSequence(null);
    }

    /**
     * Verifies that a single MoveCommand results in the expected move sequence.
     */
    @Test
    public void singleRequest()
    {
        assertThat(createMoveStrategy().getMoveSequence(ImmutableList.of(new MoveCommand(8, 1))), Matchers.contains(8, 1));
    }

    /**
     * Verifies that a single MoveCommand results in the expected move sequence.
     */
    @Test
    public void pickUpSameAsDropOff()
    {
        assertThat(createMoveStrategy().getMoveSequence(
                ImmutableList.of(
                        new MoveCommand(10, 8),
                        new MoveCommand(8, 1))),
                Matchers.contains(10, 8, 1));
    }

    /**
     * Verifies that multiple ascending MoveCommands result in the expected move sequence.
     */
    @Test
    public void multipleAscendingMoves()
    {
        assertThat(createMoveStrategy().getMoveSequence(
                ImmutableList.of(
                        new MoveCommand(2, 4),
                        new MoveCommand(3, 7),
                        new MoveCommand(5, 11))),
                Matchers.contains(2, 4, 3, 7, 5, 11));
    }

    /**
     * Verifies that multiple descending MoveCommands result in the expected move sequence.
     */
    @Test
    public void multipleDescendingMoves()
    {
        assertThat(createMoveStrategy().getMoveSequence(
                ImmutableList.of(
                        new MoveCommand(13, 4),
                        new MoveCommand(7, 3),
                        new MoveCommand(5, 4))),
                Matchers.contains(13, 4, 7, 3, 5, 4));
    }

    /**
     * Verifies that an ascending MoveCommand followed by a descending MoveCommand results in the expected move sequence.
     */
    @Test
    public void ascendingMoveFollowedByDescendingMove()
    {
        assertThat(createMoveStrategy().getMoveSequence(
                ImmutableList.of(
                        new MoveCommand(3, 40),
                        new MoveCommand(39, 2))),
                Matchers.contains(3, 40, 39, 2));
    }

    /**
     * Verifies that a descending MoveCommand followed by an ascending MoveCommand results in the expected move sequence.
     */
    @Test
    public void descendingMoveFollowedByAscendingMove()
    {
        assertThat(createMoveStrategy().getMoveSequence(
                ImmutableList.of(
                        new MoveCommand(13, 4),
                        new MoveCommand(7, 12))),
                Matchers.contains(13, 4, 7, 12));
    }

    /**
     * Creates a new {@linkplain MoveByRequestsInSameDirection}. Each test should create a new instance to guarantee test integrity.
     *
     * @return Non-null MoveByRequestsInSameDirection.
     */
    private static MoveBySingleRequest createMoveStrategy()
    {
        return new MoveBySingleRequest();
    }
}