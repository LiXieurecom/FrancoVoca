package fr.eurecom.os.francovoca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxllx on 2016/1/6.
 */
public class Arena implements Serializable{
    private List<Question> questions = new ArrayList<>();
    private final String title;
    private final long difficulty;

    public Arena (String title, long difficulty) {
        this.title = title;
        this.difficulty = difficulty;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public int getSize() {
        return this.questions.size();
    }

    public Question getQuestion(int location){
        return this.questions.get(location);
    }

    public List<Question> getList () {
        return this.questions;
    }

    public void updataQuestion(int location, Question question) {
        this.questions.set(location,question);
    }
}
