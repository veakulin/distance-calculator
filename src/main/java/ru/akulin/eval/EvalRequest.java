// Класс для десериализации JSON-запроса на расчет расстояний.

package ru.akulin.eval;

public class EvalRequest {

    private String[] from;
    private String[] to;
    private int evalType;

    public EvalRequest() {
    }

    public EvalRequest(String[] from, String[] to, int evalType) {
        this.from = from;
        this.to = to;
        this.evalType = evalType;
    }

    public String[] getFrom() {
        return from;
    }

    public void setFrom(String[] from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public int getEvalType() {
        return evalType;
    }

    public void setEvalType(int evalType) {
        this.evalType = evalType;
    }
}
