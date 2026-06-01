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
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

class Main {
	private class Pt2D implements Comparable<Pt2D> {
		public Long x, y;

		@Override
		public int compareTo(Pt2D o) {
			int cmp = this.x.compareTo(o.x);
			return (cmp != 0) ? cmp : this.y.compareTo(o.y);
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof Pt2D)) return false;
			Pt2D o = (Pt2D) obj;
			return x.equals(o.x) && y.equals(o.y);
		}

		@Override
		public int hashCode() {
			return 31 * x.hashCode() + y.hashCode();
		}

		public Pt2D(Pt2D o) {
			this.y = o.y;
			this.x = o.x;
		}

		public Pt2D(Long x, Long y) {
			this.x = x;
			this.y = y;
		}

		public Pt2D(ArrayList<Long> l) {
			this.x = l.get(0);
			this.y = l.get(1);
		}

		public boolean isVertix(ArrayList<Pt2D> perimeter, int i) {
			Pt2D prev = perimeter.get(Math.floorMod(i - 1, perimeter.size()));
			Pt2D next = perimeter.get((i + 1) % perimeter.size());

			return prev.y.equals(this.y) && !next.y.equals(this.y) ||
				   !prev.y.equals(this.y) && next.y.equals(this.y); 
		}

		public Long manhattanDistance(Pt2D other) {
			return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
		}

		// public boolean isInscribed(ArrayList<Segment> souterperimeter) {
		// 	ArrayList<Segment> interceptions = new ArrayList<>(souterperimeter);
		// 	boolean isincluded = interceptions.stream().anyMatch(s -> s.include(this));
		// 	if (isincluded) return true;
		// 	interceptions.removeIf(s -> !s.interceptVertically(this));
		// 	return (interceptions.size() % 2) > 0;
		// }

		@Override
		public String toString() {
			return "{x=" + x + ", y=" + y + "}";
		}
	}

	private class Rectangle {
		public Pt2D vertix1;
		public Pt2D vertix2;

		public Rectangle(Pt2D v1, Pt2D v2) {
			this.vertix1 = v1.compareTo(v2) <= 0? v1: v2;
			this.vertix2 = v2.compareTo(v1) <= 0? v1: v2;
		}

		public boolean isSegment() {
			return vertix1.x.compareTo(vertix2.x) == 0 ||
				   vertix1.y.compareTo(vertix2.y) == 0;
		}

		public ArrayList<Segment> sides() {
			ArrayList<Segment> sides = new ArrayList<>();
			ArrayList<Pt2D> vertices = this.vertices();
			for (int i = 0; i < vertices.size(); i++) {
				Segment side = new Segment(vertices.get(i), vertices.get((i + 1) % vertices.size()));
				sides.add(side);
			}
			return sides;
		}
		
		public ArrayList<Pt2D> perimeter() {
			Pt2D cursor1 = new Pt2D(vertix1);
			Pt2D cursor2 = new Pt2D(vertix2);
			HashSet<Pt2D> perimeter = new HashSet<>();
			while (true) {
				if (cursor1.equals(vertix2) && cursor2.equals(vertix1)) break;
				perimeter.add(new Pt2D(cursor1));
				perimeter.add(new Pt2D(cursor2));

				if (!cursor1.x.equals(vertix2.x)) {
					cursor1.x += cursor1.x < vertix2.x? 1: -1;
				} else if (!cursor1.y.equals(vertix2.y)) {
					cursor1.y += cursor1.y < vertix2.y? 1: -1;
				}

				if (!cursor2.x.equals(vertix1.x)) {
					cursor2.x += cursor2.x < vertix1.x? 1: -1;
				} else if (!cursor2.y.equals(vertix1.y)) {
					cursor2.y += cursor2.y < vertix1.y? 1: -1;
				}
			}

			// System.out.println(perimeter);

			return new ArrayList<>(perimeter);
		}

		public ArrayList<Pt2D> vertices() {
			if (this.isSegment()) {
				return new ArrayList<Pt2D>(Arrays.asList( 
					vertix1,
					vertix2	
					));
			}

			return new ArrayList<Pt2D>(Arrays.asList( 
				vertix1,
				new Pt2D(vertix2.x, vertix1.y),
				vertix2,
				new Pt2D(vertix1.x, vertix2.y)
				));
		}
		
		public boolean isInscribed(ArrayList<Segment> outerperimeter) {
			// boolean r = this.vertices().stream()
			// boolean r = this.perimeter().stream()
			boolean r = this.sides().stream()
				.allMatch(s -> s.isInscribed(outerperimeter));
			return r;
		}

		public Long area() {
			if (this.isSegment()) return this.vertix1.manhattanDistance(this.vertix2);
			return (Math.abs(this.vertix1.x - this.vertix2.x) + 1) *
				   (Math.abs(this.vertix1.y - this.vertix2.y) + 1);
		}

		@Override
		public String toString() {
			return "[vertix1 = "   + vertix1.toString() + " " +
				   "vertix2 = " + vertix2.toString() +
				   "] - area: " + this.area();
		}
	}


	private class Segment implements Comparable<Segment> {
		public Pt2D a, b;

		public Segment(Pt2D a, Pt2D b) {
			assert !a.equals(b): a.toString() + " " + b.toString() + " is point, not segment";
	        // Order by x, then by y to have deterministic endpoints
			if (a.x < b.x || (a.x == b.x && a.y <= b.y)) {
				this.a = a;
				this.b = b;
			} else {
				this.a = b;
				this.b = a;
			}
			
		}

		public boolean isConsecutiveTo(Segment o) {
			if (this.isVertical())
				return this.a.y.equals(o.b.y) || this.b.y.equals(o.a.y);
			return this.a.x.equals(o.b.x) || this.b.x.equals(o.a.x);
		}
		
		public boolean isInscribed(ArrayList<Segment> souterperimeter) {
			boolean isincluded = souterperimeter.stream().anyMatch(s -> s.equals(this));
			// System.out.println(this + "is included :" + isincluded);
			if (isincluded) return true;
			// ArrayList<Segment> interceptions = new ArrayList<>(souterperimeter);
			ArrayList<Segment> interceptions = new ArrayList<>();
			// filtra quelli intercettati

			for (Segment s: souterperimeter) {
				if (this.isVertical() && !s.isVertical()) continue;
				// boolean cond = this.rayCollide(s);
				// System.out.println(this + " " + this.isVertical() + " collide with " + s + cond);
				if (!this.rayCollide(s)) continue;
				if (!interceptions.isEmpty())
					if (s.isConsecutiveTo(interceptions.get(interceptions.size() - 1)))
						continue;
				interceptions.add(s);
			}
			// interceptions.removeIf(s -> !s.isVertical());
			// System.out.println(interceptions);
			// interceptions.forEach(s -> s.interceptVertically(this));
			// interceptions.removeIf(s -> !s.intercept(this));
			// System.out.println("intercepts " + ((interceptions.size() % 2) > 0) + "  " + interceptions);
			return (interceptions.size() % 2) > 0;
		}

		public boolean equals(Segment o) {
			return this.a.equals(o.a) && this.b.equals(o.b);
		}


		public boolean isAligned() {
			return a.x.equals(b.x) || a.y.equals(b.y);
		}

		public boolean rayCollide(Segment o) {
			if (o.isVertical() != this.isVertical()) return false;
			if (!this.isVertical()) {
				if (this.a.y.compareTo(o.a.y) >= 0) return false;
				return !(o.a.x.compareTo(this.b.x) > 0 || o.b.x.compareTo(this.a.x) < 0);
			}
			if (this.a.x.compareTo(o.a.x) >= 0) return false;
			return !(o.a.y.compareTo(this.b.y) > 0 || o.b.y.compareTo(this.a.y) < 0);
		}

		@Override
		public int compareTo(Segment o) {
			if (this.equals(o)) return 0;
			return -1;
		}

		public boolean isVertical() {
			return a.x.equals(b.x);
		}

		@Override
		public String toString() {
			return "a = " + a.toString() + " " +
				   "b = " + b.toString();
		}
	}


    public Long mySol(String input) {
		ArrayList<Pt2D> perimeter = Arrays.stream(input.split("\n"))
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong)
				.collect(Collectors.toCollection(ArrayList::new))
			)
			.map(Pt2D::new)
			.collect(Collectors.toCollection(ArrayList::new));

		// filtrare dal perimetro solo i vertici (tra vertice e vertice ci possono essere altri punti)
		List<Pt2D> vertices = IntStream.range(0, perimeter.size())
			.filter(i -> perimeter.get(i).isVertix(perimeter, i))
			.mapToObj(perimeter::get)
			.collect(Collectors.toList());

		System.out.println("vertices: " + vertices.size());


		// costruire il perimetro sotto forma di segmenti, tra vertice e vertice
		ArrayList<Segment> sides = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			Segment s = new Segment(vertices.get(i), vertices.get((i+1) % vertices.size()));
			sides.add(s);
		}

		// costruire tutti i rettangoli possibili
		ArrayList<Rectangle> rectangles = new ArrayList<>();
		for (int i = 0; i < perimeter.size(); i++) {
		 	for (int j = i + 1; j < perimeter.size(); j++) {
		 		rectangles.add(new Rectangle(perimeter.get(i), perimeter.get(j)));
			}
		}
		System.out.println("rectangles: " + rectangles.size());
		// rectangles.forEach(r -> r.sides().size());
		// rectangles.forEach(r -> System.out.println(r.perimeter().size()));
		// rectangles.forEach(r -> r.perimeter());
		// filtrare i rettangoli che non sono dentro il perimetro
		rectangles.removeIf(r -> !r.isInscribed(sides));
		System.out.println("inscribed rectangles: " + rectangles.size());
		rectangles.forEach(System.out::println);

		
		// 7,1 11,7
		// Rectangle prova = new Rectangle(new Pt2D(7l, 1l), new Pt2D(11l, 7l));
		// Rectangle prova = new Rectangle(new Pt2D(2l, 3l), new Pt2D(9l, 5l));
		// System.out.println("sides:");
		// prova.sides().forEach(System.out::println);
		// System.out.println(prova);
		// System.out.println("is inscribed:");
		// System.out.println(prova.isInscribed(sides));
		// System.out.println(":::::::::::::::");

		Long maxarea = 0l;

		for (Rectangle r: rectangles) {
			maxarea = maxarea.compareTo(r.area()) < 0? r.area(): maxarea;
		}

		return maxarea;
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/9/input.txt"));
		String example = Files.readString(Path.of("2025/9/example.txt"));

        Long totcount = 0l;
        totcount = sol.mySol(example);
        System.out.printf("max area: %d\n\n", totcount);
		// totcount = sol.mySol(input);
        System.out.printf("max area: %d\n", totcount);
    }
}
