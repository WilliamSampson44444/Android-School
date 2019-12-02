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
import edu.csumb.school2.model.Enrollment;
import edu.csumb.school2.model.Instructor;
import edu.csumb.school2.model.School;

public class CourseView extends AppCompatActivity  {

    private TextView status;

    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CourseView", "onCreate called");
        setContentView(R.layout.course_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        status = findViewById(R.id.textView2);

        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter();
        rv.setAdapter(courseAdapter);


        Button  cancel = findViewById(R.id.button2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CourseView", "onClick cancel called");
                finish();
            }
        });

        Button add  = findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CourseView", "add");
                addCourseDialog( null );

            }
        });

        Button delete = findViewById(R.id.button5);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CourseView", "delete");
                deleteCourseDialog( null );
            }
        });

        Button grade = findViewById(R.id.button6);
        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CourseView", "grade");
                gradeCourseDialog( null );
            }
        });
    }

    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(CourseView.this);
            return new CourseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course inst = School.getSchool().catalog.get(position);
            holder.bind(inst);
        }

        @Override
        public int getItemCount() {
            return School.getSchool().catalog.size();
        }
    }

    private class CourseHolder extends RecyclerView.ViewHolder {

        public CourseHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.course_display, parent, false));
        }

        public void bind(Course course){
            TextView v = itemView.findViewById(R.id.course_display);
            v.setText( course.toString());
        }
    }

    private void addCourseDialog(String message ) {
        // show dialog to add course
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (message != null) {
            builder.setTitle(message);
        } else {
            builder.setTitle("Add course");
        }
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_add_course, null);
        builder.setView(v);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add Course button
                EditText tId = v.findViewById(R.id.course_id);
                int cId = Integer.parseInt( tId.getText().toString() );
                EditText tTitle = v.findViewById(R.id.title);
                String title = tTitle.getText().toString();
                EditText inId = v.findViewById(R.id.instructor_id);
                int iId = Integer.parseInt(inId.getText().toString());
                EditText tRoom = v.findViewById(R.id.room);
                String room = tRoom.getText().toString();

                boolean rc = School.getSchool().addCourse(cId, title, iId, room);

                if (rc) {
                    status.setText("Course added.");
                    // notify adapter that course list has changed
                    courseAdapter.notifyDataSetChanged();
                } else {
                    addCourseDialog("Duplicate id.");
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


    private void deleteCourseDialog(String message) {
        // show dialog to add course
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (message!=null) {
            builder.setTitle(message);
        } else {
            builder.setTitle("Delete course");
        }
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_delete_course, null);
        builder.setView(v);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add Course button

                EditText tCourseId = v.findViewById(R.id.course_id);
                int cId = Integer.parseInt(tCourseId.getText().toString());

                if (School.getSchool().deleteCourse(cId)) {
                    status.setText("Course deleted.");
                } else {
                    deleteCourseDialog("Cannot delete course. Check if course ID is correct, and if no students are enrolled.");
                }
                courseAdapter.notifyDataSetChanged();
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

    private void gradeCourseDialog(String message) {
        // show dialog to add course
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (message!=null) {
            builder.setTitle(message);
        } else {
            builder.setTitle("Change grade");
        }
        LayoutInflater inflater = getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.dialog_grade_course, null);
        builder.setView(v);

        builder.setPositiveButton("Change Grade", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Add Course button
                Log.d("CourseView", "Change Grade clicked.");
                EditText tSId = v.findViewById(R.id.student_id);
                int sId = Integer.parseInt(tSId.getText().toString());
                EditText tCourseId = v.findViewById(R.id.course_id);
                int cId = Integer.parseInt(tCourseId.getText().toString());
                EditText tGrade = v.findViewById(R.id.grade);
                double grade = Double.parseDouble(tGrade.getText().toString());
                EditText tLGrade = v.findViewById(R.id.letter_grade);
                String letterGrade = tLGrade.getText().toString();

                if(School.getSchool().getStudentInfo(sId).updateGrade(grade, letterGrade, cId)){
                    status.setText("Grade updated.");
                }else{
                    gradeCourseDialog("Student not enrolled in that course.");
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

