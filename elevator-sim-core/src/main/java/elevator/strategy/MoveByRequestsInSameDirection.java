package elevator.strategy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import elevator.sim.MoveCommand;

import java.util.List;

/**
 * Created by Adam on 1/27/2017.
 */
public final class MoveByRequestsInSameDirection implements MoveStrategy
{
    @Override
    public List<Integer> getMoveSequence(final ImmutableList<MoveCommand> moveCommands)
    {
        if (moveCommands.isEmpty())
        {
            return ImmutableList.of();
        }

        MoveDirection previousDirection = null;
        MoveDirection currentDirection = null;
        int pivotFloor = 0;

        final ImmutableList.Builder cumulativeMoveSequence = ImmutableList.builder();
        ImmutableSortedSet.Builder movesSequencedByCurrentDirection = null;

        for (final MoveCommand moveCommand : moveCommands)
        {
            final int originatingFloor = moveCommand.getOriginatingFloor();
            final int destinationFloor = moveCommand.getDestinationFloor();
            assert originatingFloor != destinationFloor : "moveCommand.getOriginatingFloor() == moveCommand.getDestinationFloor()";

            previousDirection = currentDirection;
            currentDirection = originatingFloor < destinationFloor ? MoveDirection.UP : MoveDirection.DOWN;

            if (!currentDirection.equals(previousDirection))
            {
                if (movesSequencedByCurrentDirection != null)
                {
                    final ImmutableSortedSet<Integer> pendingMoveSequence = movesSequencedByCurrentDirection.build();
                    pivotFloor = pendingMoveSequence.last();
                    cumulativeMoveSequence.addAll(pendingMoveSequence);
                }
                movesSequencedByCurrentDirection = MoveDirection.UP.equals(currentDirection) ? ImmutableSortedSet.naturalOrder() : ImmutableSortedSet.reverseOrder();
            }

            if (pivotFloor != originatingFloor)
            {
                movesSequencedByCurrentDirection.add(originatingFloor);
            }
            movesSequencedByCurrentDirection.add(destinationFloor);
        }

        return cumulativeMoveSequence.addAll(movesSequencedByCurrentDirection.build()).build();
    }
}