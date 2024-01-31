package com.example.octuto.ui.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.octuto.R;
import com.example.octuto.data.Question;
import com.example.octuto.databinding.FragmentQuizBinding;
import com.example.octuto.databinding.FragmentWelcomeBinding;
import com.example.octuto.injection.ViewModelFactory;
import com.example.octuto.ui.welcome.WelcomeFragment;

import java.util.Arrays;
import java.util.List;


// /!\ pour faire le lien entre ce code et le ViewModel, on ne peut pas simplement initialiser le ViewModel ici sinon il va se recréer à chaque fois que l'on va revenir sur l'application après une interruption !
// Se référer à ViewModelFactory et aux méthodes d'injection de VM !!
public class QuizFragment extends Fragment {
    private QuizViewModel viewModel;// on créé notre variable VM

    FragmentQuizBinding binding;

    public static QuizFragment newInstance() {
        QuizFragment fragment = new QuizFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(QuizViewModel.class); // on instancie ici notre VM !

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_quiz, container, false);
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { // maintenant que la vue est créée
        super.onViewCreated(view, savedInstanceState);
        viewModel.startQuiz(); // on appelle la fonction startquiz de notre VM
        viewModel.currentQuestion.observe(getViewLifecycleOwner(), new Observer<Question>() { // on appelle la fonction "observe" qui permet "d'écouter" les changement du LiveData concerné
            @Override
            public void onChanged(Question question) { // lorsque la valeur du LiveData change, on appelle la fonction updateQuestion définie plus loin ci-dessous
                updateQuestion(question);
            }
        });


        binding.answer1.setOnClickListener(new View.OnClickListener() { //ici on met en place des setOnClickListener pour déclencher des évènements selon la réponse données par l'utilisateur en cliquant sur les réponses
            @Override
            public void onClick(View view) {
                updateAnswer(binding.answer1, 0); // lorsque l'utilisateur clic on appelle une fonction pour mettre à jour la réponse, définie plus loin ci-dessous
            }
        });

        binding.answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnswer(binding.answer2, 1);
            }
        });

        binding.answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnswer(binding.answer3, 2);
            }
        });

        binding.answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnswer(binding.answer4, 3);
            }
        });

        viewModel.isLastQuestion.observe(getViewLifecycleOwner(), new Observer<Boolean>() { //fonction pour vérifier simplement si on arrive à la dernière question et modifier le texte du bouton "suite" en conséquence
            @Override
            public void onChanged(Boolean isLastQuestion) {
                if (isLastQuestion){
                    binding.next.setText("Finish");
                } else {
                    binding.next.setText("Next");
                }
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() { // on définit à travers cette fonction les actions à executer selon l'état du quiz lors du clic sur le bouton 'suivant'
            @Override
            public void onClick(View view) {
                Boolean isLastQuestion = viewModel.isLastQuestion.getValue();
                if(isLastQuestion != null && isLastQuestion){ // si on est à la dernière question on affiche la boite de dialogue indiquant le score du participant au quiz
                    displayResultDialog();
                }
                else { // sinon on appelle la prochaine question et on réinitialise l'interface pour accueillir la nouvelle question avec la fonction 'resetQuestion' définie plus ci-dessous
                    viewModel.nextQuestion();
                    resetQuestion();
                }
            }
        });
    }


    private void updateQuestion(Question question) { // cette fonction se charge de remplacer les textes des composants de l'interface avec setText
        binding.question.setText(question.getQuestion());
        binding.answer1.setText(question.getChoiceList().get(0));
        binding.answer2.setText(question.getChoiceList().get(1));
        binding.answer3.setText(question.getChoiceList().get(2));
        binding.answer4.setText(question.getChoiceList().get(3));
    }

    private void updateAnswer(Button button, Integer index){ // fonction pour mettre à jour la réponse
        showAnswerValidity(button, index); // on appelle la fonction qui met à jour la couleur du bouton de la réponse sélectionnée et affiche un texte selon si c'est la bonne ou non (fonction ci-dessous)
        enableAllAnswers(false); // on désactive tous les boutons de réponse
        binding.next.setVisibility(View.VISIBLE); // et on rend le bouton next visible pour passer à la question suivante
    }

    private void showAnswerValidity(Button button, Integer index){ // fonction pour mettre en place l'interface de feedback sur la réponse de l'utilisateur
        Boolean isValid = viewModel.isAnswerValid(index); // on appelle la fonction isAnswerValid de notre VM
        if (isValid) {
            button.setBackgroundColor(Color.parseColor("#388e3c")); // si c'est la bonne réponse, met le bouton en vert
            binding.validityText.setText("Good Answer ! 💪"); // et choisit le texte de bonne réponse
            button.announceForAccessibility("Good Answer !"); // annonce pour les utilisateurs de talkback
        } else {
            button.setBackgroundColor(Color.RED); // sinon affiche le bouton en rouge et choisit le texte de mauvaise réponse
            binding.validityText.setText("Bad answer 😢");
            button.announceForAccessibility("Bad answer"); // annonce pour les utilisateurs de talkback
        }
        binding.validityText.setVisibility(View.VISIBLE); // rend le texte de réponse visible
    }

    private void resetQuestion(){
        List<Button> allAnswers = Arrays.asList(binding.answer1, binding.answer2, binding.answer3, binding.answer4);
        allAnswers.forEach( answer -> {
            answer.setBackgroundColor(Color.parseColor("#6200EE"));
        });
        binding.validityText.setVisibility(View.INVISIBLE);
        enableAllAnswers(true);
    }

    private void enableAllAnswers(Boolean enable) { // fonction pour simplifier l'activation/désactivation de toutes les réponses à une question d'un coup
        List<Button> allAnswers = Arrays.asList(binding.answer1, binding.answer2, binding.answer3, binding.answer4);
        allAnswers.forEach(answer -> {
            answer.setEnabled(enable);
        });
    }

    private void displayResultDialog() { // on construit la boite de dialogue pour la fin du quiz sans avoir besoin de la créer dans le fichier XML !
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // on instancie

        builder.setTitle("Finished !"); // on met un titre
        Integer score = viewModel.score.getValue(); // on récupère la valeur de score
        builder.setMessage("Your final score is "+ score); // on affiche le message à faire passer pour indiquer le score à l'utilisateur
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() { // on créé un bouton pour retourner à l'accueil de l'application
            public void onClick(DialogInterface dialog, int id) {
                goToWelcomeFragment(); // fonction définie ci-dessous
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToWelcomeFragment(){ // de la même manière que précédemment pour passer de l'accueil au quiz, on passe du quiz à l'accueil :)
        WelcomeFragment welcomeFragment = WelcomeFragment.newInstance();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, welcomeFragment).commit();
    }



   /***
    // On override toutes les fonctions suivantes et on affiche un log simplement pour voir comment marche le cycle de vie d'une application !
    @Override
    public void onStart(){
        super.onStart();
        Log.d("Vic", "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("Vic", "onResume() called");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d("Vic", "onPause() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("Vic", "onStop() called");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d("Vic", "onDestroyView() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("Vic", "onDestroy() called");
    } ***/

}