package com.example.octuto.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.octuto.R;
import com.example.octuto.databinding.FragmentWelcomeBinding;
import com.example.octuto.ui.quiz.QuizFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment {


    FragmentWelcomeBinding binding; // on créé une variable pour faire le lien entre les éléments d'interface et le "back-end"



    public WelcomeFragment() {
        // Required empty public constructor
    }


    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { // fonction appelée au démarrage de l'application
        super.onCreate(savedInstanceState); // permet de récupérer l'état dans lequel était l'application si besoin
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //tentative pour faire monter les éléments lorque le clavier apparait et ne pas cacher le bouton play
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) { // création de l'interface
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { // "démarrage" de l'interface pour la rendre utilisable par l'utilisateur
        super.onViewCreated(view, savedInstanceState);
        binding.playButton.setEnabled(false); // on ne veut pas cliquer sur le bouton pour jouer tant qu'un nom n'est pas rentré on désactive donc le bouton d'entrée de jeu
        binding.usrnameEditTxt.addTextChangedListener(new TextWatcher() { // ici on va gérer ce qu'il va se passer sur l'écran d'accueil en fonction du champs à remplir
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { // rien à faire ici avant que le champs soit modifié

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // pareil pendant la modification

            }
            @Override
            public void afterTextChanged(Editable s) { // une fois que la modification est effectuée on vérifie si le champs est vide ou non et on active le bouton play en fonction
                boolean txtVide = s.toString().isEmpty();
                binding.playButton.setEnabled(!txtVide);
            }
        });

        binding.playButton.setOnClickListener(new View.OnClickListener(){ // ici on va gérer la transition vers l'interface de quiz lorsque l'utilisateur clic sur le bouton
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager(); // on recupère le fragment manager pour intéragir avec les fragments associés à l'activité
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // on indique qu'on commence la transition
                QuizFragment quizFragment = QuizFragment.newInstance(); // on créé une nouvelle instance de l'interface de quiz
                fragmentTransaction.add(R.id.fragment_container_view, quizFragment); // on effectue la transition vers la nouvelle instance de l'interface de quiz
                fragmentTransaction.addToBackStack(null); // on garde le fragment précédent pour permette à l'utilisateur de retourner dessus, permet de défaire les opérations générée par la transition
                fragmentTransaction.commit();
            }
        });
    }
}