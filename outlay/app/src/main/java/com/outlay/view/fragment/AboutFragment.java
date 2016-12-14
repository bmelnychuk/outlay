package com.outlay.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.outlay.R;
import com.outlay.view.fragment.base.StaticContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class AboutFragment extends StaticContentFragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.githubContainer)
    View githubContainer;

    @Bind(R.id.aboutText)
    TextView aboutText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null, false);
        ButterKnife.bind(this, view);
        setToolbar(toolbar);
        setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(getString(R.string.caption_about));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        githubContainer.setOnClickListener(v -> {
            String url = getString(R.string.text_github_url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        aboutText.setText(Html.fromHtml(getString(R.string.text_about)));
        aboutText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
