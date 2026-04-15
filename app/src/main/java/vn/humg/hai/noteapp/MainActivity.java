package vn.humg.hai.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> noteList;
    private ActivityResultLauncher<Intent> noteLauncher;
    private FloatingActionButton fabAdd;
    private EditText edtSearch;
    private ImageButton btnChangeLayout;
    private NoteDatabase database;
    private boolean isGridView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = NoteDatabase.getInstance(this);
        
        edtSearch = findViewById(R.id.edt_search);
        fabAdd = findViewById(R.id.fab_add);
        recyclerView = findViewById(R.id.recycler_view);
        btnChangeLayout = findViewById(R.id.btn_change_layout);

        noteList = new ArrayList<>();
        updateLayoutManager();

        noteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        if (data.hasExtra("note_updated")) {
                            Note updatedNote = (Note) data.getSerializableExtra("note_updated");
                            if (updatedNote != null) {
                                database.noteDao().update(updatedNote);
                            }
                        } else {
                            String title = data.getStringExtra("note_title");
                            String content = data.getStringExtra("note_content");
                            int color = data.getIntExtra("note_color", 0xFFFFFFFF);
                            boolean isImportant = data.getBooleanExtra("note_important", false);
                            
                            Note newNote = new Note(title, content);
                            newNote.setColor(color);
                            newNote.setImportant(isImportant);
                            database.noteDao().insert(newNote);
                        }
                        reloadNotes();
                    }
                }
        );

        adapter = new NoteAdapter(new ArrayList<>(), new NoteAdapter.OnNoteActionListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("note_to_edit", note);
                noteLauncher.launch(intent);
            }

            @Override
            public void onNotePinChanged(Note note) {
                note.setPinned(!note.isPinned());
                database.noteDao().update(note);
                reloadNotes();
            }

            @Override
            public void onNoteDelete(Note note) {
                database.noteDao().delete(note);
                reloadNotes();
            }
        });
        recyclerView.setAdapter(adapter);
        
        reloadNotes();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            noteLauncher.launch(intent);
        });

        btnChangeLayout.setOnClickListener(v -> {
            isGridView = !isGridView;
            updateLayoutManager();
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refreshDisplayList();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateLayoutManager() {
        int spanCount = isGridView ? 2 : 1;
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        btnChangeLayout.setImageResource(isGridView ? android.R.drawable.ic_dialog_dialer : android.R.drawable.ic_menu_sort_by_size);
    }

    private void reloadNotes() {
        noteList = database.noteDao().getAllNotes();
        refreshDisplayList();
    }

    private void refreshDisplayList() {
        Collections.sort(noteList, (n1, n2) -> {
            if (n1.isPinned() != n2.isPinned()) {
                return Boolean.compare(n2.isPinned(), n1.isPinned());
            }
            return Long.compare(n2.getTimestamp(), n1.getTimestamp());
        });

        String query = edtSearch.getText().toString().toLowerCase().trim();
        List<Note> filteredList = new ArrayList<>();
        for (Note note : noteList) {
            if (note.getTitle().toLowerCase().contains(query) ||
                    note.getContent().toLowerCase().contains(query)) {
                filteredList.add(note);
            }
        }
        adapter.updateList(filteredList);
    }
}