// Константы для указания выбора способа расчета расстояний.
// Можно было бы использовать перечисление, но тогда пользователь
// Получит ошибку, если укажет неправильный способ.
// В случае числовых констант пользователь получит пустой ответ.
// Да и переслать циферку легче, чем строчку.

package ru.akulin.eval;

public final class EvalType {

    public final static int CROWFLIGHT = 0;
    public final static int DISTANCE_MAP = 1;
    public final static int PREFER_CROWFLIGHT = 2;
    public final static int PREFER_DISTANCE_MAP = 3;

}
