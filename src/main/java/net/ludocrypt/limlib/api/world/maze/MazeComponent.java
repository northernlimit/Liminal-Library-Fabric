package net.ludocrypt.limlib.api.world.maze;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Map;
import java.util.Objects;

public abstract class MazeComponent {

	public final int width;
	public final int height;
	public final CellState[] maze;
	public boolean generated = false;

	public MazeComponent(int width, int height) {
		this.width = width;
		this.height = height;
		this.maze = new CellState[width * height];

		for (int x = 0; x < width; x++) {

			for (int y = 0; y < height; y++) {
				CellState state = new CellState();
				state.setPosition(new Vec2i(x, y));
				this.maze[y * this.width + x] = state;
			}

		}

	}

	/**
	 * Attempt to generate the maze
	 **/
	public void generateMaze() {
		this.generateMaze(false);
	}

	/**
	 * Attempt to generate the maze, throw if this has already been generated.
	 **/
	public void generateMaze(boolean doesThrow) {

		if (generated) {

			if (doesThrow) {
				throw new UnsupportedOperationException("This maze has already been created");
			}

		} else {
			create();
			generated = true;
		}

	}

	public abstract void create();

	public CellState cellState(int x, int y) {
		return this.maze[y * this.width + x];
	}

	public CellState cellState(Vec2i pos) {
		return this.cellState(pos.getX(), pos.getY());
	}

	public boolean fits(Vec2i vec) {
		return vec.getX() >= 0 && vec.getX() < this.width && vec.getY() >= 0 && vec.getY() < this.height;
	}

	@Override
	public String toString() {

		StringBuilder row = new StringBuilder();
		row.append("\n");

		for (int x = 1; x <= width; x++) {

			for (int y = 0; y < height; y++) {
				row.append(cellState(width - x, y).toString());

			}

			row.append("\n");
		}

		return row.toString();
	}

	/**
	 * Describes the state of a particular room or 'cell' in a maze
	 * <p>
	 * 
	 * @param up       has wall up open
	 * @param right    has right wall open
	 * @param down     has wall down open
	 * @param left     has left wall open
	 * @param extra    information appended to this state
	 * @param position inside the maze
	 **/
	public static class CellState {

		private Vec2i position = new Vec2i(0, 0);
		private boolean up = false;
		private boolean right = false;
		private boolean down = false;
		private boolean left = false;
		private Map<String, NbtCompound> extra = Maps.newHashMap();

		public CellState copy() {
			CellState newState = new CellState();
			newState.setPosition(this.position);
			newState.up(this.up);
			newState.right(this.right);
			newState.down(this.down);
			newState.left(this.left);
			newState.appendAll(this.extra);
			return newState;
		}

		public void up() {
			this.up = true;
		}

		public void right() {
			this.right = true;
		}

		public void down() {
			this.down = true;
		}

		public void left() {
			this.left = true;
		}

		public void go(Face face) {

			switch (face) {
				case DOWN:
					down();
					break;
				case LEFT:
					left();
					break;
				case RIGHT:
					right();
					break;
				case UP:
					up();
					break;
			}

		}

		public void setPosition(Vec2i position) {
			this.position = position;
		}

		public void up(boolean up) {
			this.up = up;
		}

		public void right(boolean right) {
			this.right = right;
		}

		public void down(boolean down) {
			this.down = down;
		}

		public void left(boolean left) {
			this.left = left;
		}

		public void append(String name, NbtCompound data) {
			this.extra.put(name, data);
		}

		public void appendAll(Map<String, NbtCompound> data) {
			this.extra.putAll(data);
		}

		public Vec2i getPosition() {
			return position;
		}

		public boolean goesUp() {
			return up;
		}

		public boolean goesRight() {
			return right;
		}

		public boolean goesDown() {
			return down;
		}

		public boolean goesLeft() {
			return left;
		}

		public boolean goes() {
			return up || down || left || right;
		}

		public boolean goes(Face face) {
			return switch (face) {
				case UP -> up;
				case DOWN -> down;
				case LEFT -> left;
				case RIGHT -> right;
			};
		}

		public Map<String, NbtCompound> getExtra() {
			return extra;
		}

