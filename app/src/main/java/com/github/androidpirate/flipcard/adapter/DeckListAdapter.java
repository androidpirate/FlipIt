package com.github.androidpirate.flipcard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.androidpirate.flipcard.R;
import com.github.androidpirate.flipcard.model.Deck;

import java.util.ArrayList;

/**
 * Adapter class for deck list.
 */
public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.DeckHolder> {
    private ArrayList<Deck> mDecks = new ArrayList<>();
    private OnAdapterInteractionListener mListener;

    public DeckListAdapter(OnAdapterInteractionListener listener,
                           ArrayList<Deck> decks) {
        mListener = listener;
        mDecks = decks;
    }

    @Override
    public DeckHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.deck_list_item, parent, false);
        return new DeckHolder(view);
    }

    @Override
    public void onBindViewHolder(DeckHolder holder, int position) {
        Deck deck = mDecks.get(position);
        holder.onBindDeck(deck);
    }

    @Override
    public int getItemCount() {
        return mDecks.size();
    }

    class DeckHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mSize;

        public DeckHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_title);
            mSize = itemView.findViewById(R.id.tv_size);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Deck deck = mDecks.get(getAdapterPosition());
                    mListener.onItemClick(deck);
                }
            });
        }

        public void onBindDeck(Deck deck) {
            mTitle.setText(deck.getTitle());
            mSize.setText(String.valueOf(deck.getSize()));
        }
    }

    public interface OnAdapterInteractionListener {
        void onItemClick(Deck deck);
    }
}