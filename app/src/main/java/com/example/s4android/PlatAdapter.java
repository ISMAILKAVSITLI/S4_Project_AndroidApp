package com.example.s4android;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.Button;





public class PlatAdapter extends RecyclerView.Adapter<PlatAdapter.MenuViewHolder> {

    private List<PlatItem> platItems;
    private OnPlatClickListener listener;
    private boolean modeResume;

    public PlatAdapter(List<PlatItem> platItems, OnPlatClickListener listener, boolean modeResume) {
        this.platItems = platItems;
        this.listener = listener;
        this.modeResume = modeResume;
    }

    public void setIsResume(boolean isResume) {
        this.modeResume = isResume;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        PlatItem item = platItems.get(position);

        holder.nomPlat.setText(item.getNom());
        holder.prixPlat.setText(String.format("€ %.2f", item.getPrix()));
        holder.compteur.setText("x" + item.getCompteur());

        boolean afficherBoutons = item.getCompteur() > 0 || modeResume;

        holder.boutonSupprimer.setVisibility(afficherBoutons ? View.VISIBLE : View.GONE);
        holder.boutonCommentaire.setVisibility(afficherBoutons ? View.VISIBLE : View.GONE);
        holder.compteur.setVisibility(afficherBoutons ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (!modeResume) {
                listener.onPlatClick(item); // La logique de compteur est uniquement côté activité
            }
        });

        holder.boutonSupprimer.setOnClickListener(v -> {
            listener.onPlatSupprime(item); // La décrémentation est faite côté activité aussi
        });

        holder.boutonCommentaire.setOnClickListener(v -> {
            listener.onCommentaireClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return platItems.size();
    }

    public interface OnPlatClickListener {
        void onPlatClick(PlatItem item);
        void onPlatSupprime(PlatItem item);
        void onCommentaireClick(PlatItem item);
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView nomPlat, prixPlat, compteur;
        Button boutonSupprimer, boutonCommentaire;

        public MenuViewHolder(View itemView) {
            super(itemView);
            nomPlat = itemView.findViewById(R.id.nom);
            prixPlat = itemView.findViewById(R.id.prix);
            compteur = itemView.findViewById(R.id.compteur);
            boutonSupprimer = itemView.findViewById(R.id.boutonSupprimer);
            boutonCommentaire = itemView.findViewById(R.id.boutonCommentaire);
        }
    }
}