package me.vickychijwani.thrones.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.vickychijwani.thrones.R;
import me.vickychijwani.thrones.ThronesApplication;
import me.vickychijwani.thrones.data.entity.Episode;
import me.vickychijwani.thrones.network.HboApi;

public class SynopsisFragment extends Fragment {

    public static final String KEY_EPISODE = "episode";
    private static final String TAG = "SynopsisFragment";

    public static SynopsisFragment newInstance(@NonNull Episode episode) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_EPISODE, episode);
        SynopsisFragment fragment = new SynopsisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_synopsis, container, false);
        ImageView synopsisImageView = (ImageView) view.findViewById(R.id.synopsis_image);
        TextView numberView = (TextView) view.findViewById(R.id.episode_number);
        TextView titleView = (TextView) view.findViewById(R.id.episode_title);
        TextView synopsisTextView = (TextView) view.findViewById(R.id.synopsis_text);
        View closeView = view.findViewById(R.id.close);

        Episode episode = getArguments().getParcelable(KEY_EPISODE);
        if (episode == null) {
            Log.wtf(TAG, "This isn't supposed to happen!");
            return view;
        }
        ThronesApplication.getInstance().getPicasso()
                .load(HboApi.getImageUrl(episode.synopsisImage, HboApi.ImageSize.LARGE))
                .fit()
                .centerCrop()
                .into(synopsisImageView);
        numberView.setText(getString(R.string.season_and_episode_number, episode.seasonNumber, episode.number));
        titleView.setText(episode.title);
        String synopsisHtml = HboApi.sanitizeHboSynopsisHtml(episode.synopsis);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            synopsisTextView.setText(Html.fromHtml(synopsisHtml, Html.FROM_HTML_MODE_COMPACT));
        } else {
            synopsisTextView.setText(Html.fromHtml(synopsisHtml));
        }
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SynopsisFragment.this.getActivity().finish();
            }
        });

        return view;
    }
}