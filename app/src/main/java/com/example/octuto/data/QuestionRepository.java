package com.example.octuto.data;

import java.util.List;

public class QuestionRepository { // le repository sert à gérer l'accès aux données, on pourrait techniquement ne pas en avoir besoin ici puisqu'on a qu'une BDD locale peu complexe, mais par soucis de clareté et si l'appli venait à évoluer, il sera bien pratique :)
    private final QuestionBank questionBank;
    public QuestionRepository(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }
    public List<Question> getQuestions(){
        return questionBank.getQuestions();
    }
}
