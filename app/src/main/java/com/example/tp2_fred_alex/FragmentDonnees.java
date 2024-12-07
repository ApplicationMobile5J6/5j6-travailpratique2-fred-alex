package com.example.tp2_fred_alex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDonnees extends Fragment {

    private static final String ARG_COUNTRY = "country";
    private static final String ARG_CITY = "city";
    private static final String ARG_AGE = "age";

    private String country;
    private String city;
    private String age;

    public static FragmentDonnees newInstance(String country, String city, String age) {
        FragmentDonnees fragment = new FragmentDonnees();
        Bundle args = new Bundle();
        args.putString(ARG_COUNTRY, country);
        args.putString(ARG_CITY, city);
        args.putString(ARG_AGE, age);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            country = getArguments().getString(ARG_COUNTRY);
            city = getArguments().getString(ARG_CITY);
            age = getArguments().getString(ARG_AGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_donnees, container, false);


        TextView tvCountry = view.findViewById(R.id.tv_country_data);
        TextView tvCity = view.findViewById(R.id.tv_city_data);
        TextView tvAge = view.findViewById(R.id.tv_age_data);

        tvCountry.setText("Country: " + country);
        tvCity.setText("City: " + city);
        tvAge.setText("Age: " + age);

        return view;
    }
}
