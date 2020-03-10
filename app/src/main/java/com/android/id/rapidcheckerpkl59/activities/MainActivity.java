package com.android.id.rapidcheckerpkl59.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.id.rapidcheckerpkl59.R;
import com.android.id.rapidcheckerpkl59.data.adapter.TeamMemberAdapter;
import com.android.id.rapidcheckerpkl59.data.viewmodel.TeamMemberViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private boolean isFirstTime = true;
    RecyclerView rvTeamMember;
    ProgressBar progressBar;
    ImageButton btnNextTeam, btnPrevTeam;
    TextInputEditText idTeam;
    TextInputLayout idCube;
    TextView error;
    TeamMemberViewModel teamMemberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvTeamMember = findViewById(R.id.list_team_member);
        progressBar = findViewById(R.id.progress_bar);
        rvTeamMember.setHasFixedSize(true);
        btnNextTeam = findViewById(R.id.btn_next_team);
        btnPrevTeam = findViewById(R.id.btn_prev_team);
        idTeam = findViewById(R.id.main_team_code_query);
        idTeam.setText("21001");
        idCube = findViewById(R.id.main_team_code_cube);
        error = findViewById(R.id.text_error);
        teamMemberViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TeamMemberViewModel.class);
        idTeam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (idCube.getError() != null) {
                    idCube.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int id = Integer.parseInt(idTeam.getText().toString());
                if (id > 21110) {
                    idCube.setError("Maksimum 21110");
                    Toast.makeText(MainActivity.this, "Kode tim hanya sampai 21110", Toast.LENGTH_SHORT).show();
                } else if (id < 21001) {
                    idCube.setError("Minimum 21001");
                    Toast.makeText(MainActivity.this, "Kode tim hanya sampai 21001", Toast.LENGTH_SHORT).show();
                } else {
                    getTeamData();
                }

            }
        });
        if (isFirstTime) {
            getTeamData();
            isFirstTime = false;
        }
        btnNextTeam.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (error.getVisibility() == View.VISIBLE) {
                error.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(idTeam.getText())) {
                idCube.setError("Tidak boleh kosong!");
                progressBar.setVisibility(View.GONE);
            } else {
                int id = Integer.parseInt(idTeam.getText().toString()) + 1;
                if (id < 21111) {
                    idTeam.setText(String.valueOf(id));
                    getTeamData();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Kode Tim hanya sampai 21110", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnPrevTeam.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (error.getVisibility() == View.VISIBLE) {
                error.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(idTeam.getText())) {
                idCube.setError("Tidak Boleh Kosong");
                progressBar.setVisibility(View.GONE);
            } else {
                int id = Integer.parseInt(idTeam.getText().toString()) - 1;
                if (id > 21000) {
                    idTeam.setText(String.valueOf(id));
                    getTeamData();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Kode Tim hanya sampai 21001", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getTeamData() {
        if (!TextUtils.isEmpty(idTeam.getText())) {
            teamMemberViewModel.setTeamListMember(idTeam.getText().toString());
            teamMemberViewModel.getTeamMemberList().observe(this, teamMembers -> {
                if (teamMembers != null) {
                    rvTeamMember.setVisibility(View.VISIBLE);
                    rvTeamMember.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    TeamMemberAdapter adapter = new TeamMemberAdapter(getApplicationContext());
                    adapter.setListProvince(teamMembers);
                    rvTeamMember.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    rvTeamMember.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
