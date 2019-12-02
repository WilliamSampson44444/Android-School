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
import edu.csumb.school2.model.Student;
import edu.csumb.school2.model.School;

public class StudentView extends AppCompatActivity  {

    private TextView status;

    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("StudentView", "onCreate called");
        setContentView(R.layout.student_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        status = findViewById(R.id.textView2);

        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter();
        rv.setAdapter(studentAdapter);


        Button  cancel = findViewById(R.id.button2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StudentView", "onClick cancel called");
                finish();
            }
        });

        Button add  = findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StudentView", "add");
                addStudentDialog( null );

            }
        });

        Button enroll  = findViewById(R.id.button6);
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StudentView", "enroll");
                enrollStudentDialog( null );

            }
        });

        Button graduate = findViewById(R.id.button5);
        graduate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StudentView", "graduate");
                graduateStudentDialog( null );
            }
        });
    }

    private class StudentAdapter extends RecyclerView.Adapter<StudentHolder> {

        @Override
        public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StudentView.this);
            return new StudentHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(StudentHolder holder, int position) {
            Student s = School.getSchool().students.get(position);
            holder.bind(s);
        }

        @Override
        public int getItemCount() { return School.getSchool().students.size(); }
    }

    private class StudentHolder extends RecyclerView.ViewHolder {

        public StudentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.student_display, parent, false));
        }

        public void bind(Student student){
            TextView v = itemView.findViewById(R.id.student_display);
            v.setText( student.toString());
        }
    }

    private void addStudentDialog(String message ) {
        // show dialog to add student
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (message != null) {
            builder.setTitle(message);
        } else {
            builder.setTitle("Add student");
        }
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_add_student, null);
        builder.setView(v);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add Student button
                Log.d("StudentView", "Clicked Add Student");
                EditText tId = v.findViewById(R.id.student_id_add);
                int sId = Integer.parseInt( tId.getText().toString() );
                Log.d("StudentView", "Student ID ok");
                EditText tName = v.findViewById(R.id.student_name);
                String name = tName.getText().toString();
                Log.d("StudentView", "name ok");
                EditText tCId = v.findViewById(R.id.student_course_id);
                int cId = Integer.parseInt(tCId.getText().toString());
                Log.d("StudentView", "course id ok");
                EditText tGrade = v.findViewById(R.id.student_grade);
                double grade = Double.parseDouble(tGrade.getText().toString());
                Log.d("StudentView", "grade ok");
                EditText tLetterGrade = v.findViewById(R.id.student_letter_grade);
                String letterGrade = tLetterGrade.getText().toString();
                Log.d("StudentView", "letter grade ok");

                boolean rc = School.getSchool().addStudent(sId, name, cId, grade, letterGrade);

                if (rc) {
                    status.setText("Student added.");
                    // notify adapter that student list has changed
                    studentAdapter.notifyDataSetChanged();
                } else {
                    addStudentDialog("Duplicate id.");
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


    private void enrollStudentDialog(String message ) {
        // show dialog to add student
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (message != null) {
            builder.setTitle(message);
        } else {
            builder.setTitle("Enroll student");
        }
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_enroll_student, null);
        builder.setView(v);
        builder.setPositiveButton("Enroll", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add Student button
                EditText tId = v.findViewById(R.id.student_id_enroll);
                int sId = Integer.parseInt( tId.getText().toString() );
                EditText tCId = v.findViewById(R.id.course_id_enroll);
                int cId = Integer.parseInt(tCId.getText().toString());

                if(School.getSchool().getCourse(cId) != null) {
                    School.getSchool().getStudentInfo(sId).enroll(School.getSchool().getCourse(cId));
                    status.setText("Student enrolled.");
                    // notify adapter that student list has changed
                    studentAdapter.notifyDataSetChanged();
                }else{
                    enrollStudentDialog("Course does not exist.");
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


    private void graduateStudentDialog(String message) {
        // show dialog to add student
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (message!=null) {
            builder.setTitle(message);
        } else {
            builder.setTitle("Graduate student");
        }
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_graduate_student, null);
        builder.setView(v);

        builder.setPositiveButton("Graduate", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add Student button
                EditText tId = v.findViewById(R.id.graduate_student_id);
                int iId = Integer.parseInt(tId.getText().toString());

                if(School.getSchool().graduateStudent(iId)){
                    status.setText("Graduation complete.");
                    studentAdapter.notifyDataSetChanged();
                }else{
                    graduateStudentDialog("Invalid ID.");
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

