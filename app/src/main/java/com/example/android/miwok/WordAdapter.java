package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adeen-s on 9/2/17.
 */


public class WordAdapter extends ArrayAdapter<Word> {

    public MediaPlayer mediaPlayer = null;
    Context mContext = getContext();
    private AudioManager mAudioManager;
    private int mColorResourceId;

    public WordAdapter(Context context, ArrayList<Word> pWords, int colorResourceId) {
        super(context, 0, pWords);
        mColorResourceId = colorResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Word local_word = getItem(position);

        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(local_word.getMiwokTranslation());

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaultTextView.setText(local_word.getDefaultTranslation());

        ImageView playButton = (ImageView) listItemView.findViewById(R.id.play_button_container);

        View textContainer = listItemView.findViewById(R.id.text_container);
        View bigContainer = listItemView.findViewById(R.id.big_container);

        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);
        playButton.setBackgroundColor(color);
        bigContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                            // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                            // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                            // our app is allowed to continue playing sound but at a lower volume. We'll treat
                            // both cases the same way because our app is playing short sound files.

                            // Pause playback and reset player to the start of the file. That way, we can
                            // play the word from the beginning when we resume playback.
                            mediaPlayer.pause();
                            mediaPlayer.seekTo(0);
                        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                            // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                            mediaPlayer.start();
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                            // Stop playback and clean up resources
                            mediaPlayer.release();
                        }
                    }
                };
                mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getContext(), local_word.getAudioResourceId());
                    mediaPlayer.start();
                    Log.v("MediaPlayer", "AFTER calling start()");
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release();
                                mediaPlayer = null;
                                Log.v("mediaPlayerRelease", "media player released successfully");
                            }
                        }
                    });
                    mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
                }
            }
        });

        ImageView image = (ImageView) listItemView.findViewById(R.id.image);

        if (local_word.hasImage()) {
            image.setImageResource(local_word.getImageResourceId());
            image.setVisibility(View.VISIBLE);
        } else
            image.setVisibility(View.GONE);

        return listItemView;
    }
}