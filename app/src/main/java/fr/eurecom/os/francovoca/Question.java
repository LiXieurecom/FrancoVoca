package fr.eurecom.os.francovoca;

import java.io.Serializable;

/**
 * Created by lxllx on 2015/12/14.
 */
public class Question implements Serializable {
    private final String question;
    private final String option01;
    private final String option02;
    private final String option03;
    private final String option04;
    private final int right_result;
    private int choosed_result=0;

    public Question(String question, String option01, String option02, String option03, String option04, int right_result){
        this.question = question;
        this.option01 = option01;
        this.option02 = option02;
        this.option03 = option03;
        this.option04 = option04;
        this.right_result = right_result;
    }

    public int getRightResult() {
        return this.right_result;
    }

    public void setChoosed_result(int choosed_result) {
        this.choosed_result = choosed_result;
    }

    public int getChoosed_result(){
        return this.choosed_result;
    }

    public String getQuestion () {
        return this.question;
    }

    public String getOption01 () { return this.option01; }

    public String getOption02 (){
        return this.option02;
    }

    public String getOption03 () { return this.option03; }

    public String getOption04 () { return this.option04; }

}
