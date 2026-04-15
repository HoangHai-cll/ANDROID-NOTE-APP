package vn.humg.hai.noteapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {
    private EditText edtTitle, edtContent;
    private Button btnSave;
    private CheckBox cbImportant;
    private Note existingNote;
    private int selectedColor = 0xFFFFFFFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
        btnSave = findViewById(R.id.btn_save);
        cbImportant = findViewById(R.id.cb_important);

        setupColorPickers();

        if (getIntent().hasExtra("note_to_edit")) {
            existingNote = (Note) getIntent().getSerializableExtra("note_to_edit");
            if (existingNote != null) {
                edtTitle.setText(existingNote.getTitle());
                edtContent.setText(existingNote.getContent());
                selectedColor = existingNote.getColor();
                cbImportant.setChecked(existingNote.isImportant());
                updateBackgroundColor(selectedColor);
                btnSave.setText("Cập nhật");
            }
        }

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString();
            String content = edtContent.getText().toString();
            boolean isImportant = cbImportant.isChecked();

            if (title.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề!", Toast.LENGTH_SHORT).show();
            } else {
                Intent returnIntent = new Intent();
                
                if (existingNote != null) {
                    existingNote.setTitle(title);
                    existingNote.setContent(content);
                    existingNote.setColor(selectedColor);
                    existingNote.setImportant(isImportant);
                    existingNote.setTimestamp(System.currentTimeMillis());
                    returnIntent.putExtra("note_updated", existingNote);
                } else {
                    returnIntent.putExtra("note_title", title);
                    returnIntent.putExtra("note_content", content);
                    returnIntent.putExtra("note_color", selectedColor);
                    returnIntent.putExtra("note_important", isImportant);
                }
                
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void setupColorPickers() {
        View.OnClickListener colorClickListener = v -> {
            if (v.getId() == R.id.color_white) selectedColor = 0xFFFFFFFF;
            else if (v.getId() == R.id.color_red) selectedColor = 0xFFFFCDD2;
            else if (v.getId() == R.id.color_blue) selectedColor = 0xFFBBDEFB;
            else if (v.getId() == R.id.color_green) selectedColor = 0xFFC8E6C9;
            else if (v.getId() == R.id.color_yellow) selectedColor = 0xFFFFF9C4;
            
            updateBackgroundColor(selectedColor);
        };

        findViewById(R.id.color_white).setOnClickListener(colorClickListener);
        findViewById(R.id.color_red).setOnClickListener(colorClickListener);
        findViewById(R.id.color_blue).setOnClickListener(colorClickListener);
        findViewById(R.id.color_green).setOnClickListener(colorClickListener);
        findViewById(R.id.color_yellow).setOnClickListener(colorClickListener);
    }

    private void updateBackgroundColor(int color) {
        findViewById(R.id.layout_add_note).setBackgroundColor(color);
    }
}