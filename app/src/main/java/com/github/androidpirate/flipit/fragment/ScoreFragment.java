package com.github.androidpirate.flipit.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.androidpirate.flipit.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {
    private static final String ARG_SCORE = "score";
    private static final String ARG_BONUS = "bonus";
    private static final String ARG_PERCENTAGE_SCORE = "percent_score";
    private static final String ARG_PERCENTAGE_BONUS = "percent_bonus";
    private int mScore;
    private float mPercentScore;
    private int mBonus;
    private float mPercentBonus;
    private OnFragmentInteractionListener mListener;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void restart();
    }

    public ScoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScoreFragment.
     */
    public static ScoreFragment newInstance(int score, int bonus, float percentScore,
                                            float percentBonus) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        args.putInt(ARG_BONUS, bonus);
        args.putFloat(ARG_PERCENTAGE_SCORE, percentScore);
        args.putFloat(ARG_PERCENTAGE_BONUS, percentBonus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mScore = getArguments().getInt(ARG_SCORE);
            mBonus = getArguments().getInt(ARG_BONUS);
            mPercentScore = getArguments().getFloat(ARG_PERCENTAGE_SCORE);
            mPercentBonus = getArguments().getFloat(ARG_PERCENTAGE_BONUS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score, container, false);
        final TextView scoreTag = view.findViewById(R.id.tv_score_tag);
        final TextView scoreText = view.findViewById(R.id.tv_score);
        scoreText.setText(String.valueOf(mScore));
        ImageButton restart = view.findViewById(R.id.bt_restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.restart();
            }
        });

        DecoView decoView = view.findViewById(R.id.dynamicArcView);
        decoView.configureAngles(300, 0);
        // Builds background arc
        SeriesItem backgroundArc = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, 100, 0)
                .build();
        // Add the item to DecoView. Index will be used to pair animation and item
        int backIndex = decoView.addSeries(backgroundArc);
        // Builds baseScore arc
        SeriesItem baseScoreArc = new SeriesItem.Builder(Color.parseColor("#FF4081"))
                .setRange(0, 100, 0)
                .build();
        baseScoreArc.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                String format = "%.0f%%";
                scoreText.setText(String.format(format, mPercentScore));
            }
            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
                // no op
            }
        });
        // Add the item to DecoView. Index will be used to pair animation and item
        int baseScoreIndex = decoView.addSeries(baseScoreArc);

        // Builds bonusScore arc
        SeriesItem bonusScoreArc = new SeriesItem.Builder(Color.parseColor("#2bc327"))
                .setRange(0, 100, 0)
                .setLineWidth(25f)
                .setInitialVisibility(false)
                .setInset(new PointF(-15f, -15f))
                .build();
        bonusScoreArc.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentProgress) {
                if(currentProgress > 1.0) {
                    scoreTag.setText("BONUS");
                    scoreTag.setTextColor(getResources().getColor(R.color.colorGreen));
                    scoreText.setText(String.valueOf(mBonus));
                    scoreText.setTextColor(getResources().getColor(R.color.colorGreen));
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float v) {
                // no op
            }
        });
        int bonusScoreIndex = decoView.addSeries(bonusScoreArc);

        // Background arc animation
        decoView.addEvent(new DecoEvent.Builder(100)
                .setIndex(backIndex)
                .setInterpolator(new DecelerateInterpolator())
                .build());
        // BaseScore animation
        decoView.addEvent(new DecoEvent.Builder(mPercentScore)
                .setIndex(baseScoreIndex)
                .setDelay(500)
                .setInterpolator(new DecelerateInterpolator())
                .build());
        // BonusScore animation
        decoView.addEvent(new DecoEvent.Builder(mPercentBonus)
                .setIndex(bonusScoreIndex)
                .setDelay(2500)
                .setInterpolator(new DecelerateInterpolator())
                .build());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScoreFragment.OnFragmentInteractionListener) {
            mListener = (ScoreFragment.OnFragmentInteractionListener) context;
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
}