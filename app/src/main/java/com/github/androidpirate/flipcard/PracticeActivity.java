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

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.github.androidpirate.flipcard.fragment.BackCardFragment;
import com.github.androidpirate.flipcard.fragment.CorrectCardFragment;
import com.github.androidpirate.flipcard.fragment.FrontCardFragment;
import com.github.androidpirate.flipcard.fragment.ScoreFragment;
import com.github.androidpirate.flipcard.model.FlipCard;
import com.github.androidpirate.flipcard.utils.CardFactoryUtils;

import java.util.ArrayList;

public class PracticeActivity extends SingleFragmentActivity implements
        FrontCardFragment.OnFragmentInteractionListener,
        BackCardFragment.OnFragmentInteractionListener,
        CorrectCardFragment.OnFragmentInteractionListener,
        ScoreFragment.OnFragmentInteractionListener {
    // In milliseconds
    private static final int ANIMATION_DELAY_TIME = 1500;
    private final ArrayList<FlipCard> mCards = CardFactoryUtils.getInstance("Test").getCards();
    private FlipCard mFlipCard;
    private int mCardIndex = 0;
    private int mScore = 0;

    @Override
    protected Fragment createFragment() {
        mFlipCard = mCards.get(mCardIndex);
        mProgressBar.setVisibility(View.VISIBLE);
        getSupportActionBar().hide();
        return FrontCardFragment.newInstance(mFlipCard);
    }

    @Override
    public void flipToBack() {
        Fragment fragment = BackCardFragment.newInstance(mFlipCard);
        replaceCard(fragment);
    }

    @Override
    public void displayCorrectAnswerAnimation() {
        updateScore();
        Fragment fragment = CorrectCardFragment.newInstance();
        replaceCard(fragment);
    }

    private void updateScore(){
        mScore++;
    }

    @Override
    public void moveToNextCard() {
        if(++mCardIndex < mCards.size()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCardIndex = mCardIndex % mCards.size();
                    mFlipCard = mCards.get(mCardIndex);
                    updateProgress();
                    Fragment fragment = FrontCardFragment.newInstance(mFlipCard);
                    replaceCard(fragment);
                }
            }, ANIMATION_DELAY_TIME);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Fragment fragment = ScoreFragment.newInstance(mScore);
                    replaceCard(fragment);
                }
            }, ANIMATION_DELAY_TIME);
        }
    }

    private void replaceCard(Fragment fragment) {
        int enterAnimRes = 0;
        int exitAnimRes = 0;
        if(fragment instanceof BackCardFragment) {
            enterAnimRes = R.animator.card_flip_right_in;
            exitAnimRes = R.animator.card_flip_right_out;
        } else if (fragment instanceof CorrectCardFragment ||
                    fragment instanceof FrontCardFragment ||
                    fragment instanceof ScoreFragment) {
            enterAnimRes = R.animator.card_right_in;
            exitAnimRes = R.animator.card_left_out;
        }
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(enterAnimRes, exitAnimRes)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void updateProgress() {
        float deckSize = mCards.size();
        float index = mCardIndex;
        float progress = 1;
        if(mCardIndex != 0) {
            progress = (index / deckSize) * 100;
        }
        mProgressBar.setProgress((int) progress);
    }

    @Override
    public void restart() {
        finish();
        startActivity(new Intent(this, PracticeActivity.class));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.exit_dialog_title)
                .setMessage(R.string.exit_dialog_message)
                .setPositiveButton(R.string.exit_dialog_positive_button_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })
                .setNegativeButton(R.string.exit_dialog_negative_button_text,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
        builder.create().show();
    }
}