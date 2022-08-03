package com.example.hangmang;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.*;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    /*En layout som ligger inne i våran huvudlayout ConstraintLayout
    * Layout kommer innehålla rätt och fel bokstäver
    * En TextInputEditText.
    * */
    LinearLayout linearLayoutWrongLetters;
    TextInputEditText userInputEditText;

    //WordsList klass
    WordsList list;

    //Game klass
    Game game;

    TextView correctWordTextView;
    TextView startaOmKnapp, avslutaKnapp;
    LinearLayout.LayoutParams layoutParamsWrapContent;

    @Override
    //Kommer köras när programmet exekveras
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //En TextInputEditText komponent som har UserInput som id
        userInputEditText = (TextInputEditText) findViewById(R.id.UserInput);

        // Anropar en instans av WordList.
        list = new WordsList();

        //Anropar klassen Game och skickar slumpat ord till konstruktor av klassen Game
        String randomWord =  list.getRandomWord();
        game = new Game(randomWord);


        //får längden av det Slumpat ord
        correctWordTextView = findViewById(R.id.TextViewWord);
        createWordInLayout(list.getRandomWordLength());

        //Knappar starta om och avsluta och de hittas med id:et borjaOmButton och avslutaButton.
        startaOmKnapp = (TextView)findViewById(R.id.borjaOmButton);
        avslutaKnapp=(TextView)findViewById(R.id.avslutaButton);

        //Key Event på userInputEditText
        userInputEditText.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                /*
                * onKey körs två gånger, när man trycker på knappen och när du släpper fingrar från knappen.
                * Med ACTION_DOWN ska vi lyssna keyEvent bara en gång, när man trycker på knappen
                * */
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        //Kollar om användaren trycker på Enter.
                        case KeyEvent.KEYCODE_ENTER:
                            // Gör om det till en sträng.
                            String letter = userInputEditText.getText().toString().trim();
                            // Villkoret blir sant om bokstaven finns.
                            if (letter.length() > 0) {
                                //Nollställa användarinput
                                userInputEditText.setText("");
                                // Fel när användaren ger samma bokstav mer än en gång annars körs spelet.
                                if(!game.checkDuplicate(letter)){
                                    game.addTriedLetters(letter);
                                    // Om bosktaven inte finns i ordet.
                                    if(!game.checkIfLetterExistsInWord(letter)){
                                        game.addWrongLetter(letter);
                                        addWrongLetterToView(letter);
                                    }else{
                                        // Byter underscore till rätt bokstav
                                        updateUnderScoreToLetter(letter,randomWord);
                                    }
                                }else{
                                    showToastMessage("Du redan har angett bokstav "+ letter);
                                }
                            } else {
                                showToastMessage("Du måste skriva någonting");
                            }
                            /*
                            * 103 är kod för att få tillgång till tangentbordet i android
                            * 104 döljer tangentbordet*/
                            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(userInputEditText.getWindowToken(), 0);

                            // Anropar funktionen för att avgöra om spelet är slut.
                            setButtonsVisibility(isGameFinished());
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
    }


    // Skapa textfält som visar hur många bokstäver måste man gissa
    private void createWordInLayout(int totalInputs){
        //loop totalInputs gånger
        String word="";
        for(int i=0;i<totalInputs;i++){
            if(i>=0&&i<totalInputs-1){
                word+="_ ";
            }else{
                word+="_";
            }
        }
        correctWordTextView.setText(word);
        correctWordTextView.setTextSize(25);
    }
    /*
    * Funktionen skapar en Textview komponent som ska innehålla alla fel bokstäver för sedan lägga
    * till komponenten till grännssnitt.
    * */
    private void addWrongLetterToView(String letter){
        TextView wrongLettersTextView = findViewById(R.id.WrongLetters);
        wrongLettersTextView.setText(wrongLettersTextView.getText()+letter+" ");
    }
    // Ersätter understreck till rätt bokstav
    public void updateUnderScoreToLetter(String letter, String word){
        // Ändrar bokstav till char
        char letterChar = letter.charAt(0);
        // Den här räknar hur långt ordet som vi måste gissa
        int wordLength = word.length();
        //Text som finns i gränsnittet
        String textInTextView = correctWordTextView.getText().toString();
        //Text som finns i gränsnittet utan mellanslag
        String textInTextViewWithoutSpace = textInTextView.replaceAll("\\s", "");
        //Text som ska ersätta texten som finns i gränsnitt.
        String newWordInTextView ="";
        // Den här loopar igenom ordet
        for(int i=0;i<wordLength;i++){
            // Om ordet innehåller bokstaven lägg till bokstav till newWordInTextView
            if(word.charAt(i)==letterChar||word.charAt(i)==Character.toUpperCase(letterChar)){
                newWordInTextView+=letterChar;
            }else{
                // Om den plats i ordet inte lika med en bokstav
                // vi lägger till mellanslag plus understreck till vår textview
                if(textInTextViewWithoutSpace.charAt(i)=='_'){
                    newWordInTextView+="_";
                }else{
                    // Om det finns någon bokstav av platsen av ordet
                    // lägger till bokstaven till newWordInTextView
                    newWordInTextView+=word.charAt(i);
                }
            }
        }
        // Sätter mellanslag mellan varje bokstav och lägger till de till grännsnittet
        // Ersätter den gamla texten med den nya texten
        correctWordTextView.setText(newWordInTextView.replaceAll("\\B", " "));
    }
    // Kontrollerar ifall spelstatusen är slut eller inte
    public String isGameFinished(){
        String gameStatus="";
        if(game.getTotalWrongTries()==7){
            gameStatus="lost";
            showToastMessage("Du har gjort 7 fel och förlorat");
        }else{
            if(checkIfUnderscoreExists()){
                gameStatus="playing";
            }else{
                gameStatus="won";
                showToastMessage("Grattis du vann!!");
            }
        }
        if(!gameStatus.equals("playing")){
            disableInput();
        }
        return gameStatus;
    }
    // Blockerar inputen av userInputEditText
    public void disableInput(){
        userInputEditText.setVisibility(INVISIBLE);
    }
    // Den här returnerar en true eller false isGamefinished
    public boolean checkIfUnderscoreExists(){
        TextView word = findViewById(R.id.TextViewWord);
        if(!word.getText().toString().contains("_")){
            return false;
        }
        return true;
    }
    // Kontrollerar knappens uteseende när användaren inte spelar
    public void setButtonsVisibility(String gameStatus){
        if(!gameStatus.equals("playing")){
            startaOmKnapp.setVisibility(VISIBLE);
            avslutaKnapp.setVisibility(VISIBLE);
        }
    }
    // Funktion för knappen avslutaSpel.
    public void avslutaSpel(View view){
        finish();
    }
    // Funktion för knappen börjaOmSpel.
    public void borjaOmSpel(View view){
        finish();
        startActivity(getIntent());
    }
    // Meddlande funktion.
    private void showToastMessage(String message){
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}