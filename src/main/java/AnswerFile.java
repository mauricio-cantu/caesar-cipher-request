import com.google.gson.annotations.SerializedName;

public class AnswerFile {

    @SerializedName(value = "numero_casas")
    private int displacement;

    private String token;

    @SerializedName(value = "cifrado")
    private String cipheredText;

    @SerializedName(value = "decifrado")
    private String decipheredText;

    @SerializedName(value = "resumo_criptografico")
    private String cipheredHash;

    public int getDisplacement() {
        return displacement;
    }

    public String getCipheredText() {
        return cipheredText;
    }

    public void setDecipheredText(String decipheredText) {
        this.decipheredText = decipheredText;
    }

    public void setCipheredHash(String cipheredHash) {
        this.cipheredHash = cipheredHash;
    }

}
