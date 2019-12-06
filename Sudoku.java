import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import java.util.*;
import javafx.scene.shape.Line;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class Sudoku extends Application {

	private boolean playable = true;

	// the number of numbers in a given row
	private int numsPerEntity = 4;

	// the entire game board, used in combos. 3 types of combos: horizontal, vertical, boxes.
	// Each combo is from 0-9. Once all possible combos are complete, the game is complete. 
	private Tile[][] board = new Tile[9][9];

	private List<Combo> combos = new ArrayList<>();

	private Pane root = new Pane();

	private Tile curr;

	private Rectangle selected;

	private Line line;

	private Rectangle boxComplete;

	// to keep track of how many lines we have drawn for both horizontal/vertical lines as well as boxes.
	private int lineSize;

	// made this accessible up here in order to delete all lines and boxes when we selected clear.
	private List<Line> comboLines = new ArrayList<>();

	private List<Rectangle> comboRectangles = new ArrayList<>();



	private Parent createContent () {
		root.setPrefSize(1100, 1000);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Tile tile = new Tile();
				tile.setTranslateX(j * 100);
				tile.setTranslateY(i * 100);

				//add all tiles to the root pane so that we can see all tiles on the same pane.
				root.getChildren().add(tile);

				board[j][i] = tile;
			}
		}

		//Thicker Lines to distinguish sections of the board
		for (int i = 0; i < 4; i++) {
			Line lVert = new Line();

			root.getChildren().add(lVert);

			lVert.setStartX(i * 300);
			lVert.setStartY(0);

			lVert.setEndX(i * 300);
			lVert.setEndY(900);			
			lVert.setStrokeWidth(4);

			Line lHor = new Line();

			root.getChildren().add(lHor);

			lHor.setStartX(0);
			lHor.setStartY(i * 300);

			lHor.setEndX(900);
			lHor.setEndY(i * 300);			
			lHor.setStrokeWidth(4);
		}


		// Section for adding combinations of Xs and Os on the board

		// horizontal
		for (int y = 0; y < 9; y++){
			combos.add(new Combo(board[0][y], board[1][y], board[2][y], board[3][y], 
				board[4][y], board[5][y], board[6][y], board[7][y], board[8][y]));
		}

		// vertical
		for (int x = 0; x < 9; x++){
			combos.add(new Combo(board[x][0], board[x][1], board[x][2], board[x][3], 
				board[x][4], board[x][5], board[x][6], board[x][7], board[x][8]));
		}

		// box
		combos.add(new Combo(board[0][0], board[1][0], board[2][0], 
			board[0][1], board[1][1], board[2][1],
			board[0][2], board[1][2], board[2][2]));

		combos.add(new Combo(board[3][0], board[4][0], board[5][0], 
			board[3][1], board[4][1], board[5][1],
			board[3][2], board[4][2], board[5][2]));

		combos.add(new Combo(board[6][0], board[7][0], board[8][0], 
			board[6][1], board[7][1], board[8][1],
			board[6][2], board[7][2], board[8][2]));

		combos.add(new Combo(board[0][3], board[1][3], board[2][3], 
			board[0][4], board[1][4], board[2][4],
			board[0][5], board[1][5], board[2][5]));

		combos.add(new Combo(board[3][3], board[4][3], board[5][3], 
			board[3][4], board[4][4], board[5][4],
			board[3][5], board[4][5], board[5][5]));

		combos.add(new Combo(board[6][3], board[7][3], board[8][3], 
			board[6][4], board[7][4], board[8][4],
			board[6][5], board[7][5], board[8][5]));

		combos.add(new Combo(board[0][6], board[1][6], board[2][6], 
			board[0][7], board[1][7], board[2][7],
			board[0][8], board[1][8], board[2][8]));

		combos.add(new Combo(board[3][6], board[4][6], board[5][6], 
			board[3][7], board[4][7], board[5][7],
			board[3][8], board[4][8], board[5][8]));

		combos.add(new Combo(board[6][6], board[7][6], board[8][6], 
			board[6][7], board[7][7], board[8][7],
			board[6][8], board[7][8], board[8][8]));

		//buttons 
		Button button1 = new Button(" 1 ");
		Button button2 = new Button(" 2 ");
		Button button3 = new Button(" 3 ");
		Button button4 = new Button(" 4 ");
		Button button5 = new Button(" 5 ");
		Button button6 = new Button(" 6 ");
		Button button7 = new Button(" 7 ");
		Button button8 = new Button(" 8 ");
		Button button9 = new Button(" 9 ");

		Button checkCombos = new Button(" Check ");

		Button createBoard = new Button(" New Board ");

		button1.setMinWidth(50);
		button1.setMinHeight(75);
		button2.setMinWidth(50);
		button2.setMinHeight(75);
		button3.setMinWidth(50);
		button3.setMinHeight(75);	
		button4.setMinWidth(50);
		button4.setMinHeight(75);
		button5.setMinWidth(50);
		button5.setMinHeight(75);
		button6.setMinWidth(50);
		button6.setMinHeight(75);
		button7.setMinWidth(50);
		button7.setMinHeight(75);
		button8.setMinWidth(50);
		button8.setMinHeight(75);
		button9.setMinWidth(50);
		button9.setMinHeight(75);

		checkCombos.setMinWidth(75);
		checkCombos.setMinHeight(75);

		createBoard.setMinWidth(100);
		createBoard.setMinHeight(100);
		
		
		button1.setLayoutX(910);
		button1.setLayoutY(400);
		button2.setLayoutX(960);
		button2.setLayoutY(400);
		button3.setLayoutX(1010);
		button3.setLayoutY(400);
		button4.setLayoutX(910);
		button4.setLayoutY(480);
		button5.setLayoutX(960);
		button5.setLayoutY(480);
		button6.setLayoutX(1010);
		button6.setLayoutY(480);
		button7.setLayoutX(910);
		button7.setLayoutY(560);
		button8.setLayoutX(960);
		button8.setLayoutY(560);
		button9.setLayoutX(1010);
		button9.setLayoutY(560);

		checkCombos.setLayoutX(910);
		checkCombos.setLayoutY(300);

		createBoard.setLayoutX(910);
		createBoard.setLayoutY(200);

		root.getChildren().add(button1);
		root.getChildren().add(button2);
		root.getChildren().add(button3);
		root.getChildren().add(button4);
		root.getChildren().add(button5);
		root.getChildren().add(button6);
		root.getChildren().add(button7);
		root.getChildren().add(button8);
		root.getChildren().add(button9);
		root.getChildren().add(checkCombos);
		root.getChildren().add(createBoard);

		button1.setOnAction(value ->  {
				if (curr == null || !playable)
					return;
           		addOne(curr);
        	});
		button2.setOnAction(value ->  {
				if (curr == null || !playable)
					return;
           		addTwo(curr);
        	});
		button3.setOnAction(value ->  {
				if (curr == null || !playable)
						return;
           		addThree(curr);
        	});
		button4.setOnAction(value ->  {
				if (curr == null || !playable)
					return;
           		addFour(curr);
        	});
		button5.setOnAction(value ->  {
				if (curr == null || !playable)
					return;
           		addFive(curr);
        	});
		button6.setOnAction(value ->  {
				if (curr == null || !playable)
					return;
           		addSix(curr);
        	});
		button7.setOnAction(value ->  {
				if (curr == null || !playable)
					return;
           		addSeven(curr);
        	});
		button8.setOnAction(value ->  {
				if (curr == null || !playable)
					return;
           		addEight(curr);
        	});
		button9.setOnAction(value ->  {
				if (curr == null || !playable)
					return;
           		addNine(curr);
        	});

		checkCombos.setOnAction(value -> {
				if (!playable)
					return;
				checkState();
		});

		createBoard.setOnAction(value ->  {
           		createNewBoard();
        	});


		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
	}

	private void createNewBoard() {
		

		//another hashtable for locations of a given row
		Hashtable<Integer, Boolean> locations = new Hashtable<Integer, Boolean>();

		for (int i = 0; i < 9; i++) {
			locations.put(0, false);
			locations.put(1, false);
			locations.put(2, false);
			locations.put(3, false);
			locations.put(4, false);
			locations.put(5, false);
			locations.put(6, false);
			locations.put(7, false);
			locations.put(8, false);
				

			Random rand = new Random();

			for (int j = numsPerEntity; j > 0; j--){
				// location (0-8) of a given value in a given row/column.
				int boardLocation = rand.nextInt(9);

				//until we find a new location to place a number, keep generating a new number
				while (locations.get(boardLocation) == true) {
					boardLocation = rand.nextInt(9);
				}
				locations.put(boardLocation, true);
				

				
				// number for a given square
				int nextVal = rand.nextInt(9);

				
				
				boolean foundFalse = false;

				while (!foundFalse){

						// make sure we haven't already inserted a number, and that the number entry would keep the board correct
						nextVal = rand.nextInt(9);
					board[i][boardLocation].val = nextVal + 1;
					boolean internal = false;
					for (Combo c : combos){
						if (!c.checkEntry()) {
							foundFalse = false;
							internal = true;
						}
					}			
					if (!internal) foundFalse = true;
					System.out.println(internal);
				}
				board[i][boardLocation].val = nextVal + 1;
				board[i][boardLocation].text.setText(Integer.toString(nextVal + 1));
				

			}




		}
		return;
	}


	private void checkState() {
		lineSize = 0;

		for (Line l : comboLines) {
			root.getChildren().remove(l);
		}

		for (Rectangle r : comboRectangles) {
			root.getChildren().remove(r);
		}
		for (Combo combo : combos) {
			if (combo.isComplete()) {

				// animation when the combination is complete
				playWinAnimation(combo);
				lineSize++;
			}
		}
		System.out.println("number of comboline and rectangles: ");
		System.out.println(lineSize);
		System.out.println();
		//check if the game is over (9 rows, 9 columns, 9 boxes)
		if (lineSize == 27) {

			for (Line l : comboLines) {
				root.getChildren().remove(l);
			}

			for (Rectangle r : comboRectangles) {
				root.getChildren().remove(r);
			}
			
			board[1][3].text.setText("P");
			board[2][3].text.setText("U");
			board[3][3].text.setText("Z");
			board[4][3].text.setText("Z");
			board[5][3].text.setText("L");
			board[6][3].text.setText("E");

			board[0][4].text.setText("C");
			board[1][4].text.setText("O");
			board[2][4].text.setText("M");
			board[3][4].text.setText("P");
			board[4][4].text.setText("L");
			board[5][4].text.setText("E");
			board[6][4].text.setText("T");
			board[7][4].text.setText("E");
			board[8][4].text.setText("!");

			playable = false;
		}
	}

	// played when the game is over, passed in the winning combination 
	private void playWinAnimation(Combo c){

		// draw a square if a box
		if (c.tiles[8].getCenterX() != c.tiles[0].getCenterX() && c.tiles[8].getCenterY() != c.tiles[0].getCenterY()) {
			boxComplete = new Rectangle(200, 200);
			comboRectangles.add(boxComplete);
			root.getChildren().add(boxComplete);

			boxComplete.setStroke(Color.GREEN);
			boxComplete.setStrokeWidth(3);

			boxComplete.setFill(null);
			boxComplete.setTranslateX(c.tiles[0].getCenterX());
			boxComplete.setTranslateY(c.tiles[0].getCenterY());

			

			return;
		}

		line = new Line();
		comboLines.add(line);
		root.getChildren().add(line);

		line.setStroke(Color.GREEN);
		line.setStrokeWidth(3);

		line.setStartX(c.tiles[0].getCenterX());
		line.setStartY(c.tiles[0].getCenterY());
		line.setEndX(c.tiles[0].getCenterX());
		line.setEndY(c.tiles[0].getCenterY());

		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), 
			new KeyValue(line.endXProperty(), c.tiles[8].getCenterX()), 
			new KeyValue(line.endYProperty(), c.tiles[8].getCenterY())));
		timeline.play();

		return;
	}

	private class Combo {
		private Tile[] tiles;
		public Combo(Tile... tiles){
			this.tiles = tiles;
		}

		// checking if the game is over, ie if there is a combo of 3 in a row(diagonal, vertical, or horizontal)
		public boolean isComplete() {
			
			// if empty, cannot be a completed row/column. 
			for (int i = 0; i < 9; i++){
				if (tiles[i].getValue().isEmpty()) {
					return false;
				}
			}


			// to keep track of each number in a row, column, or box
			Hashtable<Integer, Boolean> numbers = new Hashtable<Integer, Boolean>();

			numbers.put(1, false);
			numbers.put(2, false);
			numbers.put(3, false);
			numbers.put(4, false);
			numbers.put(5, false);
			numbers.put(6, false);
			numbers.put(7, false);
			numbers.put(8, false);
			numbers.put(9, false);

			// here we check that each entry has not yet been seen in the dictionary, if it has been seen we return false!
			for (int j = 0; j < 9; j++){
				int curr = tiles[j].val;
				if (numbers.get(curr) == true) {
					return false;

				}
				else numbers.put(curr, true);
			}

			return true;
		}

		public boolean checkEntry () {
			for (Combo c : combos){
				// to keep track of each number in a row, column, or box
				Hashtable<Integer, Boolean> entries = new Hashtable<Integer, Boolean>();

				entries.put(1, false);
				entries.put(2, false);
				entries.put(3, false);
				entries.put(4, false);
				entries.put(5, false);
				entries.put(6, false);
				entries.put(7, false);
				entries.put(8, false);
				entries.put(9, false);

				// here we check that each entry has not yet been seen in the dictionary, if it has been seen we return false!
				//System.out.println("------------------------------------------------------");
				for (int j = 0; j < 9; j++){
					//System.out.println(tiles[j].val);
					if (tiles[j].val == 0)
						continue;
					int curr = tiles[j].val;
					
					
					if (entries.get(curr) == true) {
						return false;

					}
					else entries.put(curr, true);
				}

				
			}
			return true;
		}
	}

	private class Tile extends StackPane {
		private Text text = new Text();
		private int val;


		public Tile() {
			Rectangle border = new Rectangle(100, 100);
			border.setFill(null);
			border.setStroke(Color.BLACK);

			text.setFont(Font.font(72));

			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);

			setOnMouseClicked(event -> {
					if (!playable)
						return;

					if (event.getButton() == MouseButton.PRIMARY) {
						drawX();
						// checkState();
					}
			});
		
		}

		

		// these methods get the center of the X/Y (+100 because rectangle is size 200)
		public double getCenterX(){
			return getTranslateX() + 50;
		}

		public double getCenterY(){
			return getTranslateY() + 50;
		}

		public String getValue() {
			return text.getText();
		}

		private void drawX() {
			root.getChildren().remove(selected);

			// line to highlight a box
			selected = new Rectangle(100, 100);

			root.getChildren().add(selected);
			selected.setStroke(Color.ORANGE);
			selected.setFill(null);
			selected.setTranslateX(getCenterX()-50);
			selected.setTranslateY(getCenterY()-50);

			selected.setStrokeWidth(5);
			curr = this;
			
		}

	}

	public void addOne(Tile t) {
			t.text.setText("1");
			t.val = 1;
			return;
		}
	public void addTwo(Tile t) {
			t.text.setText("2");
			t.val = 2;
			return;
		}
	public void addThree(Tile t) {
			t.text.setText("3");
			t.val = 3;
			return;
		}
	public void addFour(Tile t) {
			t.text.setText("4");
			t.val = 4;
			return;
		}
	public void addFive(Tile t) {
			t.text.setText("5");
			t.val = 5;
			return;
		}
	public void addSix(Tile t) {
			t.text.setText("6");
			t.val = 6;
			return;
		}
	public void addSeven(Tile t) {
			t.text.setText("7");
			t.val = 7;
			return;
		}
	public void addEight(Tile t) {
			t.text.setText("8");
			t.val = 8;
			return;
		}
	public void addNine(Tile t) {
			t.text.setText("9");
			t.val = 9;
			return;
		}

	public static void main(String[] args) {
		launch(args);		
	}
	
}