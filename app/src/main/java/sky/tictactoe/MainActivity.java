package sky.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.id.empty;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static sky.tictactoe.R.drawable.board;

public class MainActivity extends AppCompatActivity {

    // Variable to indicate first player or 2nd player
    // 1st player = 0 , 2nd player = 1
    // 1st player = red, 2nd player = yellow
    int activePlayer = 0;

    // 2 means unplayed
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    // Winning positions
    int[][] winningPos = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    RelativeLayout layout;
    GridLayout gridLayout;

    boolean gameIsActive = true;
    boolean gameIsOver = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout)findViewById(R.id.playAgainLayout);
        gridLayout = (GridLayout)findViewById(R.id.gridLayout);
    }

    public void newGame(View view){

        gameIsActive = true;

        layout.setVisibility(View.INVISIBLE);

        activePlayer = 0;

        for (int i = 0; i < gameState.length; i++){
            gameState[i] = 2;
        }
        // Get the number of childs inside GridLayout
        // reset the image resource of each imageview
        for (int i = 0; i < gridLayout.getChildCount(); i++){
            ((ImageView)gridLayout.getChildAt(i)).setImageResource(0);
            ((ImageView)gridLayout.getChildAt(i)).setClickable(true);
        }
    }

    public void gridClicked(View view){
        // Finding imageview
        ImageView img = (ImageView)view;

        // Set the image to be out of bound for animation purpose
        img.setTranslationY(-1000f);

        // Getting tag of the imageview
        int counter = Integer.parseInt(img.getTag().toString());

        // Refer imageview tag as a position of the gamestate array
        // and check if the value is 2
        if(gameState[counter] == 2 && gameIsActive) {
            // change the gamestate to 0 or 1 to prevent further clicking procedure
            gameState[counter] = activePlayer;

            // Check wether first or second player
            if (activePlayer == 0) {
                img.setImageResource(R.drawable.red);
                img.animate().translationYBy(1000f).rotationYBy(360).setDuration(500);
                img.setClickable(false);
                activePlayer = 1;
            } else {
                img.setImageResource(R.drawable.yellow);
                img.animate().translationYBy(1000f).rotationXBy(360).setDuration(500);
                img.setClickable(false);
                activePlayer = 0;
            }
            /**Firstly, for (int[] winningPosition : winningPos) .
             * The syntax for (a : B)  means for each item in the collection B,
             * you pick out each one individually to work on and you call the one you are working on a.
             * So you are picking out each possible winningPos and see if each winningPosition
             * has all the pieces in those 3 spots having the same colour.   int[]  means an integer array eg {0, 1, 2}.
             * So when winningPosition = { 0, 1, 2}  you are checking if the pieces in those spots are of the same colour.
             * I think in Rob's video 2  means the spot is empty. So if {0, 1, 2}  have all 3 spots equal 2
             * then they are just all blank without pieces so that should not indicate anyone has won. That is what the last part of the following is doing:
             * gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2
             * Recall gameState[] = { a, b, c, d, e, f, g, h, i}  represents the 9 spots in the tic-tac-toe board. So using winningPosition = { 0, 1, 2}
             * still as an example, gameState[winningPosition[0]]  is just gameState[0]  which in this case would be a .
             * Similarly, gameState[winningPosition[1]]  is just gameState[1]  which would be b  and gameState[winningPosition[2]]  is just gameState[2]  or c .
             * So you are checking if a == b  and b == c  which if they both are true then a == b == c .
             * But you want to make sure one of them (which means all of them since they are all the same) is NOT a 2 .
             * So when you cycle to the next item in winningPos say { 3, 4, 5}  you would be checking if d == e  and e ==f  and f !=2 .
             * Etc, etc, until you have gone through all the winningPos  to see if any one of those have all 3 pieces the same colour AND not empty.**/

            for(int[] winningPosition : winningPos){
                if(gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2){

                    gameIsActive = false;

                    String winner = "Yellow";
                    //check if the value is 0 or 1
                    if(gameState[winningPosition[0]] == 0){
                        winner = "Red";
                    }
                        TextView winnerMessage = (TextView) findViewById(R.id.winnerText);
                        winnerMessage.setText(winner + " has won!");
                        layout.setVisibility(View.VISIBLE);


                    for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        ((ImageView) gridLayout.getChildAt(i)).setClickable(false);
                    }

                }else{
                    //Checking whether game is played until the end
                    gameIsOver = true;

                    // checking gameState whether all of the arrays are set to be played
                    // looping into each array in gameState
                    for(int counterState: gameState){
                        // if the counter = 2 which indicate unplayed, set false to gameIsOver
                        if(counterState == 2){
                            gameIsOver = false;
                        }
                    }
                    // checking if game is really over and whether the game is still in active
                    // to prevent stating for draw when a user win at the last round
                    if(gameIsOver && gameIsActive){
                        TextView winnerMessage = (TextView)findViewById(R.id.winnerText);
                        winnerMessage.setText("It's a draw");
                        layout.setVisibility(View.VISIBLE);
                    }

                }
            }
        }
    }
}
