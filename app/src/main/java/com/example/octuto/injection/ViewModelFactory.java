package com.example.octuto.injection;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.octuto.data.QuestionBank;
import com.example.octuto.data.QuestionRepository;
import com.example.octuto.ui.quiz.QuizViewModel;

import org.jetbrains.annotations.NotNull;

// on implémente l'interface Google ViewModelProvide.Factory pour pouvoir déclarer notre ViewModel ailleurs et tenir compte du cycle de vie de l'application, (permet aussi de créer plusieurs VM au besoin)
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final QuestionRepository questionRepository;
    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    private ViewModelFactory() {
        QuestionBank questionBank = QuestionBank.getInstance();
        this.questionRepository = new QuestionRepository(questionBank);
    }

    @Override
    @NotNull
    public <T extends ViewModel>  T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(QuizViewModel.class)) {
            return (T) new QuizViewModel(questionRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
