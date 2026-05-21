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
		
		public boolean isInscribed(ArrayList<Pt2D> area) {
			return this.vertices().stream().allMatch(v -> area.contains(v));
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

	private class Pt2D {
		public Long x, y;
		public boolean red = true;

		public Pt2D(Long x, Long y) {
			this.x = x;
			this.y = y;
		}

		public Pt2D(Long x, Long y, boolean red) {
			this.x = x;
			this.y = y;
			this.red = red;
		}

		public Pt2D(ArrayList<Long> l) {
			this.x = l.get(0);
			this.y = l.get(1);
		}

		public Long manhattanDistance(Pt2D other) {
			if (this.x != other.x && this.y != other.y) return 0l;
			return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
		}

		public Pt2D next(ArrayList<Pt2D> vertices) {
			// ArrayList<Pt2D> nextycloser = vertices.filter(i -> i.y == this.y);
			return null;
		}

		@Override
		public String toString() {
			return "{x=" + x + ", y=" + y + ", red=" + this.red + "}";
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

			Pt2D lupper = left.a.y >= right.a.y? left.a: new Pt2D(left.a.x, right.a.y, false);
			Pt2D rbotto = right.b.y <= left.b.y? right.b: new Pt2D(right.b.x, left.b.y, false);

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

		// vertices.sort((a, b) -> a.x.compareTo(b.x));
		// vertices.sort((a, b) -> a.y.compareTo(b.y));
		// vertices.forEach(System.out::println);
		ArrayList<Pt2D> perimeter = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			Pt2D actual = vertices.get(i);
			perimeter.add(actual);
			Pt2D next = vertices.get(i);
			if (i + 1 >= vertices.size()) {
				next = vertices.get(0);
			} else {
				next = vertices.get(i + 1);
			}
			if (actual.x.compareTo(next.x) == 0) {
				Long cond = actual.y.compareTo(next.y) <= 0? 1l: -1l;
				for (Long y = actual.y + cond; y.compareTo(next.y) != 0; y += cond)
					perimeter.add(new Pt2D(actual.x, y, false));
			} else if (actual.y.compareTo(next.y) == 0) {
				Long cond = actual.x.compareTo(next.x) <= 0? 1l: -1l;
				for (Long x = actual.x + cond; x.compareTo(next.x) != 0; x += cond)
					perimeter.add(new Pt2D(x, actual.y, false));
			}
		}
		// perimeter.forEach(System.out::println);
		
		System.out.println(":::::::::::::::");

		Long maxarea = 0l;

		for (int i = 0; i < vertices.size(); i++) {
			Pt2D actual = vertices.get(i);
			Pt2D next = vertices.get((i + 2) % vertices.size());

			Rectangle r = new Rectangle(actual, next);
			System.out.println(r);
			System.out.println(r.isInscribed(perimeter));
			if (r.isInscribed(perimeter)) maxarea = maxarea.compareTo(r.area()) < 0? r.area(): maxarea;  
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
