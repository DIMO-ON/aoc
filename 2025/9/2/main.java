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
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;

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
			return Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + 1;
		}

		public boolean isInscribed(ArrayList<Segment> souterperimeter) {
		 	ArrayList<Segment> interceptions = new ArrayList<>(souterperimeter);
		 	boolean isincluded = interceptions.stream().anyMatch(s -> s.include(this));
		 	if (isincluded) return true;
		 	interceptions.removeIf(s -> !s.rayCast(this));
		 	return (interceptions.size() % 2) > 0;
		}

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
		
		public boolean isInscribed(ArrayList<Segment> polygon) {
			return this.perimeter().stream()
				.allMatch(p -> p.isInscribed(polygon));
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
			// assert !a.equals(b): a.toString() + " " + b.toString() + " is point, not segment";
	        // Order by x, then by y to have deterministic endpoints
			if (a.x < b.x || (a.x == b.x && a.y <= b.y)) {
				this.a = a;
				this.b = b;
			} else {
				this.a = b;
				this.b = a;
			}
			
		}


		public boolean rayCast(Pt2D p) {
			if (this.isPoint()) return a.y.equals(p.y);
			return this.isVertical() && 
				this.a.x.compareTo(p.x) > 0 &&
				this.a.y.compareTo(p.y) < 0 &&
				this.b.y.compareTo(p.y) >= 0;
		}


		public boolean isConsecutiveTo(Segment o) {
            if (this.isVertical() != o.isVertical()) return false;
			if (this.isVertical())
				return this.a.y.equals(o.b.y) || this.b.y.equals(o.a.y);
			return this.a.x.equals(o.b.x) || this.b.x.equals(o.a.x);
		}

        public boolean include(Pt2D p) {
			if (this.isVertical()) 
				return a.y.compareTo(p.y) <= 0 && b.y.compareTo(p.y) >= 0;

			return a.x.compareTo(p.x) <= 0 && b.x.compareTo(p.x) >= 0;

		}

        public boolean include(Segment o) {
            if (this.equals(o)) return true;
            if (this.isVertical() != o.isVertical()) return false;
            if (!this.a.equals(o.a.x) && !this.a.y.equals(o.a.y)) return false;
            if (!this.isVertical())
                return this.a.y.equals(o.a.y) && 
                       this.a.x.compareTo(o.a.x) <= 0 &&
                       this.b.x.compareTo(o.b.x) >= 0;
            return this.a.x.equals(o.a.x) && 
                   this.a.y.compareTo(o.a.y) <= 0 &&
                   this.b.y.compareTo(o.b.y) >= 0;
        }

		public boolean isAfter(Segment o) {
			if (this.isVertical() != o.isVertical()) return false;
			if (this.isVertical()) return this.a.x.compareTo(o.a.x) >= 0;
			return this.a.y.compareTo(o.a.y) >= 0;
		}

		public boolean isPoint() {
			return a.equals(b);
		}
		

		public boolean equals(Segment o) {
			return this.a.equals(o.a) && this.b.equals(o.b);
		}


		public boolean isAligned() {
			return a.x.equals(b.x) || a.y.equals(b.y);
		}

		public Segment cutInterception(Segment o) {
			// assert this.isVertical() != o.isVertical(): this + " " + o + "are not parallel";
			
			if (this.isVertical()) {
				return new Segment(
						this.a.compareTo(o.a) >= 0? this.a: new Pt2D(this.a.x, o.a.y),
						this.b.compareTo(o.b) <= 0? this.b: new Pt2D(this.b.x, o.b.y)
						);
			}
			return new Segment(
					this.a.compareTo(o.a) >= 0? this.a: new Pt2D(o.a.x, this.a.y),
					this.b.compareTo(o.b) <= 0? this.b: new Pt2D(o.b.x, this.b.y)
					);
		}

		public boolean intercept(Segment o) {
			if (o.isVertical() != this.isVertical()) return false;
			if (this.isVertical()) {
			    return !(o.a.y.compareTo(this.b.y) >= 0 || o.b.y.compareTo(this.a.y) <= 0);
			}
            return !(o.a.x.compareTo(this.b.x) >= 0 || o.b.x.compareTo(this.a.x) <= 0);
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

	public Map<Pt2D, Pt2D> coordCompression(ArrayList<Pt2D> pts) {
		ArrayList<Long> xset = new ArrayList<>(pts.stream()
			.map(p -> p.x)
			.collect(Collectors.toCollection(TreeSet::new)));

		ArrayList<Long> yset = new ArrayList<>(pts.stream()
			.map(p -> p.y)
			.collect(Collectors.toCollection(TreeSet::new)));

		// System.out.println(xset);
		// System.out.println(yset);

		Map<Pt2D, Pt2D> compressed = new HashMap<>();
		for (Pt2D p: pts) {
			Long compx = (long) xset.indexOf(p.x);
			Long compy = (long) yset.indexOf(p.y);
			compressed.put(p, new Pt2D(compx, compy));
		}
		return compressed;
	}

	public void printArea(List<Pt2D> polygon, Rectangle rectangle) {
		Long width  = polygon.stream().mapToLong(i -> i.x).max().getAsLong();
		Long height = polygon.stream().mapToLong(i -> i.y).max().getAsLong();
		ArrayList<StringBuilder> area = new ArrayList<>();
		for (int i = 0; i < width + 1; i++) {
			area.add(new StringBuilder());
			for (int j = 0; j < height + 1; j++) {
				area.get(i).append(' ');
			}
		}
		for (Pt2D p: polygon) {
			area.get(p.y.intValue()).setCharAt(p.x.intValue(), '#');
		}
		area.forEach(System.out::println);
	}

    public Long mySol(String input) {
		ArrayList<Pt2D> points = Arrays.stream(input.split("\n"))
			.map(i -> Arrays.stream(i.split(","))
				.map(Long::parseLong)
				.collect(Collectors.toCollection(ArrayList::new))
			)
			.map(Pt2D::new)
			.collect(Collectors.toCollection(ArrayList::new));

		// comprimere l'input
		ArrayList<Pt2D> compressed_points = new ArrayList<>();

		// mappa per comprimere
		Map<Pt2D, Pt2D> origTocomp = this.coordCompression(points);
		// mappa per decomprimere
		Map<Pt2D, Pt2D> compToorig = new HashMap<>();

		for (Pt2D p: points) {
			Pt2D compressed = origTocomp.get(p);
			// estrarre solo i punti compressi (valori) dalla mappa
			compressed_points.add(compressed);
			compToorig.put(compressed, p);
		}


		// filtrare dal perimetro compresso solo i vertici (in caso tra vertice e vertice ci sono altri punti)
		List<Pt2D> vertices = IntStream.range(0, compressed_points.size())
		   	.filter(i -> compressed_points.get(i).isVertix(compressed_points, i))
			.mapToObj(compressed_points::get)
		   	.collect(Collectors.toList());

		// System.out.println("vertices: " + vertices.size());


		// costruire il perimetro compresso sotto forma di segmenti, tra vertice e vertice
		ArrayList<Segment> sides = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			Segment s = new Segment(vertices.get(i), vertices.get((i+1) % vertices.size()));
			sides.add(s);
		}
		// System.out.println("sides: " + sides.size());

		// costruire tutti i rettangoli (compressi) possibili
		ArrayList<Rectangle> rectangles = new ArrayList<>();
		for (int i = 0; i < compressed_points.size(); i++) {
		 	for (int j = i + 1; j < compressed_points.size(); j++) {
		 		rectangles.add(new Rectangle(compressed_points.get(i), compressed_points.get(j)));
			}
		}
		System.out.println("rectangles: " + rectangles.size());
		// rectangles.forEach(r -> r.perimeter());
		// filtrare i rettangoli che non sono dentro il perimetro
		rectangles.removeIf(r -> !r.isInscribed(sides));
		System.out.println("inscribed rectangles: " + rectangles.size());

		// ordino (decrescente) i rettangoli per area (compressa)
		rectangles.sort((a, b) -> b.area().compareTo(a.area()));
		// rectangles.forEach(System.out::println);

		System.out.println("\n\n\n");

		printArea(compressed_points, rectangles.get(0));

		System.out.println("\n\n\n");


		// prendo i vertici originali del rettangolo compresso più grande
		// decomprimendo i vertici e ricalcolo il rettangolo (quindi l'area originale) con i vertici originali
		Rectangle biggest = new Rectangle(
			compToorig.get(rectangles.get(0).vertix1),
			compToorig.get(rectangles.get(0).vertix2)
		);

		return biggest.area();
    }

    public static void main(String[] args) throws IOException {
        Main sol = new Main();
		String input = Files.readString(Path.of("2025/9/input.txt"));
		String example = Files.readString(Path.of("2025/9/example.txt"));
		// String example2 = Files.readString(Path.of("2025/9/example2.txt"));

        System.out.printf("max area: %d\n\n", sol.mySol(example));
        // System.out.printf("max area: %d\n\n", sol.mySol(example2));
        // System.out.printf("max area: %d\n", sol.mySol(input));
    }
}
