import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
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

class Main {
	private class Rectangle {
		public Segment base;
		public Segment height;

		@Override
		public String toString() {
			return "base   = " + base.toString() + " " +
				   "height = " + height.toString();
		}
	}

	private class TDpt {
		public Long x, y;
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

		public Segment(TDpt a, TDpt b) {
			this.a = a;
			this.b = b;
		}

		public Rectangle interceptHoriz(Segment other) {
			if (this.a.x != other.a.x && this.a.y != other.a.y) return null;
			if (this.b.x < other.a.x || this.a.x > other.b.x) return null;
			if (this.b.x < other.a.x || this.a.x > other.b.x) return null;
			Long ax = this.a.x > other.a.x? this.a.x: other.a.x;
			Long bx = this.b.x < other.b.x? this.b.x: other.b.x;

			TDpt newa = new TDpt(ax, other.a.y);
			TDpt newb = new TDpt(bx, other.a.y);
			if (this.a.y > other.a.y) {
				Segment base   = new Segment(newa, newb);
				Segment height = new Segment(this.a, this.b);
			} else {
				Segment base   = new Segment(newa, newb);
				Segment height = new Segment(newa, newb);
			}
			return null;
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
				if (coordinate == 0)
					if (a.x.equals(b.x)) col.add(new Segment(a, b));
				if (coordinate == 1)
					if (a.y.equals(b.y)) col.add(new Segment(a, b));
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

		// vertices.forEach(System.out::println);

		ArrayList<Segment> bases = collectSegments(vertices, 0);
		ArrayList<Segment> heights = collectSegments(vertices, 1);
		// bases.forEach(System.out::println);
		// heights.forEach(System.out::println);
		
		// collect rectangles
		ArrayList<Rectangle> internalspace = new ArrayList<Rectangle>();




		Long maxarea = 0l;
		

		// for (int i = 0; i < vertices.size(); i++) {
		// 	ArrayList<Long> pointa = vertices.get(i);
		// 	for (int j = i + 1; j < vertices.size(); j++) {
		// 		ArrayList<Long> pointb = vertices.get(j);
		// 		Long height = Math.abs(pointa.get(1) - pointb.get(1) + 1);
		// 		Long base   = Math.abs(pointa.get(0) - pointb.get(0) + 1);
		// 		Long actualarea = height * base;
		// 		maxarea = maxarea < actualarea? actualarea: maxarea; 
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
		// totcount = sol.mySol(input);
        System.out.printf("max area: %d", totcount);
    }
}
