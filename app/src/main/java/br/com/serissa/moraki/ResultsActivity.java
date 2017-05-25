package br.com.serissa.moraki;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    public static final String EXTRA_TIPO = "tipo";
    public static final String EXTRA_CIDADE = "cidade";
    public static final String EXTRA_BAIRRO = "bairro";
    public static final String EXTRA_DORMITORIOS = "dorms";
    public static final String EXTRA_VAGAS = "vagas";

    public static Intent getIntent(Context context, @NonNull String tipo, @NonNull String cidade, @Nullable String bairro, @Nullable String dormitorios, @Nullable String vagas) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(EXTRA_TIPO, tipo);
        intent.putExtra(EXTRA_CIDADE, cidade);
        if (!TextUtils.isEmpty(bairro)) {
            intent.putExtra(EXTRA_BAIRRO, bairro);
        }
        if (!TextUtils.isEmpty(dormitorios) && TextUtils.isDigitsOnly(dormitorios)) {
            try {
                intent.putExtra(EXTRA_DORMITORIOS, Integer.parseInt(dormitorios));
            } catch (NumberFormatException ignored) {
                //
            }
        }
        if (!TextUtils.isEmpty(vagas) && TextUtils.isDigitsOnly(vagas)) {
            try {
                intent.putExtra(EXTRA_VAGAS, Integer.parseInt(vagas));
            } catch (NumberFormatException ignored) {
                //
            }
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Bundle extras = getIntent().getExtras();
        StringBuilder sb = new StringBuilder("Procurando imóveis com as seguintes características:\n\n");
        sb.append("Tipo: ");
        sb.append(extras.getString(EXTRA_TIPO));
        sb.append("\n\n");
        sb.append("Cidade: ");
        sb.append(extras.getString(EXTRA_CIDADE));
        sb.append("\n\n");
        if (extras.containsKey(EXTRA_BAIRRO)) {
            sb.append("Bairro: ");
            sb.append(extras.getString(EXTRA_BAIRRO));
            sb.append("\n\n");
        }
        if (extras.containsKey(EXTRA_DORMITORIOS)) {
            sb.append("Dormitórios: ");
            sb.append(extras.getInt(EXTRA_DORMITORIOS));
            sb.append("\n\n");
        }
        if (extras.containsKey(EXTRA_VAGAS)) {
            sb.append("Vagas: ");
            sb.append(extras.getInt(EXTRA_VAGAS));
        }

        ((TextView) findViewById(R.id.results)).setText(sb.toString());
    }
}
