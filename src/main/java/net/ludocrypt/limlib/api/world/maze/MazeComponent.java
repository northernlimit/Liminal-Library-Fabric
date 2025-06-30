package net.ludocrypt.limlib.api.world.maze;

import com.google.common.collect.Maps;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
		return this.cellState(pos.x(), pos.y());
	}

	public boolean fits(Vec2i vec) {
		return vec.x() >= 0 && vec.x() < this.width && vec.y() >= 0 && vec.y() < this.height;
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
		private final Map<String, NbtCompound> extra = Maps.newHashMap();

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
				case UP -> up();
				case DOWN -> down();
				case LEFT -> left();
				case RIGHT -> right();
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

	public record Vec2i(int x, int y) {

		public static Vec2i ZERO = new Vec2i(0, 0);

		public Vec2i(Vec3i pos) {
			this(pos.getX(), pos.getZ());
		}

		public BlockPos toBlock() {
			return new BlockPos(x, 0, y);
		}

		public Vec2i add(int x, int y) {
			return new Vec2i(this.x + x, this.y + y);
		}

		public Vec2i add(Vec2i vec) {
			return new Vec2i(x + vec.x(), y + vec.y());
		}

		public Vec2i sub(int x, int y) {
			return new Vec2i(this.x - x, this.y - y);
		}

		public Vec2i sub(Vec2i vec) {
			return new Vec2i(x - vec.x(), y - vec.y());
		}

		public Vec2i mult(int scalar) {
			return new Vec2i(x * scalar, y * scalar);
		}

		public double mag() {
			return Math.sqrt(x * x + y * y);
		}

		public double sqMag() {
			return x * x + y * y;
		}

		public Vec2i inv() {
			return new Vec2i(-x, -y);
		}

		public Vec2i swap() {
			return new Vec2i(y, x);
		}

		public double dot(Vec2i vec) {
			return x * vec.x() + y * vec.y();
		}

		public double cross(Vec2i vec) {
			return y * vec.x() - x * vec.y();
		}

		public double dist(Vec2i vec) {
			return this.sub(vec).mag();
		}

		public double sqDist(Vec2i vec) {
			return this.sub(vec).sqMag();
		}

		public Vec2i up() {
			return new Vec2i(x + 1, y);
		}

		public Vec2i down() {
			return new Vec2i(x - 1, y);
		}

		public Vec2i left() {
			return new Vec2i(x, y - 1);
		}

		public Vec2i right() {
			return new Vec2i(x, y + 1);
		}

		public Vec2i up(int d) {
			return new Vec2i(x + d, y);
		}

		public Vec2i down(int d) {
			return new Vec2i(x - d, y);
		}

		public Vec2i left(int d) {
			return new Vec2i(x, y - d);
		}

		public Vec2i right(int d) {
			return new Vec2i(x, y + d);
		}

		public Vec2i go(Face face) {
			return switch (face) {
				case DOWN -> this.down();
				case LEFT -> this.left();
				case RIGHT -> this.right();
				case UP -> this.up();
			};
		}

		public Vec2i go(Face face, int d) {
			return switch (face) {
				case DOWN -> this.down(d);
				case LEFT -> this.left(d);
				case RIGHT -> this.right(d);
				case UP -> this.up(d);
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
		public String toString() {
			return "(" + this.x + ", " + this.y + ")";
		}

	}

	/**
	 * This method only works with directions contained in a non Y axis!
	 * @param id The ordinal of a {@code Direction} contained in the X or Z axis
	 * @return The corresponding {@code Direction}
	 */
	public static Direction dirId(byte id) {
		return switch (id) {
			case 0 -> Direction.EAST;
			case 1 -> Direction.WEST;
			case 2 -> Direction.NORTH;
			case 3 -> Direction.SOUTH;
			default -> throw new IllegalArgumentException("Can't resolve direction ordinal: " + id);
		};
	}

	public static Face faceId(byte id) {
		return switch (id) {
			case 0 -> Face.UP;
			case 1 -> Face.DOWN;
			case 2 -> Face.LEFT;
			case 3 -> Face.RIGHT;
			default -> throw new IllegalArgumentException("Can't resolve face ordinal: " + id);
		};
	}

	public enum Face {

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
			return switch (this) {
				case UP -> LEFT;
				case DOWN -> RIGHT;
				case LEFT -> DOWN;
				case RIGHT -> UP;
			};
		}

		/**
		 * Extra utils
		 */
		public byte getOrdinal() {
			return switch (this) {
				case UP -> (byte) 0;
				case DOWN -> (byte) 1;
				case LEFT -> (byte) 2;
				case RIGHT -> (byte) 3;
			};
		}

		public Direction getDirection() {
			return switch (this) {
				case UP -> Direction.EAST;
				case DOWN -> Direction.WEST;
				case LEFT -> Direction.NORTH;
				case RIGHT -> Direction.SOUTH;
			};
		}

		public char dir() {
			return switch (this) {
				case UP -> 'e';
				case DOWN -> 'w';
				case LEFT -> 'n';
				case RIGHT -> 's';
			};
		}

		/**
		 * Returns the required manipulation to set the face to {@code UP}
		 * @return The Manipulation that you would later apply to the face
		 */
		public Manipulation manip() {
			return switch (this) {
				case UP -> Manipulation.NONE;
				case DOWN -> Manipulation.CLOCKWISE_180;
				case LEFT -> Manipulation.COUNTERCLOCKWISE_90;
				case RIGHT -> Manipulation.CLOCKWISE_90;
			};
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

}