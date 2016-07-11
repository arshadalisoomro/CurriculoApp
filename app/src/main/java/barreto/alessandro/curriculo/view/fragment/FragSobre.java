package barreto.alessandro.curriculo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import barreto.alessandro.curriculo.R;

/**
 * Created by Alessandro on 07/07/2016.
 */
public class FragSobre extends Fragment{

    public FragSobre() {

    }

    //ids dos cards
    private int ids[] = new int[]{R.id.c1,R.id.c2,R.id.c3,R.id.c4,R.id.c5};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_sobre, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Animação
        Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        for (int id : ids) view.findViewById(id).setAnimation(animFadein);
    }

}
