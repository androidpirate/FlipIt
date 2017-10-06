/*
 * <!--
 *  Copyright (C) 2016 The Android Open Source Project
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 * -->
 */

package com.github.androidpirate.flipcard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.androidpirate.flipcard.model.FlipCard;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BackCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BackCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackCardFragment extends Fragment {
    private static final String ARG_CARD = "card";
    private static final String ARG_IS_ANSWER_CORRECT = "is_answer_correct";
    private TextView mBackText;
    private CardView mCardView;
    private FlipCard mCard;
    private boolean mIsAnswerCorrect;
    private OnFragmentInteractionListener mListener;

    public BackCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BackCardFragment.
     */
    public static BackCardFragment newInstance(FlipCard card, boolean isAnswerCorrect) {
        BackCardFragment fragment = new BackCardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD, card);
        args.putBoolean(ARG_IS_ANSWER_CORRECT, isAnswerCorrect);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCard = (FlipCard) getArguments().getSerializable(ARG_CARD);
            mIsAnswerCorrect = getArguments().getBoolean(ARG_IS_ANSWER_CORRECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_back_card, container, false);
        mBackText = view.findViewById(R.id.tv_back_text);
        mBackText.setText(mCard.getRearSide());
        mCardView = view.findViewById(R.id.cv_back_card);
        setCardBackgroundColor();
        return view;
    }

    private void setCardBackgroundColor() {
        if(mIsAnswerCorrect) {
            mCardView.setCardBackgroundColor(getResources()
                    .getColor(R.color.correctCardBackground));
        } else {
            mCardView.setCardBackgroundColor(getResources()
                    .getColor(android.R.color.holo_red_light));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mListener.moveToNextCard();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void moveToNextCard();
    }
}