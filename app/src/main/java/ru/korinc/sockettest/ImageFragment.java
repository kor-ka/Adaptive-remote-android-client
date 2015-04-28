package ru.korinc.sockettest;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageFragment extends android.support.v4.app.Fragment {

    private static final String SCREEN_TITTLE = "screenTitle";
    private static final String HINTS = "hints";

    private static final String IMAGE_ID = "imageId";


    private String screenTitle;
    private String[] hints;

    private int imageId;

    ImageView image;
    TextView hintsTv;

    TextView title;


    public static ImageFragment newInstance(String[] hints, String title, int imageId) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(SCREEN_TITTLE, title);
        args.putStringArray(HINTS, hints);

        args.putInt(IMAGE_ID, imageId);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            screenTitle = getArguments().getString(SCREEN_TITTLE);
            hints = getArguments().getStringArray(HINTS);
            imageId = getArguments().getInt(IMAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        title = (TextView) v.findViewById(R.id.title);
        hintsTv = (TextView) v.findViewById(R.id.hints);

        image = (ImageView) v.findViewById(R.id.image);

        title.setText(screenTitle);
        hintsTv.setText("");
        int i = 0;
        for(String hint:hints){
            hintsTv.append(++i+". " + hint + "\n");
        }


        image.setImageResource(imageId);
        return v;

    }





    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
