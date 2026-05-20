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
		public Pt2D leftuppervertix;
		public Pt2D rightbottomvertix;
		public Pt2D leftbottomvertix;
		public Pt2D rightuppervertix;

		public Rectangle(Pt2D left, Pt2D right) {
			this.leftuppervertix   = left;
			this.rightbottomvertix = right;
			this.leftbottomvertix  = new Pt2D(left.x, right.y);
			this.rightuppervertix  = new Pt2D(right.x, left.y);
		}

		public List<Pt2D> vertices() {
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

		public boolean includes(Pt2D p) {
			return p.x.compareTo(leftuppervertix.x) >= 0 && p.x.compareTo(rightbottomvertix.x) <= 0 &&
				   p.y.compareTo(leftuppervertix.y) >= 0 && p.y.compareTo(rightbottomvertix.y) <= 0;
		}

		public Long area() {
			Pt2D a = leftuppervertix;
			Pt2D b = rightbottomvertix;
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

	private class Pt2D {
		public Long x, y;
		public int idx;
		public boolean red = true;
		public Pt2D(Long x, Long y) {
			this.x = x;
			this.y = y;
		}

		public Pt2D(ArrayList<Long> l) {
			this.x = l.get(0);
			this.y = l.get(1);
		}

		public Long manhattanDistance(Pt2D other) {
			if (this.x != other.x && this.y != other.y) return null;
			return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
		}

		public Pt2D next(ArrayList<Pt2D> vertices) {
			// ArrayList<Pt2D> nextycloser = vertices.filter(i -> i.y == this.y);
			return null;
		}

		@Override
		public String toString() {
			return "{x=" + x + ", y=" + y + "}";
		}
	}

	private class Segment {
		public Pt2D a, b;

		public Segment(Pt2D a, Pt2D b, boolean base) {
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

			Pt2D lupper = left.a.y >= right.a.y? left.a: new Pt2D(left.a.x, right.a.y);
			Pt2D rbotto = right.b.y <= left.b.y? right.b: new Pt2D(right.b.x, left.b.y);

			return new Rectangle(lupper, rbotto);
		}

		public Rectangle interceptHoriz(Segment other) {
			if (this == other) return null;
			Segment upper = this.a.y <= other.a.y? this: other;
			Segment lower = this.a.y >  other.a.y? this: other;
			// System.out.println("upper " + upper + " - lower " + lower);
			if (other.a.x != other.a.x && this.a.y != other.a.y) return null;
			if (upper.a.x > lower.b.x || upper.b.x < lower.a.x) return null;

			Pt2D lupper = upper.a.x >= lower.a.x? upper.a: new Pt2D(lower.a.x, upper.a.y);
			Pt2D rbotto = lower.b.x <= upper.b.x? lower.b: new Pt2D(upper.b.x, lower.b.y);

			return new Rectangle(lupper, rbotto);
		}

		@Override
		public String toString() {
			return "a = " + a.toString() + " " +
				   "b = " + b.toString();
		}
	}


	private ArrayList<Segment> collectSegments(ArrayList<Pt2D> vertices, int coordinate) {
		ArrayList<Segment> col = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i ++) {
			Pt2D a = vertices.get(i);
			for (int j = i + 1; j < vertices.size(); j ++) {
				Pt2D b = vertices.get(j);
				if (coordinate == 1) // horizontally aligned 
					if (a.x.equals(b.x)) col.add(new Segment(a, b, false));
				if (coordinate == 0) // vertically aligned 
					if (a.y.equals(b.y)) col.add(new Segment(a, b, true));
			}
		}

		return col;
	}




    public Long mySol(String input) {
		ArrayList<Pt2D> vertices = Arrays.stream(input.split("\n"))
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong)
				.collect(Collectors.toCollection(ArrayList::new))
			)
			.map(Pt2D::new)
			.collect(Collectors.toCollection(ArrayList::new));

		vertices.sort((a, b) -> a.x.compareTo(b.x));
		vertices.sort((a, b) -> a.y.compareTo(b.y));
		// vertices.forEach(System.out::println);

		
		List<Pt2D> area = new ArrayList<>();
		List<Pt2D> excluded = new ArrayList<>();
		Pt2D actual = vertices.remove(0);

		while (vertices.size() > 0 && area.indexOf(actual) < 0) {
			area.add(actual);
			int i = 0;
			for (; i < vertices.size(); i++) {
				if (vertices.get(i).y.compareTo(actual.y) == 0 || vertices.get(i).x.compareTo(actual.x) == 0) break;
			};
			if (i < vertices.size()) {
				actual = vertices.remove(i);
			}
			else {
				actual = vertices.remove(0);
				excluded.add(actual);
			}
		}
		area.forEach(System.out::println);

		// area.sort((a,b) -> a.manhattanDistance(b));
		for (Pt2D p: vertices) {
			area.add(p);
		}




		ArrayList<Segment> bases   = collectSegments(vertices, 0);
		ArrayList<Segment> heights = collectSegments(vertices, 1);
		// bases.forEach(System.out::println);
		// heights.forEach(System.out::println);
		
		// collect rectangles
		ArrayList<Rectangle> internalspace = new ArrayList<Rectangle>();
		for (int i  = 0; i < bases.size(); i++)
			for (int j = i + 1; j < bases.size(); j++)
				internalspace.add(bases.get(i).interceptHoriz(bases.get(j)));

		for (int i  = 0; i < heights.size(); i++)
			for (int j = i + 1; j < heights.size(); j++)
				internalspace.add(heights.get(i).interceptVert(heights.get(j)));

		internalspace.removeIf(Objects::isNull);
		// internalspace.forEach(System.out::println);
		System.out.println(":::::::::::::::");

		Long maxarea = 0l;

		// ArrayList<Rectangle> internalrectangles = new ArrayList<Rectangle>();
		// for (int i = 0; i < vertices.size(); i++) {
		// 	Pt2D pointa = vertices.get(i);
		// 	for (int j = i + 1; j < vertices.size(); j++) {
		// 		Pt2D pointb = vertices.get(j);
		// 		Rectangle actual = new Rectangle(pointa, pointb);
		// 		if (!actual.isInscribed(internalspace)) continue;
		// 		// System.out.println(actual);
		// 		maxarea = maxarea < actual.area()? actual.area(): maxarea;
		// 	}
		// }
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
		totcount = sol.mySol(input);
        System.out.printf("max area: %d", totcount);
    }
}