		@Override
		public String toString() {

			if (this.goesLeft() && this.goesUp() && this.goesRight() && this.goesDown()) {
				return ("┼");
			} else if (this.goesLeft() && this.goesUp() && this.goesRight() && !this.goesDown()) {
				return ("┴");
			} else if (this.goesLeft() && this.goesUp() && !this.goesRight() && this.goesDown()) {
				return ("┤");
			} else if (this.goesLeft() && this.goesUp() && !this.goesRight() && !this.goesDown()) {
				return ("┘");
			} else if (this.goesLeft() && !this.goesUp() && this.goesRight() && this.goesDown()) {
				return ("┬");
			} else if (this.goesLeft() && !this.goesUp() && this.goesRight() && !this.goesDown()) {
				return ("─");
			} else if (this.goesLeft() && !this.goesUp() && !this.goesRight() && this.goesDown()) {
				return ("┐");
			} else if (this.goesLeft() && !this.goesUp() && !this.goesRight() && !this.goesDown()) {
				return ("╴");
			} else if (!this.goesLeft() && this.goesUp() && this.goesRight() && this.goesDown()) {
				return ("├");
			} else if (!this.goesLeft() && this.goesUp() && this.goesRight() && !this.goesDown()) {
				return ("└");
			} else if (!this.goesLeft() && this.goesUp() && !this.goesRight() && this.goesDown()) {
				return ("│");
			} else if (!this.goesLeft() && this.goesUp() && !this.goesRight() && !this.goesDown()) {
				return ("╵");
			} else if (!this.goesLeft() && !this.goesUp() && this.goesRight() && this.goesDown()) {
				return ("┌");
			} else if (!this.goesLeft() && !this.goesUp() && this.goesRight() && !this.goesDown()) {
				return ("╶");
			} else if (!this.goesLeft() && !this.goesUp() && !this.goesRight() && this.goesDown()) {
				return ("╷");
			} else {
				return ("░");
			}

		}

	}

	public static class Vec2i {

		private int x;
		private int y;

		public Vec2i(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Vec2i(Vec3i pos) {
			this.x = pos.getX();
			this.y = pos.getZ();
		}

		public BlockPos toBlock() {
			return new BlockPos(x, 0, y);
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public Vec2i add(int x, int y) {
			return new Vec2i(this.x + x, this.y + y);
		}

		public Vec2i up() {
			return up(1);
		}

		public Vec2i down() {
			return down(1);
		}

		public Vec2i left() {
			return left(1);
		}

		public Vec2i right() {
			return right(1);
		}

		public Vec2i up(int d) {
			return add(d, 0);
		}

		public Vec2i down(int d) {
			return add(-d, 0);
		}

		public Vec2i left(int d) {
			return add(0, -d);
		}

		public Vec2i right(int d) {
			return add(0, d);
		}

		public Vec2i go(Face face) {
			return go(face, 1);
		}

		public Vec2i go(Face face, int d) {
			return switch (face) {
				case DOWN -> this.down(d);
				case LEFT -> this.left(d);
				case RIGHT -> this.right(d);
				case UP -> this.up();
			};
		}

		public Face normal(Vec2i b) {

			if (b.equals(this.up())) {
				return Face.UP;
			} else if (b.equals(this.left())) {
				return Face.LEFT;
			} else if (b.equals(this.right())) {
				return Face.RIGHT;
			} else if (b.equals(this.down())) {
				return Face.DOWN;
			}

			throw new IllegalArgumentException("Cannot find the normal between two non-adjacent vectors");
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}

		@Override
		public boolean equals(Object obj) {

			if (obj instanceof Vec2i pos) {
				return pos.x == this.x && pos.y == this.y;
			}

			return super.equals(obj);
		}

		@Override
		public String toString() {
			return "(" + this.x + ", " + this.y + ")";
		}

	}

	public static enum Face {

		UP,
		DOWN,
		LEFT,
		RIGHT;

		public Face mirror() {
			return switch (this) {
				case UP -> DOWN;
				case DOWN -> UP;
				case LEFT -> RIGHT;
				case RIGHT -> LEFT;
			};
		}

		public Face clockwise() {
			return switch (this) {
				case UP -> RIGHT;
				case DOWN -> LEFT;
				case LEFT -> UP;
				case RIGHT -> DOWN;
			};
		}

		public Face anticlockwise() {
			return clockwise().clockwise().clockwise();
		}

	}

}
