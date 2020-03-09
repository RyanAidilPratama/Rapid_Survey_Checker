package com.android.id.rapidcheckerpkl59.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.id.rapidcheckerpkl59.R;
import com.android.id.rapidcheckerpkl59.activities.DetailsActivity;
import com.android.id.rapidcheckerpkl59.data.model.TeamMember;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder> {

    private Context context;
    private ArrayList<TeamMember> listTeamMember = new ArrayList<>();

    private ArrayList<TeamMember> getListTeamMember() {
        return listTeamMember;
    }

    public void setListProvince(ArrayList<TeamMember> teamMember) {
        listTeamMember.clear();
        listTeamMember.addAll(teamMember);
        notifyDataSetChanged();
    }

    public TeamMemberAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kotak_tim, parent, false);
        return new TeamMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberViewHolder holder, int position) {
        final TeamMember teamMember = getListTeamMember().get(position);
        String baseUrlImage = String.format(context.getString(R.string.base_url_image), teamMember.getNim());
        holder.nim.setText(teamMember.getNim());
        holder.namaDesa.setText(teamMember.getNamaDesa());
        holder.kodeDesa.setText(teamMember.getKodeDesa());
        holder.jmlhBangunan.setText(String.valueOf(teamMember.getJumlah_bangunan()));
        String statusListing = teamMember.getStatus().toUpperCase();
        if (statusListing.equalsIgnoreCase("UPLOADED")) {
            statusListing = "SAMPEL DIAMBIL";
        }
        holder.statusListing.setText(statusListing);
        Glide.with(context)
                .load(baseUrlImage)
                .into(holder.profPict);
        holder.btnDetail.setOnClickListener(view -> {
            if (teamMember.getJumlah_bangunan() > 0) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(DetailsActivity.EXTRA_MEMBER, teamMember);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Tidak ada bangunan terakhir!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTeamMember.size();
    }

    class TeamMemberViewHolder extends RecyclerView.ViewHolder {
        private TextView nim, kodeDesa, namaDesa, jmlhBangunan, statusListing;
        private ImageView profPict;
        private Button btnDetail;

        TeamMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nim = itemView.findViewById(R.id.txt_nim);
            kodeDesa = itemView.findViewById(R.id.txt_kode_desa);
            namaDesa = itemView.findViewById(R.id.txt_nama_desa);
            kodeDesa = itemView.findViewById(R.id.txt_kode_desa);
            profPict = itemView.findViewById(R.id.prof_pict);
            btnDetail = itemView.findViewById(R.id.btn_lihat_bangunan);
            jmlhBangunan = itemView.findViewById(R.id.txt_jmlh_bangunan);
            statusListing = itemView.findViewById(R.id.txt_status_listing);
        }
    }
}
