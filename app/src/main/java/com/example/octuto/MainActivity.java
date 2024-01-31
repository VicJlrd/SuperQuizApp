package com.example.octuto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.octuto.databinding.ActivityMainBinding;

import android.view.View;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}