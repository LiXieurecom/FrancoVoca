package fr.eurecom.os.francovoca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class Questions extends AppCompatActivity {

    private Arena test;
    private ListView listView;
    private QuestionsAdapter Ca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Intent getIntent = getIntent();
        test = (Arena) getIntent.getSerializableExtra("test");
        listView = (ListView) findViewById(R.id.listView1);
        Ca = new QuestionsAdapter(this, R.layout.questions, test);
        listView.setAdapter(Ca);
    }

    public void openDialog(View v) {
        test = Ca.updataTest();
        int goals = 0;
        for (int i = 0; i < test.getSize(); i++){
            Log.i("test", i + "this is" + test.getQuestion(i).getChoosed_result());
            if(test.getQuestion(i).getChoosed_result() == 0){
                Toast.makeText(getApplicationContext(), "Haven't finished this test yet!\nGo back to work!",Toast.LENGTH_LONG).show();
                return;
            } else {
                if(test.getQuestion(i).getChoosed_result() == test.getQuestion(i).getRightResult()){
                    goals ++;
                }
            }
        }
        String result = "Congradulations! \nYour have got "+goals+" / "+test.getSize()+" scores!";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(result);
        builder.setCancelable(false);
        builder.setPositiveButton("Yeah!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
