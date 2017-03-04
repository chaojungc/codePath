package com.codepath.apps.simpletweets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;

import org.parceler.Parcels;

/**
 * Created by ChaoJung on 2017/3/3.
 */

public class ComposeTweetDialogFragment extends DialogFragment {

    //private FragmentComposeTweetBinding binding;

    private ImageView ivProfileImage;
    private TextView tvUserName;
    private TextView tvUserID;
    private EditText etTweetContent;
    private TextView tvCountText;
    private Button btnTweet;
    User loginUser;
    Tweet currentTweet;
    private ComposeTweetDialogListener mCallback;

    public ComposeTweetDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface ComposeTweetDialogListener {
        void onFinishComposeDialog(String status);
    }

    public static ComposeTweetDialogFragment newInstance(User loginUser,Tweet currentTweet) {
        ComposeTweetDialogFragment frag = new ComposeTweetDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("loginUser", Parcels.wrap(loginUser));
        args.putParcelable("currentTweet", Parcels.wrap(currentTweet));
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvUserID = (TextView) view.findViewById(R.id.tvUserID);
        etTweetContent = (EditText) view.findViewById(R.id.etTweetContent);
        tvCountText = (TextView) view.findViewById((R.id.tvCountText));
        btnTweet = (Button) view.findViewById(R.id.btnTweet);

        // Fetch arguments from bundle and set title
        loginUser = (User) Parcels.unwrap(getArguments().getParcelable("loginUser"));
        currentTweet = (Tweet) Parcels.unwrap(getArguments().getParcelable("currentTweet"));
//        int source = getArguments().getInt("source");
//        getDialog().setTitle(title);

        if(currentTweet.getBody() == null){
            etTweetContent.setText("");
        }
        else{
            etTweetContent.setText("@"+currentTweet.getUser().getScreenName());
        }

        etTweetContent.requestFocus();
        etTweetContent.addTextChangedListener(mTextEditorWatcher);
        etTweetContent.setSelection(etTweetContent.getText().length());

        tvUserName.setText(loginUser.getName());
        tvUserID.setText("@"+loginUser.getScreenName());
        Glide.with(this).load(loginUser.getProfileImageUrl()).into(ivProfileImage);


        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status = etTweetContent.getText().toString();

                ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
                listener.onFinishComposeDialog(status);
                dismiss();

            }
        });
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            if (context instanceof ComposeTweetDialogListener){
                mCallback = (ComposeTweetDialogListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCountText.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

}
