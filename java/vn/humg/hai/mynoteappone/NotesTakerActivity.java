package vn.humg.hai.mynoteappone;

import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.humg.hai.mynoteappone.models.Notes;

public class NotesTakerActivity extends AppCompatActivity {

    EditText editText_title , editText_notes;
    ImageView imageView_save;
    Notes notes;
    private final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes_taker);

    }
}