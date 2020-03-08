package com.android.id.rapidcheckerpkl59.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.id.rapidcheckerpkl59.R;
import com.android.id.rapidcheckerpkl59.data.adapter.AnggotaTimAdapter;
import com.android.id.rapidcheckerpkl59.data.model.TeamMember;
import com.android.id.rapidcheckerpkl59.data.viewmodel.TeamMemberViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvAnggotatim = findViewById(R.id.list_team_member);
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        rvAnggotatim.setHasFixedSize(true);
        ImageButton btnSearch = findViewById(R.id.main_btn_search);
        TextInputEditText idTim = findViewById(R.id.main_team_code_query);
        TextInputLayout kotakId = findViewById(R.id.main_team_code_cube);
        TextView error = findViewById(R.id.text_error);
        TeamMemberViewModel teamMemberViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TeamMemberViewModel.class);
        idTim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (kotakId.getError() != null) {
                    kotakId.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnSearch.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (error.getVisibility() == View.VISIBLE) {
                error.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(idTim.getText().toString())) {
                kotakId.setError("Tidak boleh kosong!");
                progressBar.setVisibility(View.GONE);
            } else {
                teamMemberViewModel.setTeamListMember(idTim.getText().toString());
                teamMemberViewModel.getTeamMemberList().observe(this, new Observer<ArrayList<TeamMember>>() {
                    @Override
                    public void onChanged(ArrayList<TeamMember> teamMembers) {
                        if (teamMembers != null) {
                            rvAnggotatim.setVisibility(View.VISIBLE);
                            rvAnggotatim.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            AnggotaTimAdapter adapter = new AnggotaTimAdapter(getApplicationContext());
                            adapter.setListProvince(teamMembers);
                            rvAnggotatim.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            rvAnggotatim.setVisibility(View.GONE);
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }
}
