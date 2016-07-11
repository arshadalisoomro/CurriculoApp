package barreto.alessandro.curriculo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import barreto.alessandro.curriculo.R;
import barreto.alessandro.curriculo.model.ChatModel;

/**
 * Created by Alessandro on 10/07/2016.
 */
public class MyAdapterChat extends RecyclerView.Adapter<MyAdapterChat.MyVIewHolder>{

    static final int DIREITA = 0;
    static final int ESQUERDA = 1;
    static final String NOME = "nome";

    private String nome;
    SharedPreferences preferences ;
    private final Context context;

    private List<ChatModel> mList;

    public MyAdapterChat(List<ChatModel> mList, Context context) {
        this.mList = mList;
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        nome = preferences.getString(NOME,"");
    }

    @Override
    public MyVIewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == DIREITA){
            return new MyVIewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_direita,parent,false));
        }else {
            return new MyVIewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_esquerda,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(MyVIewHolder holder, int position) {
        ChatModel model = mList.get(position);
        if (model.getMensagem() != null){
            holder.mTextView.setText(model.getMensagem());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getNome().equals(nome)){
            return DIREITA;
        }else {
            return ESQUERDA;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyVIewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;

        public MyVIewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.textView);
        }

    }

}
