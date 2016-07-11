package barreto.alessandro.curriculo.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import barreto.alessandro.curriculo.R;

/**
 * Created by Alessandro on 07/07/2016.
 */
public class FragProjetos extends Fragment implements View.OnClickListener{

    public FragProjetos() {
    }

    CustomTabsIntent customTabsIntent;
    CustomTabsIntent.Builder intentBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_projetos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Cards Projetos
        view.findViewById(R.id.card1).setOnClickListener(this);
        view.findViewById(R.id.card2).setOnClickListener(this);
        view.findViewById(R.id.card3).setOnClickListener(this);

        //Imagens para layout
        Picasso.with(getActivity()).load(R.drawable.p1).into((ImageView)view.findViewById(R.id.iv1));
        Picasso.with(getActivity()).load(R.drawable.p2).into((ImageView)view.findViewById(R.id.iv2));
        Picasso.with(getActivity()).load(R.drawable.p3).into((ImageView)view.findViewById(R.id.iv3));

        // Custom tabs para links dos projeots
        intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        intentBuilder.setExitAnimations(getActivity(), R.anim.right_to_left_end, R.anim.left_to_right_end);
        intentBuilder.setStartAnimations(getActivity(), R.anim.left_to_right_start, R.anim.right_to_left_start);
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        customTabsIntent = intentBuilder.build();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card1:
                customTabsIntent.launchUrl(getActivity(), Uri.parse("https://github.com/AleBarreto/DragRecyclerView"));
                break;
            case R.id.card2:
                customTabsIntent.launchUrl(getActivity(), Uri.parse("https://github.com/AleBarreto/FirebaseAndroidChat"));
                break;
            case R.id.card3:
                customTabsIntent.launchUrl(getActivity(), Uri.parse("https://github.com/AleBarreto/SimpleLoginLibrary"));
                break;
        }
    }

}
