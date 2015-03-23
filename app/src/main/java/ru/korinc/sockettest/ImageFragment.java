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
    private static final String HINT_1 = "hint1";
    private static final String HINT_2 = "hint2";
    private static final String IMAGE_ID = "imageId";


    private String screenTitle;
    private String hint1;
    private String hint2;
    private int imageId;

    ImageView image;
    TextView hintTv1;
    TextView hintTv2;
    TextView title;


    public static ImageFragment newInstance(String param1, String param2, String param3, int imageId) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(SCREEN_TITTLE, param3);
        args.putString(HINT_1, param1);
        args.putString(HINT_2, param2);
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
            hint1 = getArguments().getString(HINT_1);
            hint2 = getArguments().getString(HINT_2);
            imageId = getArguments().getInt(IMAGE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        hintTv1 = (TextView) v.findViewById(R.id.hint1);
        hintTv2 = (TextView) v.findViewById(R.id.hint2);
        title = (TextView) v.findViewById(R.id.title);
        image = (ImageView) v.findViewById(R.id.image);

                hintTv1.setText(hint1);
        hintTv2.setText(hint2);
        title.setText(screenTitle);
        image.setImageResource(imageId);
        return v;

    }





    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
