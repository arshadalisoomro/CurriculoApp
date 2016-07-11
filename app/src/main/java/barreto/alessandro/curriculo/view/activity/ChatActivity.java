package barreto.alessandro.curriculo.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import barreto.alessandro.curriculo.R;
import barreto.alessandro.curriculo.adapter.MyAdapterChat;
import barreto.alessandro.curriculo.model.ChatModel;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    static final String NOME = "nome";
    static final String SALA_CHAT = "chat";

    SharedPreferences preferences ;
    private String nome;
    private String salaChat;

    private RecyclerView mRecyclerViewChat;
    private LinearLayoutManager mLinearLayoutManager;
    private EditText mEditTextChat;
    private Button mButtonChat;

    private DatabaseReference root ;

    MyAdapterChat myAdapterChat;
    List<ChatModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        nome = preferences.getString(NOME,"");
        salaChat = preferences.getString(SALA_CHAT,"");

        root = FirebaseDatabase.getInstance().getReference().child(salaChat);

        mRecyclerViewChat = (RecyclerView)findViewById(R.id.recycler_view_chat);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerViewChat.setLayoutManager(mLinearLayoutManager);

        mButtonChat = (Button)findViewById(R.id.btn_send);
        mButtonChat.setOnClickListener(this);
        mEditTextChat = (EditText)findViewById(R.id.edit_mensagem);

    }

    @Override
    protected void onResume() {
        super.onResume();
        myAdapterChat = new MyAdapterChat(mList,this);
        mRecyclerViewChat.setAdapter(myAdapterChat);

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("TAG","onChildAdded "+dataSnapshot.toString());
                addData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (!verificaEditTextNull(mEditTextChat)){
            Map<String,Object> map = new HashMap<>();
            String temp_key = root.push().getKey();
            root.updateChildren(map);
            DatabaseReference mensagem_root = root.child(temp_key);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("nome",nome);
            map2.put("toke",FirebaseInstanceId.getInstance().getToken());
            map2.put("msg",mEditTextChat.getText().toString());
            mensagem_root.updateChildren(map2);
            mEditTextChat.setText("");
        }else{
            mEditTextChat.setError(getResources().getString(R.string.campo_vazio));
        }
    }

    private void addData(DataSnapshot dataSnapshot) {
        String msg,nome_usuario,token;
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            msg = (String) ((DataSnapshot) i.next()).getValue();
            nome_usuario = (String) ((DataSnapshot) i.next()).getValue();
            token = (String) ((DataSnapshot) i.next()).getValue();
            mList.add(new ChatModel(nome_usuario,msg,token));
            myAdapterChat.notifyItemInserted(mList.size()-1);
        }
        mRecyclerViewChat.scrollToPosition(myAdapterChat.getItemCount() -1);
    }

    private boolean verificaEditTextNull(EditText editText){
        String s = editText.getText().toString();
        return s.equals("") || TextUtils.isEmpty(s);
    }

}
