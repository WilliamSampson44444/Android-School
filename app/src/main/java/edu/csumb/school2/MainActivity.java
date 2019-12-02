package edu.csumb.school2;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;



import java.io.InputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

import edu.csumb.school2.model.School;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        School.createSchool("CSUMB");
        Resources resources = getResources();
        readData(resources.openRawResource(R.raw.school_data));

        Button instructors = findViewById(R.id.button2);
        instructors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // instructor button pressed
                Intent intent =  new Intent(MainActivity.this, InstructorView.class);
                startActivity(intent);
            }
        });
        Button courses = findViewById(R.id.button3);
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // courses button pressed
                Intent intent =  new Intent(MainActivity.this, CourseView.class);
                startActivity(intent);
            }
        });

        Button students = findViewById(R.id.button4);
        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // student button pressed
                Intent intent = new Intent(MainActivity.this, StudentView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readData(InputStream input) {
        School school = School.getSchool();
        String line = "";
        try {
            Scanner f = new Scanner(input);
            // read instructor data
            int icount = f.nextInt();
            f.nextLine();  // skip to next reocrd
            for (int i=0; i<icount; i++){
                line = f.nextLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                int id = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                String email = st.nextToken();
                String phone = st.nextToken();
                school.addInstructor(id, name, email, phone);

            }
            // read course data
            int ccount = f.nextInt();
            f.nextLine();  // skip to next reocrd
            for (int i=0; i<ccount; i++){
                line = f.nextLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                int id = Integer.parseInt(st.nextToken());
                String title = st.nextToken();
                int instructor_id = Integer.parseInt(st.nextToken());
                String room = st.nextToken();
                school.addCourse(id, title, instructor_id, room);
            }

            // read student records
            int scount = f.nextInt();
            f.nextLine();  // skip to next reocrd
            for (int i=0; i<scount; i++){
                line = f.nextLine();
                StringTokenizer st = new StringTokenizer(line, ",");
                int id = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                int course_id = Integer.parseInt(st.nextToken());
                double grade = Double.parseDouble(st.nextToken());
                String letterGrade = st.nextToken();
                school.addStudent(id, name, course_id, grade, letterGrade);
            }
            Log.d("School", school.faculty.size()+" instructor records read.");
            Log.d("School", school.catalog.size()+" course records read.");
            Log.d("School", school.students.size()+" student records read.");

        } catch (Exception e){
            Log.d("School", "readData exception:"+e.getMessage()+" on line:"+line);
            return;
        }
    }
}
