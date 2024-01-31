package com.example.octuto.data;

import java.util.List;

public class Question { // on créé simplement la classe 'Question' pour définir les informations que doivent contenir nos questions pour notre quiz
    private final String question; // un texte pour la question
    private final List<String> choiceList; // une liste de texte pour les choix possibles
    private final int answerIndex; // l'index de la bonne réponse

    public Question(String question, List<String> choiceList, int answerIndex) {
        this.question = question;
        this.choiceList = choiceList;
        this.answerIndex = answerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getChoiceList() {
        return choiceList;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }
}
