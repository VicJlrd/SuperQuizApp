package com.example.octuto.data;
import java.util.List;
import java.util.Arrays;
public class QuestionBank { // pour ce tuto on ne va pas chercher des questions dans un API externe, on créer donc notre propre base de données (locale) de questions pour notre quiz
    public List<Question> getQuestions() { // on créé simplement une fonction qui va nous retourner l'ensemble des questions créées pour le quiz
        return Arrays.asList(
                // on créé une nouvelle instance de notre classe Question qui prend comme paramètre pour rappel : une string, une liste de string, et un int
                new Question(
                        "When did the first man land on the moon?",
                        Arrays.asList(
                                "1958",
                                "1962",
                                "1967",
                                "1969"
                        ),
                        3
                ),
                new Question(
                        "When did the first man land on the moon?",
                        Arrays.asList(
                                "1958",
                                "1962",
                                "1967",
                                "1969"
                        ),
                        3
                ),
                new Question(
                        "What is the house number of The Simpsons?",
                        Arrays.asList(
                                "42",
                                "101",
                                "666",
                                "742"
                        ),
                        3
                ),
                new Question(
                        "Who painteddid the Mona Lisa paint?",
                        Arrays.asList(
                                "Michelangelo",
                                "Leonardo Da Vinci",
                                "Raphael",
                                "Caravaggio"
                        ),
                        1
                ),
                new Question(
                        "What is the country top-level domain of Belgium?",
                        Arrays.asList(
                                ".bg",
                                ".bm",
                                ".bl",
                                ".be"
                        ),
                        3
                )
        );
    }
    private static QuestionBank instance;
    public static QuestionBank getInstance() {
        if (instance == null) {
            instance = new QuestionBank();
        }
        return instance;
    }
}