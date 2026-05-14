import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.lang.Math;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;

class Main {
	private class Rectangle {
		public TDpt leftuppervertix;
		public TDpt rightbottomvertix;
		public TDpt leftbottomvertix;
		public TDpt rightuppervertix;

		public Rectangle(TDpt left, TDpt right) {
			this.leftuppervertix   = left;
			this.rightbottomvertix = right;
			this.leftbottomvertix  = new TDpt(left.x, right.y);
			this.rightuppervertix  = new TDpt(right.x, left.y);
		}

		public List<TDpt> vertices() {
			return Arrays.asList( 
				leftuppervertix,
				rightbottomvertix,
				leftbottomvertix,
				rightuppervertix
				);
		}
		
		public boolean isInscribed(ArrayList<Rectangle> area) {
			return this.vertices().stream()
				.allMatch(v -> area.stream().anyMatch(r -> r != null && r.includes(v))); 
		}

		public boolean includes(TDpt p) {
			return p.x.compareTo(leftuppervertix.x) >= 0 && p.x.compareTo(rightbottomvertix.x) <= 0 &&
				   p.y.compareTo(leftuppervertix.y) >= 0 && p.y.compareTo(rightbottomvertix.y) <= 0;
		}

		public Long area() {
			TDpt a = leftuppervertix;
			TDpt b = rightbottomvertix;
			Long height = Math.abs(a.y - b.y) + 1;
			Long base   = Math.abs(a.x - b.x) + 1;
			return height * base;
		}

		@Override
		public String toString() {
			return "[leftupper = "   + leftuppervertix.toString() + " " +
				   "rightbottom = " + rightbottomvertix.toString() + "] - area: " + this.area();
		}
	}

	private class TDpt {
		public Long x, y;
		public TDpt(Long x, Long y) {
			this.x = x;
			this.y = y;
		}

		public TDpt(ArrayList<Long> l) {
			this.x = l.get(0);
			this.y = l.get(1);
		}

		@Override
		public String toString() {
			return "{x=" + x + ", y=" + y + "}";
		}
	}

	private class Segment {
		public TDpt a, b;

		public Segment(TDpt a, TDpt b, boolean base) {
			if (base) {
				this.a = a.x <= b.x? a: b;
				this.b = a.x > b.x? a: b;
			} else {
				this.a = a.y <= b.y? a: b;
				this.b = a.y > b.y? a: b;
			}
		}

		public Rectangle interceptVert(Segment other) {
			if (this == other) return null;
			Segment left  = this.a.x <= other.a.x? this: other;
			Segment right = this.a.x >  other.a.x? this: other;
			// System.out.println("upper " + upper + " - lower " + lower);
			if (other.a.x != other.a.x && this.a.y != other.a.y) return null;
			if (left.a.y > right.b.y || left.b.y < right.a.y) return null;

			TDpt lupper = left.a.y >= right.a.y? left.a: new TDpt(left.a.x, right.a.y);
			TDpt rbotto = right.b.y <= left.b.y? right.b: new TDpt(right.b.x, left.b.y);

			return new Rectangle(lupper, rbotto);
		}

		public Rectangle interceptHoriz(Segment other) {
			if (this == other) return null;
			Segment upper = this.a.y <= other.a.y? this: other;
			Segment lower = this.a.y >  other.a.y? this: other;
			// System.out.println("upper " + upper + " - lower " + lower);
			if (other.a.x != other.a.x && this.a.y != other.a.y) return null;
			if (upper.a.x > lower.b.x || upper.b.x < lower.a.x) return null;

			TDpt lupper = upper.a.x >= lower.a.x? upper.a: new TDpt(lower.a.x, upper.a.y);
			TDpt rbotto = lower.b.x <= upper.b.x? lower.b: new TDpt(upper.b.x, lower.b.y);

			return new Rectangle(lupper, rbotto);
		}

		@Override
		public String toString() {
			return "a = " + a.toString() + " " +
				   "b = " + b.toString();
		}
	}


	private ArrayList<Segment> collectSegments(ArrayList<TDpt> vertices, int coordinate) {
		ArrayList<Segment> col = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i ++) {
			TDpt a = vertices.get(i);
			for (int j = i + 1; j < vertices.size(); j ++) {
				TDpt b = vertices.get(j);
				if (coordinate == 1) // horizontally aligned 
					if (a.x.equals(b.x)) col.add(new Segment(a, b, false));
				if (coordinate == 0) // vertically aligned 
					if (a.y.equals(b.y)) col.add(new Segment(a, b, true));
			}
		}

		return col;
	}



    public Long mySol(String input) {
		ArrayList<TDpt> vertices = Arrays.stream(input.split("\n"))
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong)
				.collect(Collectors.toCollection(ArrayList::new))
			)
			.map(TDpt::new)
			.collect(Collectors.toCollection(ArrayList::new));

		vertices.sort((a, b) -> a.y.compareTo(b.y));

		// vertices.forEach(System.out::println);

		ArrayList<Segment> bases   = collectSegments(vertices, 0);
		ArrayList<Segment> heights = collectSegments(vertices, 1);
		// bases.forEach(System.out::println);
		heights.forEach(System.out::println);
		
		// collect rectangles
		ArrayList<Rectangle> internalspace = new ArrayList<Rectangle>();
		// for (int i  = 0; i < bases.size(); i++)
		// 	for (int j = i + 1; j < bases.size(); j++)
		// 		internalspace.add(bases.get(i).interceptHoriz(bases.get(j)));

		for (int i  = 0; i < heights.size(); i++)
			for (int j = i + 1; j < heights.size(); j++)
				internalspace.add(heights.get(i).interceptVert(heights.get(j)));

		internalspace.removeIf(Objects::isNull);
		internalspace.forEach(System.out::println);
		System.out.println(":::::::::::::::");

		Long maxarea = 0l;

		ArrayList<Rectangle> internalrectangles = new ArrayList<Rectangle>();
		for (int i = 0; i < vertices.size(); i++) {
			TDpt pointa = vertices.get(i);
			for (int j = i + 1; j < vertices.size(); j++) {
				TDpt pointb = vertices.get(j);
				Rectangle actual = new Rectangle(pointa, pointb);
				if (!actual.isInscribed(internalspace)) continue;
				// System.out.println(actual);
				maxarea = maxarea < actual.area()? actual.area(): maxarea;
			}
		}
		return maxarea;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/9/input.txt"));
        String example = "";
		example += "7,1\n";
		example += "11,1\n";
		example += "11,7\n";
		example += "9,7\n";
		example += "9,5\n";
		example += "2,5\n";
		example += "2,3\n";
		example += "7,3\n";

        Long totcount = 0l;
        totcount = sol.mySol(example);
		// totcount = sol.mySol(input);
        System.out.printf("max area: %d", totcount);
    }
}
