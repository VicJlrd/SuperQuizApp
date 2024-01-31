package com.example.octuto.ui.quiz;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.octuto.data.Question;
import com.example.octuto.data.QuestionRepository;

import java.util.List;

public class QuizViewModel extends ViewModel { // on s'occupe ici du ViewModel qui fait donc le lien entre les données et l'affichage + les interactions utilisateurs qui vont modifier les données (et donc l'affichage)
    private QuestionRepository questionRepository; // on initialise donc notre repository...
    private List<Question> questions; // ... la liste de questions ...
    private Integer currentQuestionIndex = 0; // ... et un compteur pour déterminer à combien de questions nous avons répondus
    public QuizViewModel(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    // le ViewModel comporte des états et "observe" des évènement, les LiveData correspondent ici aux différents états de l'application auxquels il faut faire attention pour savoir ce qu'il faut afficher à l'écran
    MutableLiveData<Question> currentQuestion = new MutableLiveData<Question>(); // la question actuelle
    MutableLiveData<Integer> score = new MutableLiveData<Integer>(0); // le score actuel
    MutableLiveData<Boolean> isLastQuestion = new MutableLiveData<Boolean>(false); // un booléen pour savoir si nous avons atteint la dernière question

    // Ci-dessous, les évènements qui sont "observés" par le ViewModel
    public void startQuiz (){ // démarrage du quiz
        questions = questionRepository.getQuestions(); // on récupère l'ensemble des questions
        currentQuestion.postValue(questions.get(0)); // on initialise la première question (la question courante)
    }
    public boolean isAnswerValid(Integer indexAnswer){ // une fonction pour vérifier si la réponse de l'utilisateur est valide
        Question question = currentQuestion.getValue(); //on récupère la question actuelle
        boolean isValid = question != null && question.getAnswerIndex() == indexAnswer; // on vérifie si l'index passé en paramètre (choix de l'utilisateur) correspond à l'index de la bonne réponse
        Integer currentScore = score.getValue(); // on récupère le score actuel
        if(currentScore != null && isValid){ // on vérifie qu'il a bien été initialisé et que la réponse est valide
            score.setValue(currentScore+1); // si c'est le cas on ajoute 1 point au score actuel
        }
        return isValid; // on retourne un booléen qui indique si la réponse est valide
    }

    public void nextQuestion(){ // fonction pour afficher/mettre en place la nouvelle question
        Integer nextIndex = currentQuestionIndex+1; // on prend l'index suivant dans la liste de question
        if(nextIndex >= questions.size()){ // on vérifie qu'on ne déborde pas de la liste de question mais ça ne devrait pas arriver puisqu'à la dernière question il n'y aura plus de bouton "next"
            return;
        }
        else if (nextIndex == questions.size()-1){ // on vérifie si on est à la dernière question
            isLastQuestion.postValue(true); // si oui on passe le booléen qui nous indique ça à true
        }
        currentQuestionIndex = nextIndex; // sinon on change l'index de la question "courante"
        currentQuestion.postValue(questions.get(currentQuestionIndex)); // et on "charge" la question correspondant au nouvel index
    }
}
