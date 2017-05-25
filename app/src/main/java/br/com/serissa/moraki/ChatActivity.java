package br.com.serissa.moraki;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatActivity extends AppCompatActivity {

    AIDataService aiService;
    MessagesListAdapter<IMessage> messagesAdapter;
    MessageInput messageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagesAdapter = new MessagesListAdapter<>(Message.getUserId(), null);
        MessagesList messagesList = (MessagesList) findViewById(R.id.messagesList);
        messagesList.setAdapter(messagesAdapter);
        messageInput = (MessageInput) findViewById(R.id.input);
        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(final CharSequence input) {
                sendRequest(input.toString());
                return true;
            }
        });

        messagesAdapter.addToStart(Message.fromBot("Oi, sou o chatbot da Moraki. Vou te ajudar a achar um imóvel."), true);
        messagesAdapter.addToStart(Message.fromBot("Digite os detalhes do imóvel que você procura, como o tipo de imóvel, a cidade e outros detalhes relevantes."), true);

        initChatBot();
    }

    private void sendRequest(String input) {
        messagesAdapter.addToStart(Message.fromUser(input), true);
        final AIRequest request = new AIRequest();
        request.setQuery(input);
        messageInput.setEnabled(false);
        new AiAsyncRequest(this).execute(request);
    }

    private void initChatBot() {
        final AIConfiguration config = new ai.api.android.AIConfiguration("283d360ce9854141bf08ab0d48586319",
                AIConfiguration.SupportedLanguages.PortugueseBrazil, AIConfiguration.RecognitionEngine.System);

        aiService = new AIDataService(config);
    }

    private static class AiAsyncRequest extends AsyncTask<AIRequest, Void, AIResponse> {

        private WeakReference<ChatActivity> activityRef;

        AiAsyncRequest(ChatActivity chatActivity) {
            activityRef = new WeakReference<>(chatActivity);
        }

        @Override
        protected AIResponse doInBackground(AIRequest... request) {
            ChatActivity activity = activityRef.get();
            if (activity == null || request == null || request.length == 0) {
                cancel(true);
                return null;
            }

            try {
                return activity.aiService.request(request[0]);
            } catch (AIServiceException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {

            ChatActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            activity.messageInput.setEnabled(true);

            if (isCancelled()) {
                return;
            }

            if (aiResponse == null) {
                activity.messagesAdapter.addToStart(Message.fromBot("Houve um erro ao processar sua solicitação, sinto muito."), true);
                return;
            }

            Result result = aiResponse.getResult();
            HashMap<String, JsonElement> resultParams = result.getParameters();

            if (resultParams.size() == 0 || result.isActionIncomplete()) {
                activity.messagesAdapter.addToStart(Message.fromBot(result.getFulfillment().getSpeech()), true);
            } else {
                Intent intent = ResultsActivity.getIntent(activity,
                        getValueFromParams(resultParams.get("tipo")),
                        getValueFromParams(resultParams.get("cidade")),
                        getValueFromParams(resultParams.get("bairro")),
                        getNumFromJsonElem(resultParams.get("dorms")),
                        getNumFromJsonElem(resultParams.get("vagas")));
                activity.startActivity(intent);
                activity.finish();
            }
        }

        private static String getNumFromJsonElem(JsonElement jsonElement) {
            if (jsonElement != null) {
                if (jsonElement.isJsonObject()) {
                    JsonElement numberElement = ((JsonObject) jsonElement).get("number");
                    if (numberElement != null) {
                        return numberElement.toString();
                    }
                    return "1";
                }
                return jsonElement.toString();
            }
            return null;
        }

        private static String getValueFromParams(JsonElement jsonElement) {
            return jsonElement != null ? jsonElement.getAsString() : null;
        }
    }
}
