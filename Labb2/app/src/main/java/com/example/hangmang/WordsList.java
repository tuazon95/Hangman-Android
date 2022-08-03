package com.example.hangmang;


import java.util.*;
// Wordlist är en klass som innehåller vår ordlista..
public class WordsList {
    String[] words;
    String randomText;
    public WordsList(){
        words = new String[]{
                "Sverige","Atmosfär","Dator","Kontinent","Pandemi","Undervisning",
                "Programmering","Hund","Android","Jordnöt"
        };
    }
    // Den här funktionen returnerar slumpord för änvändaren.
    public String getRandomWord(){
        Random random = new Random();
        // Random.nextInt returnerar från 0-9 och den blir index för vektorn words.
        randomText = words[random.nextInt(words.length)];
        return randomText;
    }
    public int getRandomWordLength(){
        return randomText.length();
    }
}
