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
	private class Pt2D {
		public Long x, y;

		public Pt2D(Long x, Long y) {
			this.x = x;
			this.y = y;
		}

		public Pt2D(ArrayList<Long> l) {
			this.x = l.get(0);
			this.y = l.get(1);
		}

		public boolean isVertex(Collection<Pt2D> perimeter, int i) {
			Pt2D prev = perimeter.get((i - 1) % perimeter.size());
			Pt2D next = perimeter.get((i + 1) % perimeter.size());

			return prev.y == this.y && next.y != this.y ||
				   prev.y != this.y && next.y == this.y; 
		}

		public Long manhattanDistance(Pt2D other) {
			if (this.x != other.x && this.y != other.y) return 0l;
			return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
		}

		@Override
		public String toString() {
			return "{x=" + x + ", y=" + y + ", red=" + this.red + "}";
		}
	}

	private class Rectangle {
		public Pt2D vertix1;
		public Pt2D vertix2;

		public Rectangle(Pt2D v1, Pt2D v2) {
			this.vertix1 = v1;
			this.vertix2 = v2;
		}

		public boolean isSegment() {
			return vertix1.x.compareTo(vertix2.x) == 0 ||
				   vertix1.y.compareTo(vertix2.y) == 0;
		}
		
		public List<Pt2D> perimeter() {
			Pt2D cursor1 = vertex1.copy();
			Pt2D cursor2 = vertex2.copy();
			ArrayList<Pt2D> side1 = new ArrayList<>();
			ArrayList<Pt2D> side2 = new ArrayList<>();
			while (true) {
				if (cursor1.equals(vertex2) && cursor2.equals(vertex1)) break;
				if (!cursor1.equals(vertex2)) side1.add(cursor1);
				if (!cursor2.equals(vertex1)) side2.add(cursor2);

				if (!cursor1.x.equals(vertex2.x)) {
					cursor1.x += cursor1.x < vertex2.x? 1: -1;
				} else if (!cursor1.y.equals(vertex2.y)) {
					cursor1.y += cursor1.x < vertex2.y? 1: -1;
				}

				if (!cursor2.x.equals(vertex1.x)) {
					cursor2.x += cursor2.x < vertex1.x? 1: -1;
				} else if (!cursor2.y.equals(vertex1.y)) {
					cursor2.y += cursor2.x < vertex1.y? 1: -1;
				}
			}

			side1.forEach(System.out::println);
			side2.forEach(System.out::println);

			return side1.addAll(side2);
		}

		public List<Pt2D> vertices() {
			if (this.isSegment()) {
				return Arrays.asList( 
					vertix1,
					vertix2	
					);
			}

			return Arrays.asList( 
				vertix1,
				vertix2,
				new Pt2D(vertix1.x, vertix2.y),
				new Pt2D(vertix2.x, vertix1.y)
				);
		}
		
		public boolean isInscribed(ArrayList<VerticalSegment> outerperimeter) {
			return this.perimeter().stream().matchAll(p -> p.red ||
					outerperimeter.stream().matchAny());
		}

		// public boolean includes(Pt2D p) {
		// 	return p.x.compareTo(leftuppervertix.x) >= 0 && p.x.compareTo(rightbottomvertix.x) <= 0 &&
		// 		   p.y.compareTo(leftuppervertix.y) >= 0 && p.y.compareTo(rightbottomvertix.y) <= 0;
		// }
		//


		public Long area() {
			if (this.isSegment()) return this.vertix1.manhattanDistance(this.vertix2);
			return (Math.abs(this.vertix1.x - this.vertix2.x) + 1) *
				   (Math.abs(this.vertix1.y - this.vertix2.y) + 1);
		}

		@Override
		public String toString() {
			return "[vertix1 = "   + vertix1.toString() + " " + this.isSegment() + 
				   "vertix2 = " + vertix2.toString() + "] - area: " + this.area();
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
		ArrayList<Pt2D> perimeter = Arrays.stream(input.split("\n"))
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong)
				.collect(Collectors.toCollection(ArrayList::new))
			)
			.map(Pt2D::new)
			.collect(Collectors.toCollection(ArrayList::new));

		// perimeter.sort((a, b) -> a.x.compareTo(b.x));
		// perimeter.sort((a, b) -> a.y.compareTo(b.y));
		perimeter.forEach(System.out::println);

		// ArrayList<Rectangle> rectangles = new ArrayList<>();
		// for (int i = 0; i < perimeter.size(); i++) {
		// 	for (int j = i + 1; j < perimeter.size(); j++) {
		// 		rectangles.add(new Rectangle(perimeter.get(i), perimeter.get(j)));
		// 	}
		// }
		
		
		System.out.println(":::::::::::::::");

		Long maxarea = 0l;

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
