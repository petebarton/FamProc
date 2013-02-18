package com.freeborrow.familyproc;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Memorizor extends Activity implements OnClickListener {
	
	String strPartial, strwhtFamProc1, strwhtOldTest, strwhtGeBuAddr, strCurrentWord, strWholeThing;
	TextView tvPartialThing, tvCorrect, tvWrong;
	LinearLayout llGuessBtnHolder1;
	LinearLayout llGuessBtnHolder2;
	LinearLayout llGuessBtnHolder3;
	LinearLayout llGuessBtnHolder4;
	Button btnFamProc1, btnOldTest, btnGeBuAdrs;
	int positionWeOn = (int) 0;
	int intWrong = (int) 0;
	int intCorrect = (int) 0;
	String[] straWholeArray;
	Button[] btnaGuess;
	int howManyGuessButtons = 15;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("debug","before contentViewRset");

        setContentView(R.layout.main);
        
        Log.i("debug","contentViewRset");
        //Get Whole Things from strings.xml
        strPartial = (String) "";
        strCurrentWord = (String) "";
        strwhtFamProc1 = (String) getString(R.string.strwhtFamProc1);
        strwhtOldTest = (String) getString(R.string.strwhtOldTest);
        strwhtGeBuAddr = (String) getString(R.string.strwhtGeBuAdrs);
        tvPartialThing = (TextView) findViewById(R.id.tvPartialThing);
        llGuessBtnHolder1 = (LinearLayout) findViewById(R.id.llGuessButtonRow1);
        llGuessBtnHolder2 = (LinearLayout) findViewById(R.id.llGuessButtonRow2);
        llGuessBtnHolder3 = (LinearLayout) findViewById(R.id.llGuessButtonRow3);
        llGuessBtnHolder4 = (LinearLayout) findViewById(R.id.llGuessButtonRow4);
        tvCorrect = (TextView) findViewById(R.id.tvCorrect);
        tvWrong = (TextView) findViewById(R.id.tvWrong);
        btnFamProc1 = (Button) findViewById(R.id.btnFamProc1);
        btnOldTest = (Button) findViewById(R.id.btnOldTest);
        btnGeBuAdrs = (Button) findViewById(R.id.btnGeBuAdrs);

        Log.i("debug","variables set");

        
        //start the game
        makeGuessButtons(howManyGuessButtons);
        Log.i("debug","guessbuttonsmade");

        startGame(strwhtOldTest);
                
        btnFamProc1.setOnClickListener(this);
        btnOldTest.setOnClickListener(this);
        btnGeBuAdrs.setOnClickListener(this);
    }

     	
    private void makeGuessButtons(int i) {
    	btnaGuess = new Button[i];
    	for (int f=0; f < i; f++) {
	  	  btnaGuess[f] = new Button(this);
		  btnaGuess[f].setText(Integer.toString(f));
		  btnaGuess[f].getBackground().setColorFilter(0xFFfff3c6, PorterDuff.Mode.MULTIPLY);
		  btnaGuess[f].setTextSize(11);
		  switch (f) {
		    case 0: case 1: case 2: case 3: case 4:
			  btnaGuess[f].setOnClickListener(this);
			  llGuessBtnHolder1.addView(btnaGuess[f]);
		    break;
		    case 5: case 6: case 7: case 8: case 9:
			  btnaGuess[f].setOnClickListener(this);
			  llGuessBtnHolder2.addView(btnaGuess[f]);
		    break;
		    case 10: case 11: case 12: case 13: case 14:
			  btnaGuess[f].setOnClickListener(this);
			  llGuessBtnHolder3.addView(btnaGuess[f]);
		    break;
		    default:
			  btnaGuess[f].setOnClickListener(this);
			  llGuessBtnHolder4.addView(btnaGuess[f]);
		    break;
		  }
		  
		}
		
	}

	private void putNextButtons(){
		positionWeOn++;
    	
    	//get a random button
    	int r = (int) (Math.random() * btnaGuess.length);
    	for (int f=0; f < btnaGuess.length; f++) {
    		if (f == r) {
    			btnaGuess[f].setText(normalize(straWholeArray[positionWeOn]) );
    			btnaGuess[f].setTag(normalize(straWholeArray[positionWeOn]) );
    		} else {
    			//put a random word on the button
    			String s = straWholeArray[(int) (Math.random()*straWholeArray.length)];
    			btnaGuess[f].setText(normalize(s));
    			btnaGuess[f].setTag(normalize(s));
    		}
    	}
    	
    }
    
    public boolean weAreAtTheEnd(){
    	boolean z = (positionWeOn + 1 == straWholeArray.length);
    	
    	if (z) {
    		disableGuessButtons();
    	}
    	
    	return z;
    }
    
    public void addWordToPartial() {
    	tvPartialThing.setText(tvPartialThing.getText().toString() + " " + straWholeArray[positionWeOn]);
    }
    
    public void increaseCorrectScore() {
    	tvCorrect.setText("Correct: " + ++intCorrect);
    }
    
    public void increaseWrongScore() {
    	tvWrong.setText("Wrong: " + ++intWrong);
    }
    
    public void resetScores() {
    	intCorrect = 0; 
    	intWrong = 0;
    	tvCorrect.setText("Correct: 0");
    	tvWrong.setText("Wrong: 0");
    }
    
	private void guessRightWrong(View ii) {
		String i = (String) ii.getTag().toString();
		if (i.intern() == normalize(straWholeArray[positionWeOn]).intern()){  
			
			//it's correct! do this
			addWordToPartial();
			increaseCorrectScore(); 
			enableGuessButtons();
			
			
			if ( !(weAreAtTheEnd()) ) {
				putNextButtons();
			} else {
				disableGuessButtons();
				popAlert("Your Score", strScoreMessage());
			}

		} else {  // it's incorrect, do this
			increaseWrongScore();
			ii.setEnabled(false);
		} //end if
	}
    	
	private void enableGuessButtons() {
		for (int f=0; f<btnaGuess.length; f++) {
			btnaGuess[f].setEnabled(true);
		}
	}

	private void disableGuessButtons() {
		for (int f=0; f<btnaGuess.length; f++) {
			btnaGuess[f].setEnabled(false);
		}
	}
	
	@Override
	public void onClick(View src) {
		Log.i("onclicker", src.toString());
		switch(src.getId()) {
		case R.id.btnFamProc1:
			startGame(strwhtFamProc1);
			break;
		case R.id.btnOldTest:
			startGame(strwhtOldTest);
			break;
		case R.id.btnGeBuAdrs:
			startGame(strwhtGeBuAddr);
			break;
		default:
			guessRightWrong(src);
			break;
	    } //switch
	} //onClick

	public void startGame(String x) {
		strWholeThing = x;
		straWholeArray = strWholeThing.split(" ");
		tvPartialThing.setText(straWholeArray[0]);
		positionWeOn = 0;
		enableGuessButtons();
		resetScores();
		putNextButtons();
	}
	
	public void popToast(String aa){
		Log.i("poptoast", getApplicationContext().toString());
		Toast.makeText(this, aa, Toast.LENGTH_LONG).show();
	}
	
	
	public void popAlert(String strTitle, String strMessage){
		AlertDialog.Builder boom = new AlertDialog.Builder(this);
		boom.setTitle(strTitle);
		boom.setMessage(strMessage);
		boom.setIcon(android.R.drawable.ic_dialog_alert);
		boom.setPositiveButton("OK", (android.content.DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// we don't do anything here when they click "OK".
			}
		});
		boom.show();
	}


	private String strScoreMessage() {
		int sc = (int) ((double) intCorrect / (((double) intCorrect + (double) intWrong))*100);
		return Integer.toString(sc) + "% of your guesses were correct!";
	}
    
	private String normalize(String n) {
		return n.replace(".", "").replace(",", "").toLowerCase();
	}
}