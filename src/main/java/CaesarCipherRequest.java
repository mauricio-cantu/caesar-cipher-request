import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class CaesarCipherRequest {

    private static char[] alphabet;
    private static final Gson gson = new Gson();
    private static final String TOKEN = "4f30b2bd0879ac3178cc67f93f0b1e207306befd";

    public static void main(String[] args) {
        populateAlphabet();

        try {
            getFileAndSaveIt();
            AnswerFile answerFile = getAnswerFileObject();
            String decipheredText = getDecipheredText(answerFile.getCipheredText(), answerFile.getDisplacement());
            answerFile.setDecipheredText(decipheredText);
            answerFile.setCipheredHash(DigestUtils.sha1Hex(decipheredText));
            saveFile(gson.toJson(answerFile));
            sendFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void populateAlphabet() {
        alphabet = new char[26];
        int i = 0;

        for (char c = 'a'; c <= 'z'; c++) {
            alphabet[i++] = c;
        }
    }

    private static String getDecipheredText(String ciphered, int places) {
        StringBuilder stringBuilder = new StringBuilder(ciphered);

        for (int i = 0; i < ciphered.length(); i++) {
            stringBuilder.setCharAt(i, getDecipheredChar(ciphered.charAt(i), places));
        }

        return stringBuilder.toString();
    }

    private static char getDecipheredChar(char character, int places) {
        char decipheredChar = character;

        if (!Character.isLetter(character))
            return decipheredChar;

        for (int i = 0; i < alphabet.length; i++) {
            if (character == alphabet[i]) {
                decipheredChar = alphabet[Math.floorMod((i - places), 26)];
            }
        }

        return decipheredChar;
    }

    private static void getFileAndSaveIt() throws Exception {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=" + TOKEN)).build();
        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply(HttpResponse::body).get();
        saveFile(result);
    }

    private static void sendFile() throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(URI.create("https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=" + TOKEN));
        FileBody uploadFile = new FileBody(new File("answer.json"));

        MultipartEntity multipartEntity = new MultipartEntity();
        multipartEntity.addPart("answer", uploadFile);

        postRequest.setEntity(multipartEntity);
        org.apache.http.HttpResponse httpResponse = httpClient.execute(postRequest);
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }

    private static void saveFile(String content) throws IOException {
        FileWriter answerFile = new FileWriter("answer.json");
        answerFile.write(content);
        answerFile.close();
    }

    private static AnswerFile getAnswerFileObject() throws Exception {
        AnswerFile fileObject;
        BufferedReader br = new BufferedReader(new FileReader("answer.json"));
        fileObject = gson.fromJson(br, AnswerFile.class);

        return fileObject;
    }

}
