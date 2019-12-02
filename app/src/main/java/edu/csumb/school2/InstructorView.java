package edu.csumb.school2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.csumb.school2.model.Course;
import edu.csumb.school2.model.Instructor;
import edu.csumb.school2.model.School;

public class InstructorView extends AppCompatActivity  {

    private TextView status;

    private InstructorAdapter instructorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("InstructorView", "onCreate called");
        setContentView(R.layout.instructor_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        status = findViewById(R.id.textView2);

        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        instructorAdapter = new InstructorAdapter();
        rv.setAdapter(instructorAdapter);


        Button  cancel = findViewById(R.id.button2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("InstructorView", "onClick cancel called");
                finish();
            }
        });

        Button add  = findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("InstructorView", "add");
                addInstructorDialog( null );

            }
        });

        Button assign = findViewById(R.id.button5);
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("InstructorView", "assign");
                assignInstructorDialog( null );
            }
        });
    }

    private class InstructorAdapter extends RecyclerView.Adapter<InstructorHolder> {

        @Override
        public InstructorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(InstructorView.this);
            return new InstructorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(InstructorHolder holder, int position) {
            Instructor inst = School.getSchool().faculty.get(position);
            holder.bind(inst);
        }

        @Override
        public int getItemCount() {
            return School.getSchool().faculty.size();
        }
    }

    private class InstructorHolder extends RecyclerView.ViewHolder {

        public InstructorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.instructor_display, parent, false));
        }

        public void bind(Instructor instructor){
            TextView v = itemView.findViewById(R.id.instructor_display);
            v.setText( instructor.toString());
        }
    }

    private void addInstructorDialog(String message ) {
        // show dialog to add instructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (message != null) {
            builder.setTitle(message);
        } else {
            builder.setTitle("Add instructor");
        }
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_add_instructor, null);
        builder.setView(v);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add Instructor button
                EditText tId = v.findViewById(R.id.instructor_id);
                int iId = Integer.parseInt( tId.getText().toString() );
                EditText tName = v.findViewById(R.id.username);
                String username = tName.getText().toString();
                EditText tEmail = v.findViewById(R.id.email);
                String email = tEmail.getText().toString();
                EditText tPhone = v.findViewById(R.id.phone);
                String phone = tPhone.getText().toString();

                boolean rc = School.getSchool().addInstructor(iId, username, email, phone);

                if (rc) {
                    status.setText("Instructor added.");
                    // notify adapter that instructor list has changed
                    instructorAdapter.notifyDataSetChanged();
                } else {
                    addInstructorDialog("Duplicate id.");
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void assignInstructorDialog(String message) {
        // show dialog to add instructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (message!=null) {
            builder.setTitle(message);
        } else {
            builder.setTitle("Assign instructor");
        }
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_assign_instructor, null);
        builder.setView(v);

        builder.setPositiveButton("Assign", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add Instructor button
                EditText tId = v.findViewById(R.id.instructor_id);
                int iId = Integer.parseInt(tId.getText().toString());
                EditText tCourseId = v.findViewById(R.id.course_id);
                int cId = Integer.parseInt(tCourseId.getText().toString());

                School s = School.getSchool();
                Course c = s.getCourse(cId);
                Instructor instructor = s.getInstructor(iId);
                if (c == null) {
                    assignInstructorDialog("course id invalid.");
                } else if (instructor == null) {
                    assignInstructorDialog("instructor invalid.");
                } else {
                    c.updateInstructor(iId);
                    status.setText("Assignment complete.");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}

