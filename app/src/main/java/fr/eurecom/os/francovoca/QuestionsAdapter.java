package fr.eurecom.os.francovoca;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lxllx on 2015/12/15.
 */
public class QuestionsAdapter extends ArrayAdapter<Question> {

    private final Context context;
    private final int id;
    protected Arena test;
    private Question question;

    public QuestionsAdapter (Context context, int id, Arena test){
        super(context, id, test.getList());
        this.context = context;
        this.id = id;
        this.test = test;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(id, parent, false);
        question = test.getQuestion(position);
        final TextView questions_question = (TextView) rowView.findViewById(R.id.questions_question);
        final RadioButton button01 = (RadioButton) rowView.findViewById(R.id.radioButton);
        final RadioButton button02 = (RadioButton) rowView.findViewById(R.id.radioButton2);
        final RadioButton button03 = (RadioButton) rowView.findViewById(R.id.radioButton3);
        final RadioButton button04 = (RadioButton) rowView.findViewById(R.id.radioButton4);
        RadioGroup group = (RadioGroup) rowView.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Question current_question = test.getQuestion(position);
               if(checkedId == button01.getId()){
                   current_question.setChoosed_result(1);
               } else if(checkedId == button02.getId()) {
                   current_question.setChoosed_result(2);
               } else if(checkedId == button03.getId()) {
                   current_question.setChoosed_result(3);
               } else if(checkedId == button04.getId()) {
                   current_question.setChoosed_result(4);
               }
                test.updataQuestion(position, current_question);
            }
        });

        questions_question.setText(question.getQuestion());
        button01.setText(question.getOption01());
        button02.setText(question.getOption02());
        button03.setText(question.getOption03());
        button04.setText(question.getOption04());

        return rowView;
    }

    public Arena updataTest() {
        return this.test;
    }
}
