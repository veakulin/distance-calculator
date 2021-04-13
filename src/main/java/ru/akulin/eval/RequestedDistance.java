// Можно было-бы использовать класс Distance,
// но тогда пришлось бы бороться с исключениями приведения строк к Long.
// С отдельным классом проще и гибче.

package ru.akulin.eval;

public class RequestedDistance {

    private String from;
    private String to;
    private Double distance;

    public RequestedDistance() {
    }

    public RequestedDistance(String from, String to, Double distance) {
        setFrom(from);
        setTo(to);
        setDistance(distance);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestedDistance)) return false;

        RequestedDistance that = (RequestedDistance) o;

        if (getFrom() != null ? !getFrom().equals(that.getFrom()) : that.getFrom() != null) return false;
        if (getTo() != null ? !getTo().equals(that.getTo()) : that.getTo() != null) return false;
        return getDistance() != null ? getDistance().equals(that.getDistance()) : that.getDistance() == null;
    }

    @Override
    public int hashCode() {
        int result = getFrom() != null ? getFrom().hashCode() : 0;
        result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
        result = 31 * result + (getDistance() != null ? getDistance().hashCode() : 0);
        return result;
    }
}
