package barreto.alessandro.curriculo.view.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import barreto.alessandro.curriculo.R;
import barreto.alessandro.curriculo.view.activity.ChatActivity;

/**
 * Created by Alessandro on 07/07/2016.
 */
public class FragContato extends Fragment implements View.OnClickListener {

    static final String PRIMEIRAZVEZ = "primeiraVez";
    static final String NOME = "nome";
    static final String SALA_CHAT = "chat";

    private boolean primeiraVez;
    private String nome;
    private String salaChat;

    ProgressDialog progressDialog;

    public FragContato() {
    }

    SharedPreferences preferences ;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_contato, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        verificaPreferencias();
        view.findViewById(R.id.btnChat).setOnClickListener(this);
        ImageView imageView = (ImageView)view.findViewById(R.id.ivChat);
        Picasso.with(getActivity()).load(R.drawable.chat).resize(300,300).centerCrop().into(imageView);
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        verificaPreferencias();
    }

    @Override
    public void onClick(View v) {
        if (primeiraVez){
            addLoad();
            dialogAdicionarNome();
        }else{
            startActivity(new Intent(getActivity(), ChatActivity.class));
        }

    }


    private void verificaPreferencias(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        primeiraVez = preferences.getBoolean(PRIMEIRAZVEZ,true);
        nome = preferences.getString(NOME,"");
        salaChat = preferences.getString(SALA_CHAT,"");
    }

    private void editPreferencias(String nome){
        preferences.edit().putBoolean(PRIMEIRAZVEZ,false).apply();
        preferences.edit().putString(NOME,nome).apply();
        preferences.edit().putString(SALA_CHAT,nome+SALA_CHAT).apply();
        verificaPreferencias();
    }

    private void dialogAdicionarNome(){
        if (primeiraVez){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setCancelable(false); // nao pode cancelar o dialog
            alertDialog.setTitle(getResources().getString(R.string.dialog_titulo));
            alertDialog.setMessage(getResources().getString(R.string.dialog_mensagem));
            final EditText editTextNome = new EditText(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            editTextNome.setLayoutParams(lp);
            alertDialog.setView(editTextNome);
            alertDialog.setPositiveButton(getResources().getString(R.string.botao_dialog_sim), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!verificaEditTextNull(editTextNome)){
                        final String nome = editTextNome.getText().toString();
                        Map<String,Object> map = new HashMap<>();
                        map.put(nome+SALA_CHAT,"");
                        root.updateChildren(map, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                editPreferencias(nome);
                                hideLoad();
                                startActivity(new Intent(getActivity(),ChatActivity.class));
                            }
                        });
                    }else{
                        hideLoad();
                        addToast();
                        dialogAdicionarNome();
                    }

                }
            });
            alertDialog.setNegativeButton(getResources().getString(R.string.botao_dialog_nao), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    private boolean verificaEditTextNull(EditText editText){
        String s = editText.getText().toString();
        return s.equals("") || TextUtils.isEmpty(s);
    }

    private void addToast(){
        Toast.makeText(getActivity(),getResources().getString(R.string.campo_vazio),Toast.LENGTH_SHORT).show();
    }

    private void addLoad(){
        if (progressDialog != null){
            progressDialog.setTitle(getResources().getString(R.string.dialog_load_titulo_contato));
            progressDialog.setMessage(getResources().getString(R.string.dialog_load_aguarde_contato));
            progressDialog.show();
        }

    }

    private void hideLoad(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
