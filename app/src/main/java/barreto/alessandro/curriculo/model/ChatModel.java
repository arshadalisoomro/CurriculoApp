package barreto.alessandro.curriculo.model;

/**
 * Created by Alessandro on 10/07/2016.
 */
public class ChatModel {
    String nome;
    String mensagem;
    String token;

    public ChatModel() {
    }

    public ChatModel(String nome, String mensagem,String token) {
        this.nome = nome;
        this.mensagem = mensagem;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
