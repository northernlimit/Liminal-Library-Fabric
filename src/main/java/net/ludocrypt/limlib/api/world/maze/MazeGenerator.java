package net.ludocrypt.limlib.api.world.maze;

import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.maze.MazeComponent.CellState;
import net.ludocrypt.limlib.api.world.maze.MazeComponent.Vec2i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;

import java.util.HashMap;

public class MazeGenerator<M extends MazeComponent> {

	private final HashMap<Vec2i, M> mazes = new HashMap<>(30);
	public final int width;
	public final int height;
	public final int thicknessX;
	public final int thicknessY;
	public final long seedModifier;

	/**
	 * Creates a rectangular maze generator.
	 * <p>
	 * 
	 * @param width        of the maze
	 * @param height       of the maze
	 * @param thicknessX   of the cells in real world coordinates.
	 * @param thicknessY   of the cells in real world coordinates.
	 * @param seedModifier is the change to the seed when generating a new random
	 *                     maze. In code, it uses the world seed + seedModifier
	 */
	public MazeGenerator(int width, int height, int thicknessX, int thicknessY, long seedModifier) {
		this.width = width;
		this.height = height;
		this.thicknessX = thicknessX;
		this.thicknessY = thicknessY;
		this.seedModifier = seedModifier;
	}

	/**
	 * Begins generating a maze starting at pos. This should be run in every chunk
	 * with the pos being the beginning position of the chunk.
	 *
	 * @param pos           the starting position for the maze logic to work.
	 * @param region        the region to which the starting position belongs
	 * @param mazeCreator   functional interface to create a new maze at a position
	 * @param cellDecorator functional interface to generate a single maze block, or 'cell'
	 */
	public void generateMaze(Vec2i pos, ChunkRegion region, MazeCreator<M> mazeCreator, CellDecorator<M> cellDecorator) {

		for (int x = 0; x < 16; x++) {

			for (int y = 0; y < 16; y++) {
				Vec2i inPos = pos.add(x, y);

				if (Math.floorMod(inPos.x(), thicknessX) == 0 && Math.floorMod(inPos.y(), thicknessY) == 0) {
					Vec2i mazePos = new Vec2i(inPos.x() - Math.floorMod(inPos.x(), (width * thicknessX)),
						inPos.y() - Math.floorMod(inPos.y(), (height * thicknessY)));
					M maze;

					if (this.mazes.containsKey(mazePos)) {
						maze = this.mazes.get(mazePos);
					} else {
						maze = mazeCreator
							.newMaze(region, mazePos, width, height,
								Random
									.create(LimlibHelper
										.blockSeed(mazePos.x(), mazePos.y(), region.getSeed() + seedModifier)));
						this.mazes.put(mazePos, maze);
					}

					int mazeX = (inPos.x() - mazePos.x()) / thicknessX;
					int mazeY = (inPos.y() - mazePos.y()) / thicknessY;
					CellState originCell = maze.cellState(mazeX, mazeY);
					cellDecorator
						.generate(region, inPos, mazePos, maze, originCell, new Vec2i(this.thicknessX, this.thicknessY),
							Random
								.create(LimlibHelper
									.blockSeed(mazePos.x(), mazePos.y(), region.getSeed() + seedModifier)));
				}

			}

		}

	}

	public HashMap<Vec2i, M> getMazes() {
		return mazes;
	}

	@FunctionalInterface
	public interface CellDecorator<M extends MazeComponent> {

		void generate(ChunkRegion region, Vec2i pos, Vec2i mazePos, M maze, CellState state, Vec2i thickness,
				Random random);

	}

	@FunctionalInterface
	public interface MazeCreator<M extends MazeComponent> {

		M newMaze(ChunkRegion region, Vec2i mazePos, int width, int height, Random random);

	}

}
