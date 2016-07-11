package barreto.alessandro.curriculo.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import barreto.alessandro.curriculo.R;

/**
 * Created by Alessandro on 07/07/2016.
 */
public class FragHabilidades extends Fragment{

    public FragHabilidades() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_habilidade, container, false);
    }

}
