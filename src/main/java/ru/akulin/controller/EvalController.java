package ru.akulin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.akulin.eval.DistanceEvaluator;
import ru.akulin.eval.EvalRequest;
import ru.akulin.eval.RequestedDistance;
import ru.akulin.exception.EvalControllerException;
import java.util.List;

@RestController
@RequestMapping(path = "/eval")
public class EvalController {

    @Autowired
    DistanceEvaluator distanceEvaluator;

    @GetMapping(path = "")
    public String get() {
        return "Здесь может быть инструкция по использованию конечной точки.";
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public List<RequestedDistance> post(@RequestBody EvalRequest evalRequest) {
        try {
            return distanceEvaluator.eval(evalRequest);
        }
        catch (Exception cause) {
            throw new EvalControllerException("Во время вычисления расстояний произошла ошибка.", cause);
        }
    }
}
