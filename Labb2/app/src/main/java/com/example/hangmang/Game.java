package com.example.hangmang;


import java.util.ArrayList;
// Game är en klass för spelets algoritm.
public class Game {
    // Arraylist triedLetters kommer att innehålla alla bokstäver som användaren har testat..
    ArrayList<String> triedLetters = new ArrayList<String>();
    // Uppgiften gav som förslag att användaren ska förlora efter 7 fel.
    String [] wrongLetters = new String[7];
    int totalWrongTries = 0;
    String gameWord="";
    // Konstruktorn hämtar ordet från WordsList klassen och spara det som gameWord.
    public Game(String word){
        gameWord=word;
    }
    /*
    * Funktionen ska läsa vilken bokstav som användaren har mata in. För sedan kontrollera om bokstaven
    * existerar i ordet eller inte. Alla ord innehåller en storbokstav i början, pga det gör vi alla
    * ord till toLowerCase().
    * Annars false.
    * */
    public boolean checkIfLetterExistsInWord(String letter){
        if(gameWord.toLowerCase().contains(letter)){
            return true;
        }else{
            return false;
        }
    }
    // Funktionen kontrollerar ifall användaren har mata in samma bokstav mer än en gång.
    public boolean checkDuplicate(String letter){
        //Kolla på dublett, returnera false om finns inte
        if(triedLetters.indexOf(letter)==-1){
            return false;
        }else{
            return true;
        }
    }
    // Lägg till bokstaven i arraylist triedLetters.
    public void addTriedLetters(String letter){
        triedLetters.add(letter);
    }
    /* När användaren har givit felbokstav som inte finns i ordet så läggs bokstaven till array:en wrongletters
        och ökar antalet felaktiga bokstäver men inte mer än 7 bokstäver får finnas i wrongletters.
     */
    public void addWrongLetter(String letter){
        totalWrongTries++;
        for(int i=0;i<7;i++){
            wrongLetters[i]=letter;
        }
    }

    // Returnerar antalet fel som användaren har gjort.
    public int getTotalWrongTries(){
        return totalWrongTries;
    }
}
